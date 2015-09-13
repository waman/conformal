package java_algorithm.datatype.number.integral.combination;

/**
 *  置換の符号
 */

public class PermutationSign {

    static int sign(int n, int v[]) {
        int[] w = new int[n + 1];
        for (int i = 1; i <= n; i++) w[v[i]] = i;

        int p = 1;
        for (int i = 1; i <= n - 1; i++) {
            int j = v[i];
            if (j != i) {
                v[w[i]] = j;
                w[j] = w[i];
                p = -p;
            }
        }
        return p;
    }

    static void test1(int n) {
        System.out.println("辞書式順序:");
        NextPermutation p = new NextPermutation(n);
        int[] v = new int[n + 1];

        int count = 0;
        do {
            System.out.print((++count) + ":" + p);
            System.arraycopy(p.a, 1, v, 1, n);
            System.out.println((sign(n, v) == 1) ? "  偶置換" : "  奇置換");
        } while (p.next());
    }

    static void test2(int n) {
        System.out.println("互換による順列生成:");
        int[] a = new int[n + 1], c = new int[n + 2], v = new int[n + 1];
        for (int i = 1; i <= n; i++) a[i] = c[i] = i;
        c[n + 1] = 0;  // c[n + 1]≠1 は番人

        int count = 0;
        for (int k = 2; k <= n; c[k]--) {
            int i;
            if ((k % 2) == 1) i = 1;  else i = c[k];

            int swap = a[k];
            a[k] = a[i];
            a[i] = swap;
            System.out.print((++count) + ":");

            for (i = 1; i <= n; i++) System.out.print(" "+ (v[i] = a[i]));
            System.out.println((sign(n, v) == 1) ? "  偶置換" : "  奇置換");
            for (k = 2; c[k] == 1; k++) c[k] = k;
        }
    }

    public static void main(String[] args) {
        test1(4);
        test2(4);
    }
}