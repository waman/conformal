package java_algorithm.statistics;


import java_algorithm.algebra.linear.QRDiagonalizer;
import java_algorithm.algebra.linear.RVector;
import java_algorithm.datatype.string.NumberFormatter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

/**
 *  因子分析
 */
class FactorAnalysis {

    static StreamTokenizer input;

    static void factor(
            int nfac, double[][] r, double[][] q, double[] lambda, double[] work)
            throws IOException {
        int iter = 0, maxiter = 0, m = r.length;
        NumberFormatter fmtRMS = new NumberFormatter(".3g");
        for ( ; ; ) {
            if (++iter > maxiter) {
                System.out.print("繰り返し数 (0:繰り返し終了) ? ");
                while (input.nextToken() != StreamTokenizer.TT_NUMBER){}
                if (input.nval <= 0) break;
                maxiter += (int)input.nval;
            }
            for (int j = 0; j < m; j++) System.arraycopy(r[j], 0, q[j], 0, m);
            if (!QRDiagonalizer.eigen(q, lambda, work)) {
                System.err.println("収束しません");
                throw new IllegalArgumentException();
            }
            double s = RVector.innerProduct(nfac, lambda, lambda);
            System.out.println(
                    iter + ": 非対角成分の RMS 誤差 " +  fmtRMS.toString(Math.sqrt(s / (m * (m - 1)))));
            for (int j = 0; j < m; j++) {
                double sum = 0;
                for (int k = 0; k < nfac; k++)
                    sum += lambda[k] * q[k][j] * q[k][j];
                r[j][j] = sum;
            }
        }
        double t = 0;
        for (int k = 0; k < m; k++) t += lambda[k];
        NumberFormatter fmtf = new NumberFormatter("3d"),
                        fmte = new NumberFormatter("8.4f"),
                        fmtp = new NumberFormatter("5.1f");
        System.out.println("因子    固有値     ％ 累積％");
        double s = 0;
        for (int k = 0; k < m; k++) {
            if (k < nfac)
                 System.out.print(" " + fmtf.toString(k + 1) + " ");
            else System.out.print("(" + fmtf.toString(k + 1) + ")");
            double percent = 100 * lambda[k] / t;  s += percent;
            System.out.println(" " + fmte.toString(lambda[k]) + 
                               "  " + fmtp.toString(percent) + 
                               "  " + fmtp.toString(s));
        }
        System.out.println("合計  " + fmte.toString(t) + "  " + fmtp.toString(s));
        for (int k = 0; k < nfac; k++)
            work[k] = Math.sqrt(Math.abs(lambda[k]));
        fmtf = new NumberFormatter("4d");
        fmte = new NumberFormatter("6.3f");
        fmtp = new NumberFormatter("7.3f");
        System.out.println("変数  共通性   因子負荷量");
        for (int j = 0; j < m; j++) {
            System.out.print(fmtf.toString(j + 1) + "  " + fmte.toString(r[j][j]) + " ");
            for (int k = 0; k < nfac; k++) {
                q[k][j] *= work[k];
                if (k < 9) System.out.print(fmtp.toString(q[k][j]));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("使用法: java FactorAnalysis filename");
            return;
        }
        StatData data = null;
        int n = 0, m = 0;
        double[][] r = null, q = null;
        double[] mean = null, lambda = null, work = null;
        try {
            data = new StatData(args[0]); // throw RuntimeException
            n = data.numberOfRows(); m = data.numberOfColumns();
            r = new double[m][m]; q = new double[m][m];
            mean = new double[m]; lambda = new double[m];
            work = new double[m];
            for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) {
                double t = data.getNum(); // throw RuntimeException
                work[j] = t - mean[j];
                mean[j] += work[j] / (i + 1);
                for (int k = 0; k <= j; k++)
                    r[j][k] += i * work[j] * work[k] / (i + 1);
            }
        } finally {
            data.close();
        }
        for (int j = 0; j < m; j++) {
            work[j] = Math.sqrt(r[j][j]); r[j][j] = 1;
            for (int k = 0; k < j; k++) {
                r[j][k] /= work[j] * work[k]; r[k][j] = r[j][k];
            }
        }
        double t = 1 / Math.sqrt(n - 1.0);
        NumberFormatter fmtd = new NumberFormatter("4d"),
                        fmtg = new NumberFormatter(" -12.5g");
        System.out.println("変数    平均値      標準偏差");
        for (int j = 0; j < m; j++)
            System.out.println(fmtd.toString(j + 1) + "  " + 
                               fmtg.toString(mean[j]) + "  " + 
                               fmtg.toString(t * work[j]));
        fmtg = new NumberFormatter("8.4f");
        System.out.println("相関係数");
        for (int j = 0; j < m; j++) {
            for (int k = 0; k <= j; k++)
                System.out.print(fmtg.toString(r[j][k]) + " ");
            System.out.println();
        }
        input = new StreamTokenizer(new InputStreamReader(System.in));
        for ( ; ; ) {
            System.out.print("\n共通因子の数 (0:実行終了) ? ");
            while (input.nextToken() != StreamTokenizer.TT_NUMBER){}
            int nfac = (int)input.nval;
            if (nfac > m) nfac = m;
            if (nfac < 1) break;
            factor(nfac, r, q, lambda, work);
        }
    }
}