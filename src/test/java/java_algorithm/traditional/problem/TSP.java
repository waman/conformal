package java_algorithm.traditional.problem;

/**
 *  巡回セールスマン問題
 */

import java.io.*;
import java.util.*;

public class TSP {

    static int n, d[][], tour[], tourLength;

    /** 巡回路を出力 */
    private static void printTour() {
        System.out.print("巡回路 = [ ");
        for (int i = 0; i < n; i++) System.out.print(tour[i] + " ");
        System.out.println("] 距離 = " + tourLength);
    }

    /** 初期巡回路を生成（nearest neighbor法） */
    private static void initialTour() {
        boolean used[] = new boolean[n];
        for (int i = 0; i < n; i++) used[i] = false;
        int current = (int)(Math.random() * n);
        for (int i = 0; i < n; i++) {
            tour[i] = current;  used[current] = true;
            int minj = 0,  min = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++)
                if (!used[j] && d[current][j] < min) {
                    min = d[current][j];  minj = j;
                }
            current = minj;  // 未巡回の最も近い都市
        }
        tourLength = 0;  // 巡回路の距離を計算
        for (int i = 0; i < n; i++)
            tourLength += d[tour[i]][tour[(i + 1) % n]];
    }

    /** 2-opt による解の更新（主に交差する枝をなくす） */
    private static boolean twoOpt(final int a) {
        final int a1 = tour[a % n],  a2 = tour[(a + 1) % n];
        for (int b = a + 2; b < a + n - 1; b++) {
            final int b1 = tour[b % n],  b2 = tour[(b + 1) % n];
            int diff = - (d[a1][a2] + d[b1][b2])
                       + (d[a1][b1] + d[a2][b2]);
            if (diff < 0) {  // 改良解を発見
                tourLength += diff;
                System.out.println("現在の距離(2-opt) = " + tourLength);
                for (int i = 0; i < (b - a) / 2; i++) {  // 改良解を実現
                    int swap = tour[(a + 1 + i) % n];
                    tour[(a + 1 + i) % n] = tour[(b - i) % n];
                    tour[(b - i) % n] = swap;
                }
                return true;  // 改良あり
            }
        }
        return false;  // 改良なし
    }

    private static void orOptModify(int a, int b, int c, int diff, boolean reverse) {
        tourLength += diff;
        System.out.println("現在の距離(Or-opt) = " + tourLength);
        int len = b - a,  tmp[] = new int[len];
        for (int i = 0; i < len; i++) tmp[i] = tour[(a + 1 + i) % n];
        for (int i = b + 1; i <= c; i++)
            tour[(i - len) % n] = tour[i % n];
        for (int i = 0; i < len; i++)
            tour[(c - i) % n] = (!reverse) ? tmp[i] : tmp[len - 1 - i];
    }

    /** Or-opt による解の更新（1～4個の都市を対岸の巡回路に移す） */
    private static boolean orOpt(final int a) {
        final int a1 = tour[a % n],  a2 = tour[(a + 1) % n];
        for (int b = a + 1; b <= a + 4; b++) {
            final int b1 = tour[b % n],  b2 = tour[(b + 1) % n];
            for (int c = b + 1; c < a + n - 1; c++) {
                final int c1 = tour[c % n],  c2 = tour[(c + 1) % n];
                final int before = d[a1][a2] + d[b1][b2] + d[c1][c2];
                int after =        d[a1][b2] + d[a2][c2] + d[b1][c1];
                if (after - before < 0) {  // 改良解を発見
                    orOptModify(a, b, c, after - before, false);
                    return true;  // 改良あり
                }
                if (b == a + 1) continue;
                after = d[a1][b2] + d[a2][c1] + d[b1][c2];
                if (after - before < 0) {  // 逆向きの接続で改良解を発見
                    orOptModify(a, b, c, after - before, true);
                    return true;  // 改良あり
                }
            }
        }
        return false;  // 改良なし
    }

    public static int[][] readDistance() throws IOException {
        BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in));
        final int n = Integer.parseInt(in.readLine());
        int[] x = new int[n],  y = new int[n];
        int[][] distance = new int[n][n];
        for (int i = 0; i < n; i++) {
            StringTokenizer t = new StringTokenizer(in.readLine());
            x[i] = Integer.parseInt(t.nextToken());
            y[i] = Integer.parseInt(t.nextToken());
            for (int j = 0; j < i; j++) {
                final int dx = x[i] - x[j];
                final int dy = y[i] - y[j];
                distance[i][j] = distance[j][i] =
                //  (int)Math.round(Math.sqrt(dx * dx + dy * dy));  // 通常
                    (int)Math.ceil(Math.sqrt((dx * dx + dy * dy) / 10.0));
            }
        }
        System.out.println("都市 x  y");
        for (int i = 0; i < n; i++)
            System.out.println(i + " " + x[i] + " " + y[i]);
        return distance;
    }

    public static void main(String[] args) throws IOException {
        d = readDistance();  n = d.length;  // 距離行列の読み込み
        tour = new int[n];  initialTour();
        System.out.println("初期巡回路");  printTour();
        for (int i = 0, last = 0; i < last + n; i++)  // 局所探索 (2-opt)
            if (twoOpt(i)) last = i + 1;
        for (int i = 0, last = 0; i < last + n; i++)  // 局所探索 (Or-opt)
            if (orOpt(i)) last = i + 1;
        System.out.println("局所最適解");  printTour();
    }
}