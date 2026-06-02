import java.util.Scanner;
import java.io.*;

public class Main {
    private static Menu    menu    = new Menu();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        tampilkanHeader();

        try {
            menu.muatDariFile();
        } catch (IOException e) {
            System.out.println("  [PERINGATAN] Gagal memuat menu: " + e.getMessage());
        }

        boolean berjalan = true;
        while (berjalan) {
            tampilkanMenuUtama();
            int pilihan = bacaInt("  Masukkan pilihan: ");

            switch (pilihan) {
                case 1: tambahItemMenu();          break;
                case 2: tampilkanMenu();           break;
                case 3: prosesPesanan();           break;
                case 4: lihatRiwayatStruk();       break;
                case 5: demonstrasiPolimorfisme(); break;
                case 6:
                    System.out.println("\n  Terima kasih! Sampai jumpa lagi.\n");
                    berjalan = false;
                    break;
                default:
                    System.out.println("  [!] Pilihan tidak valid. Coba lagi.");
            }
        }
        scanner.close();
    }

    private static void tampilkanHeader() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║    SISTEM MANAJEMEN RESTORAN         ║");
        System.out.println("  ║         RESTORAN NUSANTARA           ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println();
    }

    private static void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("  ┌─── MENU UTAMA ───────────────────────┐");
        System.out.println("  │  1. Tambah Item Menu                 │");
        System.out.println("  │  2. Tampilkan Menu Restoran          │");
        System.out.println("  │  3. Buat Pesanan Pelanggan           │");
        System.out.println("  │  4. Lihat Riwayat Struk              │");
        System.out.println("  │  5. Demo Polymorphism                │");
        System.out.println("  │  6. Keluar                           │");
        System.out.println("  └──────────────────────────────────────┘");
    }

    private static void tambahItemMenu() {
        System.out.println("\n  === TAMBAH ITEM MENU ===");
        System.out.println("  1. Makanan");
        System.out.println("  2. Minuman");
        System.out.println("  3. Item Diskon");
        int tipe = bacaInt("  Pilih jenis item (1-3): ");

        String nama  = bacaString("  Nama item         : ");
        double harga = bacaDouble("  Harga (Rp)        : ");

        MenuItem item = null;
        switch (tipe) {
            case 1:
                System.out.println("  (Kosongkan jenis untuk menggunakan default 'Umum')");
                System.out.print("  Jenis makanan     : ");
                String jenisMakanan = scanner.nextLine().trim();
                if (jenisMakanan.isEmpty()) {
                    item = new Makanan(nama, harga);
                } else {
                    item = new Makanan(nama, harga, jenisMakanan);
                }
                break;
            case 2:
                String jenisMinuman = bacaString("  Jenis minuman     : ");
                item = new Minuman(nama, harga, jenisMinuman);
                break;
            case 3:
                double diskon = bacaDouble("  Persentase diskon : ");
                item = new Diskon(nama, harga, diskon);
                break;
            default:
                System.out.println("  [!] Jenis item tidak valid.");
                return;
        }

        menu.tambahItem(item);
        System.out.println("  [OK] Item '" + nama + "' berhasil ditambahkan!");

        try {
            menu.simpanKeFile();
        } catch (IOException e) {
            System.out.println("  [!] Gagal menyimpan menu: " + e.getMessage());
        }
    }

    private static void tampilkanMenu() {
        System.out.println("\n  === DAFTAR MENU RESTORAN NUSANTARA ===");
        System.out.println("  Total item: " + menu.getJumlahItem());
        System.out.println("  ---------------------------------------");
        menu.tampilkanMenu();
    }

    private static void prosesPesanan() {
        if (menu.getJumlahItem() == 0) {
            System.out.println("\n  [!] Menu masih kosong. Tambahkan item menu terlebih dahulu.");
            return;
        }

        String namaPelanggan = bacaString("\n  Nama pelanggan: ");
        Pesanan pesanan = new Pesanan(namaPelanggan);

        System.out.println("  (Masukkan 0 untuk menyelesaikan pesanan)");

        boolean pesanLagi = true;
        while (pesanLagi) {
            System.out.println();
            System.out.println("  --- Menu Tersedia ---");
            menu.tampilkanMenu();

            int nomor = bacaInt("  Pilih nomor menu: ");
            if (nomor == 0) {
                pesanLagi = false;
            } else {
                try {
                    MenuItem item = menu.getItem(nomor - 1);

                    int jumlah = bacaInt("  Jumlah porsi   : ");
                    if (jumlah <= 0) {
                        System.out.println("  [!] Jumlah harus lebih dari 0.");
                        continue;
                    }

                    if (jumlah > 1) {
                        pesanan.tambahItem(item, jumlah);
                    } else {
                        pesanan.tambahItem(item);
                    }
                    System.out.println("  [OK] " + jumlah + "x '" + item.getNama() + "' ditambahkan.");
                } catch (MenuItemNotFoundException e) {
                    System.out.println("  [ERROR] " + e.getMessage());
                }
            }
        }

        if (pesanan.getJumlahItem() == 0) {
            System.out.println("  [INFO] Tidak ada item yang dipesan.");
            return;
        }

        pesanan.tampilkanStruk();
        try {
            pesanan.simpanStrukKeFile();
        } catch (IOException e) {
            System.out.println("  [!] Gagal menyimpan struk: " + e.getMessage());
        }
    }

    private static void lihatRiwayatStruk() {
        System.out.println("\n  === RIWAYAT STRUK PESANAN ===");
        try {
            Pesanan.muatStrukDariFile();
        } catch (IOException e) {
            System.out.println("  [!] Gagal memuat riwayat struk: " + e.getMessage());
        }
    }

    private static void demonstrasiPolimorfisme() {
        System.out.println("\n  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║         DEMONSTRASI POLYMORPHISM                 ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Array bertipe MenuItem[] menyimpan berbagai subclass:");
        System.out.println();

        MenuItem[] contohItem = new MenuItem[3];
        contohItem[0] = new Makanan("Nasi Goreng", 30000, "Nasi");
        contohItem[1] = new Minuman("Es Jeruk", 10000, "Jus");
        contohItem[2] = new Diskon("Paket Spesial", 60000, 25);

        System.out.println("  Memanggil tampilMenu() untuk setiap elemen:");
        System.out.println();

        for (int i = 0; i < contohItem.length; i++) {
            System.out.print("  [" + i + "] ");
            contohItem[i].tampilMenu();
        }

        System.out.println();
        System.out.println("  Menggunakan tipe interface Tampilable:");
        Tampilable[] tampilables = { contohItem[0], contohItem[1], contohItem[2] };
        for (Tampilable t : tampilables) {
            System.out.print("  -> ");
            t.tampilMenu();
        }
        System.out.println();
    }

    private static int bacaInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Input harus berupa angka bulat. Coba lagi.");
            }
        }
    }

    private static double bacaDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Input harus berupa angka. Coba lagi.");
            }
        }
    }

    private static String bacaString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("  [!] Input tidak boleh kosong.");
            }
        } while (input.isEmpty());
        return input;
    }
}
