package java_algorithm.equation;

/**
 * Newton1Test.java -- Newton法(Newton1)の使用例
 */

import java_algorithm.equation.algebraic.Cardano;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Newton法(Newton1)の使用例です。<br>
 * 関数 <code>f(x) = x<sup>3</sup> - 2x + 3</code>，
 * 導関数 <code>f'(x) = 3x<sup>2</sup> - 2</code>
 * を与えています。<br>
 * @see Newton1
 */
public class Newton1Test {

    /** f(x) = x^3 + bx^2 + c^x + d */
    static class CubicFunction implements Newton1.Function {

        private double b, c, d;

        public CubicFunction(double a, double b, double c, double d) {  // TODO
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

    /**
     * 丁寧な場合分けをして，３次方程式<br>
     * <code>x<sup>3</sup> + bx<sup>2</sup> + cx + d = 0</code><br>
     * を解き，その実数解を表示します。
     */
    // 参考文献：富士通FACOMマニュアル「SSL II 使用手引書」
    public static void solveCubicEq(double a, double b, double c, double d) {
        b /= a;
        c /= a;
        d /= a;
        a = b * b - 3 * c;
        CubicFunction cf = new CubicFunction(b, c, d);

        if (a > 0) {
            double minP = (-b + Math.sqrt(a))/3.0;  // 極小点の座標
            double maxP = (-b - Math.sqrt(a))/3.0;  // 極大点の座標
            double minF = cf.of(minP);              // 極小値
            double maxF = cf.of(maxP);              // 極大値
            a = (2.0 / 3.0) * Math.sqrt(a);
            if (minF * maxF < 0) { // 3つの解を持つ場合
                double x1 = Newton1.solve(cf, -a - b / 3);  // 左側から
                System.out.println("x1 = " + x1);
                double x2 = Newton1.solve(cf, a - b / 3);   // 右側から
                System.out.println("x2 = " + x2);
                double x3 = Newton1.solve(cf, b / (-3));    // 変曲点から
                System.out.println("x3 = " + x3);
            } else { // 解が1つか重解
                double x0;
                if (maxF > 0) x0 = -a - b / 3;  // 左側から
                else          x0 =  a - b / 3;  // 右側から
                double x1 = Newton1.solve(cf, x0);
                System.out.println("x1 = " + x1);
                if (minF == 0) System.out.println("x2 = " + minP + " (重解)");
                if (maxF == 0) System.out.println("x2 = " + maxP + " (重解)");
            }
        } else if (a < 0) { // f'(x) = 0 が実解を持たない場合
            double x1 = Newton1.solve(cf, b / (-3));    // 変曲点から
            System.out.println("x = " + x1);
        } else { // f'(x) = 0 が重解を持つ場合
            double x0 = b / (-3); // 変曲点
            x0 = x0 - cf.of(x0);  // 逐次代入法を1回適用
            double x1 = Newton1.solve(cf, x0);
            System.out.println("x1 = " + x1 + " (重解)");
        }
    }

    /**
     * 係数 a, b, c, d を空白で区切って入力すると３次方程式<br>
     * <code>ax<sup>3</sup> + bx<sup>2</sup> + cx + d = 0</code><br>
     * の実数解を表示します。
     * @see Cardano
     */
    public static void main(String[] args) throws IOException {
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

        System.out.println("Cardano(カルダノ)の公式を使用");
        Cardano.solve(a, b, c, d);
        System.out.println();

        System.out.println("solveCubicEq()を使用");
        solveCubicEq(a, b, c, d);
        System.out.println();

        System.out.println("Newton1 を使用");
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
            if (cf.of(mp) > 0) System.exit(0);      // 極小値が正 :この判定がないと 2 5 2 6 などで収束しない
            double x2 = Newton1.solve(cf, a - b / 3);   // 右側から
            if (x2 == x1) System.exit(0);
            System.out.println("x2 = " + x2);
            double x3 = Newton1.solve(cf, b / (-3));    // 変曲点から
            System.out.println("x3 = " + x3);
        } else {
            double x1 = Newton1.solve(cf, 0);           // 適当な点から
            System.out.println("x = " + x1);
        }
    }
}