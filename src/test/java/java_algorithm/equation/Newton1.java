package java_algorithm.equation;

/**
 *  Newton1.java -- Newton (ニュートン) 法
 */

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

/**
 * 方程式<code><i>f</i>(<i>x</i>) = 0</code>の解を
 * Newton (ニュートン) 法を使って求めます。
 * <br>
 * @see java_algorithm.equation.algebraic.Cardano
 * @see Newton1Test
 * @see Newton2
 */
public class Newton1 {

    /** 零点を求めたい関数 f(x) */
    public interface Function {
        public double of(double x);       // 関数 f(x)
        public double primeOf(double x);  // 導関数 f'(x)
    }

    static final double EPS = 1.0e-14;
    static final DecimalFormat DF2  = new DecimalFormat(" 0.0E00;-0.0E00");
    static final DecimalFormat DF15 = new DecimalFormat(" 0.000000000000000E00;-0.000000000000000E00");

    /** 初期値 $x$ を与えて $f(x) = 0$ の解を返す */
    public static double solve(Function f, double x) {
        double xPrev;
        do {
            double fx = f.of(x), fp;
            System.out.println("  x = " + DF15.format(x) + "  f(x) = " + DF2.format(fx));
            if ((fp = f.primeOf(x)) == 0) fp = 1;  // 強引にずらす
            xPrev = x;
            x -= fx / fp;
        } while (Math.abs(x - xPrev) > EPS);
        return x;
    }

    /**
     * 係数 a, b, c, d を空白で区切って入力すると３次方程式<br>
     * <code>ax<sup>3</sup> + bx<sup>2</sup> + cx + d = 0</code><br>
     * の実数解を表示します。
     * @see java_algorithm.equation.algebraic.Cardano
     */
    public static void main(String[] args) throws IOException {

        /** f(x) = x^3 + bx^2 + c^x + d */
        class CubicFunction implements Newton1.Function {

            private double b, c, d;

            public CubicFunction(double a, double b, double c, double d) {    // TODO
                this(b / a, c / a, d / a);
            }
            public CubicFunction(double b, double c, double d) {
                this.b = b;  this.c = c;  this.d = d;
            }

            @Override
            public double of(double x) { // 零点を求めたい関数 $f(x)$
                return ((b + x) * x + c) * x + d;
            }

            @Override
            public double primeOf(double x) { // $f(x)$ の導関数
                return (2 * b + 3 * x) * x + c;
            }
        }

        System.out.println("ax^3+bx^2+cx+d=0を解きます.");
        System.out.println("a b c d ? ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(in.readLine());
        double a = Double.parseDouble(st.nextToken()),
               b = Double.parseDouble(st.nextToken()),
               c = Double.parseDouble(st.nextToken()),
               d = Double.parseDouble(st.nextToken());
        if (a == 0) {
            System.out.println("3次方程式ではありません。");
            throw new IllegalArgumentException();
        }

        b /= a;
        c /= a;
        d /= a;
        a = b * b - 3 * c;
        CubicFunction cf = new CubicFunction(b, c, d);

        if (a > 0) {
            double mp = (-b + Math.sqrt(a))/3.0;    // 極小点の座標
            a = (2.0 / 3.0) * Math.sqrt(a);
            double x1 = Newton1.solve(cf, -a - b / 3);  // 左側から，極小点の谷も渡ることができる
            System.out.println("x1 = " + x1);
            if (cf.of(mp) > 0) return;      // 極小値が正 :この判定がないと 2 5 2 6 などで収束しない
            double x2 = Newton1.solve(cf, a - b / 3);   // 右側から
            if (x2 == x1) return;
            System.out.println("x2 = " + x2);
            double x3 = Newton1.solve(cf, b / (-3));    // 変曲点から
            System.out.println("x3 = " + x3);
        } else {
            double x1 = Newton1.solve(cf, 0);           // 適当な点から
            System.out.println("x = " + x1);
        }
    }
}