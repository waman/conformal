package java_algorithm.statistics.optimization;

/**
 *  Simplex.java -- 線形計画法
 */

import java.io.*;
import java.util.*;
import java.text.*;

public class Simplex {

    static final double EPSILON = 1E-6;  // 無限小
    /** アルゴリズムの途中で変化しない変数 */
    static double[][] a;  // 条件式の係数
    static char[] inequality;  //  <, >, =
    static double[] c;    // 目的関数の係数
    static int m, n;      // 行（条件）, 列（変数）の数
    static int n1;        // n + 負のスラック変数の数
    static int n2;        // n1 + 正のスラック変数の数
    static int n3;        // n2 + 人為変数の数
    static int[] nonZeroRow;   // スラック・人為変数の0でない行
    /** アルゴリズムの途中で変化する変数 */
    static double[][] q;  // 変換行列
    static int jmax;      // 最右列の番号
    static int[] col;     // 各行の基底変数の番号
    static int[] row;     // その列が基底なら対応する条件の番号, そうでなければ0

    /** 実数に変換 */
    private static double nextDouble(StringTokenizer t) {
        return Double.valueOf(t.nextToken());
    }

    /** 標準入力からデータを読む */
    private static void readData() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer t = new StringTokenizer(in.readLine());
        m = Integer.parseInt(t.nextToken());
        n = Integer.parseInt(t.nextToken());
        if (m < 1) throw new IllegalArgumentException("条件の数 m が 1 未満です");
        if (n < 1) throw new IllegalArgumentException("変数の数 n が 1 未満です");
        System.out.println(m + " 条件 " + n + " 変数");

        a = new double[m + 1][n + 1];
        c = new double[n + 1];
        q = new double[m + 1][m + 1];
        col         = new int[m + 1];
        row         = new int[n + 2 * m + 1];
        nonZeroRow  = new int[n + 2 * m + 1];
        inequality  = new char[m + 1];

        t = new StringTokenizer(in.readLine());
        for (int j = 1; j <= n; j++) c[j] = nextDouble(t);
        c[0] = -nextDouble(t);  // c[0] の符号を逆にする

        for (int i = 1; i <= m; i++) {
            t = new StringTokenizer(in.readLine());
            for (int j = 1; j <= n; j++) a[i][j] = nextDouble(t);
            inequality[i] = t.nextToken().charAt(0);

            if ("<>=".indexOf(inequality[i]) == -1)
                throw new IllegalArgumentException("入力エラー");

            a[i][0] = nextDouble(t);
            if (a[i][0] < 0) {
                if      (inequality[i] == '>') inequality[i] = '<';
                else if (inequality[i] == '<') inequality[i] = '>';
                for (int j = 0; j <= n; j++) a[i][j] = -a[i][j];
            } else if (a[i][0] == 0 && inequality[i] == '>') {
                inequality[i] = '<';
                for (int j = 1; j <= n; j++) a[i][j] = -a[i][j];
            }
        }
    }

    /** 準備 */
    private static void prepare() {
        n1 = n;
        for (int i = 1; i <= m; i++)
            if (inequality[i] == '>') {  // 係数が -1 のスラック変数
                n1++;  nonZeroRow[n1] = i;
            }
        n2 = n1;
        for (int i = 1; i <= m; i++)
            if (inequality[i] == '<') {  // 係数が +1 のスラック変数
                n2++;  col[i] = n2;
                nonZeroRow[n2] = row[n2] = i;
            }
        n3 = n2;
        for (int i = 1; i <= m; i++)
            if (inequality[i] != '<') {  // 人為変数
                n3++;  col[i] = n3;
                nonZeroRow[n3] = row[n3] = i;
            }
        for (int i = 0; i <= m; i++) q[i][i] = 1;
    }

    /** シンプレックス表の (i,j) 成分を計算する */
    private static double tableau(int i, int j) {
        if (col[i] < 0) return 0;  // 冗長な条件のため消した行
        if (j <= n) {
            double sum = 0;
            for (int k = 0; k <= m; k++) sum += q[i][k] * a[k][j];
            return sum;
        }
        double u = q[i][nonZeroRow[j]];
        if (j <= n1) return -u;
        if (j <= n2 || i != 0) return u;
        return u + 1;  // j > n2 && i == 0
    }

    /** デモンストレーションのためシンプレックス表を出力 */
    private static void printTableau() {
        printTableau(-1, -1);
    }

    private static void printTableau(int ipivot, int jpivot) {
        System.out.print("              ");
        for (int j = 1; j <= jmax; j++) {
            String s = (row[j]==0 ? "   " : "  @") + "x[" + j + "] ";
            System.out.print(s.substring(0, 8) +
                ((j == 0 || j == n || j == n2) ? "|" : ""));
        }
        System.out.println();
        final DecimalFormat df = new DecimalFormat("####0.00");
        for (int i = 0; i <= m; i++) {
            if (col[i] < 0) continue;  // 冗長な条件
            String s = "  " + i + ": ";
            System.out.print(s.substring(s.length() - 5));
            for (int j = 0; j <= jmax; j++) {
                s = "    " + df.format(tableau(i, j));
                System.out.print(s.substring(s.length() - 7) +
                    ((i == ipivot && j == jpivot) ? '*' : ' ') +
                    ((j == 0 || j == n || j == n2) ? "|" : ""));
            }
            System.out.println();
        }
    }

    /** 掃き出し */
    private static void pivot(int ipivot, int jpivot, double[] pivotColumn) {
        System.out.println("ピボット(" + ipivot + "," + jpivot + ")");
        for (int j = 1; j <= m; j++)
            q[ipivot][j] /= pivotColumn[ipivot];
        for (int i = 0; i <= m; i++)
            if (i != ipivot) {
                final double u = pivotColumn[i];
                for (int j = 1; j <= m; j++)
                    q[i][j] -= q[ipivot][j] * u;
            }
        row[col[ipivot]] = 0;
        col[ipivot] = jpivot;  row[jpivot] = ipivot;
    }

    /** 最小化 */
    private static void minimize() {
        double[] pivotColumn = new double[m + 1];
        for ( ; ; ) {
            int jpivot;  // ピボット列 jpivot を見つける
            for (jpivot = 1; jpivot <= jmax; jpivot++)
                if (row[jpivot] == 0) {  // 基底でない
                    pivotColumn[0] = tableau(0, jpivot);
                    if (pivotColumn[0] < -EPSILON) break;
                }
            if (jpivot > jmax) break;  // 最小化完了
            int ipivot = 0;  // ピボット行 ipivot を見つける
            double uMin = Double.MAX_VALUE;
            for (int i = 1; i <= m; i++) {
                pivotColumn[i] = tableau(i, jpivot);
                if (pivotColumn[i] > EPSILON) {
                    double u = tableau(i, 0) / pivotColumn[i];
                    if (u < uMin) {  ipivot = i;  uMin = u;  }
                }
            }
            if (ipivot == 0) throw new IllegalArgumentException("目的関数は下限がありません");
            printTableau(ipivot, jpivot);
            pivot(ipivot, jpivot, pivotColumn);
        }
        printTableau();
        System.out.println("最小値は " + (-tableau(0, 0)) + " です");
    }

    /** フェーズ１ */
    private static void phase1() {
        System.out.println("フェーズ１");
        jmax = n3;
        for (int j = n2 + 1; j <= n3; j++) q[0][row[j]] = -1;
        minimize();

        if (tableau(0, 0) < -EPSILON)
            throw new IllegalArgumentException("可能な解はありません");

        for (int i = 1; i <= m; i++)  // 人為変数から基底をなくす
            if (col[i] > n2) {
                int j;
                for (j = 1; j <= n; j++)
                    if (row[j] == 0 && Math.abs(tableau(i, j)) > EPSILON) {
                        // System.out.println("まれな条件での処理");
                        double[] pivotColumn = new double[m + 1];
                        for (int k = 0; k <= m; k++)
                            pivotColumn[k] = tableau(k, j);
                        pivot(i, j, pivotColumn);
                        break;
                    }
                if (j > n) {
                    System.out.println("条件 " + i + " は冗長です");
                    col[i] = -1;
                }
            }
        q[0][0] = 1;  //  フェーズ２へ向けた修正
        for (int i = 1; i <= m; i++) q[0][i] = 0;
        for (int j = 1; j <= n; j++)
            if (row[j] != 0 && c[j] != 0)
                for (int i = 1; i <= m; i++)
                    q[0][i] -= q[row[j]][i] * c[j];
    }

    /** フェーズ２ */
    private static void phase2() {
        System.out.println("フェーズ２");
        jmax = n2;
        System.arraycopy(c, 0, a[0], 0, n+1);  // 目的関数
        minimize();
    }

    /** 結果の出力 */
    private static void printVariable() {
        System.out.println("0 でない変数の値:");
        for (int j = 1; j <= n; j++) {
            final int i = row[j];
            if (i != 0)
                System.out.println("x[" + j + "] = " + tableau(i, 0));
        }
    }

    public static void main(String[] args) throws IOException {
        readData();                 // データを読む
        prepare();                  // 下ごしらえ
        if (n3 != n2) phase1();     // フェーズ1
        phase2();                   // フェーズ2
        printVariable();            // 結果の出力
    }
}