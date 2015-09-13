package java_algorithm.equation;

/**
 *  Bisection.java -- 2分法(bisection method)
 */

public class Bisection {

    /** 零点を求めたい関数 f(x) */
    public static interface Function {
        public double of(double x);
    }

    /** 同符号なら真 */
    private static boolean isSameSign(double a, double b) {
        return (a > 0) == (b > 0);
    }

    /** f(x) = 0 の解を区間 [a,b] から許容誤差 tolerance で求める */
    public static double solve(Function f, double a, double b, double tolerance) {
        if (tolerance < 0) tolerance = 0;
        if (b < a) {
            double swap = a;
            a = b;
            b = swap;
        }

        double fa = f.of(a);
        if (fa == 0) return a;

        double fb = f.of(b);
        if (fb == 0) return b;

        if (isSameSign(fa, fb))
            throw new IllegalArgumentException("区間の両端で関数値が同符号です。");

        for ( ; ; ) {
            double c = (a + b) / 2;
            if (c - a <= tolerance || b - c <= tolerance) return c;
            double fc = f.of(c);
            if (fc == 0) return c;
            if (isSameSign(fc, fa)) {
                a = c;
                fa = fc;
            }else{
                b = c;
                fb = fc;  // TODO
            }
        }
    }
}