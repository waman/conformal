package java_algorithm.algebra.linear;

/**
* UTMult.java -- 行列の積
*/

/**
* 上三角行列の積を計算します。
* @see RMatrix
* @see MatMult
* @see OptMult
*/

public class UTMult {
    /**
    * 上三角行列の積 a(n×n) = a(n×n) × b(n×n) を計算します。<br>
    * aまたはbが正方行列でない場合はエラー終了します。
    * @param a 正方行列 a，積abが上書きされます。
    * @param b 正方行列 b
    */
    public static void getOnA(double[][] a, double[][] b) { /* 上三角行列の積 */
        int n = a.length;

        if (n != a[0].length || n != b.length || n != b[0].length)
            throw new Error("積が計算できません");

        for (int i = n - 1; i >= 0; i--)
            for (int j = n - 1; j >= i; j--) {
                double s = 0;
                for (int k = i; k <= j; k++) s += a[i][k] * b[k][j];
                a[i][j] = s;
            }
    }
    /**
    * 乱数を使った例題プログラムです。
    */
// テスト用 : mathutils.RMatrix.java, MatMult.java 使用。
    public static void main(String[] args) {
        final int N = 4;
        String fmt = "10.4f";
        double[][] a = new double[N][N];
        double[][] b = new double[N][N];
        double[][] c = new double[N][N];
        double[][] d = new double[N][N];

        for (int i = 0; i < N; i++)
            for (int j = i; j < N; j++) {
                d[i][j] = a[i][j] = 10 * Math.random();
                b[i][j] = 10 * Math.random();
            }
        double[][] rm = RMatrix.multiply(a, b);

        System.out.println("A");  RMatrix.print(a, N, fmt);
        System.out.println("B");  RMatrix.print(b, N, fmt);
        MatMult.getOnC(c, a, b);
        MatMult.getOnA(d, b);
        getOnA(a, b);
        System.out.println("AB by getOnA()");          RMatrix.print(a, N, fmt);
        System.out.println("AB by MatMult.getOnC()");  RMatrix.print(c, N, fmt);
        System.out.println("AB by MatMult.getOnA()");  RMatrix.print(d, N, fmt);
        System.out.println("AB by RMatrix.multiply()");  RMatrix.print(rm, N, fmt);
    }
}