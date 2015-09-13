package java_algorithm.equation.algebraic;

/*
* GaussSeidel.java -- Gauss (ガウス)--Seidel (ザイデル) 法
* class RVector を使う。
*/

import java_algorithm.algebra.linear.RVector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* 連立一次方程式を Gauss-Seidel 法で解きます。
*@see java_algorithm.algebra.linear.RMatrix
*@see java_algorithm.datatype.string.NumberFormatter
*@see RVector
*@see GaussElimination
*@see Gauss3
*@see Gauss5
*/
public class GaussSeidel {

    /**
    *途中経過を出力しないなら false にします。
    */
    public static boolean verbose = true;
    static final double  EPS      = 1E-6; // 許容誤差
    static final int     MAX_ITER = 500;  // 最大繰返し数

    /**
    * 連立一次方程式 Ax = b を Gauss-Seidel 法で解きます。
    *@param a 行列A
    *@param b 右辺b
    *@param x 解，0に初期化しておきます。
    *@return 成功 <code>true</code>，収束せず <code>false</code>
    */
    public static boolean solve(double[][] a, double[] x, double[] b) {
        int n = b.length;

        for (int iter = 1; iter <= MAX_ITER; iter++) {
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                double s = b[i];
                for (int j = 0    ; j < i; j++) s -= a[i][j] * x[j];
                for (int j = i + 1; j < n; j++) s -= a[i][j] * x[j];
                s /= a[i][i];  // あらかじめ対角成分を1にしておけば不要
                if (ok && Math.abs(x[i] - s) > EPS * (1 + Math.abs(s)))
                    ok = false;
                x[i] = s;  // SOR法なら例えば x[i] += 1.2 * (s - x[i]);
            }
            if (verbose) {
                System.out.println(iter + ":");
                RVector.print(x, 7, "11.6f");
            }
            if (ok) return true;  // 成功
        }
        return false;  // 収束せず
    }

    /**
    * 例題プログラムです。
    *<b>&quot;n = &quot;</b>と聞いてきますから 2 以上の整数値を入力してください。<br>
    *途中経過を表示し，最後に表示される解はすべて 1.0 になります。
    */

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double[][] a = new double[n][n];
        double[] x = new double[n], b = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) a[i][j] = 1;
            a[i][i] = 2;  b[i] = n + 1;  x[i] = 0;
        }
        if (!solve(a, x, b)) System.out.println("収束しません");
        System.out.println("解 (正解はすべて 1)");
        RVector.print(x, 7, "11.6f");
    }
}