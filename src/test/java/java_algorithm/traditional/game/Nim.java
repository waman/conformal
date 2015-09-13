package java_algorithm.traditional.game;

/**
* Nim.java -- 三山くずし
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Nim {
    /** キーボードから整数を受け取る */
    private static int readInt() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 三山くずしの対戦ゲームです。
     * 最初に三つの山の石の数を入力してください。<br>
     * ゲーム開始です。何番の山からいくつとるか聞いてきますから，
     * 答えてください。
     */
    public static void main(String[] args) throws IOException {
        int[] a = new int[4];
        for (int i = 1; i <= 3; i++)
            do {
                System.out.print(i + " 番の山の石の数? ");
            } while ((a[i] = readInt()) < 1);

        boolean myTurn;
        for (myTurn = true; ; myTurn = !myTurn) {
            int iMax = 0, max = 0;
            for (int i = 1; i <= 3; i++) {
                System.out.print(" " + i + " ");
                for (int j = 1; j <= a[i]; j++) System.out.print("*");
                System.out.println();
                if (a[i] > max) {  max = a[i];  iMax = i;  }
            }
            if (max == 0) break;
            if (myTurn) {
                System.out.println("私の番です.");
                int x = a[1] ^ a[2] ^ a[3];  // 排他的論理和
                int j = 0;
                for (int i = 1; i <= 3; i++)
                    if (a[i] > (a[i] ^ x)) j = i;
                if (j != 0) a[j] ^= x;  else  a[iMax]--;
            } else {
                int i, n;
                do {
                    System.out.print("何番の山からとりますか? ");
                    i = readInt();
                } while (i < 1 || i > 3 || a[i] == 0);
                do {
                    System.out.print("何個とりますか? ");
                    n = readInt();
                } while (n <= 0 || n > a[i]);
                a[i] -= n;
            }
        }
        if (myTurn) System.out.println("あなたの勝ちです!");
        else        System.out.println("私の勝ちです!");
    }
}