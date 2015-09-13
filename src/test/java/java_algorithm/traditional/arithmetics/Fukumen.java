package java_algorithm.traditional.arithmetics;

/**
 *  Fukumen.java -- 覆面算
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Fukumen {

    static class Letter {
        int current, lower, upper;
        Letter(char c) {  
            if (!Character.isDigit(c)) current = -1;  // 文字（値は可変）
            else current = Character.digit(c, 10);    // 数字（値は固定）
            lower = 0;  upper = 10;
        }
    }

    static final Letter ZERO = new Letter('0');
    static int row, col, solution;
    static Letter[][] words;
    static boolean fixed[] = new boolean[10];

    private static void found() { // 解の表示
        System.out.println("\n解 " + (++solution));
        for (int i = 0; i < row; i++) {
            if (i == row - 1) {
                for (int j = col; j >= 0; j--) System.out.print("-");
                System.out.println();
            }
            if (i == row - 2) System.out.print("+");
            else              System.out.print(" ");
            for (int j = col - 1; j >= 0; j--)
                if (words[i][j] == null) System.out.print(" ");
                else System.out.print(words[i][j].current);
            System.out.println();
        }
    }

    private static void search(int sum, int i, int j) {
        Letter a = words[i][j];  if (a == null) a = ZERO;
        if (i < row - 1) {
            if (a.current < 0) {  // 定まっていないなら
                for (int d = a.lower; d < a.upper; d++)
                    if (!fixed[d]) {
                        a.current = d;  fixed[d] = true;
                        search(sum + d, i + 1, j);  fixed[d] = false;
                    }
                a.current = -1;
            } else search(sum + a.current, i + 1, j);
        } else {  // 最下段の合計
            int d = sum % 10,  carry = sum / 10;
            if (a.current == d) {
                if (j + 1 < col) search(carry, 0, j + 1);
                else if (carry == 0) found();
            } else if (a.current < 0 && !fixed[d] && d >= a.lower) {
                a.current = d;  fixed[d] = true;
                if (j + 1 < col) search(carry, 0, j + 1);
                else if (carry == 0) found();
                a.current = -1;  fixed[d] = false;
            }
        }
    }

    /**
     * 指定した個数（<b>行数</b>）の文字列を
     * enter キーで区切って入力すると，可能な解をすべて出力します。
     * <blockquote><pre>
     * <b>入力例</b>（下線の部分を入力する）
     * 行数? <code><u>3</u>
     * 1: <u>send</u>
     * 2: <u>more</u>
     * 3: <u>money</u><br></code>
     * 出力
     * 解 1<code>
     *   9567
     * + 1085
     * ------
     *  10652
     * </code></pre></blockquote>
     */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("行数？ ");
        row = Integer.parseInt(in.readLine());
        String[] buffers = new String[row];
        for (int i = 0; i < row; i++) {
            System.out.print((i + 1) + ": ");
            buffers[i] = in.readLine();
            if (col < buffers[i].length()) col = buffers[i].length();
        }

        words = new Letter[row][col];
        Map<Character, Letter> letters = new HashMap<>();  // Character -> Letter の対応表
        for (int i = 0; i < row; i++) {
            int k = buffers[i].length();
            for (int j = 0; j < k; j++) {
                char c = buffers[i].charAt(k - 1 - j);
                Letter a = letters.get(c);
                if (a == null) {  // 新規に Letter を追加
                    a = new Letter(c);
                    letters.put(c, a);
                }
                words[i][j] = a;
                if (j == k - 1) a.lower = 1;  // 左端の文字は1以上
            }
        }
        for (int i = 0; i < 10; i++) fixed[i] = false;
        solution = 0;  search(0, 0, 0);
        if (solution == 0) System.out.println("解はありません．");
    }
}