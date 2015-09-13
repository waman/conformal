package java_algorithm.statistics.optimization;

/**
* Polytope.java -- ポリトープ法
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
*Polytope（ポリトープ）法：多変数関数を最小化する方法の１つです。
* @see NumberFormatter
*/
public class Polytope {

    /*
      最小化する関数
    */
    static final int N = 11;  /* number of data points */
    static final double X[] = {-10, -8, -6, -4, -2, 0, 2, 4, 6, 8, 10};
    static final double Y[] = {
        2.127, 2.520, 2.629, 2.938, 3.414, 4.669,
        8.014, 6.372, 4.596, 4.296, 4.291
    };

    static double func(double parameter[]) {
        double f = 0.0, g2 = parameter[1] * parameter[1];
        for (int i = 0; i < N; i++) {
            double dx = X[i] - parameter[0];
            double d = parameter[2] * g2 / (dx * dx + g2)
                + parameter[3] + parameter[4] * X[i] - Y[i];
            f += d * d;
        }
        return f;
    }

    /*
      最小化
    */
    static final int M = 5;           /* number of parameters  */
    static final int MAX_ITER = 1000;  /* max number of iterations */
    static final double ALPHA = 1.0;  /* reflection coefficient  */
    static final double BETA  = 2.0;  /* expansion coefficient   */
    static final double GAMMA = 0.5;  /* contraction coefficient */
    static double vertex[][] = new double[M + 1][M];

    static void initializeVertices() {
        final double VER0[] = {0.0, 1.0, 6.0, 3.5, 0.2};
        final double SCALE_FACTOR[] = {1.0, 1.0, 1.0, 1.0, 0.1};
        vertex[0] = VER0;
        for (int j = 0; j < M; j++) {
            final double C = 1 / (Math.sqrt(M + 1) + 2);
            double t = vertex[0][j] + C * SCALE_FACTOR[j];
            for (int k = 1; k <= M; k++) vertex[k][j] = t;
            vertex[j + 1][j] = vertex[0][j] + SCALE_FACTOR[j];
        }
    }

    static void iterPrint(int iter, double f) {
        final NumberFormatter FMT   = new NumberFormatter("-14g");
        final NumberFormatter FMT5d = new NumberFormatter("5d");
        System.out.print("\n" + FMT5d.toString(iter) + "   " + FMT.toString(f));
    }

    /**
    *例題プログラムです。
    */
    public static void main(String[] args) {
        final double TOLERANCE[] = {0.001, 0.001, 0.001, 0.001, 0.001};
        double fNew, vBest[], vWorst[];
        double[] f = new double[M + 1];  /* func values at vertices */
        double[] vCenter = new double[M], vNew = new double[M];

        initializeVertices();
        int kBest = 0, kWorst1 = 0, kWorst2 = 1;
        f[0] = func(vertex[0]);
        for (int k = 1; k <= M; k++) {
            f[k] = func(vertex[k]);
            if (f[k] < f[kBest]){
                kBest = k;
            }else if (f[k] >= f[kWorst1]) {
                kWorst2 = kWorst1;
                kWorst1 = k;
            } else if (f[k] >= f[kWorst2]){
                kWorst2 = k;
            }
        }
        vBest = vertex[kBest];  vWorst = vertex[kWorst1];

        int iter = 1;
        System.out.print("iter     value");
        iterPrint(iter, f[kBest]);
        while (f[kBest] != f[kWorst1]) {
            int m;
            for (m = 0; m < M; m++)
                if (Math.abs(vBest[m] - vWorst[m]) > TOLERANCE[m])
                    break;
            if (m == M) break;
            if (++iter > MAX_ITER) {
                System.out.println("\n収束しません");
                break;
            }
            for (int i = 0; i < M; i++) {
                double x = 0.0;
                for (int k = 0; k <= M; k++)
                    if (k != kWorst1) x += vertex[k][i];
                vCenter[i] = x / M;
                vNew[i] = vCenter[i] + ALPHA * (vCenter[i] - vWorst[i]);
            }
            fNew = func(vNew);
            if (fNew < f[kBest]) { /* vNew is the new best point */
                for (int j = 0; j < M; j++)
                    vWorst[j] = vCenter[j] +
                        BETA * (vNew[j] - vCenter[j]);
                f[kWorst1] = func(vWorst);
                if (f[kWorst1] >= fNew) {
                    System.out.print("R");  /* reflection */
                    System.arraycopy(vNew, 0, vWorst, 0 , M);   // vWorst[] <-- vNew[]
                    f[kWorst1] = fNew;
                } else System.out.print("E");  /* expansion */
                kBest = kWorst1;  vBest = vWorst;
                iterPrint(iter, f[kBest]);
                kWorst1 = kWorst2;
                vWorst = vertex[kWorst1];
                kWorst2 = kBest;
                for (int k = 0; k <= M; k++)
                    if (k != kWorst1 && f[k] > f[kWorst2])
                        kWorst2 = k;
            } else if (fNew <= f[kWorst2]) {
                /* vNew is not the new worst point */
                System.out.print("R");  /* reflection */
                System.arraycopy(vNew, 0, vWorst, 0 , M);
                f[kWorst1] = fNew;
                kWorst1 = kWorst2;  vWorst = vertex[kWorst1];
                kWorst2 = kBest;
                for (int k = 0; k <= M; k++)
                    if (k != kWorst1 && f[k] > f[kWorst2])
                        kWorst2 = k;
            } else {
                if (fNew < f[kWorst1]) {
                    System.arraycopy(vNew, 0, vWorst, 0 , M);
                    f[kWorst1] = fNew;
                }
                do {
                    System.out.print("C");  /* contraction */
                    for (int j = 0; j < M; j++)
                        vWorst[j] += (1 - GAMMA) * (vBest[j] - vWorst[j]);
                    fNew = func(vWorst);
                } while (fNew >= f[kWorst1]);
                f[kWorst1] = fNew;
                if (fNew < f[kBest]) {
                    kBest = kWorst1;
                    f[kBest] = fNew;
                    vBest = vWorst;
                    iterPrint(iter, fNew);
                }
                if (fNew < f[kWorst2]) {
                    kWorst1 = kWorst2;
                    vWorst = vertex[kWorst1];
                    kWorst2 = kBest;
                    for (int k = 0; k <= M; k++)
                        if (k != kWorst1 && f[k] > f[kWorst2])
                            kWorst2 = k;
                }
            }
        }

        System.out.println("\n\niterations: " + iter);
        System.out.println("minimum value: " + f[kBest]);
        System.out.println("parameters:");
        for (int j = 0; j < M; j++)
            System.out.println(j + "  " + vBest[j]);
    }
}