package java_algorithm.statistics.interpolation;

public class Spline2 {

    private Spline sx, sy;

    public Spline2(double[] x, double[] y) { // コンストラクタ
        int n = x.length;
        double[] p = new double[n];
        p[0] = 0;
        for (int i = 1; i < n; i++) {
            double t1 = x[i] - x[i - 1], t2 = y[i] - y[i - 1];
            p[i] = p[i - 1] + Math.sqrt(t1 * t1 + t2 * t2);
        }
        for (int i = 1; i < n; i++) p[i] /= p[n - 1];
        sx = new Spline(p, x);
        sy = new Spline(p, y);
    }

    public double[] interpolate(double t) { // 配列 {x(t), y(t)} を返す
        return new double[] { sx.interpolate(t), sy.interpolate(t) };
    }

    // テスト用
    public static void main(String[] args) {
        double[] x = new double[] {1,2,3,4,5},
                 y = new double[] {2,1,2,3,5};
        Spline2 spl2 = new Spline2(x, y);
        for (int i = 0; i <= 10; i++) {
            double[] point = spl2.interpolate(i / 10.0);
            System.out.println("t = " + (i / 10.0) + ", x = " + point[0] + ", y = " + point[1]);
        }
    }
}