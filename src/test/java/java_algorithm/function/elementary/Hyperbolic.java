package java_algorithm.function.elementary;

import java_algorithm.function.FuncViewer;

public class Hyperbolic extends FuncViewer {

    private static final double EPS5 = 0.001; // 機械エプシロンの1/5乗程度

    public static double sinh(double x) { // sinh x
        if (Math.abs(x) > EPS5) {
            double t = Math.exp(x);
            return (t - 1 / t) / 2;
        }
        return x * (1 + x * x / 6);
    }

    public static double cosh(double x) { // cosh x
        double t = Math.exp(x);
        return (t + 1 / t) / 2;
    }

    public static double tanh(double x) { // tanh x
        if (x >  EPS5) return 2 / (1 + Math.exp(-2 * x)) - 1;
        if (x < -EPS5) return 1 - 2 / (Math.exp(2 * x) + 1);
        return x * (1 - x * x / 3);
    }

    /* 以下は FuncViewer の abstract メソッドのオーバーライド */
    @Override
    public String getName() {
        return "sinh(x)";
    }

    @Override
    public double getValue(double x) {
        return sinh(x);
    }
}