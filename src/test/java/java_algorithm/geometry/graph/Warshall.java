package java_algorithm.geometry.graph;

/**
 *  推移的閉包
 *  使用例: java Warshall < Warshall.data
 */

import java.io.*;
import java.util.*;

public class Warshall {

    static int n;  // 点の数
    static int[][] adjacent;  // 隣接行列

    public static void readGraph() throws IOException { // グラフ入力
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer t = new StringTokenizer(input.readLine());
        n = Integer.parseInt(t.nextToken());
        adjacent = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) adjacent[i][j] = 0;
            adjacent[i][i] = 1;
        }

        String s;
        while ((s = input.readLine()) != null) {
            t = new StringTokenizer(s);
            int i = Integer.parseInt(t.nextToken());
            int j = Integer.parseInt(t.nextToken());
            adjacent[i][j] = 1;
        }

        System.out.println("隣接行列:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                System.out.print(" " + adjacent[i][j]);
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        readGraph();  // 隣接行列を読む
        for (int k = 1; k <= n; k++)
            for (int i = 1; i <= n; i++)
                if (adjacent[i][k] != 0)
                    for (int j = 1; j <= n; j++)
                        if (adjacent[k][j] != 0) adjacent[i][j] = 1;
        System.out.println("推移的閉包:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                System.out.print(" " + adjacent[i][j]);
            System.out.println();
        }
    }
}