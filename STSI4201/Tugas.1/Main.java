public class Main {

    // ---- Array daftar menu (4 makanan + 4 minuman) ----
    static Menu[] daftarMenu = {
        new Menu("Nasi Padang",   25000, "makanan"),
        new Menu("Nasi Goreng",   20000, "makanan"),
        new Menu("Mie Ayam",      18000, "makanan"),
        new Menu("Sate Ayam",     30000, "makanan"),
        new Menu("Es Teh Manis",   8000, "minuman"),
        new Menu("Es Jeruk",      10000, "minuman"),
        new Menu("Kopi Hitam",    12000, "minuman"),
        new Menu("Jus Alpukat",   15000, "minuman")
    };

    // ---- Slot pesanan (max 4) - paralel array ----
    String[] namaPesanan      = new String[4];
    int[]    jumlahPesanan    = new int[4];
    int[]    hargaPesanan     = new int[4];
    String[] kategoriPesanan  = new String[4];
    int      slotTerisi       = 0;

    // Konstanta biaya
    static final int BIAYA_PELAYANAN = 20000;
    static final int BATAS_BOGO      = 50000;
    static final int BATAS_DISKON    = 100000;

    // ===== METHOD: reset state pesanan =====
    public void resetPesanan() {
        slotTerisi = 0;
    }

    // ===== METHOD: tampilkan daftar menu =====
    public void tampilkanMenu() {
        System.out.println("==================================");
        System.out.println("       DAFTAR MENU RESTORAN       ");
        System.out.println("==================================");

        System.out.println("--- Kategori: MAKANAN ---");
        cetakItemMenu(0);
        cetakItemMenu(1);
        cetakItemMenu(2);
        cetakItemMenu(3);

        System.out.println("--- Kategori: MINUMAN ---");
        cetakItemMenu(4);
        cetakItemMenu(5);
        cetakItemMenu(6);
        cetakItemMenu(7);

        System.out.println("==================================");
    }

    // Helper cetak satu item menu
    void cetakItemMenu(int idx) {
        Menu m = daftarMenu[idx];
        System.out.printf("  %-15s  Rp %,d%n", m.getNama(), m.getHarga());
    }

    // ===== METHOD: cari index menu di array berdasarkan nama (SWITCH CASE) =====
    int cariIndexMenu(String nama) {
        switch (nama) {
            case "Nasi Padang":   return 0;
            case "Nasi Goreng":   return 1;
            case "Mie Ayam":      return 2;
            case "Sate Ayam":     return 3;
            case "Es Teh Manis":  return 4;
            case "Es Jeruk":      return 5;
            case "Kopi Hitam":    return 6;
            case "Jus Alpukat":   return 7;
            default:              return -1;
        }
    }

    // ===== METHOD: catat pesanan ke slot tertentu (IF-ELSE validasi) =====
    public void catatPesanan(String namaMenu, int jumlah, int slot) {
        if (slot < 0 || slot > 3) {
            System.out.println("Slot pesanan tidak valid (max 4 menu).");
            return;
        }

        int idx = cariIndexMenu(namaMenu);
        if (idx == -1) {
            System.out.println("Menu '" + namaMenu + "' tidak ditemukan.");
            return;
        }

        Menu m = daftarMenu[idx];
        namaPesanan[slot]     = m.getNama();
        jumlahPesanan[slot]   = jumlah;
        hargaPesanan[slot]    = m.getHarga();
        kategoriPesanan[slot] = m.getKategori();
        slotTerisi++;
    }

    // ===== METHOD: hitung subtotal per slot =====
    int hitungSubtotalSlot(int slot) {
        if (slot < slotTerisi) {
            return jumlahPesanan[slot] * hargaPesanan[slot];
        }
        return 0;
    }

    // ===== METHOD: hitung subtotal seluruh pesanan =====
    int hitungSubtotal() {
        int sub = 0;
        sub += hitungSubtotalSlot(0);
        sub += hitungSubtotalSlot(1);
        sub += hitungSubtotalSlot(2);
        sub += hitungSubtotalSlot(3);
        return sub;
    }

    // ===== METHOD: cari slot pesanan minuman pertama (untuk Beli 1 Gratis 1) =====
    int cariSlotMinumanPertama() {
        if (slotTerisi >= 1 && "minuman".equals(kategoriPesanan[0])) return 0;
        if (slotTerisi >= 2 && "minuman".equals(kategoriPesanan[1])) return 1;
        if (slotTerisi >= 3 && "minuman".equals(kategoriPesanan[2])) return 2;
        if (slotTerisi >= 4 && "minuman".equals(kategoriPesanan[3])) return 3;
        return -1;
    }

    // Helper cetak satu baris pesanan
    void cetakBarisPesanan(int slot) {
        if (slot >= slotTerisi) return;
        int totalItem = jumlahPesanan[slot] * hargaPesanan[slot];
        System.out.printf("%-15s x%-2d  @ Rp %,7d  = Rp %,8d%n",
            namaPesanan[slot], jumlahPesanan[slot],
            hargaPesanan[slot], totalItem);
    }

    // ===== METHOD: cetak struk pemesanan =====
    public void cetakStruk() {
        int subtotal = hitungSubtotal();

        System.out.println();
        System.out.println("==================================");
        System.out.println("        STRUK PEMBAYARAN          ");
        System.out.println("==================================");

        // Cetak baris pesanan (max 4, tanpa loop)
        cetakBarisPesanan(0);
        cetakBarisPesanan(1);
        cetakBarisPesanan(2);
        cetakBarisPesanan(3);

        System.out.println("----------------------------------");
        System.out.printf("Subtotal             : Rp %,8d%n", subtotal);

        // ----- Klasifikasi pesanan (IF-ELSE IF) -----
        String tierPesanan;
        if (subtotal > BATAS_DISKON) {
            tierPesanan = "BESAR";
        } else if (subtotal > BATAS_BOGO) {
            tierPesanan = "MENENGAH";
        } else if (subtotal > 0) {
            tierPesanan = "KECIL";
        } else {
            tierPesanan = "KOSONG";
        }
        System.out.println("Kategori Pesanan     : " + tierPesanan);

        // ----- Beli 1 Gratis 1: nested if (subtotal > 50k DAN ada minuman) -----
        int potonganBogo = 0;
        if (subtotal > BATAS_BOGO) {
            int slotMin = cariSlotMinumanPertama();
            if (slotMin != -1) {
                potonganBogo = hargaPesanan[slotMin];
                System.out.printf("Gratis 1 %-13s: -Rp %,8d%n",
                    namaPesanan[slotMin], potonganBogo);
            } else {
                System.out.println("(Beli 1 Gratis 1 tidak aktif: tidak ada item minuman)");
            }
        }

        int subtotalSetelahBogo = subtotal - potonganBogo;

        // ----- Diskon 10% jika subtotal > 100k (IF) -----
        int diskon = 0;
        if (subtotal > BATAS_DISKON) {
            diskon = subtotalSetelahBogo / 10; // 10%
            System.out.printf("Diskon 10%%           : -Rp %,8d%n", diskon);
        }

        int subtotalSetelahDiskon = subtotalSetelahBogo - diskon;

        // ----- Pajak 10% dan biaya pelayanan -----
        int pajak     = subtotalSetelahDiskon / 10;
        int pelayanan = BIAYA_PELAYANAN;
        int totalBayar = subtotalSetelahDiskon + pajak + pelayanan;

        System.out.printf("Pajak 10%%            : Rp %,8d%n", pajak);
        System.out.printf("Biaya Pelayanan      : Rp %,8d%n", pelayanan);
        System.out.println("----------------------------------");
        System.out.printf("TOTAL BAYAR          : Rp %,8d%n", totalBayar);
        System.out.println("==================================");
    }

    // ===== MAIN: jalankan 3 skenario sebagai demo =====
    public static void main(String[] args) {
        Main app = new Main();

        // 1) Tampilkan daftar menu sekali di awal
        app.tampilkanMenu();

        // ====== SKENARIO 1: Pesanan KECIL (subtotal < Rp 50.000) ======
        // Ekspektasi: tidak ada BOGO, tidak ada diskon
        System.out.println();
        System.out.println(">>>>>> SKENARIO 1: Pesanan KECIL <<<<<<");
        app.resetPesanan();
        app.catatPesanan("Mie Ayam",      1, 0);
        app.catatPesanan("Es Teh Manis",  1, 1);
        app.cetakStruk();

        // ====== SKENARIO 2: Pesanan MENENGAH (50k < subtotal <= 100k) ======
        // Ekspektasi: BOGO aktif, diskon belum aktif
        System.out.println();
        System.out.println(">>>>>> SKENARIO 2: Pesanan MENENGAH (Beli 1 Gratis 1 aktif) <<<<<<");
        app.resetPesanan();
        app.catatPesanan("Nasi Padang", 2, 0);
        app.catatPesanan("Es Jeruk",    2, 1);
        app.cetakStruk();

        // ====== SKENARIO 3: Pesanan BESAR (subtotal > 100k) ======
        // Ekspektasi: BOGO + diskon 10% sama-sama aktif
        System.out.println();
        System.out.println(">>>>>> SKENARIO 3: Pesanan BESAR (Beli 1 Gratis 1 + Diskon 10%) <<<<<<");
        app.resetPesanan();
        app.catatPesanan("Nasi Padang",  2, 0);
        app.catatPesanan("Sate Ayam",    2, 1);
        app.catatPesanan("Es Teh Manis", 2, 2);
        app.catatPesanan("Kopi Hitam",   1, 3);
        app.cetakStruk();
    }
}
