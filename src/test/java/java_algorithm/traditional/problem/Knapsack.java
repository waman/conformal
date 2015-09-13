package java_algorithm.traditional.problem;

/**
 *  ナップザックの問題
 */

import java.io.*;

public class Knapsack {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("ナップザックの大きさ? ");
        final int knapSize = Integer.parseInt(in.readLine());
        System.out.print("品目数? ");
        final int n = Integer.parseInt(in.readLine());
        int[] size = new int[n];
        int[] price = new int[n];
        for (int i = 0; i < n; i++) {
            System.out.print("品目 " + i + " の大きさ? ");
            size[i] = Integer.parseInt(in.readLine());
            System.out.print("品目 " + i + " の価格  ? ");
            price[i] = Integer.parseInt(in.readLine());
        }
        int[] maxValue = new int[knapSize + 1];
        int[] item = new int[knapSize + 1];

        for (int s = 0; s <= knapSize; s++) maxValue[s] = 0;
        for (int i = 0; i < n; i++) {
            for (int s = size[i]; s <= knapSize; s++) {
                int space = s - size[i];
                int newValue = maxValue[space] + price[i];
                if (maxValue[s] < newValue) {
                    maxValue[s] = newValue;  item[s] = i;
                }
            }
        }

        System.out.println("品目  価格");
        for (int s = knapSize; maxValue[s] > 0; s -= size[item[s]])
            System.out.println(item[s] + " " + price[item[s]]);
        System.out.println("合計 " + maxValue[knapSize]);
    }
}