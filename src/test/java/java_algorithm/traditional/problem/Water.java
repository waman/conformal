package java_algorithm.traditional.problem;

/**
* Water.java -- 水をはかる問題
*/

import java.io.*;

/**
* 水をはかる問題を解くプログラムです。
*/

public class Water {

    static int gcd(int x, int y){  /* 最大公約数 */
        if (x == 0) return y;
        else return gcd(y % x, x);    // TODO
    }

    /**
    *水をはかる手順を表示します。<br>
    *<b>「容器Ａの容積? 」</b>，<b>「容器Ｂの容積? 」</b>，<b>「はかりたい容積? 」</b>
    *と順次聞いてきますから，正の整数を入力してください。<br>
    *<blockquote><pre>
    *<strong>入力例</strong>（下線の部分を入力する）
    *容器Ａの容積? <u>3</u>
    *容器Ｂの容積? <u>5</u>
    *はかりたい容積? <u>1</u>
    *<strong>出力</strong>
    *Ａに水を満たします
    *Ａの水をすべてＢに移します
    *Ａに水を満たします
    *Ａの水をＢがいっぱいになるまでＢに移します
    *Ａにはかれました
    *</pre></blockquote>
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("容器Ａの容積? ");
            int a = Integer.parseInt(in.readLine());
        System.out.print("容器Ｂの容積? ");
            int b = Integer.parseInt(in.readLine());
        System.out.print("はかりたい容積? ");
            int v = Integer.parseInt(in.readLine());
        if (v > a && v > b || v % gcd(a, b) != 0) {
            System.out.println("はかれません");  System.exit(-1);
        }
        int x = 0, y = 0;
        do {
            if (x == 0) {
                System.out.println("Ａに水を満たします");  x = a;
            } else if (y == b) {
                System.out.println("Ｂを空にします");  y = 0;
            } else if (x < b - y) {
                System.out.println("Ａの水をすべてＢに移します");
                y += x;    // TODO
                x = 0;
            } else {
                System.out.println("Ａの水をＢがいっぱいになるまで" +
                    "Ｂに移します");  x -= b - y;  y = b;
            }
        } while (x != v && y != v);

        if      (x == v) System.out.println("Ａにはかれました");
        else System.out.println("Ｂにはかれました");    // y == v
    }
}