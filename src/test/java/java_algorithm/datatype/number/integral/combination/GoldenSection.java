package java_algorithm.datatype.number.integral.combination;

/**
 *  GoldenSectionh.java -- 黄金分割法
 */
public class GoldenSection {

    /** 最小化する関数(探索区間では単峰であること) */
    public interface Function {
        public double of(double x);
    }

    static final double R = 2 / (3 + Math.sqrt(5));

    /** 関数 f(x) を最小にする x を区間 [a,b] から見つける */
    public static double minimize(Function f, double a, double b, double tolerance) {
        if (a > b) {
            double swap = a;
            a = b;
            b = swap;
        }
        double t = R * (b - a),  c = a + t,  d = b - t;
        double fc = f.of(c),  fd = f.of(d);
        for ( ; ; ) {
            if (fc > fd) {
                a = c;
                c = d;
                fc = fd;
                d = b - R * (b - a);
                if (d - c <= tolerance) return c;
                fd = f.of(d);
            } else {
                b = d;
                d = c;
                fd = fc;
                c = a + R * (b - a);
                if (d - c <= tolerance) return d;
                fc = f.of(c);
            }
        }
    }

    /** f(x) = (x - 0.314)^2 の例 */
    public static void main(String[] args) {
        GoldenSection.Function f = new GoldenSection.Function() {

            int count = 0;  // f(x)の呼び出し回数カウンタ
            final double xMin = 0.314;

            @Override
            public double of(double x) {
                double value = (x - xMin) * (x - xMin);
                System.out.println((++count)  + ":  f(" + x + ") = " + value);
                return value;
            }
        };
        System.out.println("x = " + minimize(f, 0, 1, 1e-6));
    }
}