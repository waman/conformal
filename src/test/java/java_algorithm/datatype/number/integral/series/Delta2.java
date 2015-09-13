package java_algorithm.datatype.number.integral.series;

/*
* Delta2.java -- Aitken (エイトケン) の $\Delta^2$ 法
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
* Aitken (エイトケン) のΔ<sup>2</sup> 法のプログラムです。
* @see NumberFormatter
*/
public class Delta2 {

    static final NumberFormatter fmt3d = new NumberFormatter("3d");
    static final NumberFormatter fmt   = new NumberFormatter("22.13e");

    static void limitValue(double a[]) {
        for (int j = 0; j < a.length; j++) {
            int i;
            for (i = j; i >= 2; i -= 2) {
                double q = a[i] - 2 * a[i - 1] + a[i - 2];
                if (q == 0) {
                    System.out.println("収束しました.");  return;
                }
                double p = a[i] - a[i - 1];
                a[i - 2] = a[i] - p * p / q;
            }
            System.out.println(fmt3d.toString(j) + "   " + a[i]);
        }
    }

    /**
    *Aitken (エイトケン) のΔ<sup>2</sup> 法の適用例です。<br>
    *Euler（オイラー）の級数<br>
    *<code> 1 - 1!x + 2!x<sup>2</sup> - 3!x<sup>3</sup> + ... ( x = 0.1 )<br></code>
    *に適用した結果を表示します。
    */
    public static void main(String[] args) {
        final int N = 50;
        double[] a = new double[N];

        /* Euler's series $1 - 1!x + 2!x^2 - 3!x^3 + \cdots$ at $x = 0.1$ */
        double t = 1;   a[0] = 1;
        for (int i = 1; i < N; i++) {
            t *= i * (-0.1);  a[i] = a[i - 1] + t;
            System.out.println(fmt3d.toString(i) + fmt.toString(a[i]));
        }
        limitValue(a);
    }
}