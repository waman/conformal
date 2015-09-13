package java_algorithm.statistics.regression;

/**
* SWEEP.java -- SWEEP演算子法
* 多変量データ読込み関係ルーチン集 StatData.java 使用
*/

import java_algorithm.datatype.string.NumberFormatter;
import java_algorithm.statistics.StatData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
*SWEEP演算子法を使った回帰分析のプログラムです。
*@see Regress
*@see StatData
*@see NumberFormatter
*/
public class SWEEP {

    static int n, m, ndf;   /* データの件数, 変数の数, 自由度 */
    static boolean[] added; /* 説明変数に採用したか */
    static double[][] a;    /* 積和行列 */
    static final NumberFormatter FMT = new NumberFormatter("11.6f");

    static void sweep(int k) { /* 掃き出し演算 */
        double d = a[k][k];

        if (d == 0) {
            System.out.println("変数 " + k + " 一次従属.");
            return;
        }
        for (int j = 0; j <= m; j++) a[k][j] /= d;
        for (int j = 0; j <= m; j++) {
            if (j == k) continue;
            double b = a[j][k];
            for (int i = 0; i <= m; i++) a[j][i] -= b * a[k][i];
            a[j][k] = -b / d;
        }
        a[k][k] = 1 / d;
        if (added[k]) {
            added[k] = false;
            ndf++;
        }else{
            added[k] = true;
            ndf--;
        }
    }

    static void regress(int k) { /* 基準変数 k について回帰係数などを出力 */
        if (added[k]) {
            System.out.println("???");
            return;
        }
        double rms = (ndf > 0 && a[k][k] >= 0) ? Math.sqrt(a[k][k] / ndf) : 0;

        System.out.println("変数 回帰係数   標準誤差        t");
        for (int j = 0; j <= m; j++) {
            if (! added[j]) continue;
            double s = (a[j][j] >= 0) ? Math.sqrt(a[j][j]) * rms : 0;
            System.out.print(" " + j + FMT.toString(a[j][k]) + FMT.toString(s));
            if (s > 0) System.out.print("  " + FMT.toString(Math.abs(a[j][k] / s)));
            System.out.println();
        }
        System.out.println("基準変数: " + k + "  残差2乗和: " +
            FMT.toString(a[k][k]) + "  自由度: " + ndf + "  残差RMS: " + FMT.toString(rms));
    }

    static void residuals() { /* 残差の積和行列を出力 */
        for (int i = 0; i <= m; i += 5) {
            System.out.print(" ");
            for (int k = i; k < i + 5 && k <= m; k++)
                System.out.print("          " + k);
            System.out.println();
            for (int j = 0; j <= m; j++) {
                System.out.print("  "+ j + "  ");
                for (int k = i; k < i + 5 && k <= m; k++)
                    System.out.print(FMT.toString(a[j][k]));
                System.out.println();
            }
        }
    }

    /**
    *対話型回帰分析のプログラムです。<br>
    *<b>使用法： java SWEEP filename</b><br>
    *詳しくは本文をお読み下さい。
    */
    public static void main(String[] args) throws IOException {
        System.out.print("********** 対話型回帰分析 **********\n\n");
        if (args.length != 1) { /* メッセージを表示し終了 */
            System.err.println("使用法: java SWEEP filename");
            throw new IllegalArgumentException();
        }
        StatData dataFile = new StatData(args[0]);
        n = ndf = dataFile.numberOfRows();
        m = dataFile.numberOfColumns();
        // n または m が読み込めない場合は class StatData でエラー
        System.out.println(n + " 件 × " + m + " 変数");
        a = new double[m + 1][m + 1];   // 0 に初期化される
        added = new boolean[m + 1];     // false に初期化される
        double[] x = new double[m + 1];

        for (int i = 0; i < n; i++) {
            System.out.print(".");
            x[0] = 1;
            for (int j = 1; j <= m; j++) x[j] = dataFile.getNum();
            for (int j = 0; j <= m; j++)
                for (int k = j; k <= m; k++) a[j][k] += x[j] * x[k];
        }
        System.out.println();  dataFile.close();
        for (int j = 0; j <= m; j++)
            for (int k = 0; k < j; k++) a[j][k] = a[k][j];

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("命令(Xj/Yj/R/Q)? ");
            String s = in.readLine();
            char c = Character.toUpperCase(s.charAt(0));
            int j = 0;
            if (c == 'X' || c == 'Y') {
                try {
                    j = Integer.parseInt(s.substring(1));
                } catch(NumberFormatException e) { j = -1; }
            }
            if (j < 0 || j > m) c = 0;
            switch (c) {
                case 'X' :  sweep(j);       break;
                case 'Y' :  regress(j);     break;
                case 'R' :  residuals();    break;
                case 'Q' :  return;
                default  :  System.out.println("???");
                            break;
            }
        }
    }
}