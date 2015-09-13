package java_algorithm.equation;

/**
 *  Newton2.java -- Newton (ニュートン) 法
 */

import java.io.*;
import java.text.DecimalFormat;

/**
 * 方程式 <code><i>f</i>(<i>x</i>) = 0</code> の解を
 * Newton (ニュートン) 法を使って求めます。<br>
 * 導関数 <code><i>f</i> '(<i>x</i>)</code> は不要です。
 * @see Newton1
 */
public class Newton2 {

    /** 零点を求めたい関数 f(x) */
    public static interface Function {
        public double of(double x);
    }

    static final double EPS = 1.0e-14; // 収束判定に使う
    static final DecimalFormat DF2  = new DecimalFormat(" 0.0E00;-0.0E00");
    static final DecimalFormat DF15 = new DecimalFormat(" 0.000000000000000E00;-0.000000000000000E00");

    /**
     * 初期値 $x$ から $f(x) = 0$ の解を求める<br>
     * 収束の様子を表示します。
     * @param x 初期値を与えます。
     * @return 方程式 <code><i>f</i>(<i>x</i>) = 0</code> の解を返します。
     */
    public static double solve(Function f, double x) {
        double fx = f.of(x);
        while (fx != 0) {
            double df = f.of(x + fx) - fx, h = fx * fx / df,
                   xPrev = x, fxPrev = fx;
            int i = 0;

            do {
                x = xPrev - h;
                fx = f.of(x);
                h /= 2;
                i++;
                System.out.println(
                        "(" + i + ") x = " + DF15.format(x) + "  f(x) = " + DF2.format(fx));
            } while (Math.abs(fx) > Math.abs(fxPrev));
            if (i == 1 && Math.abs(x - xPrev) < EPS) break;
        }
        return x;
    }

    /**
     * 初期値を入力して，方程式 <code>arctan(<i>x</i> - 1) = 0</code>
     * の解を求めるサンプル・プログラムです。
     */
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("atan(x - 1) = 0 を解きます.");
        System.out.print("初期値 = ");
        double x = Double.parseDouble(input.readLine());
        Newton2.Function f =  new Newton2.Function() {

            @Override
            public double of(double x) { // 零点を求めたい関数 f(x)
                return Math.atan(x - 1);
            }
        };
        x = Newton2.solve(f, x);
        System.out.println("解は " + x + "です.");
    }
}