import java.util.Arrays;
public class CountingSortDescending {

    static int jumlahLangkah = 0;

    public static void main(String[] args) {
        int a1 = 4, a2 = 8, a3 = 1, a4 = 6, a5 = 4, a6 = 9, a7 = 2, a8 = 6;
        int[] data = {a1, a2, a3, a4, a5, a6, a7, a8};

        System.out.println("Data awal    : " + Arrays.toString(data));
        System.out.println();
        int[] hasil = countingSort(data);
        System.out.println();
        System.out.println("Data terurut : " + Arrays.toString(hasil));

        int n = data.length;
        int k = hasil[0] + 1;
        System.out.println();
        System.out.println("Analisa kinerja:");
        System.out.println("Jumlah data (n)              = " + n);
        System.out.println("Rentang nilai (k)            = " + k + " (nilai 0 sampai " + (k - 1) + ")");
        System.out.println("Jumlah langkah perulangan    = " + jumlahLangkah);
        System.out.println("Perbandingan antar data      = 0 (tidak membandingkan data)");
    }

    public static int[] countingSort(int[] A) {
        // Langkah 1: cari nilai terbesar untuk menentukan ukuran array Count
        int max = A[0];
        for (int i = 0; i < A.length; i++) {
            jumlahLangkah++;
            max = Math.max(max, A[i]);
        }
        System.out.println("Langkah 1, nilai terbesar = " + max + ", maka ukuran Count = " + (max + 1));

        // Langkah 2: hitung berapa kali setiap nilai muncul
        int[] Count = new int[max + 1];
        for (int i = 0; i < A.length; i++) {
            jumlahLangkah++;
            Count[A[i]]++;
        }
        cetakCount("Langkah 2, jumlah kemunculan", Count);

        // Langkah 3: jumlahkan dari nilai TERBESAR ke terkecil
        // Count[v] sekarang = banyaknya data yang nilainya >= v
        for (int v = max - 1; v >= 0; v--) {
            jumlahLangkah++;
            Count[v] = Count[v] + Count[v + 1];
        }
        cetakCount("Langkah 3, jumlah kumulatif", Count);

        // Langkah 4: tempatkan data ke posisinya, ditelusuri dari belakang supaya stabil
        System.out.println();
        System.out.println("Langkah 4, penempatan data:");
        int[] Result = new int[A.length];
        for (int i = A.length - 1; i >= 0; i--) {
            jumlahLangkah++;
            Count[A[i]]--;
            Result[Count[A[i]]] = A[i];
            System.out.println("  A[" + i + "] = " + A[i] + " -> Result[" + Count[A[i]] + "]   "
                    + Arrays.toString(Result));
        }
        return Result;
    }

    // Menampilkan isi array Count beserta nilai yang diwakili setiap indeks
    static void cetakCount(String judul, int[] Count) {
        StringBuilder nilai = new StringBuilder();
        StringBuilder isi = new StringBuilder();
        for (int v = 0; v < Count.length; v++) {
            nilai.append(String.format("%3d", v));
            isi.append(String.format("%3d", Count[v]));
        }
        System.out.println(judul + ":");
        System.out.println("  nilai :" + nilai);
        System.out.println("  Count :" + isi);
    }
}
