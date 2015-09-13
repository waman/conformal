package java_algorithm.function.elementary;

import java_algorithm.function.FuncViewer;

public class Exp extends FuncViewer {

    private static final double LOG2 = 0.6931471805_5994530941_7232121458_1765680755; // log_e 2

    public static double ldexp(double x, int k) { // 2^k x
        if (k < -1074) return x * 0.0;
        if (k >  1023) return x * Double.POSITIVE_INFINITY;
        return x * Double.longBitsToDouble(
                            (k <= -1023) ?
                            (1 << (1074 + k)) : 
                            ((long)(k + 1023) << 52)
                        );
    }

    public static double exp1(double x) { // 級数展開版
        int k = (int)(x / LOG2 + (x >= 0 ? 0.5 : -0.5));
        x -= k * LOG2;
        int neg = 0;
        if (x < 0) {
            neg = 1;
            x = -x;
        }
        double e = 1 + x, a = x, prev;
        int i = 2;
        do {
            prev = e;
            a *= x / i;
            e += a;
            i++;
        } while (e != prev);

        if (neg == 1) e = 1 / e;
        return ldexp(e, k);
    }

    private static final int N = 22; // 本文参照（6，10，14，18，22，26，...）

    public static double exp(double x) { // 連分数版
        int k = (int)(x / LOG2 + (x >= 0 ? 0.5 : -0.5));
        x -= k * LOG2;
        double x2 = x * x, w = x2 / N;
        for (int i = N - 4; i >= 6; i -= 4) w = x2 / (w + i);
        return ldexp((2 + w + x) / (2 + w - x), k);
    }

    /* 以下は FuncViewer の abstract メソッドのオーバーライド */
    @Override
    public String getName() {
        return "exp(x)";
    }

    @Override
    public double getValue(double x) {
        return exp1(x);
    }
}