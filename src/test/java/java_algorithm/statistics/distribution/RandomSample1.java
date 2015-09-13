package java_algorithm.statistics.distribution;

/**
 *  無作為抽出
 */

import java.util.Random;

public class RandomSample1 {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("使用法: java RandomSample1 n m");
            System.out.println("  n=母集団の大きさ m=標本の大きさ");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        Random random = new Random();

        for (int i = 0; i < n; i++)
            if (random.nextInt(n - i) < m) {
                System.out.print((i + 1) + " ");
                if (--m <= 0) break;
            }
        System.out.println();
    }
}