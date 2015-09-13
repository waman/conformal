package java_algorithm.equation.algebraic;

/*
* Gauss3.java -- 3重対角な連立方程式
* class RVector を使う。
*/

import java_algorithm.algebra.linear.RVector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* 3重対角な連立方程式を Gauss 法で解きます。
*@see GaussElimination
*@see GaussSeidel
*@see Gauss5
*/
public class Gauss3 {

    static void solve(double[] diag, double[] sub, double[] sup, double[] b) {
        int n = diag.length;

        for (int i = 0; i < n - 1; i++) {  // 消去法
            double t = sub[i] / diag[i];
            diag[i + 1] -= t * sup[i];
            b   [i + 1] -= t * b  [i];
        }
        b[n - 1] /= diag[n - 1];       // 後退代入
        for (int i = n - 2; i >= 0; i--)
            b[i] = (b[i] - sup[i] * b[i + 1]) / diag[i];
    }

    /**
    * 例題プログラムです。
    *<b>&quot;n =&quot;</b> と聞いてきますから 2 以上の整数値を入力してください。
    *表示される解はすべて 1.0 になります。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        if (n < 2) throw new IllegalArgumentException("2 以上の整数値を入力してください。");

        double[] diag = new double[n], sub = new double[n],
                 sup  = new double[n], b   = new double[n];
        // 例題.  正解はすべて 1
        for (int i = 0; i < n; i++) {
            diag[i] = 4;
            sub[i] = 1;
            sup[i] = 2;
            b[i] = 7;
        }
        b[0] = 6;
        b[n - 1] = 5;
        solve(diag, sub, sup, b);
        RVector.print(b, 8, "9.6f");
        System.out.println("正解はすべて 1");
    }
}