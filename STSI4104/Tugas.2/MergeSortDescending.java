import java.util.Arrays;

public class MergeSortDescending {

    static int jumlahPerbandingan = 0;

    public static void main(String[] args) {
        int a1 = 27, a2 = 5, a3 = 63, a4 = 14, a5 = 88, a6 = 41, a7 = 9, a8 = 36;
        int[] data = {a1, a2, a3, a4, a5, a6, a7, a8};

        System.out.println("Data awal    : " + Arrays.toString(data));
        System.out.println();
        System.out.println("Proses Merge Sort:");
        mergeSort(data, 0);

        int n = data.length;
        int nLogN = (int) (n * (Math.log(n) / Math.log(2)));
        System.out.println();
        System.out.println("Data terurut : " + Arrays.toString(data));
        System.out.println();
        System.out.println("Analisa kinerja:");
        System.out.println("Jumlah data (n)           = " + n);
        System.out.println("Jumlah perbandingan       = " + jumlahPerbandingan);
        System.out.println("Batas atas n log2 n       = " + nLogN);
    }

    public static void mergeSort(int[] list, int level) {
        if (list.length > 1) {
            String indent = "  ".repeat(level);

            // Bagi: separuh pertama
            int[] firstHalf = new int[list.length / 2];
            System.arraycopy(list, 0, firstHalf, 0, firstHalf.length);

            // Bagi: separuh kedua
            int[] secondHalf = new int[list.length - firstHalf.length];
            System.arraycopy(list, firstHalf.length, secondHalf, 0, secondHalf.length);

            System.out.println(indent + "Bagi   " + Arrays.toString(list)
                    + " -> " + Arrays.toString(firstHalf) + " " + Arrays.toString(secondHalf));

            mergeSort(firstHalf, level + 1);
            mergeSort(secondHalf, level + 1);

            // Gabung: kedua bagian sudah terurut, satukan kembali
            int[] temp = merge(firstHalf, secondHalf);
            System.arraycopy(temp, 0, list, 0, temp.length);

            System.out.println(indent + "Gabung " + Arrays.toString(firstHalf) + " "
                    + Arrays.toString(secondHalf) + " -> " + Arrays.toString(list));
        }
    }

    public static int[] merge(int[] list1, int[] list2) {
        int[] temp = new int[list1.length + list2.length];
        int i = 0, j = 0, k = 0;

        while (i < list1.length && j < list2.length) {
            jumlahPerbandingan++;
            // Ambil yang LEBIH BESAR lebih dulu, inilah yang membuat hasilnya descending
            if (list1[i] >= list2[j]) {
                temp[k++] = list1[i++];
            } else {
                temp[k++] = list2[j++];
            }
        }

        // Sisa data langsung disalin karena sudah pasti terurut
        while (i < list1.length) {
            temp[k++] = list1[i++];
        }
        while (j < list2.length) {
            temp[k++] = list2[j++];
        }
        return temp;
    }
}
