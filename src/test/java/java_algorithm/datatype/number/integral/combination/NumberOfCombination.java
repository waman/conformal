package java_algorithm.datatype.number.integral.combination;

/**
 *  組合せの数
 */

public class NumberOfCombination {

    public static int combination1(int n, int k) {
        if (k == 0 || k == n) return 1;
        // if (k == 1 || k == n - 1) return n;  // 少し高速化
        return combination1(n - 1, k - 1) + combination1(n - 1, k);
    }

    private static long a[] = new long[33];
    public static long combination2(int n, int k) {
        if (n - k < k) k = n - k;  // nCk = nC(n-k)
        if (k == 0) return 1;
        if (k == 1) return n;
        if (k > 33) return 0;  // エラー
        for (int i = 1; i < k; i++) a[i] = i + 2;  // (i+2)C(i+2-1)
        for (int i = 3; i <= n - k + 1; i++) {
            a[0] = i;  // iC1
            for (int j = 1; j < k; j++) a[j] += a[j - 1];
        }  // (i+j)C(j+1) = (i+j-1)C(j+1) + (i+j-1)Cj
        return a[k - 1];
    }

    private static String right(long num) {
        String s = "     " + num;
        return s.substring(s.length() - 6);
    }

    static void triangle() {
        final int N = 8;

        System.out.print("\n  k");
        for (int k = 0; k <= N; k++) System.out.print(right(k));
        System.out.print("\nn  ");
        for (int k = 0; k <= N; k++) System.out.print("------");
        System.out.println();
        for (int n = 0; n <= N; n++) {
            System.out.print(n + " |");
            for (int k = 0; k <= n; k++)
                System.out.print(right(combination2(n, k)));
            System.out.println();
        }
    }

    static void test() {
        final int N = 34;
        final int K = 8;

        System.out.println("方法1");
        for (int k = 0; k <= K; k++)
            System.out.println(
                    N + "C" + (k + " ").substring(0, 2) + " = " + combination1(N, k));
        System.out.println("方法2");
        for (int k = 0; k <= K; k++)
            System.out.println(
                    N + "C" + (k + " ").substring(0, 2) + " = " + combination2(N, k));
    }

    public static void main(String[] args) {
        triangle();
        test();
    }
}