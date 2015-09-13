package java_algorithm.traditional.problem;

/**
 *  N王妃の問題
 */
public class NQueens {

    static int n;  // n×n の盤面
    static int solution = 0;
    static int[] x;
    static boolean[] a,  b,  c;

    private static void found() {
        System.out.println("\n解 " + (++solution));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                if (x[i] == j) System.out.print(" Q");
                else           System.out.print(" .");
            System.out.println();
        }
    }

    private static void search(int i) {
        for (int j = 0; j < n; j++)
            if (a[j] && b[i + j] && c[i - j + n - 1]) {
                x[i] = j;
                if (i < n - 1) {
                    a[j] = b[i + j] = c[i - j + n - 1] = false;
                    search(i + 1);
                    a[j] = b[i + j] = c[i - j + n - 1] = true;
                } else found();
            }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("使用法: java NQueens n"); return;
        }
        n = Integer.parseInt(args[0]);
        x = new int[n];
        a = new boolean[n];
        b = new boolean[2 * n - 1];
        c = new boolean[2 * n - 1];
        for (int i = 0; i < a.length; i++) a[i] = true;
        for (int i = 0; i < b.length; i++) b[i] = true;
        for (int i = 0; i < c.length; i++) c[i] = true;
        search(0);
    }
}