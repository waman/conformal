package java_algorithm.traditional.game;

/**
 *  Ishi1.java -- 石取りゲーム 1
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ishi1 {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("最後に石を取った側が負けです. パスはできません.");
        System.out.print("石の数? ");
        int n = Integer.parseInt(in.readLine());
        System.out.print("1回に取れる最大の石の数? ");
        int m = Integer.parseInt(in.readLine());
        if (n < 1 || m < 1) return;
        boolean myTurn;
        for (myTurn = true; n != 0; myTurn = !myTurn) {
            int x;
            if (myTurn) {
                x = (n - 1) % (m + 1);  if (x == 0) x = 1;
                System.out.println("私は " + x + " 個の石を取ります.");
            } else do {
                System.out.print("何個取りますか? ");
                x = Integer.parseInt(in.readLine());
            } while (x <= 0 || x > n || x > m);
            n -= x;  System.out.println("残りは " + n + " 個です.");
        }
        if (myTurn) System.out.println("あなたの負けです!");
        else        System.out.println("私の負けです!");
    }
}