package java_algorithm.function.special;

// 要 Gamma クラス (Gamma.loggamma(x) = log(Γ(x)) を使用)

import java_algorithm.function.FuncViewer;

public class Igamma extends FuncViewer {

    public static double p_gamma(double a, double x, double loggamma_a) { // 本文参照
        if (x >= 1 + a) return 1 - q_gamma(a, x, loggamma_a);
        if (x == 0)     return 0;
        double result = Math.exp(a * Math.log(x) - x - loggamma_a) / a, term = result;
        for (int k = 1; k < 1000; k++) {
            term *= x / (a + k);
            double previous = result;
            result += term;
            if (result == previous) return result;
        }
        System.err.println("収束しません。\n");
        return result;
    }

    public static double q_gamma(double a, double x, double loggamma_a) { // 本文参照
        if (x < 1 + a) return 1 - p_gamma(a, x, loggamma_a);
        double w = Math.exp(a * Math.log(x) - x - loggamma_a);
        double la = 1, lb = 1 + x - a, // Laguerre の多項式
               result = w / lb;
        for (int k = 2; k < 1000; k++) {
            double temp = ((k - 1 - a) * (lb - la) + (k + x) * lb) / k;
            la = lb;  lb = temp;
            w *= (k - 1 - a) / k;
            temp = w / (la * lb);
            double previous = result;  result += temp;
            if (result == previous) return result;
        }
        System.err.println("収束しません。\n");
        return result;
    }

    public static double p_chisq(double chisq, int df) { // カイ２乗分布の下側確率
        return p_gamma(0.5 * df, 0.5 * chisq, Gamma.loggamma(0.5 * df));
    }

    public static double q_chisq(double chisq, int df) { // カイ２乗分布の上側確率
        return q_gamma(0.5 * df, 0.5 * chisq, Gamma.loggamma(0.5 * df));
    }

    private static double LOG_PI = Math.log(Math.PI); // log(PI)

    public static double erf(double x) { // Gauss の誤差関数 erf(x)
        if (x >= 0) return   p_gamma(0.5, x * x, LOG_PI / 2);
        /* else */  return - p_gamma(0.5, x * x, LOG_PI / 2);
    }

    public static double erfc(double x) { // 1 - erf(x)
        if (x >= 0) return q_gamma(0.5, x * x, LOG_PI / 2);
        /* else */  return 1 + p_gamma(0.5, x * x, LOG_PI / 2);
    }

    public static double p_normal(double x) { // 標準正規分布の下側確率
        if (x >= 0)
            return 0.5 * (1 + p_gamma(0.5, 0.5 * x * x, LOG_PI / 2));
        /* else */
            return 0.5 * q_gamma(0.5, 0.5 * x * x, LOG_PI / 2);
    }

    public static double q_normal(double x) { // 標準正規分布の上側確率
        if (x >= 0)
            return 0.5 * q_gamma(0.5, 0.5 * x * x, LOG_PI / 2);
        /* else */
            return 0.5 * (1 + p_gamma(0.5, 0.5 * x * x, LOG_PI / 2));
    }

    @Override
    public String getName() {
        return "erf(x)";
    }

    @Override
    public double getValue(double x) {
        return erf(x);
    }
}