import java.util.LinkedList;
import java.util.Queue;

/*
 * Nama : Raja Haikal Madjid Qoba
 * NIM  : 051614863
 */
public class BreadthFirstSearch {

    static String[] nama = {"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"};

    // 63 = 2 digit terakhir NIM
    static int[] nilai = {27, 5, 14, 88, 41, 9, 36, 63};

    static int[][] adj = {
        //  a1 a2 a3 a4 a5 a6 a7 a8
        {   0, 1, 1, 0, 0, 0, 0, 0 },
        {   0, 0, 0, 1, 1, 0, 0, 0 },
        {   0, 0, 0, 0, 0, 1, 1, 0 },
        {   0, 0, 0, 0, 0, 0, 0, 1 },
        {   0, 0, 0, 0, 0, 1, 0, 0 },
        {   0, 0, 0, 0, 0, 0, 0, 0 },
        {   0, 0, 0, 0, 0, 0, 0, 1 },
        {   1, 0, 0, 0, 0, 0, 0, 0 }   // a8 ke a1
    };

    public static void main(String[] args) {
        int n = 63;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        tampilkanGraf();
        System.out.println();
        System.out.println("Mencari angka " + n + " dengan BFS, mulai dari a1");
        System.out.println();

        int hasil = bfs(0, n);

        System.out.println();
        if (hasil == -1) {
            System.out.println("Angka " + n + " tidak ditemukan di graf.");
        }
    }

    public static int bfs(int awal, int n) {
        // ditandai saat masuk queue supaya tidak masuk dua kali
        boolean[] ditandai = new boolean[nama.length];
        int[] asal = new int[nama.length];
        int[] level = new int[nama.length];
        for (int i = 0; i < asal.length; i++) {
            asal[i] = -1;
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(awal);
        ditandai[awal] = true;
        int langkah = 0;
        System.out.println("Isi awal queue: " + isiQueue(queue));

        while (!queue.isEmpty()) {
            int node = queue.poll();
            langkah++;

            System.out.print("Langkah " + langkah + ": kunjungi " + nama[node] + " (nilai " + nilai[node]
                    + ", level " + level[node] + "). ");
            if (nilai[node] == n) {
                System.out.println("Sama dengan " + n + ", DITEMUKAN!");
                System.out.println();
                System.out.println("Angka " + n + " ditemukan di node " + nama[node] + " setelah " + langkah + " node dikunjungi.");
                System.out.println("Jalur dari a1 : " + jalur(asal, node));
                return node;
            }
            System.out.println("Bukan " + n + ".");

            for (int t = 0; t < nama.length; t++) {
                if (adj[node][t] == 1) {
                    if (ditandai[t]) {
                        System.out.println("          " + nama[t] + " sudah pernah masuk queue, tidak dimasukkan lagi");
                    } else {
                        queue.add(t);
                        ditandai[t] = true;
                        asal[t] = node;
                        level[t] = level[node] + 1;
                    }
                }
            }
            System.out.println("          Isi queue sekarang: " + isiQueue(queue));
        }
        return -1;
    }

    static void tampilkanGraf() {
        System.out.println("Nilai node:");
        for (int i = 0; i < nama.length; i++) {
            System.out.print(nama[i] + " = " + nilai[i] + "   ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Adjacency matrix:");
        System.out.print("    ");
        for (String s : nama) {
            System.out.print(s + " ");
        }
        System.out.println();
        for (int i = 0; i < nama.length; i++) {
            System.out.print(nama[i] + "  ");
            for (int j = 0; j < nama.length; j++) {
                System.out.print(" " + adj[i][j] + " ");
            }
            System.out.println();
        }
    }

    static String isiQueue(Queue<Integer> queue) {
        StringBuilder sb = new StringBuilder("[");
        int i = 0;
        for (int node : queue) {
            sb.append(nama[node]);
            if (i < queue.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        return sb.append("]  (depan paling kiri)").toString();
    }

    static String jalur(int[] asal, int tujuan) {
        String hasil = nama[tujuan];
        int node = asal[tujuan];
        while (node != -1) {
            hasil = nama[node] + " > " + hasil;
            node = asal[node];
        }
        return hasil;
    }
}
