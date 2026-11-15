import java.util.Stack;

/*
 * Nama : Raja Haikal Madjid Qoba
 * NIM  : 051614863
 */
public class DepthFirstSearch {

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
        System.out.println("Mencari angka " + n + " dengan DFS, mulai dari a1");
        System.out.println();

        int hasil = dfs(0, n);

        System.out.println();
        if (hasil == -1) {
            System.out.println("Angka " + n + " tidak ditemukan di graf.");
        }
    }

    public static int dfs(int awal, int n) {
        boolean[] dikunjungi = new boolean[nama.length];
        int[] asal = new int[nama.length];
        for (int i = 0; i < asal.length; i++) {
            asal[i] = -1;
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(awal);
        int langkah = 0;
        System.out.println("Isi awal stack: " + isiStack(stack));

        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (dikunjungi[node]) {
                System.out.println("Ambil " + nama[node] + " dari stack, sudah pernah dikunjungi, dilewati");
                continue;
            }
            dikunjungi[node] = true;
            langkah++;

            System.out.print("Langkah " + langkah + ": kunjungi " + nama[node] + " (nilai " + nilai[node] + "). ");
            if (nilai[node] == n) {
                System.out.println("Sama dengan " + n + ", DITEMUKAN!");
                System.out.println();
                System.out.println("Angka " + n + " ditemukan di node " + nama[node] + " setelah " + langkah + " node dikunjungi.");
                System.out.println("Jalur dari a1 : " + jalur(asal, node));
                return node;
            }
            System.out.println("Bukan " + n + ".");

            // dari indeks terbesar supaya a2 diambil sebelum a3
            for (int t = nama.length - 1; t >= 0; t--) {
                if (adj[node][t] == 1 && !dikunjungi[t]) {
                    stack.push(t);
                    asal[t] = node;
                }
            }
            System.out.println("          Isi stack sekarang: " + isiStack(stack));
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

    static String isiStack(Stack<Integer> stack) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < stack.size(); i++) {
            sb.append(nama[stack.get(i)]);
            if (i < stack.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]  (puncak paling kanan)").toString();
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
