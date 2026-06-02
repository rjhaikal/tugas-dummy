import java.util.ArrayList;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pesanan {

    private ArrayList<MenuItem> itemPesanan;
    private String namaPelanggan;
    private static final String FILE_STRUK = "struk_pesanan.txt";

    public Pesanan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
        this.itemPesanan   = new ArrayList<>();
    }

    public void tambahItem(MenuItem item) {
        itemPesanan.add(item);
    }

    public void tambahItem(MenuItem item, int jumlah) {
        for (int i = 0; i < jumlah; i++) {
            itemPesanan.add(item);
        }
    }

    public int getJumlahItem()       { return itemPesanan.size(); }
    public String getNamaPelanggan() { return namaPelanggan; }

    public double hitungTotal() {
        double total = 0;
        for (MenuItem item : itemPesanan) {
            if (item instanceof Diskon) {
                total += ((Diskon) item).getHargaSetelahDiskon();
            } else {
                total += item.getHarga();
            }
        }
        return total;
    }

    private String getWaktu() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public void tampilkanStruk() {
        System.out.println();
        System.out.println("  ============================================");
        System.out.println("         STRUK PESANAN RESTORAN NUSANTARA     ");
        System.out.println("  ============================================");
        System.out.println("  Pelanggan : " + namaPelanggan);
        System.out.println("  Waktu     : " + getWaktu());
        System.out.println("  --------------------------------------------");
        for (int i = 0; i < itemPesanan.size(); i++) {
            MenuItem item = itemPesanan.get(i);
            if (item instanceof Diskon) {
                Diskon d = (Diskon) item;
                System.out.printf("  %2d. %-22s (%.0f%% off)  Rp%,.0f%n",
                        i + 1, item.getNama(), d.getPersentaseDiskon(), d.getHargaSetelahDiskon());
            } else {
                System.out.printf("  %2d. %-30s  Rp%,.0f%n",
                        i + 1, item.getNama(), item.getHarga());
            }
        }
        System.out.println("  --------------------------------------------");
        System.out.printf("  TOTAL                                  Rp%,.0f%n", hitungTotal());
        System.out.println("  ============================================");
        System.out.println("       Terima kasih atas kunjungan Anda!      ");
        System.out.println("  ============================================");
        System.out.println();
    }

    public void simpanStrukKeFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_STRUK, true))) {
            writer.write("============================================");
            writer.newLine();
            writer.write("       STRUK PESANAN RESTORAN NUSANTARA     ");
            writer.newLine();
            writer.write("============================================");
            writer.newLine();
            writer.write("Pelanggan : " + namaPelanggan);
            writer.newLine();
            writer.write("Waktu     : " + getWaktu());
            writer.newLine();
            writer.write("--------------------------------------------");
            writer.newLine();
            for (int i = 0; i < itemPesanan.size(); i++) {
                MenuItem item = itemPesanan.get(i);
                if (item instanceof Diskon) {
                    Diskon d = (Diskon) item;
                    writer.write(String.format("%2d. %-22s (%.0f%% off)  Rp%,.0f",
                            i + 1, item.getNama(), d.getPersentaseDiskon(), d.getHargaSetelahDiskon()));
                } else {
                    writer.write(String.format("%2d. %-30s  Rp%,.0f",
                            i + 1, item.getNama(), item.getHarga()));
                }
                writer.newLine();
            }
            writer.write("--------------------------------------------");
            writer.newLine();
            writer.write(String.format("TOTAL                                  Rp%,.0f", hitungTotal()));
            writer.newLine();
            writer.write("============================================");
            writer.newLine();
            writer.write("     Terima kasih atas kunjungan Anda!");
            writer.newLine();
            writer.write("============================================");
            writer.newLine();
            writer.newLine();
        }
        System.out.println("  [OK] Struk berhasil disimpan ke '" + FILE_STRUK + "'.");
    }

    public static void muatStrukDariFile() throws IOException {
        File file = new File(FILE_STRUK);
        if (!file.exists()) {
            System.out.println("  [INFO] Belum ada riwayat struk pesanan.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_STRUK))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }
    }
}
