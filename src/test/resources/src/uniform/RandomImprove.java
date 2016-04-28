package src.uniform;

/**
 *  乱数の改良法
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RandomImprove {

    static final int POOL_SIZE = 97;  // たとえば
    int pos = POOL_SIZE - 1;
    double pool[];
    NotGoodRandom random;

    RandomImprove(long seed) {
        random = new NotGoodRandom(seed);
        pool = new double[POOL_SIZE];
        for (int i = 0; i < POOL_SIZE; i++)
            pool[i] = random.nextDouble();
    }

    double nextDouble() {
        pos = (int)(POOL_SIZE * pool[pos]);
        double r = pool[pos];
        pool[pos] = random.nextDouble();
        return r;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("乱数の種? ");
        int seed = Integer.parseInt(input.readLine());
        System.out.print("個数? ");
        int n = Integer.parseInt(input.readLine());

        RandomImprove random = new RandomImprove(seed);
        double x = 0,  x0 = 0,  xprev = 0,  s1 = 0,  s2 = 0,  r = 0;
        for (int i = 0; i < n; i++) {
            x = random.nextDouble() - 0.5;
            if (i == 0) x0 = x;

            s1 += x;
            s2 += x * x;
            r += xprev * x;
            xprev = x;
        }
        r = (n * (r + x * x0) - s1 * s1) / (n * s2 - s1 * s1);

        System.out.println("以下は期待値との差を標準誤差で割ったもの.");
        System.out.println("20回中19回は±2以内に入るはず.");
        System.out.println("平均値………………… " + (s1 * Math.sqrt(12.0 / n)));
        System.out.println("隣どうしの相関係数… " + (((n - 1) * r + 1) * Math.sqrt((n + 1.0) / (n * (n - 3.0)))));
    }
}

/** 適当な乱数ルーチン */
class NotGoodRandom {

    long seed = 1;

    NotGoodRandom(long seed) {
        this.seed = seed;
    }

    long nextInt() {  // 32ビット整数乱数
        seed = (seed * 1566083941L + 1) % 0x100000000L;
        return seed;
    }

    double nextDouble() {  // double 実数乱数
        return (1.0 / 0x100000000L) * nextInt();
    }
}