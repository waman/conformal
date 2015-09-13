package java_algorithm.algebra.linear;

/*
* Householder.java -- Householder (ハウスホルダー) 変換
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* Householder (ハウスホルダー) 変換のプログラムです。
*@see Tridiagonalize
*@see RVector
*/

public class Householder {

    /**
    * Householder 変換のプログラムです。
    *@param x 変換するベクトル
    *@return 変換後のベクトルの最初の成分
    */
    public static double transform(double[] x) {
        double s = Math.sqrt(RVector.innerProduct(x, x));  /* 内積の平方根, すなわちベクトル $x$ の大きさ */

        if (s != 0) {
            if (x[0] < 0) s = -s;
            x[0] += s;  double t = 1 / Math.sqrt(x[0] * s);
            for (int i = 0; i < x.length; i++) x[i] *= t;
        }
        return -s;
    }

    /**
    * 乱数を使った例題プログラムです。<br>
    *<b>&quot;n = &quot;</b>と聞いてきますから 2 以上の整数値を入力してください。<br>
    *変換前と変換後のベクトル(第 1 成分以外は 0)および二乗平均誤差を出力します。
    */

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double[] x = new double[n], v = new double[n];
        double s = 0;

        for (int i = 0; i < n; i++) {
            x[i] = v[i] = Math.random() - Math.random();
            s += x[i] * x[i];
        }
        System.out.println("x:");
        RVector.print(x, 7, "10.6f");
        System.out.println("||x|| = " + Math.sqrt(s));

        double x1 = transform(v);  /* Householder変換 */
        System.out.println("x' = (" + x1 + ", 0, ..., 0)");  /* 変換後のベクトルを出力 */
        s = 0;  /* 以下は確認 */
        for (int i = 0; i < n; i++) s += v[i] * x[i];
        for (int i = 0; i < n; i++) x[i] -= s * v[i];
        s = (x[0] - x1) * (x[0] - x1);
        for (int i = 1; i < n; i++) s += x[i] * x[i];
        System.out.println("二乗平均誤差: " + Math.sqrt(s / n));
    }
}