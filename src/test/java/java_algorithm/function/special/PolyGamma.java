package java_algorithm.function.special;

import java_algorithm.function.FuncViewer;

public class PolyGamma extends FuncViewer {
    private static final int N = 8;
    private static final double B0  = 1.0; // 以下は Bernoulli 数
    private static final double B1  = (-1.0 / 2.0);
    private static final double B2  = ( 1.0 / 6.0);
    private static final double B4  = (-1.0 / 30.0);
    private static final double B6  = ( 1.0 / 42.0);
    private static final double B8  = (-1.0 / 30.0);
    private static final double B10 = ( 5.0 / 66.0);
    private static final double B12 = (-691.0 / 2730.0);
    private static final double B14 = ( 7.0 / 6.0);
    private static final double B16 = (-3617.0 / 510.0);

    public static double psi(double x) { // Ψ(x)
        double v = 0;
        while (x < N) { v += 1 / x;  x++; }
        double w = 1 / (x * x);
        v += ((((((((B16 / 16)  * w + (B14 / 14)) * w
                  + (B12 / 12)) * w + (B10 / 10)) * w
                  + (B8  /  8)) * w + (B6  /  6)) * w
                  + (B4  /  4)) * w + (B2  /  2)) * w + 0.5 / x;
        return Math.log(x) - v;
    }

    public static double polygamma(int n, double x) { // Ψ^(n)(x)
        double u = 1;
        for (int k = 1 - n; k < 0; k++) u *= k; // u = (-1)^(n-1) (n - 1)!
        double v = 0;
        while (x < N) { v += 1 / Math.pow(x, n - 1);  x++; }
        double w = x * x,
            t = (((((((B16
                * (n + 15.0) * (n + 14) / (16 * 15 * w) + B14)
                * (n + 13.0) * (n + 12) / (14 * 13 * w) + B12)
                * (n + 11.0) * (n + 10) / (12 * 11 * w) + B10)
                * (n +  9.0) * (n +  8) / (10 *  9 * w) + B8)
                * (n +  7.0) * (n +  6) / ( 8 *  7 * w) + B6)
                * (n +  5.0) * (n +  4) / ( 6 *  5 * w) + B4)
                * (n +  3.0) * (n +  2) / ( 4 *  3 * w) + B2)
                * (n +  1.0) * n        / ( 2 *  1 * w)
                + 0.5 * n / x + 1;
        return u * (t / Math.pow(x, n) + n * v);
    }

    @Override
    public String getName() {
        return "psi(x)";
    }

    @Override
    public double getValue(double x) {
        return psi(x);
    }
}