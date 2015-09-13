package java_algorithm.equation.algebraic;

/*
* GaussJordan.java -- Gauss (ガウス)--Jordan (ジョルダン) 法
* class RMatrix, RVector を使う。
*/

import java_algorithm.algebra.linear.RMatrix;
import java_algorithm.datatype.string.NumberFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* 連立一次方程式を Gauss-Jordan 法で解きます。
*@see RMatrix
*@see java_algorithm.algebra.linear.RVector
*@see NumberFormatter
*/
public class GaussJordan {

    /**
    * 連立一次方程式を Gauss-Jordan 法で解きます。
    *@param a <code>a<sub>ij</sub> = a[i-1][j-1], b<sub>j</sub> = a[j-1][n]<br></code>
    *解 x<sub>j</sub> は <code>a[j-1][n]</code> に上書きされます。
    */
    public static void solve(double[][] a) {
        int n = a.length; // n=a[][]の行数，すなわち未知数の数

        for (int k = 0; k < n; k++) {
            for (int j = k + 1; j <= n; j++) a[k][j] /= a[k][k];
            for (int i = 0; i < n; i++)
                if (i != k)
                    for (int j = k + 1; j <= n; j++)
                        a[i][j] -= a[i][k] * a[k][j];
        }
    }

    /**
    * 例題プログラムです。
    *n = と聞いてきますから未知数の個数を入力してください。<br>
    *<b>&quot;解と, 解を代入したときの両辺の差&quot;</b>を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        NumberFormatter fmt7f = new NumberFormatter("13.7f"),
                        fmt7e = new NumberFormatter("18.7e"),
                        fmt3d = new NumberFormatter("3d");
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double[][] a = new double [n][n + 1], b = new double [n][n + 1];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = b[i][j] = Math.random() - Math.random();

        for (int i = 0; i < n; i++)
            a[i][n] = b[i][n] = Math.random() - Math.random();

        System.out.println("係数行列 (右辺も含む)");
        RMatrix.print(a, 10, "7.3f");
        solve(a);
        System.out.println("解と, 解を代入したときの両辺の差");
        for (int i = 0; i < n; i++) {
            double s = b[i][n];
            for (int j = 0; j < n; j++) s -= b[i][j] * a[j][n];
            System.out.println(
                    fmt3d.toString(i) + ": " + fmt7f.toString(a[i][n]) + " " + fmt7e.toString(s));
        }
    }
}