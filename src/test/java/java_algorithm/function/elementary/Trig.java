package java_algorithm.function.elementary;

import java_algorithm.function.FuncViewer;

public class Trig extends FuncViewer {

    private static class Ur_Tan { // tan x の素(本文参照)

        static int N = 19;
        static double D = 4.4544551033_8076867830_8360248557_9E-6;
        int k; double t;

        Ur_Tan(double x) {
            k = (int)(x / (Math.PI / 2) + (x >= 0 ? 0.5 : -0.5));
            x = +(x - (3217.0 / 2048) * k) + D * k;
            double x2 = x * x;
            for (int i = N; i >= 3; i -= 2) t = x2 / (i - t);
            t = x / (1 - t);
        }
    }

    public static double tan(double x) { // tan x
        Ur_Tan ret = new Ur_Tan(x);
        if (ret.k % 2 == 0) return ret.t;
        if (ret.t != 0)     return -1 / ret.t;
        /* overflow */      return Double.POSITIVE_INFINITY;
    }

    public static double sin(double x) { // sin x
        Ur_Tan ret = new Ur_Tan(x / 2);
        ret.t = 2 * ret.t / (1 + ret.t * ret.t);
        if (ret.k % 2 == 0) return  ret.t;
        /* else */          return -ret.t;
    }

    public static double cos(double x) { // cos x
        return sin(Math.PI / 2 - Math.abs(x));
    }

    public static double cos1(double x) { // 1 - cos x TODO
        Ur_Tan ret = new Ur_Tan(Math.abs(x) / 2);
        ret.t *= ret.t;
        if (ret.k % 2 == 0) return 2 * ret.t / (1 + ret.t);
        /* else */          return 2         / (1 + ret.t);
    }

    @Override
    public String getName() {
        return "tan(x)";
    }

    @Override
    public double getValue(double x) {
        return tan(x);
    }
}