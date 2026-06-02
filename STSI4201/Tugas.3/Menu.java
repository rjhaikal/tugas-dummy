import java.util.ArrayList;
import java.io.*;

public class Menu {
    private ArrayList<MenuItem> daftarMenu;
    private static final String FILE_MENU = "menu.txt";

    public Menu() {
        daftarMenu = new ArrayList<>();
    }

    public void tambahItem(MenuItem item) {
        daftarMenu.add(item);
    }

    public MenuItem getItem(int index) throws MenuItemNotFoundException {
        if (index < 0 || index >= daftarMenu.size()) {
            throw new MenuItemNotFoundException(
                "Item menu tidak ditemukan pada nomor: " + (index + 1) +
                ". Menu hanya berisi " + daftarMenu.size() + " item.");
        }
        return daftarMenu.get(index);
    }

    public int getJumlahItem() {
        return daftarMenu.size();
    }

    public void tampilkanMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("  Menu kosong! Silakan tambahkan item terlebih dahulu.");
            return;
        }
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.print("  " + (i + 1) + ". ");
            daftarMenu.get(i).tampilMenu();
        }
    }

    public void simpanKeFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_MENU))) {
            for (MenuItem item : daftarMenu) {
                writer.write(item.toFileString());
                writer.newLine();
            }
        }
        System.out.println("  [OK] Menu berhasil disimpan ke '" + FILE_MENU + "'.");
    }

    public void muatDariFile() throws IOException {
        File file = new File(FILE_MENU);
        if (!file.exists()) {
            System.out.println("  [INFO] File '" + FILE_MENU + "' tidak ditemukan. Memulai dengan menu kosong.");
            return;
        }
        daftarMenu.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_MENU))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String tipe  = parts[0];
                    String nama  = parts[1];
                    double harga = Double.parseDouble(parts[2]);
                    switch (tipe) {
                        case "Makanan":
                            daftarMenu.add(new Makanan(nama, harga, parts[3]));
                            break;
                        case "Minuman":
                            daftarMenu.add(new Minuman(nama, harga, parts[3]));
                            break;
                        case "Diskon":
                            daftarMenu.add(new Diskon(nama, harga, Double.parseDouble(parts[3])));
                            break;
                    }
                }
            }
        }
        System.out.println("  [OK] " + daftarMenu.size() + " item menu berhasil dimuat dari '" + FILE_MENU + "'.");
    }
}
