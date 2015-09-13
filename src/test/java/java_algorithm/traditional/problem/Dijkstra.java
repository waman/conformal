package java_algorithm.traditional.problem;

/**
 *  最短路問題
 *  使用例: java Dijkstra <Dijkstra.dat
 */

import java.io.*;
import java.util.*;

public class Dijkstra {

    // データ入力
    private static int[][] readWeight() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s = in.readLine();
        StringTokenizer t = new StringTokenizer(s);
        final int n = Integer.parseInt(t.nextToken());

        int[][] weight = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                weight[i][j] = Integer.MAX_VALUE;

        while ((s = in.readLine()) != null) {
            t = new StringTokenizer(s);
            int i = Integer.parseInt(t.nextToken());
            int j = Integer.parseInt(t.nextToken());
            weight[i][j] = weight[j][i] =
                Integer.parseInt(t.nextToken());
        }

        System.out.println("データ weight(i,j)");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (weight[i][j] < Integer.MAX_VALUE) {
                    s = "   " + weight[i][j];
                    s = s.substring(s.length() - 4);
                } else {
                    s = "  ∞";
                }
                System.out.print(s);
            }
            System.out.println();
        }
        return weight;
    }

    static final int START = 0; // 出発点

    public static void main(String[] args) throws IOException {
        int[][] weight = readWeight(); // 距離 weight[0..n-1][0..n-1] を読む
        final int n = weight.length;
        boolean[] visited = new boolean[n];
        int[] distance = new int[n];
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            visited[i] = false;  distance[i] = Integer.MAX_VALUE;
        }

        distance[START] = 0;
        int next = START;
        int min;
        do {
            final int i = next;
            visited[i] = true;
            min = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (visited[j]) continue;
                if (weight[i][j] < Integer.MAX_VALUE &&
                    distance[i] + weight[i][j] < distance[j]) {
                    distance[j] = distance[i] + weight[i][j];
                    prev[j] = i;
                }
                if (distance[j] < min) {
                    min = distance[j];  next = j;
                }
            }
        } while (min < Integer.MAX_VALUE);

        System.out.println("点  直前の点  最短距離");
        for (int i = 0; i < n; i++) {
            if (i == START || !visited[i]) continue;
            System.out.println(i + " " + prev[i] + " " + distance[i]);
        }
    }
}