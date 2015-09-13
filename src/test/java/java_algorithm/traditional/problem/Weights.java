package java_algorithm.traditional.problem;

/*
* Weights.java -- 秤の問題
*/
import java.io.*;

/**
* 秤の問題を解きます。
*/
public class Weights {

    /**
    *秤の問題の解を表示します。<br>
    *<b>「何グラムをはかりますか？ 」</b>と聞いてきますから，
    *正の整数を入力してください。<br>
    *<blockquote><pre>
    *<strong>入力例</strong>（下線の部分を入力する）
    *何グラムをはかりますか？ <u>15</u>
    *<strong>出力</strong>
    *はかるものを左の皿に乗せてください。
    *3 グラムの重りを右の皿に乗せます。
    *9 グラムの重りを右の皿に乗せます。
    *27 グラムの重りを左の皿に乗せます。
    *</pre></blockquote>
    */
    public static void main(String[] args) throws IOException {
        String side[] = { "左", "右" };
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("何グラムをはかりますか？ ");
        int x = Integer.parseInt(in.readLine());
        System.out.println("はかるものを左の皿に乗せてください。");

        int k = 1;
        while (x > 0) {
            int r = x % 3;  x /= 3;
            if (r != 0) {
                System.out.println(k + " グラムの重りを" + side[r - 1] + "の皿に乗せます。");
                if (r == 2) x++;
            }
            k *= 3;
        }
    }
}