import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);

    static final int KAPASITAS_MENU = 100;
    static final int KAPASITAS_PESANAN = 100;

    static Menu[] daftarMenu = new Menu[KAPASITAS_MENU];
    static int jumlahMenu = 0;

    static Menu[] menuDipesan = new Menu[KAPASITAS_PESANAN];
    static int[] jumlahDipesan = new int[KAPASITAS_PESANAN];
    static int jumlahItemPesanan = 0;

    static final double PAJAK = 0.10;
    static final int BIAYA_PELAYANAN = 20000;

    public static void main(String[] args) {
        inisialisasiMenuAwal();

        int pilihan;
        do {
            tampilMenuUtama();
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    menuPelanggan();
                    break;
                case 2:
                    menuPemilik();
                    break;
                case 0:
                    System.out.println("Terima kasih. Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Coba lagi.");
            }
        } while (pilihan != 0);
    }

    static void inisialisasiMenuAwal() {
        tambahMenuInternal("Nasi Goreng", 25000, "makanan");
        tambahMenuInternal("Mie Ayam", 20000, "makanan");
        tambahMenuInternal("Ayam Bakar", 30000, "makanan");
        tambahMenuInternal("Sate Ayam", 28000, "makanan");

        tambahMenuInternal("Es Teh", 8000, "minuman");
        tambahMenuInternal("Jus Jeruk", 12000, "minuman");
        tambahMenuInternal("Kopi Hitam", 10000, "minuman");
        tambahMenuInternal("Air Mineral", 6000, "minuman");
    }

    static void tambahMenuInternal(String nama, int harga, String kategori) {
        if (jumlahMenu < KAPASITAS_MENU) {
            daftarMenu[jumlahMenu] = new Menu(nama, harga, kategori);
            jumlahMenu++;
        }
    }

    static void tampilMenuUtama() {
        System.out.println("\n===== APLIKASI RESTORAN =====");
        System.out.println("1. Menu Pelanggan");
        System.out.println("2. Manajemen Menu (Pemilik)");
        System.out.println("0. Keluar");
    }

    static void menuPelanggan() {
        int pilihan;
        do {
            System.out.println("\n--- MENU PELANGGAN ---");
            System.out.println("1. Lihat daftar menu");
            System.out.println("2. Pesan menu");
            System.out.println("3. Cetak struk pesanan");
            System.out.println("0. Kembali");

            pilihan = bacaInt("Pilih menu pelanggan: ");

            if (pilihan == 1) {
                tampilDaftarMenuPerKategori();
            } else if (pilihan == 2) {
                prosesPemesanan();
            } else if (pilihan == 3) {
                cetakStruk();
            } else if (pilihan == 0) {
                System.out.println("Kembali ke menu utama.");
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    static void menuPemilik() {
        int pilihan;
        do {
            System.out.println("\n--- MANAJEMEN MENU PEMILIK ---");
            System.out.println("1. Lihat daftar menu");
            System.out.println("2. Tambah menu baru");
            System.out.println("3. Ubah harga menu");
            System.out.println("4. Hapus menu");
            System.out.println("0. Kembali");

            pilihan = bacaInt("Pilih menu pemilik: ");

            switch (pilihan) {
                case 1:
                    tampilDaftarMenuDenganNomor();
                    break;
                case 2:
                    tambahMenuBaru();
                    break;
                case 3:
                    ubahHargaMenu();
                    break;
                case 4:
                    hapusMenu();
                    break;
                case 0:
                    System.out.println("Kembali ke menu utama.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    static void tampilDaftarMenuPerKategori() {
        System.out.println("\n===== DAFTAR MENU =====");

        System.out.println("\nMakanan:");
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i].getKategori().equalsIgnoreCase("makanan")) {
                System.out.println((i + 1) + ". " + daftarMenu[i].getNama() + " - Rp " + daftarMenu[i].getHarga());
            }
        }

        System.out.println("\nMinuman:");
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i].getKategori().equalsIgnoreCase("minuman")) {
                System.out.println((i + 1) + ". " + daftarMenu[i].getNama() + " - Rp " + daftarMenu[i].getHarga());
            }
        }
    }

    static void tampilDaftarMenuDenganNomor() {
        System.out.println("\n===== DAFTAR MENU (UNTUK MANAJEMEN) =====");
        for (int i = 0; i < jumlahMenu; i++) {
            System.out.println((i + 1) + ". " + daftarMenu[i]);
        }
    }

    static void prosesPemesanan() {
        jumlahItemPesanan = 0;

        tampilDaftarMenuPerKategori();
        System.out.println("\nMasukkan nomor menu yang ingin dipesan.");
        System.out.println("Ketik 0 untuk selesai.");

        while (true) {
            int nomorMenu = bacaInt("Nomor menu: ");

            if (nomorMenu == 0) {
                break;
            }

            if (nomorMenu < 1 || nomorMenu > jumlahMenu) {
                System.out.println("Nomor menu tidak valid, silakan input ulang.");
                continue;
            }

            int jumlah = bacaInt("Jumlah pesanan: ");
            if (jumlah <= 0) {
                System.out.println("Jumlah harus lebih dari 0.");
                continue;
            }

            Menu menuDipilih = daftarMenu[nomorMenu - 1];

            int indexPesanan = cariIndexPesanan(menuDipilih.getNama());
            if (indexPesanan != -1) {
                jumlahDipesan[indexPesanan] += jumlah;
            } else {
                menuDipesan[jumlahItemPesanan] = menuDipilih;
                jumlahDipesan[jumlahItemPesanan] = jumlah;
                jumlahItemPesanan++;
            }

            System.out.println("Pesanan ditambahkan: " + menuDipilih.getNama());
        }

        if (jumlahItemPesanan == 0) {
            System.out.println("Belum ada pesanan.");
        } else {
            System.out.println("Pemesanan selesai.");
        }
    }

    static int cariIndexPesanan(String namaMenu) {
        int i = 0;
        while (i < jumlahItemPesanan) {
            if (menuDipesan[i].getNama().equalsIgnoreCase(namaMenu)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    static void cetakStruk() {
        if (jumlahItemPesanan == 0) {
            System.out.println("\nBelum ada pesanan untuk dicetak.");
            return;
        }

        int subtotal = 0;
        int totalMinuman = 0;
        int minumanTermurah = Integer.MAX_VALUE;

        System.out.println("\n========== STRUK PESANAN ==========");
        System.out.printf("%-20s %-8s %-12s %-12s%n", "Item", "Jumlah", "Harga", "Total");

        for (int i = 0; i < jumlahItemPesanan; i++) {
            int totalItem = menuDipesan[i].getHarga() * jumlahDipesan[i];
            subtotal += totalItem;

            if (menuDipesan[i].getKategori().equalsIgnoreCase("minuman")) {
                totalMinuman += jumlahDipesan[i];
                if (menuDipesan[i].getHarga() < minumanTermurah) {
                    minumanTermurah = menuDipesan[i].getHarga();
                }
            }

            System.out.printf("%-20s %-8d %-12d %-12d%n",
                    menuDipesan[i].getNama(),
                    jumlahDipesan[i],
                    menuDipesan[i].getHarga(),
                    totalItem);
        }

        int diskon = 0;
        if (subtotal > 100000) {
            diskon = (int) (0.10 * subtotal);
        }

        int promoMinuman = 0;
        if (subtotal > 50000) {
            if (totalMinuman >= 2 && minumanTermurah != Integer.MAX_VALUE) {
                promoMinuman = minumanTermurah;
            }
        }

        int setelahPotongan = subtotal - diskon - promoMinuman;
        int pajak = (int) (PAJAK * setelahPotongan);
        int totalAkhir = setelahPotongan + pajak + BIAYA_PELAYANAN;

        System.out.println("----------------------------------------------");
        System.out.println("Subtotal               : Rp " + subtotal);
        System.out.println("Diskon 10%             : Rp " + diskon);
        System.out.println("Promo B1G1 minuman     : Rp " + promoMinuman);
        System.out.println("Setelah potongan       : Rp " + setelahPotongan);
        System.out.println("Pajak 10%              : Rp " + pajak);
        System.out.println("Biaya pelayanan        : Rp " + BIAYA_PELAYANAN);
        System.out.println("TOTAL BAYAR            : Rp " + totalAkhir);
        System.out.println("==============================================");
    }

    static void tambahMenuBaru() {
        int jumlahTambah = bacaInt("Berapa menu baru yang ingin ditambahkan? ");
        if (jumlahTambah <= 0) {
            System.out.println("Jumlah tidak valid.");
            return;
        }

        for (int i = 1; i <= jumlahTambah; i++) {
            if (jumlahMenu >= KAPASITAS_MENU) {
                System.out.println("Kapasitas menu penuh.");
                break;
            }

            System.out.println("\nInput menu baru ke-" + i);
            String nama = bacaString("Nama menu: ");
            int harga = bacaInt("Harga menu: ");

            String kategori;
            do {
                kategori = bacaString("Kategori (makanan/minuman): ").toLowerCase();
                if (!kategori.equals("makanan") && !kategori.equals("minuman")) {
                    System.out.println("Kategori tidak valid. Masukkan makanan atau minuman.");
                }
            } while (!kategori.equals("makanan") && !kategori.equals("minuman"));

            daftarMenu[jumlahMenu] = new Menu(nama, harga, kategori);
            jumlahMenu++;
            System.out.println("Menu berhasil ditambahkan.");
        }
    }

    static void ubahHargaMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Daftar menu kosong.");
            return;
        }

        tampilDaftarMenuDenganNomor();
        int nomor = bacaInt("Pilih nomor menu yang ingin diubah: ");

        if (nomor < 1 || nomor > jumlahMenu) {
            System.out.println("Nomor menu tidak valid.");
            return;
        }

        Menu target = daftarMenu[nomor - 1];
        int hargaBaru = bacaInt("Masukkan harga baru untuk " + target.getNama() + ": ");

        String konfirmasi = bacaString("Yakin ubah harga? Ketik 'Ya' untuk konfirmasi: ");
        if (konfirmasi.equalsIgnoreCase("Ya")) {
            target.setHarga(hargaBaru);
            System.out.println("Harga berhasil diubah.");
        } else {
            System.out.println("Perubahan dibatalkan.");
        }
    }

    static void hapusMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Daftar menu kosong.");
            return;
        }

        tampilDaftarMenuDenganNomor();
        int nomor = bacaInt("Pilih nomor menu yang ingin dihapus: ");

        if (nomor < 1 || nomor > jumlahMenu) {
            System.out.println("Nomor menu tidak valid.");
            return;
        }

        String konfirmasi = bacaString("Yakin hapus menu? Ketik 'Ya' untuk konfirmasi: ");
        if (konfirmasi.equalsIgnoreCase("Ya")) {
            for (int i = nomor - 1; i < jumlahMenu - 1; i++) {
                daftarMenu[i] = daftarMenu[i + 1];
            }
            daftarMenu[jumlahMenu - 1] = null;
            jumlahMenu--;
            System.out.println("Menu berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    static int bacaInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            if (input.hasNextInt()) {
                int nilai = input.nextInt();
                input.nextLine();
                return nilai;
            } else {
                System.out.println("Input harus angka.");
                input.nextLine();
            }
        }
    }

    static String bacaString(String pesan) {
        System.out.print(pesan);
        return input.nextLine();
    }
}
