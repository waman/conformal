package java_algorithm.statistics.interpolation;

public class Spline {

    private double[] x, y, z;

    public Spline(double[] x, double[] y) { // コンストラクタ
        int n = x.length;
        this.x = x;  this.y = y;
        z = new double[n];
        double[] h = new double[n],  d = new double[n];
        z[0] = z[n - 1] = 0;    // 両端点での y''(x)/6
        for (int i = 0; i < n - 1; i++) {
            h[i    ] =  x[i + 1] - x[i];
            d[i + 1] = (y[i + 1] - y[i]) / h[i];
        }
        z[1] = d[2] - d[1] - h[0] * z[0];
        d[1] = 2 * (x[2] - x[0]);
        for (int i = 1; i < n - 2; i++) {
            double t = h[i] / d[i];
            z[i + 1] = d[i + 2] - d[i + 1] - z[i] * t;
            d[i + 1] = 2 * (x[i + 2] - x[i]) - h[i] * t;
        }
        z[n - 2] -= h[n - 2] * z[n - 1];
        for (int i = n - 2; i > 0; i--)
            z[i] = (z[i] - h[i] * z[i + 1]) / d[i];
    }

    public double interpolate(double t) { // 補間値を求める
        int i = 0, j = x.length - 1;
        while (i < j) {
            int k = (i + j) / 2;
            if (x[k] < t) i = k + 1;  else j = k;
        }
        if (i > 0) i--;
        double h = x[i + 1] - x[i], d = t - x[i];
        return (((z[i + 1] - z[i]) * d / h + z[i] * 3) * d
            + ((y[i + 1] - y[i]) / h
            - (z[i] * 2 + z[i + 1]) * h)) * d + y[i];
    }

    /** テスト用 */
    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,2,3,5};
        Spline spline = new Spline(x, y);

        if (args.length == 0)
            for (int i = 0; i <= 10; i++) {
                double t = i / 10.0;
                System.out.println("t = " + t + ", y(t) = " + spline.interpolate(t));
            }
        for (String arg : args) {
            double t = Double.parseDouble(arg);
            System.out.println("t = " + t + ", y(t) = " + spline.interpolate(t));
        }
    }
}