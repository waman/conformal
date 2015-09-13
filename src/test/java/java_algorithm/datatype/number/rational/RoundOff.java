package java_algorithm.datatype.number.rational;

/*
* RoundOff.java -- 四捨五入
*/

import java_algorithm.datatype.string.NumberFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* 四捨五入のプログラムです。
*/

public class RoundOff {

    static int round1000(int a, int b){ // $0 \le a \le b$, $b \ne 0$
        int d;

        if (a <= Integer.MAX_VALUE / 2000) { // Integer.MAX_VALUE = 2^31 - 1 = 2147483647
            d = a * 2000 / b;
        } else {
            int bl = b % 2000, bh = b / 2000;
            d = a / bh;
            int rp = (a % bh) * 2000, rm = bl * d;
            if (rp < rm) {
                d--;
                rm -= rp;
                while (rm > b) {
                    d--;  rm -= b;
                }
            }
        }
        return (d + 1) / 2;
    }

    /**
    *分数を小数に変換します。
    *&quot;<strong>a =</strong>&quot;，&quot;<strong>b =</strong>&quot; と聞いてきます。
    * <strong>a &lt; b &lt; 2147483647</strong> を満たす自然数を入力すると，a/b を
    *２つの方法で小数第4位で四捨五入した結果を表示します。<br>
    *1000 * a が桁あふれを起こす場合は２つの結果は異なります。<br>
    *<b>例</b> <code>a = 12458978, b = 21245879</code>
    */
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        NumberFormatter fmt03d = new NumberFormatter("03d");

        System.out.print("a = ");
        int a = Integer.parseInt(input.readLine());
        System.out.print("b = ");
        int b = Integer.parseInt(input.readLine());
        int r = round1000(a, b);

        System.out.println("a / b = " + (r / 1000) + "." + fmt03d.toString(r % 1000));
        r = (1000 * a + b / 2) / b; // 1000 * a で桁あふれの可能性
        System.out.println("a / b = " + (r / 1000) + "." + fmt03d.toString(r % 1000));
        System.out.println("a / b = " + (double)a/b + " (double value)");
    }
}