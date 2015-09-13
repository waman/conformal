package java_algorithm.datatype.collection.sort;

/**
 *  トポロジカル・ソーティング
 *  使用法: java TopologicSort < TopologicSort.data
 */

import java.io.*;
import java.util.*;

public class TopologicalSort {

    static int n;  // 点の数
    static int[][] adjacent;  // 隣接行列
    static int[] visited;
    static final int NEVER = 1;  // まだ訪れていない
    static final int JUST  = 2;  // 探索中
    static final int ONCE  = 3;  // 訪問済み

    public static void readGraph() throws IOException { // グラフ入力
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer t = new StringTokenizer(input.readLine());
        n = Integer.parseInt(t.nextToken());
        adjacent = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                adjacent[i][j] = 0;

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

    public static void visit(final int i) {
        visited[i] = JUST;
        for (int j = 1; j <= n; j++) {
            if (adjacent[j][i] == 0) continue;
            if (visited[j] == NEVER) visit(j);
            else if (visited[j] == JUST)
                throw new Error("サイクルあり!");
        }
        visited[i] = ONCE;  System.out.print(" " + i);
    }

    public static void main(String[] args) throws IOException {
        readGraph();  // 隣接行列を読む
        visited = new int[n + 1];
        for (int i = 1; i <= n; i++) visited[i] = NEVER;
        System.out.println("トポロジカル・ソーティングの結果:");
        for (int i = 1; i <= n; i++)
            if (visited[i] == NEVER) visit(i);
        System.out.println();
    }
}