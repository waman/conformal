package java_algorithm.statistics.interpolation;

public class PSpline2 {

    private PSpline psx, psy;

    public PSpline2(double[] x, double[] y) {
        int N = x.length - 1;
        double[] p = new double[N + 1];
        p[0] = 0;
        for (int i = 1; i <= N; i++) {
            double t1 = x[i] - x[i - 1], t2 = y[i] - y[i - 1];
            p[i] = p[i - 1] + Math.sqrt(t1 * t1 + t2 * t2);
        }
        for (int i = 1; i <= N; i++) p[i] /= p[N];
        psx = new PSpline(p, x);
        psy = new PSpline(p, y);
    }

    public double[] interpolate(double t) { // 配列 {x(t), y(t)} を返す
        return new double[] { psx.interpolate(t), psy.interpolate(t) };
    }

    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,2,3,2};
        PSpline2 pspl2 = new PSpline2(x, y);
        for (double t = 0; t <= 1; t += 0.1) {
            double[] point = pspl2.interpolate(t);
            System.out.println("t = " + t + ", x = " + point[0] + ", y = " + point[1]);
        }
    }
}