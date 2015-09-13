package java_algorithm.algebra.linear;

/**
* MatMult.java -- 行列の積
*/

/**
* 行列の積を計算します。
* @see RMatrix
* @see OptMult
* @see UTMult
*/
public class MatMult {
    /**
    * 行列の積 c(n×m) = a(n×l)×b(l×m) を計算します。<br>
    * ここで，x(Y×Z)はY行Z列の行列xを意味します。<br>
    * aの列数がbの行数と異なる場合にはエラー終了します。
    * @param c 積abが入ります。
    * @param a 行列 a
    * @param b 行列 b
    */
    public static void getOnC(double[][] c, double[][] a, double[][] b) {
        // a[n][l] のとき，a.length = n, a[0].length = l になる。
        int n = a.length, l = a[0].length, m = b[0].length;

        if (l != b.length) throw new Error("積が計算できません");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                double s = 0;
                for (int k = 0; k < l; k++) s += a[i][k] * b[k][j];
                c[i][j] = s;
            }
    }

    /**
    * 正方行列の積 a(n×n) = a(n×n) x b(n×n) を計算します。<br>
    * aまたはbが正方行列でない場合はエラー終了します。
    * @param a 正方行列 a，積abが上書きされます。
    * @param b 正方行列 b
    */
    public static void getOnA(double[][] a, double[][] b) {
        int n = a.length;

        if (n != a[0].length || n != b.length || n != b[0].length)
            throw new Error("積が計算できません");

        double[] temp = new double[n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, temp, 0, n);
            for (int j = 0; j < n; j++) {
                double s = 0;
                for (int k = 0; k < n; k++) s += temp[k] * b[k][j];
                a[i][j] = s;
            }
        }
    }

    /**
    * 乱数を使った例題プログラムです。
    */
// テスト用 : mathutils.RMatrix.java 使用。
    public static void main(String[] args) {
        final int N = 3, L = 5, M = 4;
        String fmt = "8.1f";
        int i, j;
        double[][] a = new double[N][L];
        double[][] b = new double[L][M];
        double[][] c = new double[N][M];

        for (i = 0; i < N; i++)
            for (j = 0; j < L; j++)
                a[i][j] = 10 * Math.random();
        for (i = 0; i < L; i++)
            for (j = 0; j < M; j++)
                b[i][j] = 10 * Math.random();

        System.out.println("A");  RMatrix.print(a, L, fmt);
        System.out.println("B");  RMatrix.print(b, M, fmt);
        getOnC(c, a, b);
        System.out.println("AB"); RMatrix.print(c, M, fmt);

        double[][] d = new double[N][N];
        double[][] e = new double[N][N];
        double[][] f = new double[N][N];

        for (i = 0; i < N; i++)
            for (j = 0; j < N; j++) {
                d[i][j] = 10 * Math.random();
                e[i][j] = 10 * Math.random();
            }

        System.out.println("D");  RMatrix.print(d, N, fmt);
        System.out.println("E");  RMatrix.print(e, N, fmt);
        getOnC(f, d, e);
        getOnA(d, e);
        System.out.println("DE by getOnC()");  RMatrix.print(f, N, fmt);
        System.out.println("DE by getOnA()");  RMatrix.print(d, N, fmt);
    }
}