package java_algorithm.traditional.game;

/**
 *  Ishi2.java -- 石取りゲーム 2
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

import static java.io.StreamTokenizer.TT_NUMBER;

public class Ishi2 {
    public static void main(String[] args) throws IOException {
        int[] f = new int[21];
        f[1] = f[2] = 1;
        for (int i = 3; i <= 20; i++) f[i] = f[i - 1] + f[i - 2];
        StreamTokenizer input = new StreamTokenizer(new InputStreamReader(System.in));
        System.out.print("石の数 (2..10000)? ");
        while (input.nextToken() != TT_NUMBER){}
        int n = (int)input.nval;
        if (n < 2 || n > 10000) return;
        int max = n - 1;  boolean myTurn;
        for (myTurn = true; n != 0; myTurn = !myTurn) {
            System.out.println(max + " 個まで取れます.");
            int x;
            if (myTurn) {
                x = n;
                for (int i = 20; x != f[i]; i--) if (x > f[i]) x -= f[i];
                if (x > max) x = 1;
                System.out.println("私は " + x + " 個の石を取ります.");
            } else do {
                System.out.print("何個取りますか? ");
                while (input.nextToken() != TT_NUMBER){}
                x = (int)input.nval;
            } while (x < 1 || x > max);
            n -= x;  max = 2 * x;  if (max > n) max = n;
            System.out.println("残りは " + n + " 個です.");
        }
        if (myTurn) System.out.println("あなたの勝ちです!");
        else        System.out.println("私の勝ちです!");
    }
}