package java_algorithm.functional.integration;

/**
 *  Gauss-Legendre の積分公式
 */

public class GaussLegendre {

    static final double z[] = { 0.148874338981631211,
        0.433395394129247191, 0.679409568299024406,
        0.865063366688984511, 0.973906528517171720
    };

    static final double w[] = { 0.295524224714752870,
        0.269266719309996355, 0.219086362515982044,
        0.149451349150580593, 0.0666713443086881376
    };

    // 10 点公式
    public static double integration(Function f, double a, double b) {
        double h = (b - a) / 2;
        double ave = (b + a) / 2;
        double sum = 0;
        for (int i = 0; i < 5; i++) {
            double tmp = h * z[i];
            sum += w[i] * (f.of(ave + tmp) + f.of(ave - tmp));
        }
        return sum * h;
    }

    public static interface Function {
        public double of(double x);
    }

    public static void main(String[] args) {
        Function f = new Function() { // 被積分関数 $f(x)=e^{x^2}$
            @Override
            public double of(double x) { return Math.exp(x * x); }
        };
        System.out.println("ret = " + integration(f, 0, 1));
    }
}