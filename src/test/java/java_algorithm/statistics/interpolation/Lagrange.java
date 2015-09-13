package java_algorithm.statistics.interpolation;

public class Lagrange {

    private double[] x, y;

    public Lagrange(double[] x, double[] y) {
        this.x = x;  this.y = y;
    }

    public double interpolate(double t) {
        double sum = 0;
        for (int i = 0; i < y.length; i++) {
            double prod = y[i];
            for (int j = 0; j < x.length; j++)
                if (j != i) prod *= (t - x[j]) / (x[i] - x[j]);
            sum += prod;
        }
        return sum;
    }

    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,2,3,5};
        Lagrange lgn = new Lagrange(x, y);
        for (String arg : args) {
            double t = Double.parseDouble(arg);
            System.out.println("t = " + t + ", y(t) = " + lgn.interpolate(t));
        }
    }
}