package java_algorithm.function.elementary;

// 要 Exp クラス (Exp.ldexp(x,k) = 2^k x を求めるために利用)

import java_algorithm.function.FuncViewer;

public class Log extends FuncViewer {

    private static final double LOG2  = 0.6931471805_5994530941_7232121458; // log_e 2
    private static final double SQRT2 = 1.4142135623_7309504880_168872421; // sqrt(2)

    // C/C++ ライブラリ関数 t = frexp(x,&k) (x = 2^k t) の Java 版
    public static class Frexp {
        private static double bias = Double.longBitsToDouble(0x3ca0_000_000_000_000L); // 2^-52
        public double t;
        public int k;
        public Frexp(double x) {
            long bits = Double.doubleToLongBits(x);
            k = (int)((bits >> 52) & 0x7ffL);
            int s = (bits & 0x8_000_000_000_000_000L) != 0 ? -1 : 1;
            bits &= 0xfffffffffffffL;
            if (k != 0) bits |= 0x10_000_000_000_000L; // 正規数
            t = bits * s * bias;
            k -= 1022;
        }
    }

    public static double log(double x) { // 自然対数（級数展開版）
        if (x <= 0) return Double.NaN;
        Frexp frexp = new Frexp(x / SQRT2);
            // 2^(frexp.k-1) <= x/sqrt(2) < 2^frexp.k
        x /= Exp.ldexp(1, frexp.k); // x -> x / 2^frexp.k
        x = (x - 1) / (x + 1);
        double x2 = x * x, s = x, last;
        int i = 1;
        do {
            x *= x2;
            i += 2;
            last = s;
            s += x / i;
        } while (last != s);
        return LOG2 * frexp.k + 2 * s;
    }

    private static final int N = 9; // 本文参照

    public static double log_cf(double x) { // 自然対数（連分数版）
        if (x <= 0) return Double.NaN;
        Frexp frexp = new Frexp(x / SQRT2);
        x /= Exp.ldexp(1, frexp.k);  x--;
        double s = 0;
        for (int i = N; i >= 1; i--)
            s = i * x / (2 + i * x / (2 * i + 1 + s));
        return LOG2 * frexp.k + x / (1 + s);
    }

    /* 以下は FuncViewer の abstract メソッドのオーバーライド */
    @Override
    public String getName() {
        return "log(x)";
    }

    @Override
    public double getValue(double x) {
        return log(x);
    }
}