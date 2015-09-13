package java_algorithm.function.special;

// 要 Gamma クラス (Gamma.loggamma(x) = log(Γ(x)) を使用)

import java_algorithm.function.FuncViewer;

public class Ibeta extends FuncViewer {

    public static double p_beta(double x, double a, double b) { // I_x(a,b)
        if (a <= 0) return Double.POSITIVE_INFINITY;
        if (b <= 0) {
            if (x <  1) return 0;
            if (x == 1) return 1;
            /* else */  return Double.POSITIVE_INFINITY;
        }
        if (x > (a + 1) / (a + b + 2))
            return 1 - p_beta(1 - x, b, a);
        if (x <= 0) return 0;

        double p1 = 0, q1 = 1,
            p2 = Math.exp(a * Math.log(x) + b * Math.log(1 - x)
                          + Gamma.loggamma(a + b)
                          - Gamma.loggamma(a) - Gamma.loggamma(b)) / a,
            q2 = 1;
        for (int k = 0; k < 200; ) {
            double previous = p2,
                d = - (a + k) * (a + b + k) * x / ((a + 2 * k) * (a + 2 * k + 1));

            p1 = p1 * d + p2;
            q1 = q1 * d + q2;
            k++;
            d = k * (b - k) * x / ((a + 2 * k - 1) * (a + 2 * k));

            p2 = p2 * d + p1;
            q2 = q2 * d + q1;
            if (q2 == 0) {
                p2 = Double.POSITIVE_INFINITY;
                continue;
            }

            p1 /= q2;
            q1 /= q2;
            p2 /= q2;
            q2 = 1;
            if (p2 == previous) return p2;
        }
        System.err.println("収束しません。\n");
        return p2;
    }

    public static double q_beta(double x, double a, double b) { // 1 - I_x(a,b) TODO
        return 1 - p_beta(x, a, b);
    }

    public static double p_t(double t, int df) { // t 分布の下側確率 TODO
        return 1 - 0.5 * p_beta(df / (df + t * t), 0.5 * df, 0.5);
    }

    public static double q_t(double t, int df) { // t 分布の上側確率 TODO
        return 0.5 * p_beta(df / (df + t * t), 0.5 * df, 0.5);
    }

    public static double p_f(double f, int df1, int df2) { // F 分布の下側確率 TODO
        if (f <= 0) return 0;
        return p_beta(df1 / (df1 + df2 / f), 0.5 * df1, 0.5 * df2);
    }

    public static double q_f(double f, int df1, int df2) { // F 分布の上側確率 TODO
        if (f <= 0) return 1;
        return p_beta(df2 / (df2 + df1 * f), 0.5 * df2, 0.5 * df1);
    }

    public static double p_binomial(int n, double p, int k) { // 二項分布 B(n,p) の下側（X <= k）の確率 TODO
        if (k <  0) return 0;
        if (k >= n) return 1;
        return p_beta(1 - p, k + 1, n - k);
    }

    public static double q_binomial(int n, double p, int k) { // 二項分布 B(n,p) の上側（X <= k）の確率 TODO
        if (k <= 0) return 1;
        if (k >  n) return 0;
        return p_beta(p, k, n - k + 1);
    }

    @Override
    public String getName() {
        return "Ix(0.5,0.5)";
    }

    @Override
    public double getValue(double x) {
        return p_beta(x,0.5,0.5);
    }
}