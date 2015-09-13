package java_algorithm.computerscience;

/**
 *  MultiPrecision.java -- 多倍長演算
 */

import java.math.BigDecimal;
import java.util.Arrays;

/**
*多倍長演算のプログラムです。
*/
public class MultiPrecision {

    /** 基数のビット数です。30 にとっています。 */
    public static final int RADIXBITS = 30;

    /** 基数です。2<sup>30</sup> = 1,073,741,824 にとっています。 */
    public static final int RADIX = (1 << RADIXBITS);

    private int n;              // RADIX 進法で小数第 n 位まで

    /**
    *コンストラクタ
    *@param n RADIX 進法の桁数
    */
    public MultiPrecision(int n) {    // コンストラクタ（n は RADIX 進法の桁数）
        this.n = n;
    }

    /**
    *和 c = a + b を計算します。
    */
    public void add(int[] a, int[] b, int[] c) {
        int t = 0;
        for (int i = n; i >= 0; i--) {
            t += a[i] + b[i];
            c[i] = t & (RADIX - 1);
            t >>= RADIXBITS;
        }
        if (t != 0) throw new Error("Overflow");
    }

    /**
    *差 c = a - b を計算します。a &gt; b でなければいけません。
    */
    public void sub(int[] a, int[] b, int[] c) {
        int t = 0;
        for (int i = n; i >= 0; i--) {
            t = a[i] - b[i] - t;
            c[i] = t & (RADIX - 1);
            t = (t >> RADIXBITS) & 1;
        }
        if (t != 0) throw new Error("Overflow");
    }

    /**
    *小さい数との積 c = a * x を計算します。
    */
    public void muls(int[] a, int x, int[] b) {
        long t = 0;
        for (int i = n; i >= 0; i--) {
            t += (long)a[i] * x;
            b[i] = (int)t & (RADIX - 1);
            t >>= RADIXBITS;
        }
        if (t != 0) throw new Error("Overflow");
    }

    /**
    *小さい数での商 c = a / x を計算します。
    *@param m a[] の 0でない最左位置
    */
    public int divs(int m, int[] a, int x, int[] b) {// a[m..0] / x --> b
        long t = 0;
        for (int i = m; i <= n; i++) {
            t = (t << RADIXBITS) + a[i];
            b[i] = (int)(t / x);
            t = t - (long)b[i] * x; // t %= x より速い。
//          t %= x;
        }
        if (2 * t >= x)         // 四捨五入
            for (int i = n; (++b[i] & RADIX) != 0; i--)
                b[i] &= RADIX - 1;
        return (b[m] != 0) ? m : (m + 1);  // 0でない最左位置
    }

    /**
    *10進表示の文字列へ変換します。
    */
    public String toString(int[] r) {
        int[] a = new int[r.length];
        System.arraycopy(r, 0, a, 0, r.length); // r[] の内容を壊さないようにコピーを作る。
        StringBuilder x = new StringBuilder(a[0] + ".");

        for (int i = 0; i < n; i++) {
            a[0] = 0;
            muls(a, 1000000000, a);
            String sa = Integer.toString(a[0]);
            x.append("000000000".substring(0, 9 - sa.length())).append(sa);
        }
        return x.toString();
    }

    /**
    *BigDecimal 数値に変換します。
    */
    public BigDecimal toBigDecimal(int[] r) {
        return new BigDecimal(toString(r));
    }
    /**
     *
    *10進表示で標準出力に出力します。
    */
    public void print(int[] r) {
        System.out.println(toString(r));
    }

    /**
    *全要素を 0 で初期化します。
    */
    public static void setZero(int[] a) {
        Arrays.fill(a, 0);
    }

    // 以下はテスト用
    /**
    *自然対数の底を計算します。
    */
    public int[] e() {
        int[] a = new int[n+1];
        int[] t = new int[n+1];
        a[0] = 2;  a[1] = t[1] = RADIX / 2; // a := 2.5, t := 0.5
        int k = 3, m = 1;
        while ((m = divs(m, t, k, t)) <= n) { // t := t/k
            add(a, t, a);       // a := a + t
            if (++k == RADIX) throw new Error("桁数が多すぎます");
        }
        return a;
    }

    /**
    *円周率を Machinの公式を使って計算します。
    */
    public int[] pi() {
        int[] a = new int[n+1];
        int[] t = new int[n+1];
        int[] u = new int[n+1];
        t[0] = 16;              // t := 16
        divs(0, t, 5, t);       // t := t/5
        System.arraycopy(t, 0, a, 0, n);
        int j = 0, m = 0, k = 1;
        while (true) {
            if ((m = divs(m, t, 25, t)) > n) break; // t := t/25
            if ((k += 2) >= RADIX) throw new Error("桁数が多すぎます");
            while (j < m) u[j++] = 0;
            if (divs(m, t, k, u) > n) break; // u := t/k
            if ((k & 2) != 0) sub(a, u, a);
            else              add(a, u, a); // a := a -+ u
        }
        t[0] = 4;               // t := 4
        divs(0, t, 239, t);     // t := t/239
        sub(a, t, a);           // a := a - t
        j = m = 0;  k = 1;
        while (true) {
            if ((m = divs(m, t, 239 * 239, t)) > n) break; // t := t/(239*239)
            if ((k += 2) >= RADIX) throw new Error("桁数が多すぎます");
            while (j < m) u[j++] = 0;
            if (divs(m, t, k, u) > n) break; // u := t/k
            if ((k & 2) != 0) add(a, u, a);
            else              sub(a, u, a); // a := a +- u
        }
        return a;
    }
    /**
    *例題プログラムです。
    *自然対数の底と円周率を出力します。
    */
    public static void main(String[] args) {
        final int N = 100;
        long start= System.currentTimeMillis();
        MultiPrecision m = new MultiPrecision(N);
        int[] e = m.e(), pi = m.pi();

        System.out.println("e = \n" + m.toString(e));
        System.out.println("pi = \n" + m.toString(pi));
        long end =  System.currentTimeMillis();
        System.out.println("CPU time" + (end - start) + "(ms)");
    }
}