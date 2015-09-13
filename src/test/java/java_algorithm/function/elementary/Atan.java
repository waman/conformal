package java_algorithm.function.elementary;

import java_algorithm.function.FuncViewer;

public class Atan extends FuncViewer {

    private static final int N = 24; // 本文参照

    public static double atan(double x) { // アークタンジェント
        int sign = 0;
        if (x > 1)       { sign =  1;  x = 1 / x; }
        else if (x < -1) { sign = -1;  x = 1 / x; }
        double a = 0;
        for (int i = N; i >= 1; i--)
            a = (x * x * i * i) / (2 * i + 1 + a);
        if (sign > 0) return   Math.PI / 2 - x / (1 + a);
        if (sign < 0) return - Math.PI / 2 - x / (1 + a);
        /* else */    return x / (1 + a);
    }

    @Override
    public String getName() {
        return "Arctan(x)";
    }

    @Override
    public double getValue(double x) {
        return atan(x);
    }
}