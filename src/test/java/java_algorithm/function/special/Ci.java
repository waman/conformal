package java_algorithm.function.special;

import java_algorithm.function.FuncViewer;

public class Ci extends FuncViewer {

    private static final double EULER = 0.5772156649_0153286060_6512090082; // Euler の定数 γ

    private static double series(double x) { // 級数展開
        double s = EULER + Math.log(x), t = 1;
        x = - x * x;
        for (int k = 2; k < 1000; k += 2) {
            t *= x / ((k - 1) * k);
            double u = s;  s += t / k;
            if (s == u) return s;
        }
        System.err.println("Ci.series(): 収束しません。");
        return s;
    }

    private static double asympt(double x) { // 漸近展開
        double  fmax = 2, gmax = 2, fmin = 0, gmin = 0, f = 0, g = 0,
                t = 1 / x;
        int k = 0, flag = 0;
        while (flag != 15) {
            f += t;  t *= ++k / x;
            if (f < fmax) fmax = f;  else flag |= 1;
            g += t;  t *= ++k / x;
            if (g < gmax) gmax = g;  else flag |= 2;
            f -= t;  t *= ++k / x;
            if (f > fmin) fmin = f;  else flag |= 4;
            g -= t;  t *= ++k / x;
            if (g > gmin) gmin = g;  else flag |= 8;
        }
        return 0.5 * ((fmax + fmin) * Math.sin(x) - (gmax + gmin) * Math.cos(x));
    }

    public static double valueOf(double x) { // Ci(x)
        if (x <  0) return - valueOf(-x);
        if (x < 18) return series(x);
        return             asympt(x);
    }

    @Override
    public String getName() {
        return "Ci(x)";
    }

    @Override
    public double getValue(double x) {
        return valueOf(x);
    }
}