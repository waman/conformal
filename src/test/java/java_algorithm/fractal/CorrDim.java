package java_algorithm.fractal;

/**
 *  フラクタルの相関次元 Correlation Dimension
 */

public class CorrDim {

    public double[][] result;
    final double log10 = Math.log(10);

    public CorrDim(int mmax, int lag, int n, double[] x) {
        result = new double[mmax][101];
        for (int i = 0; i <= 100; i++)
            result[0][i] = 0.02 * i;
        normalize(n, x);
        double[] t1 = new double[mmax];
        double[] t2 = new double[mmax];
        for (int m = 2; m <= mmax; m++) {
            for (int i = 0; i < n - (m - 1) * lag - 1; i++) {
                for (int k = 0; k < m; k++) t1[k] = x[i + k * lag];
                for (int j = i + 1; j < n - (m - 1) * lag; j++) {
                    for (int k = 0; k < m; k++) t2[k] = x[j + k * lag];
                    double d = 100 * distance(m, t1, t2);
                    if (d < 0.1) d = 0.1;
                    int c = (int)(50 * Math.log(d) / log10 + 1);
                    if (c < 0) c = 0;
                    if (c <= 100) result[m-1][c]++;
                }
            }
            for (int c = 1; c <= 100; c++)
                result[m-1][c] += result[m-1][c-1];
            for (int c = 0; c <= 100; c++)
                result[m-1][c] = Math.log(result[m-1][c]) / log10;
        }
    }

    final double distance(int m, double[] p, double[] q) {
        double d = 0;
        for (int k = 0; k < m; k++)
            d += (p[k] - q[k]) * (p[k] - q[k]);
        return Math.sqrt(d / m);
    }

    void normalize(int n, double[] x) {
        double max = x[0];
        double min = x[0];
        for (int i = 1; i < n; i++)
            if (x[i] > max) max = x[i];
            else if (x[i] < min) min = x[i];
        for (int i = 0; i < n; i++)
            x[i] = (x[i] - min) / (max - min);
    }

    // テスト用
    static void henon(int n, double[] x) {
        double u = 0.3, v = 0.3;
        for (int i = 0; i < 100; i++) {
            double t = 1 - 1.4 * u * u + 0.3 * v;
            v = u;  u = t;
        }
        for (int i = 0; i < n; i++) {
            double t = 1 - 1.4 * u * u + 0.3 * v;
            v = u;  x[i] = u = t;
        }
    }

    public static void main(String[] args) {
        int n = 1000;
        double[] x = new double[n];
        henon(n, x);
        CorrDim c = new CorrDim(5, 1, n, x);
        for (int i = 0; i <= 100; i++) {
            for (int m = 0; m < 4; m++)
                System.out.print(c.result[m][i] + "\t");
            System.out.println(c.result[4][i]);
        }
    }
}