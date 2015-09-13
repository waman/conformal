package java_algorithm.function;

public class Sqrt extends FuncViewer {

    public static double sqrt(double x) { // 私家版 sqrt(x)
        if (x <  0) return Double.NaN;
        if (x == 0) return 0;
        double s = 1, last;
        if (x > 1) s = x;
        do {
            last = s;  s = (x / s + s) / 2;
        } while (s < last);
        return last;
    }

    @Override
    public String getName() {
        return "sqrt(x)";
    }

    @Override
    public double getValue(double x) {
        return sqrt(x);
    }
}