package java_algorithm.function.special;

import java_algorithm.function.FuncViewer;

public class Bessel extends FuncViewer {

    private static final double EPS = 1e-10; // 許容相対誤差
    private static final double EULER = 0.5772156649_01532861; // Euler の定数γ
    private static boolean odd(int x) { return (x & 1) != 0; } // 奇数？

    public static double J(int n, double x) { // J_n(x)
        if (x < 0) {
            if (odd(n)) return -J(n, -x);
            /* else */  return  J(n, -x);
        }
        if (n < 0) {
            if (odd(n)) return -J(-n, x);
            /* else */  return  J(-n, -x);
        }
        if (x == 0) return (n == 0) ? 1 : 0;
        double b = 1, x_2 = x / 2;

        int k = n;
        if (k < x) k = (int)x;

        do { k++; } while ((b *= x_2 / k) > EPS);
        if (odd(k)) k++; // 奇数なら偶数にする
        double a = 0, s = 0, r = Double.NaN;
        while (k > 0) {
            s += b;
            a = 2 * k * b / x - a;
            k--;
            if (n == k) r = a;
            b = 2 * k * a / x - b;
            k--;
            if (n == k) r = b;
        }
        return r / (2 * s + b);
    }

    public static double Y(int n, double x) { // Y_n(x)
        if (x <= 0) return Double.NaN;
        if (n < 0) {
            if (odd(n)) return -Y(-n, x);
            /* else */  return  Y(-n, x);
        }
        int k = (int)x;
        double b = 1, x_2 = x / 2, log_x_2 = Math.log(x_2);
        do { k++; } while ((b *= x_2 / k) > EPS);
        if (odd(k)) k++; // 奇数なら偶数にする
        double  a = 0, // a = J_(k+1)(x) = 0, b = J_k(x), k 偶数
                s = 0, // 規格化の因子
                t = 0, // Y_0(x)
                u = 0; // Y_1(x)
        while (k > 0) {
            s += b;  t = b / (k / 2) - t;
            a = 2 * k * b / x - a;
            k--; // a = J_k(x), k 奇数

            if (k > 2) u = (k * a) / ((k / 2) * (k / 2+ 1)) - u;
            b = 2 * k * a / x - b;
            k--; // b = J_k(x), k 偶数
        }
        s = 2 * s + b;
        a /= s;
        b /= s;
        t /= s;
        u /= s; // a = J_1(x), b = J_0(x)

        t = (2 / Math.PI) * (2 * t + (log_x_2 + EULER) * b); // Y_0(x)
        if (n == 0) return t;
        u = (2 / Math.PI) * (u + ((EULER - 1) + log_x_2) * a - b / x); // Y_1(x)
        for (k = 1; k < n; k++) {
            s = (2 * k) * u / x - t;  t = u;  u = s;
        }
        return u;
    }

    @Override
    public String getName() {
        return "J_3(x)";
    }

    @Override
    public double getValue(double x) {
        return J(3, x);
    }
}