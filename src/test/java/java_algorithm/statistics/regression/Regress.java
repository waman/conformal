package java_algorithm.statistics.regression;

import java_algorithm.algebra.linear.RVector;
import java_algorithm.datatype.string.NumberFormatter;
import java_algorithm.statistics.StatData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  最小２乗法による線形回帰分析
 */
public class Regress {

    static final double EPS2 = 1e-12;       //  許容誤差の２乗

    /**
     *  QR 分解を行うルーチン(ピボット選択を行わない)
     *
     *  @param  x       行列
     *  @param  b       回帰係数
     *  @param  col     列番号
     *  @param  normsq  各列の２乗和
     */
    static int lsq(double[][] x, double[] b, int[] col, double[] normsq) throws IOException {
        final boolean VERBOSE = question("途中経過を含めますか");
        final int n = x[0].length, m = b.length;
        for (int j = 0; j < m; j++)
            normsq[j] = RVector.innerProduct(x[j], x[j]);
        int r = 0;
        for (int k = 0; k < m; k++) {
            if (normsq[k] == 0.0) continue;
            double[] v = x[k];
            double u = RVector.innerProduct(r, v, v);
            if (VERBOSE) {
                NumberFormatter dfmt = new NumberFormatter("4d"),
                                gfmt = new NumberFormatter("-14g");
                System.out.print(
                        "\n" + dfmt.toString(k + 1) + ": ２乗和÷初期２乗和 = " +
                        gfmt.toString(u / normsq[k]));
            }
            if (u / normsq[k] < EPS2) continue;
            x[r] = v;
            col[r] = k;
            u = Math.sqrt(u);
            if (v[r] < 0.0) u = -u;
            v[r] += u;
            final double t = 1 / (v[r] * u);
            for (int j = k + 1; j <= m; j++) {
                double[] w = x[j];
                double s = t * RVector.innerProduct(r, v, w);
                for (int i = r; i < n; i++) w[i] -= s * v[i];
            }
            v[r] = -u;
            if (VERBOSE)
                System.out.print(
                        "  残差２乗和 = " +  RVector.innerProduct(r + 1, x[m], x[m]));
            r++;
        }
        for (int j = r - 1; j >= 0; j--) {
            double s = x[m][j];
            for (int i = j + 1; i < r; i++) s -= x[i][j] * b[i];
            b[j] = s / x[j][j];
        }
        if (VERBOSE) System.out.print("\n\n");
        return r;   //  階数
    }

    /**
     *  QR 分解を行うルーチン(ピボット選択を行う)
     *
     *  @param  x           行列
     *  @param  b           回帰係数
     *  @param  col         列番号
     *  @param  initnormsq  各列の初期２乗和
     *  @param  normsq      各列の２乗和
     */
    static int lsq(double[][] x, double[] b, int[] col, double[] initnormsq, double[] normsq) {
        final int n = x[0].length, m = b.length;
        for (int j = 0; j < m; j++) {
            col[j] = j;
            normsq[j] = RVector.innerProduct(x[j], x[j]);
            initnormsq[j] = (normsq[j] != 0.0) ? normsq[j] : -1;
        }
        int r = 0;
        for ( ; r < m; r++) {
            if (r != 0) {
                int j = r;
                double u = 0.0;
                for (int i = r; i < m; i++) {
                    final double t = normsq[i] / initnormsq[i];
                    if (t > u) { u = t; j = i; }
                }

                int k = col[j];
                col[j] = col[r];
                col[r] = k;

                double s = normsq[j];
                normsq[j] = normsq[r];
                normsq[r] = s;

                s = initnormsq[j];
                initnormsq[j] = initnormsq[r];
                initnormsq[r] = s;
                double[] v = x[j]; x[j] = x[r]; x[r] = v;
            }

            double[] v = x[r];
            double u = RVector.innerProduct(r, v, v);
            if (u / initnormsq[r] < EPS2) break;
            u = Math.sqrt(u);   if (v[r] < 0.0) u = -u;
            v[r] += u;
            double t = 1 / (v[r] * u);
            for (int j = r + 1; j <= m; j++) {
                double[] w = x[j];
                final double s = t * RVector.innerProduct(r, v, w);
                for (int i = r; i < n; i++) w[i] -= s * v[i];
                if (j < m) normsq[j] -= w[r] * w[r];
            }
            v[r] = -u;
        }
        for (int j = r - 1; j >= 0; j--) {
            double s = x[m][j];
            for (int i = j + 1; i < r; i++) s -= x[i][j] * b[i];
            b[j] = s / x[j][j];
        }
        return r;   //  階数
    }

    /**
     *  上三角行列の逆行列を求める
     *
     *  @param  r   行列の次元 (r <= x.length)
     *  @param  x   行列
     */
    static void invr(int r, double[][] x) {
        for (int k = 0; k < r; k++) {
            x[k][k] = 1 / x[k][k];
            for (int j = k - 1; j >= 0; j--) {
                double s = 0.0;
                for (int i = j + 1; i <= k; i++) s -= x[i][j] * x[i][k];
                x[j][k] = s * x[j][j];
            }
        }
    }

    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /** YES/NO 形式の質問の答えを返す */
    private static boolean question(String msg) throws IOException {
        System.out.print(msg + " (y/n)? ");
        return input.readLine().compareToIgnoreCase("y") == 0;
    }

    /** エントリポイント */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("使用法: java Regress filename");
            return;
        }
        StatData datafile = new StatData(args[0]);
        double[][] raw_x = datafile.readData();
        datafile.close();
        final int n = raw_x[0].length, m = raw_x.length,
                  con = question("定数項を含めますか") ? 1 : 0,
                  p = m + con - 1;
        double[][] x = new double[p + 1][];
        System.arraycopy(raw_x, 0, x, con, m);
        if (con != 0) {
            x[0] = new double[n];
            for (int i = 0; i < n; i++) x[0][i] = 1.0;  //  定数項
        }
        int[] col = new int[p];             //  列番号の表
        double[] b = new double[p],         //  回帰係数
                 normsq = new double[p],    //  ノルム２乗
                 initnormsq = question("ピボット選択を行いますか") ? new double[p] : null;   //  初期ノルム２乗
        final int r = (initnormsq == null) ?
                lsq(x, b, col, normsq) :    //  最小２乗法(ピボット選択なし)
                lsq(x, b, col, initnormsq, normsq); //  〃  あり)
        double rss = RVector.innerProduct(r, x[p], x[p]);   //  残差２乗和
        invr(r, x); //  x[0…r-1][0…r-1] の逆行列を求める
        NumberFormatter vfmt = new NumberFormatter("4d"),
                        bfmt = new NumberFormatter(" #-14g"),
                        tfmt = new NumberFormatter(" #-11.3g");
        System.out.println("変数  回帰係数       標準誤差       t");
        for (int j = 0; j < r; j++) {
            double t = RVector.innerProduct(j, r, x[j], x[j]),  //  内積
                   s = Math.sqrt(t * rss / (n - r));
            System.out.print(
                    vfmt.toString(col[j] + 1 - con) + "  " +
                    bfmt.toString(b[j]) + " " + bfmt.toString(s));
            if (s > 0)
                System.out.print("  " + tfmt.toString(Math.abs(b[j] / s)));
            System.out.println("");
        }
        System.out.println(
                    "残差２乗和 / 自由度 = " + rss + " / " + (n - r) + " = " + (rss / (n - r)));
    }
}