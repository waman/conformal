package java_algorithm.equation.differential;

/**
 * OrdinaryDiffEq.java -- 常微分方程式
 */

import java_algorithm.function.elementary.Hyperbolic;

import java.text.DecimalFormat;

/** Euler法 */
class EulerMethod {

    /** $y' = F(x,y)$ を解くべき関数 $F(x,y)$ */
    public interface Function {
        public double dy(double x, double y);
    }

    /** $y_0 = f(x_0)$ を初期条件として $y = f(x)$ を求める */
    public static double solve(
            Function f, int n, int nPrint, double x0, double y0, double xn) {
        double x = x0,  y = y0,  h = (xn - x0) / n;

        for (int i = 1; i <= n; i++) {
            y += f.dy(x, y) * h;
            x = x0 + i * h;
            if (i % nPrint == 0) System.out.println(x + "   " + y);
        }
        return y;
    }
}

/** 3次Taylor級数法 */
class Taylor3 {

    /** $y' = F(x,y)$ を解くべき関数 $F(x,y)$ */
    public interface Function {
        public double dy(double x, double y);   // $y' = F(x,y)$
        public double dy2(double x, double y);  // $y''$
        public double dy3(double x, double y);  // $y'''$
    }

    /** $y_0 = f(x_0)$ を初期条件として $y = f(x)$ を求める */
    public static double solve(
            Function f, int n, int nPrint, double x0, double y0, double xn) {
        double x = x0,  y = y0,  h = (xn - x0) / n;

        for (int i = 1; i <= n; i++) {
            y += h * (f.dy(x, y) + (h / 2) * (f.dy2(x, y) + (h / 3) * f.dy3(x, y)));
            x = x0 + i * h;
            if (i % nPrint == 0) System.out.println(x + "   " + y);
        }
        return y;
    }
}

/** 4次Runge-Kutta法 */
class RungeKutta4 {

    /** $ y' = F(x,y)$ を解くべき関数 $F(x,y)$ */
    public interface Function {  public double dy(double x, double y);  }

    /** $y_0 = f(x_0)$ を初期条件として $y = f(x)$ を求める */
    public static double solve(
            Function f, int n, int nPrint, double x0, double y0, double xn) {
        double x = x0,  y = y0,  h = (xn - x0) / n;

        for (int i = 1; i <= n; i++) {
            double f1 = h * f.dy(x, y);
            double f2 = h * f.dy(x + h / 2, y + f1 / 2);
            double f3 = h * f.dy(x + h / 2, y + f2 / 2);
            double f4 = h * f.dy(x + h, y + f3);
            y += (f1 + 2 * f2 + 2 * f3 + f4) / 6;
            x = x0 + i * h;
            if (i % nPrint == 0) System.out.println(x + "   " + y);
        }
        return y;
    }
}

public class OrdinaryDiffEq {
    /** $y' = 1 - y^2$ */
    static class Function implements EulerMethod.Function, Taylor3.Function, RungeKutta4.Function {

        @Override
        public double dy(double x, double y) {
            return 1 - y * y;           // $y' = F(x,y)$
        }

        @Override
        public double dy2(double x, double y) {  //  Taylor3.Function だけに必要
            return -2 * y * dy(x, y);   // $y''$
        }

        @Override
        public double dy3(double x, double y) {  //  Taylor3.Function だけに必要
            return -2 * (dy(x, y) * dy(x, y) + y * dy2(x, y));  // $y'''$
        }
    }

    public static void print(double x, double y) {
        final DecimalFormat FMT2 = new DecimalFormat(" 0.00");
        final DecimalFormat FMT7 = new DecimalFormat(" 0.#######");
        System.out.println(FMT2.format(x) + "   " + FMT7.format(y));
    }

    public static void main(String[] args) {
        Function f = new Function();

        for (int n = 4; n <= 128; n *= 2) {
            System.out.println("\nEuler法:          n = " + n);
            EulerMethod.solve(f, n, n / 4, 0, 0, 1);
            System.out.println("\n3次Taylor級数法:  n = " + n);
            Taylor3.solve(f, n, n / 4, 0, 0, 1);
            System.out.println("\n4次Runge-Kutta法: n = " + n);
            RungeKutta4.solve(f, n, n / 4, 0, 0, 1);
        }
        System.out.println("\n正解");
        for (int i = 1; i <= 4; i++) print(i / 4.0, Hyperbolic.tanh(i / 4.0));
    }
}