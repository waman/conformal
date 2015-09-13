package java_algorithm.equation.algebraic;

/*
* LUDecomposition.java -- LU分解
* main() で class RMatrix, RVector, NumberFormatter を使う。
* mathutils.LU.java; とほぼ同じ。
*/

import java_algorithm.algebra.linear.RMatrix;
import java_algorithm.algebra.linear.RVector;
import java_algorithm.datatype.string.NumberFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
*LU分解で連立１次方程式を解きます。
*@see RMatrix
*@see RVector
*@see NumberFormatter
*/
public class LUDecomposition {

    static double lu(double[][] a, int[] ip) {
        int n = a.length;
        double[] weight = new double[n];    // weight[0..n-1] の記憶領域確保

        for (int k = 0; k < n; k++) {   // 各行について
            ip[k] = k;                  // 行交換情報の初期値
            double u = 0;               // その行の絶対値最大の要素を求める
            for (int j = 0; j < n; j++) {
                double t = Math.abs(a[k][j]);
                if (t > u) u = t;
            }
            if (u == 0) return 0;   // 0 なら行列はLU分解できない
            weight[k] = 1 / u;      // 最大絶対値の逆数
        }
        double det = 1;                 // 行列式の初期値
        for (int k = 0; k < n; k++) {   // 各行について
            double u = -1;
            int m = 0;
            for (int i = k; i < n; i++) {// より下の各行について
                int ii = ip[i];          // 重み×絶対値 が最大の行を見つける
                double t = Math.abs(a[ii][k]) * weight[ii];
                if (t > u) {  // 初回は u = -1 && t >= 0 であるから{}は必ず実行される。
                    u = t;
                    m = i;
                }
            }
            int ik = ip[m];
            if (m != k) {
                ip[m] = ip[k];
                ip[k] = ik; // 行番号を交換
                det = -det;  // 行を交換すれば行列式の符号が変わる
            }
            u = a[ik][k];
            det *= u;    // 対角成分
            if (u == 0) return 0;       // 0 なら行列はLU分解できない
            for (int i = k + 1; i < n; i++) {// Gauss消去法
                int ii = ip[i];
                double t = (a[ii][k] /= u);
                for (int j = k + 1; j < n; j++) a[ii][j] -= t * a[ik][j];
            }
        }
        return det;           // 戻り値は行列式
    }

    /**
    *LU分解の結果を使って連立１次方程式 Ax = b を解きます。
    *右辺だけが異なる複数の連立１次方程式を解くのに便利です。
    *@param a LU分解した係数行列
    *@param b 右辺
    *@param x 解
    *@param ip <code>lu()</code>からの行交換情報
    */
    public static void solve(double[][] a, double[] b, int[] ip, double[] x) {
        int n = a.length;

        for (int i = 0; i < n; i++) {   // Gauss消去法の残り
            int ii = ip[i];
            double t = b[ii];
            for (int j = 0; j < i; j++) t -= a[ii][j] * x[j];
            x[i] = t;
        }
        for (int i = n - 1; i >= 0; i--) { // 後退代入
            double t = x[i];
            int ii = ip[i];
            for (int j = i + 1; j < n; j++) t -= a[ii][j] * x[j];
            x[i] = t / a[ii][i];
        }
    }

    /**
    *LU分解の結果を使って連立１次方程式を解きます。
    *@param a 係数行列
    *@param b 右辺
    *@param x 解
    *@return 行列式
    */
    public static double gauss(double[][] a, double[] b, double[] x) {
        int n = a.length;
        int[] ip = new int[n]; // 行交換の情報
        double det = lu(a, ip);  // LU分解して行列式を受け取る

        if (det != 0) solve(a, b, ip, x);  // LU分解の結果を使って連立方程式を解く
        return det;                        // 戻り値は行列式
    }

    /**
    * 乱数を使った例題プログラムです。<br>
    *<strong>&quot;行列の次数 n = &quot;</strong>と聞いてきますから2以上の
    *整数値を入力してください。<br>
    *行列式の値および解と, 解を代入したときの両辺の差を出力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("行列の次数 n = ");
        int n = Integer.parseInt(in.readLine());    // 行列の次数を入力
        double[][] a = new double[n][n], aSave = new double[n][n];
        double[] b = new double[n], bSave = new double[n], x = new double[n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = aSave[i][j] = Math.random() - Math.random();

        System.out.println("係数行列");
        RMatrix.print(a, 10, "7.3f");
        for (int i = 0; i < n; i++)
            b[i] = bSave[i] = Math.random() - Math.random();
        System.out.println("右辺");
        RVector.print(b, 10, "7.3f");

        double det = gauss(a, b, x);  // Gauss法で $Ax=b$ を解く
        System.out.println("行列式 = " + det);
        System.out.println("解と, 解を代入したときの両辺の差");

        NumberFormatter fmtf = new NumberFormatter("16.7f"),
                        fmte = new NumberFormatter("16.7e"),
                        fmtd = new NumberFormatter("2d");
        for (int i = 0; i < n; i++) {
            double s = bSave[i];
            for (int j = 0; j < n; j++) s -= aSave[i][j] * x[j];
            System.out.println(
                    fmtd.toString(i) + ":" + fmtf.toString(x[i]) + fmte.toString(s));
        }
    }
}