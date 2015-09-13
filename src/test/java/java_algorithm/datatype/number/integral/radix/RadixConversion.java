package java_algorithm.datatype.number.integral.radix;

/**
 * RadixConversion.java -- 基数の変換
*/
import java.io.*;
import java.util.*;

/**
* 短い数(long 値の範囲)の基数の変換プログラムです。
*/

public class RadixConversion {

    // 本文では外部クラスとしている。
    static class IntArray {

    // 桁数(0 でない先頭の桁位置)を保持させればよいが，複雑になるためやっていない。
        int radix; // 基数
        int size;  // 最大桁数
        int[] c;

        IntArray(int radix, int size) {
            this.radix = radix;
            c = new int[this.size = size];
        }

        void init() {
            Arrays.fill(c, 0);  // 0 で初期化
        }
    }

    static int longToArray(long value, IntArray array) {
        int i, d = array.radix;

        array.init();
        for (i = 0; value != 0 && i < array.size; i++) {
            array.c[i] = (int)(value % d);  value /= d;
        }
        if (value == 0) return i;   /* 桁数 */
        else        return -1;  /* エラー */
    }

    static int arrayToArray(IntArray src, IntArray dst) { // src の内容は壊される
        dst.init();
        int i, ms = src.size, md = dst.size, rs = src.radix, rd = dst.radix;

        for (i = 0; ms > 0 && i < md; i++) {
            int r = 0;
            for (int j = ms - 1; j >= 0; j--) {
                int t = rs * r + src.c[j];   src.c[j] = t / rd;
                r = t % rd;
            }
            dst.c[i] = r;
            while (ms > 0 && src.c[ms - 1] == 0) ms--;
        }
        if (ms == 0) return i;   /* 桁数 */
        else         return -1;  /* エラー */
    }

    static long arrayToLong(IntArray x) { // d進→10進
        int m = x.size - 1;
        // 下の２行はなくてもよい
        while (m > 0 && x.c[m] == 0) m--; // 0 でない先頭の桁を探す
        if (m == 0) return 0; // 全ての桁が 0
        // y に先頭の桁を入れる
        long y = x.c[m--], d = (long)x.radix;

        for ( ; m >= 0; m--) y = y * d + x.c[m]; // Horner 法
        return y;
    }

    static final int M = 100;

    /**
    * 例題プログラムです。<br>
    * <b>&quot;x = &quot;</b>と聞いてきますから，正の数値を long の範囲内(約19桁以内)で
    * 入力してください。<br>
    * 8 進，2 進に変換した結果を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("x = ");
        long x = Long.parseLong(in.readLine());
        IntArray x8 = new IntArray(8, M);
        IntArray x2 = new IntArray(2, M);
        int m1 = longToArray(x, x8);

        if (m1 < 0) {
            System.out.println("longToArray: error");
            throw new IllegalArgumentException();
        }

        System.out.print("8進(octal)   : ");
        for (int i = m1 - 1; i >= 0; i--) System.out.print(x8.c[i]);
        System.out.println();
        System.out.println("toOctalString: " + Long.toOctalString(x));

        int m2 = arrayToArray(x8, x2);
        if (m2 < 0) {
            System.out.println("arrayToArray: error");
            throw new IllegalArgumentException();
        }

        System.out.print("2進(binary)   : ");
        for (int i = m2 - 1; i >= 0; i--) System.out.print(x2.c[i]);
        System.out.println();
        System.out.println("toBinaryString: " + Long.toBinaryString(x));

        longToArray(x, x8); // arrayToArray() で x8 の内容は壊されているため再変換
        System.out.println("8進→10進: " + arrayToLong(x8));
        System.out.println("2進→10進: " + arrayToLong(x2));
        System.out.println("2つとも入力した値に戻るはず。");
    }
}