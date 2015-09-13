package java_algorithm.datatype.collection.search;

/**
 *  FibonacciSearch.java -- Fibonacci (フィボナッチ) 探索
 */

public class FibonacciSearch {

    /** 最小化する関数(探索区間では単峰であること) */
    public interface Function {
        public double of(double x);
    }

    /** 関数 f(x) を最小にする x を区間 [a,b] から見つける */
    public static double minimize(Function f, double a, double b, double tolerance) {
        if (a > b) {
            double swap = a;
            a = b;  b = swap;
        }

        if (tolerance <= 0)
            throw new IllegalArgumentException("許容誤差は正であること");

        long ia = (long)((b - a) / tolerance), ic = 1, id = 2, ib = 3;
        while (ib < ia) {  ic = id;  id = ib;  ib = ic + id;  }
        ia = 0;  tolerance = (b - a) / ib;
        double fc = f.of(a + ic * tolerance);
        double fd = f.of(a + id * tolerance);
        for ( ; ; ) {
            if (fc > fd) {
                ia = ic;  ic = id;  id = ib - (ic - ia);
                if (ic == id) return a + id * tolerance;
                fc = fd;  fd = f.of(a + id * tolerance);
            } else {
                ib = id;  id = ic;  ic = ia + (ib - id);
                if (ic == id) return a + ic * tolerance;
                fd = fc;  fc = f.of(a + ic * tolerance);
            }
        }
    }

    /** f(x) = (x - 0.314)^2 の例 */
    public static void main(String[] args) {

        class Function implements FibonacciSearch.Function {

            int count = 0;  // f(x)の呼び出し回数カウンタ
            final double xMin = 0.314;

            @Override
            public double of(double x) {
                double value = (x - xMin) * (x - xMin);
                System.out.println((++count) + ":  f(" + x + ") = " + value);
                return value;
            }
        }

        System.out.println("x = " + minimize(new Function(), 0, 1, 1e-6));
    }
}