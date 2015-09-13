package java_algorithm.statistics.interpolation;

public class Newton {

    private double[] x, y, a;    // TODO

    public Newton(double[] x, double[] y) {
        this.x = x;  this.y = y;
        a = new double[x.length];
        double[] w = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            w[i] = y[i];
            for (int j = i - 1; j >= 0; j--)
                w[j] = (w[j + 1] - w[j]) / (x[i] - x[j]);
            a[i] = w[0];
        }
    }

    public double interpolate(double t) {
        double p = a[a.length - 1];
        for (int i = a.length - 2; i >= 0; i--)
            p = p * (t - x[i]) + a[i];
        return p;
    }

    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,2,3,5};
        Newton ntn = new Newton(x, y);
        for (String arg : args) {
            double t = Double.parseDouble(arg);
            System.out.println("t = " + t + ", y(t) = " + ntn.interpolate(t));
        }
    }
}