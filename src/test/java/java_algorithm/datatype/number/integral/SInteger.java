package java_algorithm.datatype.number.integral;

/*
* SInteger.java -- 多倍長整数クラス
*/

import java_algorithm.datatype.string.NumberFormatter;

import java.math.BigInteger;

/**
* 基数10,000の多倍長整数クラスです。
* FFT 乗算のテスト・プログラム用であって，必要最小限の機能しか持っていません。
*/
public class SInteger {

    static final int FIGURES  = 4;

    /**
    * 基数(= 10,000)を保持しています。
    */
    public static final short RADIX = 10000; // 基数 10^FIGURES

    /**
    * 基数の BigInteger 表現を保持しています。
    */
    public static final BigInteger BI_RADIX = BigInteger.valueOf(RADIX);

    /**
    * 各桁を保持しています。f[i] < RADIX です。
    */
    public short[] f;   // static にしてはいけない。

    /**
    * 基数10,000での桁数を保持しています。
    * <code>size = 2<sup>n</sup></code>です。
    */
    public int size;    // 同上 size = 2^n にとる

    static int ceilpow2(int sz) { // sz <= k = 2^n を満たす最小の k を返す。sz <= 0 の場合は0を返す。
        if (sz < 2) return sz > 0 ? 1 : 0;
        int k = 2;

        while (k < sz) k *= 2;
        return k;
    }

    /**
    * @param size 基数10,000での桁数を与えます。
    * <code>size = 2<sup>n</sup></code> になるように調整されます。
    */
    public SInteger(int size) {
        f = new short[this.size = ceilpow2(size)];
    }

    /**
    * SInteger → BigInteger 基数変換を提供します。
    */
    public BigInteger toBigInteger() {
        int i = size - 1;
        while (f[i] == 0 && i >=0) i--;
        if (i < 0) i = 0;
        BigInteger biVal = BigInteger.valueOf(f[i--]); // 最上位の桁

        for (; i >= 0; i--) { // Horner 法で計算
            biVal = biVal.multiply(BI_RADIX);
            biVal = biVal.add(BigInteger.valueOf(f[i]));
        }
        return biVal;
    }

    /**
    * SInteger を内部表現のまま出力します。
    */
    public void print() {
        NumberFormatter fmt04d = new NumberFormatter("04d");
        NumberFormatter fmt4d  = new NumberFormatter("4d");
        int i = size - 1;
        while (f[i] == 0) {
            i--;
            if (i < 0) { System.out.println("0");    return; }
        }
        System.out.print(fmt4d.toString(f[i--]) + " "); // 最上位の桁
        for (; i >= 0; i--) System.out.print(fmt04d.toString(f[i]) + ' ');
        System.out.println();
    }

    /**
    * SInteger オブジェクトに size 桁の乱数をセットします。
    */
    public void setRandom(int size) {
        int cs = ceilpow2(size);
        if (this.size != cs) f = new short[this.size = cs];
        for (int i = 0; i < size; i++) f[i] = (short)(Math.random() * RADIX); // f[i] < RADIX
    }
}