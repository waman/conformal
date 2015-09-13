package java_algorithm.algebra.linear;

/*
* QRDecomposition.java -- QR分解
* class RMatrix, RVector を使う。
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/* \see 行列 */

/**
*行列をQR分解するプログラムです。
*@see RMatrix
*@see RVector
*/
public class QRDecomposition {

    /**
    *R<sup>T</sup>を求めます。
    *@param x 入力：X<sup>T</sup>，出力：R<sup>T</sup>を上書き
    */
    public static void getR(double[][] x) {
        int m = x.length, n = x[0].length;

        for (int k = 0; k < m; k++) {
            double[] v = x[k];
            double   u = Math.sqrt(RVector.innerProduct(k, v, v));
            if (v[k] < 0) u = -u;
            v[k] += u;  double t = 1 / (v[k] * u);
            for (int j = k + 1; j < m; j++) {
                double[] w = x[j];
                double   s = t * RVector.innerProduct(k, v, w); /* 内積 $\mbox{\tt v[k]}\times\mbox{\tt w[k]} + \cdots + \mbox{\tt v[n-1]}\times\mbox{\tt w[n-1]}$ */
                for (int i = k; i < n; i++) w[i] -= s * v[i];
            }
            v[k] = -u;
        }
    }

    /**
    *XとRからQを求めます。
    *@param x 入力：X<sup>T</sup>，出力：Q<sup>T</sup>を上書き
    *@param r 入力：R<sup>T</sup>
    */

    public static void getQ(double[][] x, double[][] r) {
        int m = x.length, n = x[0].length;

        for (int k = 0; k < m; k++) {
            for (int i = 0; i < n; i++) x[k][i] /= r[k][k];
            for (int j = k + 1; j < m; j++)
                for (int i = 0; i < n; i++)
                    x[j][i] -= r[j][k] * x[k][i];
        }
    }

    /**
    * 乱数を使ったデモンストレーション・プログラムです。<br>
    *<b>&quot;n = &quot;</b>, <b>&quot;m = &quot;</b> と聞いてきますから，3以上の整数を
    *入力してください。<br>
    *m &gt; n の場合は m = n にします。<br>
    *X<sup>T</sup>，Q<sup>T</sup>，R<sup>T</sup>と二乗平均誤差を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        System.out.print("m = ");
        int m = Integer.parseInt(in.readLine());
        if (m > n) m = n;
        double[][] x = new double[m][n], q = new double[m][n],
                   r = new double[m][n];

        for (int j = 0; j < m; j++)
            for (int i = 0; i < n; i++) // 負のものもセットされるように差をとる。
                x[j][i] = q[j][i] = r[j][i] = Math.random() - Math.random();

        // QR分解
        getR(r);
        getQ(q, r);
        // 行列の表示
        String format = "10.6f";
        int perLine = 7;

        System.out.println("X^T:");
        RMatrix.print(x, perLine, format);
        System.out.println("Q^T:");
        RMatrix.print(q, perLine, format);
        for (int j = 0; j < m; j++)
            for (int i = j + 1; i < n; i++) r[j][i] = 0; // 無意味な値が入っている部分を0にする。
        System.out.println("R^T:");
        RMatrix.print(r, m, 7, format);

        // $\|QR - X\|_F / mn$
        double t = 0;
        for (int j = 0; j < m; j++)
            for (int i = 0; i < n; i++) {
                double s = 0;
                for (int k = 0; k <= j; k++) s += q[k][i] * r[j][k];
                s -= x[j][i];  t += s * s;
            }
        System.out.println("二乗平均誤差: " + Math.sqrt(t / (m * n)));
    }
}