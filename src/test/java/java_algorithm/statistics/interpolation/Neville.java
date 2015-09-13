package java_algorithm.statistics.interpolation;

public class Neville {

    private double[] x, y;

    public Neville(double[] x, double[] y) {
        this.x = x;  this.y = y;
    }

    public double interpolate(double t) {
        double[] w = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            w[i] = y[i];
            for (int j = i - 1; j >= 0; j--)
                w[j] = w[j + 1] + (w[j + 1] - w[j]) * (t - x[i]) / (x[i] - x[j]);
        }
        return w[0];
    }

    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,2,3,5};
        Neville nvl = new Neville(x, y);
        for (String arg :args) {
            double t = Double.parseDouble(arg);
            System.out.println("t = " + t + ", y(t) = " + nvl.interpolate(t));
        }
    }
}