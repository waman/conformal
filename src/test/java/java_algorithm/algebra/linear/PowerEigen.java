package java_algorithm.algebra.linear;

/**
* PowerEigen.java -- 累乗法
*/

import java_algorithm.datatype.string.NumberFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
*累乗法により行列の固有値・固有ベクトルを求めるプログラムです。
*@see Jacobi
*@see Tridiagonalize
*@see QR
*@see RVector
*@see RMatrix
*/

public class PowerEigen {

    static final double EPS    = 1E-6;  /* 固有ベクトルの許容誤差 */
    static final int  MAX_ITER =  200;  /* 最大繰返し数 */

    /**
    *<code>a[0..n-1][0..n-1]</code>に入った対称行列 A から，m個の固有値と固有ベクトルを累乗法で求めます。<br>
    *<code>lambda[k]</code>に固有値が，それに対応する固有ベクトルは <code>x[k][0..n-1]</code> に入ります。
    *@return 収束した固有ベクトルの数
    */
    public static int solve(int m, double[][] a, double[] lambda, double[][] x) {
        int n = a.length;
        int kk = m;             /* 実際に求められた固有値・固有ベクトルの個数 */
        double[] y = new double[n];

        for (int k = 0; k < m; k++) { /* \texttt{k} 番目の固有値・固有ベクトルを求める */
            int iter = 0;
            double d = 0, d1, e, s = 0, s1, xk[] = x[k];

            Arrays.fill(xk, 1 / Math.sqrt(n));   /* 大きさ1の初期値ベクトル */
            do {
                d1 = d;  s1 = s;  s = e = 0;
                for (int i = 0; i < n; i++) {
                    double t = 0;
                    for (int j = 0; j < n; j++) t += a[i][j] * xk[j];
                    y[i] = t;  s += t * t;  /* $y = Ax$ */
                }
                s = Math.sqrt(s);  if (s1 < 0) s = -s;  /* $s = \pm \| y \|$ */
                for (int i = 0; i < n; i++) {
                    double t = y[i] / s, u = xk[i] - t;
                    e += u * u;  xk[i] = t;  /* \texttt{xk[]}: 固有ベクトル */
                }
                if (e > 2) s = -s;  /* ベクトルが反転したなら固有値は負 */
                d = Math.sqrt(e);  d1 -= d;
// テスト用///
                NumberFormatter fmt   = new NumberFormatter("12.8f");
                NumberFormatter fmt3d = new NumberFormatter("3d");
                System.out.println("iter = " + fmt3d.toString(iter) + " lambda[" + k + "]=" + fmt.toString(s) + " "
                    + "||x' - x|| = " + fmt.toString(e));
/////////////
            } while (++iter < MAX_ITER && e > EPS * d1);
            if (iter >= MAX_ITER && kk == m) kk = k;
            lambda[k] = s;  /* 固有値 */
            if (k < m - 1)
                for (int i = 0; i < n; i++)
                    for (int j = 0; j < n; j++)
                        a[i][j] -= s * xk[i] * xk[j];
        }
        return kk;  /* 収束した固有ベクトルの数 */
    }

// テスト用 main()
    /**
    * 乱数を使った例題プログラムです。<br>
    *<b>&quot;n = &quot;</b>と聞いてきますから2以上の整数値(次数)を入力してください。<br>
    *元の行列 A，収束の様子，収束した固有ベクトルの数，固有値，二乗平均誤差，固有ベクトルの直交性を
    *出力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new
            BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double[][]  a   = new double[n][n],
                    b   = new double[n][n],
                    x   = new double[n][n];
        double[] lambda = new double[n];

        for (int i = 0; i < n ; i++)
            for (int j = 0; j <= i; j++)
                a[i][j] = a[j][i] =
                b[i][j] = b[j][i] = Math.random() - Math.random();
        RMatrix.print(a, 5, "14.8f");
        int m = solve(n, a, lambda, x);

        double s, e = 0;
        for (int i = 0; i < n ; i++)
            for (int j = 0; j < n; j++) {
                s = b[i][j];
                for (int k = 0; k < n ; k++)
                    s -= lambda[k] * x[k][i] * x[k][j];
            e += s * s;
        }

        NumberFormatter fmt   = new NumberFormatter("14.5e");
        System.out.println("\n固有ベクトルの直交性の確認");
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                System.out.println("x[" + i + "]・x[" + j + "] = "
                    + fmt.toString(RVector.innerProduct(x[i], x[j])));

        System.out.println("収束した固有ベクトルの数: " + m);
        System.out.println("固有値:");
        RVector.print(lambda, 5, "14.8f");
        System.out.println("二乗平均誤差: " + fmt.toString(Math.sqrt(e / (n * n))));
    }
}