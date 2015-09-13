package java_algorithm.algebra.linear;

/**
 *  QR 法による行列対角化ルーチン
 */
public final class QRDiagonalizer {

    static final double EPS = 1E-6;
    static final int MAX_ITER = 100;

    private QRDiagonalizer() {}

    public static boolean eigen(double[][] a, double[] d, double[] e) {
        triDiagonalize(a, d, e);
        e[0] = 0.0;
        int n = a.length;
        for (int h = n - 1; h > 0; h--) {
            int j = h;
            while (j > 0 && Math.abs(e[j]) > EPS * (Math.abs(d[j - 1]) + Math.abs(d[j]))) j--;
            if (j == h) continue;

            int iter = 0;
            do {
                if (++iter > MAX_ITER) return false;
                double w = (d[h - 1] - d[h]) / 2,
                       t = e[h] * e[h], s = Math.sqrt(w * w + t);
                if (w < 0) s = -s;
                double x = d[j] - d[h] + t / (w + s), y = e[j + 1], c;
                for (int k = j; k < h; k++) {
                    if (Math.abs(x) >= Math.abs(y)) {
                        t = -y / x; c = 1 / Math.sqrt(t * t + 1);
                        s = t * c;
                    } else {
                        t = -x / y; s = 1 / Math.sqrt(t * t + 1);
                        c = t * s;
                    }
                    w = d[k] - d[k + 1];
                    t = (w * s + 2 * c * e[k + 1]) * s;
                    d[k] -= t; d[k + 1] += t;
                    if (k > j) e[k] = c * e[k] - s * y;
                    e[k + 1] += s * (c * w - 2 * s * e[k + 1]);
                    for (int i = 0; i < n; i++) {
                        x = a[k][i]; y = a[k + 1][i];
                        a[k]    [i] = c * x - s * y;
                        a[k + 1][i] = s * x + c * y;
                    }
                    if (k < h - 1) {
                        x = e[k + 1]; y = -s * e[k + 2];
                        e[k + 2] *= c;
                    }
                }
            } while (Math.abs(e[h]) > EPS * (Math.abs(d[h - 1]) + Math.abs(d[h])));
        }

        for (int k = 0; k < n - 1; k++) {
            int h = k;
            double t = d[h];
            for (int i = k + 1; i < n; i++)
                if (d[i] > t) {
                    h = i; t = d[h];
                }
            d[h] = d[k]; d[k] = t;
            double[] temp = a[h]; a[h] = a[k]; a[k] = temp;
        }
        return true;
    }

    public static void triDiagonalize(double[][] a, double[] d, double[] e) {
        int n = a.length;
        for (int k = 0; k < n - 2; k++) {
            double[] v = a[k]; d[k] = v[k];
            e[k + 1] = houseHolder(k + 1, v);
            if (e[k + 1] == 0) continue;

            for (int i = k + 1; i < n; i++) {
                double s = 0;
                for (int j = k + 1; j < i; j++) s += a[j][i] * v[j];
                for (int j = i;     j < n; j++) s += a[i][j] * v[j];
                d[i] = s;
            }

            double t = RVector.innerProduct(k + 1, v, d) / 2;
            for (int i = n - 1; i > k; i--) {
                double p = v[i], q = d[i] - t * p; d[i] = q;
                for (int j = i; j < n; j++)
                    a[i][j] -= p * d[j] + q * v[j];
            }
        }
        if (n >= 2) {
            d[n - 2] = a[n - 2][n - 2];
            e[n - 1] = a[n - 2][n - 1];
        }
        if (n >= 1) d[n - 1] = a[n - 1][n - 1];
        for (int k = n - 1; k >= 0; k--) {
            if (k < n - 2) {
                for (int i = k + 1; i < n; i++) {
                    double[] v = a[k], w = a[i];
                    double t = RVector.innerProduct(k + 1, v, w);
                    for (int j = k + 1; j < n; j++)
                        a[i][j] -= t * v[j];
                }
            }
            for (int i = 0; i < n; i++) a[k][i] = 0;
            a[k][k] = 1;
        }
    }

    public static double houseHolder(int ns, double[] x) {
        double s = Math.sqrt(RVector.innerProduct(ns, x, x));
        if (s != 0) {
            if (x[ns] < 0) s = -s;
            x[ns] += s;
            double t = 1 / Math.sqrt(x[ns] * s);
            for (int i = ns; i < x.length; i++) x[i] *= t;
        }
        return -s;
    }
}