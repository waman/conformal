package java_algorithm.statistics;

/**
* PrinComp.java -- 主成分分析
* QR.java -- QR法 を使う。
*/

import java_algorithm.algebra.linear.QR;
import java_algorithm.algebra.linear.RVector;
import java_algorithm.datatype.string.NumberFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
*主成分分析のプログラムです。
*@see NumberFormatter
*@see RVector
*@see StatData
*@see java_algorithm.algebra.linear.QRDiagonalizer
*@see QR
*/
public class PrinComp {

    static void analyze(double[][] x, double[][] q, double[] lambda, int method) {
        int m = x.length, n = x[0].length;  // n 行 m 列(x[m][n])
        int ndf = n - ((method != 0) ? 1 : 0);   /* 自由度 */
        System.out.println("変数    平均値     " + ((method == 0) ? "RMS" : "標準偏差"));
        NumberFormatter fmt12 = new NumberFormatter("12.6f"),
                        fmt8  = new NumberFormatter("8.6f"),
                        fmt6  = new NumberFormatter("6.1f"),
                        fmt3d = new NumberFormatter("3d");

        for (int j = 0; j < m; j++) {
            double t = 0;
            for (int i = 0; i < n; i++) t += x[j][i];
            t /= n;
            if (method != 0) {
                for (int i = 0; i < n; i++)
                    x[j][i] -= t;
            }
            q[j][j] = RVector.innerProduct(x[j], x[j]) / ndf;
            double s = Math.sqrt(q[j][j]);
            System.out.println(fmt3d.toString(j + 1) + fmt12.toString(t) + fmt12.toString(s));
            if (method == 2) {
                q[j][j] = 1;
                for (int i = 0; i < n; i++) x[j][i] /= s;
            }
        }
        for (int j = 0; j < m; j++)
            for (int k = 0; k < j; k++)
                q[j][k] = q[k][j] = RVector.innerProduct(x[j], x[k]) / ndf;

        if (QR.eigen(q, lambda) != 0)
            throw new IllegalArgumentException("収束しません");

        double t = 0;  /* 跡(trace) */
        for (int k = 0; k < m; k++) t += lambda[k];
        System.out.println("因子    固有値     ％  累積％");
        double s = 0;
        for (int k = 0; k < m; k++) {
            double percent = 100 * lambda[k] / t;  s += percent;
            System.out.println(fmt3d.toString(k + 1) +
                fmt12.toString(lambda[k]) + fmt6.toString(percent) +
                fmt6.toString(s));
        }

        System.out.println("合計   " + fmt8.toString(t) + fmt6.toString(s));
        System.out.println("\n変数  重み");
        for (int j = 0; j < m; j++) {
            System.out.print(fmt3d.toString(j + 1));
            for (int k = 0; k < m && k < 5; k++)
                System.out.print(fmt12.toString(q[k][j]));
            System.out.println();
        }
        System.out.println("個体  主成分");
        for (int i = 0; i < n; i++) {
            System.out.print(fmt3d.toString(i + 1));
            for (int k = 0; k < m && k < 5; k++) {
                s = 0;
                for (int j = 0; j < m; j++) s += q[k][j] * x[j][i];
                System.out.print(fmt12.toString(s));
            }
            System.out.println();
        }
    }

    /**
    *ファイルからデータを読み込んで主成分分析を行います。<br>
    *<strong>使用法:<code> java PrinComp filename</strong></code><br>
    *詳しくは本文を参照してください。
    */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) { /* メッセージを表示し終了 */
            System.err.println("使用法: java PrinComp filename");
            throw new IllegalArgumentException();
        }

        System.out.print("0:元のまま 1:平均を引く 2:さらに標準偏差で割る ?");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int method = Integer.parseInt(input.readLine());

        if (method < 0 || method > 2) throw new Error("誤入力");
        StatData dataFile = new StatData(args[0]);
        int n = dataFile.numberOfRows(), m = dataFile.numberOfColumns();  // n 行 m 列 TODO

        double[][] x, q = new double[m][m];
        double[] lambda = new double[m];

        x = dataFile.readData();    /* データを読む */
        analyze(x, q, lambda, method);  /* 主成分分析 */
    }
}