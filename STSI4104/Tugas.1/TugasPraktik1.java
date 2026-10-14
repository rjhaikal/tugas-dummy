import java.util.LinkedList;

/*
 * Tugas Praktik 1 Struktur Data
 * Nama : Raja Haikal Madjid Qoba
 * NIM  : 051614863  (2 digit awal = 05, 2 digit akhir = 63)
 */
public class TugasPraktik1 {
    public static void main(String[] args) {

        // Soal 1: variabel bertipe integer bernama StrukturBaris
        int StrukturBaris = 3;

        // Soal 2: variabel bertipe String bernama KataBaru berisi nama awal
        String KataBaru = "Raja";

        // Soal 3: array satu dimensi bertipe integer bernama empatAngka
        // 05 ditulis 5, karena tipe int tidak menyimpan angka nol di depan
        int[] empatAngka = {5, 10, 20, 63};

        // Soal 4: array dua dimensi bertipe String bernama Angka, 3 baris x 3 kolom
        String[][] Angka = {
            {"05", "3", "5"},
            {"14", "19", "20"},
            {"22", "27", "63"}
        };

        // Soal 5: linked list bernama listAngka
        LinkedList<Integer> listAngka = new LinkedList<>();
        listAngka.add(5);
        listAngka.add(19);
        listAngka.add(44);
        listAngka.add(60);
        listAngka.add(63);

        // Menampilkan hasil
        System.out.println("=== Soal 1: Variabel Integer ===");
        System.out.println("StrukturBaris = " + StrukturBaris);

        System.out.println();
        System.out.println("=== Soal 2: Variabel String ===");
        System.out.println("KataBaru = " + KataBaru);

        System.out.println();
        System.out.println("=== Soal 3: Array Satu Dimensi empatAngka ===");
        for (int i = 0; i < empatAngka.length; i++) {
            System.out.println("empatAngka[" + i + "] = " + empatAngka[i]);
        }

        System.out.println();
        System.out.println("=== Soal 4: Array Dua Dimensi Angka (3 x 3) ===");
        for (int baris = 0; baris < Angka.length; baris++) {
            for (int kolom = 0; kolom < Angka[baris].length; kolom++) {
                System.out.print(Angka[baris][kolom] + "\t");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("=== Soal 5: Linked List listAngka ===");
        System.out.println("Isi listAngka = " + listAngka);
        System.out.println("Elemen pertama = " + listAngka.getFirst());
        System.out.println("Elemen terakhir = " + listAngka.getLast());
        System.out.println("Jumlah elemen = " + listAngka.size());
    }
}
