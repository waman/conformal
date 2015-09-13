package java_algorithm.traditional;

/**
 *  魔方陣
 */
public class MagicSquare {

    /** n は奇数 */
    public static void MagicSquare13579(int[][] a, final int n) {
        int k = 0;
        for (int i = - n / 2; i <= n / 2; i++)
            for (int j = 0; j < n; j++)
                a[(j - i + n) % n][(j + i + n) % n] = ++k;
    }

    /** n は4の倍数 */
    public static void MagicSquare48(int[][] a, final int n) {
        int k = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (((i + 1) & 2) == ((j + 1) & 2)) a[i][j] = ++k;
                else                a[n - 1 - i][n - 1 - j] = ++k;
    }

    /** n は4の倍数+2 かつ n!=2 */
    public static void MagicSquare610(int[][] a, final int n) {
        int k = n * 2 - 2;
        for (int i = 1; i < n - 1; i++)
            for (int j = 1; j < n - 1; j++)
                if ((i & 2) == (j & 2)) a[i][j] = ++k;
                else    a[n - 1 - i][n - 1 - j] = ++k;

        final int sum = n * n + 1;
        k = a[0][0]     = n - 2;            a[n - 1][n - 1] = sum - k;
        k = a[0][n - 1] = n - 1;            a[n - 1][0]     = sum - k;
        k = a[0][n - 2] = sum - 2 * n + 3;  a[n - 1][n - 2] = sum - k;
        k = a[n - 2][0] = 2 * n - 2;        a[n - 2][n - 1] = sum - k;

        for (int i = 1; i < n - 2; i++) {
            int j = ((i & 2) == 0) ? 0 : n - 1;
            k = a[j][i] = n - 2 - i;  a[n - 1 -j][i] = sum - k;
            k = a[i][j] = n - 1 + i;  a[i][n - 1 -j] = sum - k;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("使用法: java MagicSquare n");
            return;
        }
        final int n = Integer.parseInt(args[0]);
        if (n == 2) {
            System.out.println("2次の魔方陣は存在しません。");
            return;
        }

        int[][] a = new int[n][n];
        if (n % 2 == 1)      MagicSquare13579(a, n);
        else if (n % 4 == 0) MagicSquare48(a, n);
        else                 MagicSquare610(a, n);

        System.out.println("n = " + n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String s = "   " + a[i][j];
                System.out.print(s.substring(s.length() - 4));
            }
            System.out.println();
        }
    }
}