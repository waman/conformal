package java_algorithm.equation;

/**
 *  RegulaFalsi.java -- はさみうち法
 */
public class RegulaFalsi {

    /** 零点を求めたい関数 f(x) */
    public interface Function {
        public double of(double x);
    }

    /** 機械エプシロン */
    static final double DBL_EPSILON = 2.2204460492503131E-16;

    /** 同符号なら真 */
    private static boolean isSameSign(double a, double b) {
        return (a > 0) == (b > 0);
    }

    /** f(x) = 0 の解を区間 [a,b] から許容誤差 tolerance で求める。
        最初の iMax 回ははさみうち法，それ以降は2分法を使う。 */
    public static double solve(
            Function f, double a, double b, double tolerance, int iMax) {
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
            throw new IllegalArgumentException("区間の両端で関数値が同符号です。") ;

        for (int i = 0; ; i++) {
            double c = (a + b) / 2;
            if (c - a <= tolerance || b - c <= tolerance) return c;
            if (i < iMax) {
                c = (a * fb - b * fa) / (fb - fa);
                if(c <= a)
                    c = a + 0.6 * DBL_EPSILON * Math.abs(a);
                else if (c >= b)
                    c = b - 0.6 * DBL_EPSILON * Math.abs(b);
            }

            double fc = f.of(c);
            if (fc == 0) return c;
            if (isSameSign(fc, fa)) {
                a = c;
                fa = fc;
            }else{
                b = c;
                fb = fc;
            }
        }
    }

    /** f(x) = x^2 - 2 を解き比べる */
    public static void main(String[] args) {
        class Function implements RegulaFalsi.Function {

            int count = 0;  // f(x)の呼び出し回数カウンタ

            @Override
            public double of(double x) {
                double value = x * x - 2;
                System.out.println((++count) + ":  x = " + x + "  f(x) = " + value);
                return value;
            }
        }

        System.out.println("2分法");
        double x = RegulaFalsi.solve(new Function(), 1, 2, 1e-6, 0);
        System.out.println("解 x = " + x);
        System.out.println("sqrt(2.0) - x = " + (Math.sqrt(2) - x));

        System.out.println("regula falsi法");
        x = RegulaFalsi.solve(new Function(), 1, 2, 1e-6, 1000);
        System.out.println("解 x = " + x);
        System.out.println("sqrt(2.0) - x = " + (Math.sqrt(2) - x));
    }
}