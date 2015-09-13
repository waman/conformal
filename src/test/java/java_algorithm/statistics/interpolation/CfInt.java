package java_algorithm.statistics.interpolation;

public class CfInt {

    private double[] x, y;

    public CfInt(double[] x, double[] y) {
        this.x = x;  this.y = y;
        for (int j = 0; j < x.length - 1; j++)
            for (int i = j + 1; i < x.length; i++)
                y[i] = (x[i] - x[j]) / (y[i] - y[j]);
    }

    public double interpolate(double t) {
        double r = y[y.length - 1];
        for (int i = y.length - 2; i >= 0; i--)
            r = (t - x[i]) / r + y[i];
        return r;
    }

    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,4,3,5};
        CfInt cfint = new CfInt(x, y);
        for (String arg : args) {
            double t = Double.parseDouble(arg);
            System.out.println("t = " + t + ", y(t) = " + cfint.interpolate(t));
        }
    }
}