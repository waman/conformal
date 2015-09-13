package java_algorithm.computerscience.check;

/**
* ISBN.java -- ISBN番号
*/

import java.io.IOException;

/**
* ISBN(International Standard Book Number)番号をチェックするプログラムです。
*/
public class ISBN {
    /**
    *ISBN番号を入力すると有効な場合&quot;Valid&quot;，無効な場合&quot;Wrong&quot;と表示します。<br>
    * 0～9 または X(x) を使って１０桁の数字を入力してください。
    * &quot;-&quot; で区切ってもかまいません。
    *10桁を超える部分は無視されます。<br>
    * 入力例１：<u>4-7561-1667-1</u><br>
    * 入力例２：<u>4-87408-852-X</u><br>
    */
    public static void main(String[] args) throws IOException {
        final int N = 10;    // ISBN番号の桁数
        int[] d = new int[N + 1];
        int nc = 1;     // 数字のカウンタ

        System.out.print("ISBN book number: ");
        while (nc <= N) {
            int c = System.in.read();
            if (c >= '0' && c <= '9') d[nc++] = c - '0';
            else if (nc == N && (c == 'x' || c == 'X')) d[nc++] = 10;
            else if (c == '-'){}
            else {
                System.err.println("正しく数値を入力してください");
                System.exit(-1);
            }
        }
// d[0] = 0;
        for (int i = 1; i <= N; i++) d[i] += d[i - 1];
        for (int i = 1; i <= N; i++) d[i] += d[i - 1];
        if (d[N] % 11 == 0) System.out.println("Valid");  /* 有効な番号 */
        else                System.out.println("Wrong");  /* 無効な番号 */
    }
}