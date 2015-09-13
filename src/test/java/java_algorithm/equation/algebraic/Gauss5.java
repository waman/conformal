package java_algorithm.equation.algebraic;

/*
* Gauss5.java -- 5重対角な連立方程式
* class RVector を使う。
*/

import java_algorithm.algebra.linear.RVector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* 5重対角な連立一次方程式を Gauss 法で解きます。
*@see GaussElimination
*@see Gauss3
*@see GaussSeidel
*/
public class Gauss5 {

    /**
    * 連立一次方程式を Gauss 法で解きます。
    */
    public static void solve(
            double[] diag, double[] sub1, double[] sub2,
            double[] sup1, double[] sup2, double[] b) {
        int n = diag.length;

        for (int i = 0; i < n - 2; i++) {  // 消去法
            double t = sub1[i] / diag[i];
            diag[i + 1] -= t * sup1[i];
            sup1[i + 1] -= t * sup2[i];
            b   [i + 1] -= t * b   [i];
            t = sub2[i] / diag[i];
            sub1[i + 1] -= t * sup1[i];
            diag[i + 2] -= t * sup2[i];
            b   [i + 2] -= t * b   [i];
        }
        double t = sub1[n - 2] / diag[n - 2];
        diag[n - 1] -= t * sup1[n - 2];
        b[n - 1] -= t * b   [n - 2];
        b[n - 1] /= diag[n - 1];       // 後退代入
        b[n - 2] = (b[n - 2] - sup1[n - 2] * b[n - 1]) / diag[n - 2];
        for (int i = n - 3; i >= 0; i--)
            b[i] = (b[i] - sup1[i] * b[i + 1] - sup2[i] * b[i + 2]) / diag[i];
    }

    /**
    * 例題プログラムです。
    *<b>&quot;n =&quot;</b> と聞いてきますから 3 以上の整数値を入力してください。
    *表示される解はすべて 1.0 になります。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        if (n < 3) throw new IllegalArgumentException("3 以上の整数値を入力してください。");

        double[] diag = new double[n], sub1 = new double[n],
                 sub2 = new double[n], sup1 = new double[n],
                 sup2 = new double[n], b    = new double[n];
        // 例題.  正解はすべて 1
        for (int i = 0; i < n; i++) {
            diag[i] = 11;
            sub1[i] = 3;
            sub2[i] = 1;
            sup1[i] =  4;
            sup2[i] = 2;
            b[i]    = 21;
        }
        b[0] = 17;
        b[n - 1] = 15;
        b[1] -= 1;
        b[n - 2] -= 2;
        solve(diag, sub1, sub2, sup1, sup2, b);
        RVector.print(b, 8, "9.6f");
        System.out.println("正解はすべて 1");
    }
}