package java_algorithm.datatype.number.javamath;

/**
* BITools.java -- BigInteger 小道具集
*/

import java.math.BigInteger;
import java.util.Random;
/**
*BigInteger の小道具集を提供します。
*/
public class BITools {

    /** 10 進での出力桁数を保持しています。 */
    public static int decNumber;   // 出力桁数
    private static long seed = System.currentTimeMillis();

    /**
    * 10進でほぼ dec 桁の乱数を返します。
    */
    public static BigInteger random(int dec) {
        int bits = (int)(dec * Math.log(10.0) / Math.log(2.0));
        Random rnd  = new Random(seed++);
        return new BigInteger(bits, rnd);
    }

    /**
    * a = A×2<sup>m</sup>, b = B × 2<sup>n</sup> の積を
    * C = (AB)×2<sup>m + n</sup> として計算します。<br>
    * BigInteger の積 a.multiply(b) は下位ビットが 0 であっても，全ビット
    * を計算するようで，時間がかかるためです。
    * @return 積ab
    */
    public static BigInteger biMult(BigInteger a, BigInteger b) {
        if (a.signum() == 0 || b.signum() == 0) return BigInteger.ZERO;
        int m = a.getLowestSetBit(), n = b.getLowestSetBit();
        if (m == 0 && n == 0) return a.multiply(b);
        BigInteger am = a.shiftRight(m), bn = b.shiftRight(n);

        return am.multiply(bn).shiftLeft(m + n);
    }

    /**
    * BigInteger r の 10 進表現での桁数の概算値を求めます。
    */
    public static int biFig10(BigInteger r) {
        return (int)(r.bitLength() * Math.log(2) / Math.log(10)) + 1;
    }

    /**
    * 0 ～ (n - 1) ビットを 0 にします。n ≦ 0 の場合は，そのまま a を返します。
    */
    public static BigInteger setBitsZeroLower(BigInteger a, int n) {
        if (n <= 0) return a;
        return a.shiftRight(n).shiftLeft(n);
    }

    /**
    * 上位の 0 でないビット数を返します。<br>
    * <b>例</b><code> 1100 0101 1010 0000 0000</code> では 11 を返します。
    */
    public static int nonZeroBitLength(BigInteger a) {
        return a.bitLength() - a.getLowestSetBit();
    }

    /**
    * BigInteger a を p 桁区切りの <code><b>[+|-]abcd ...xxx</b></code> 形式の文字列に変換します。
    * 10 進表現での桁数は {@link #decNumber} に入っています。
    *@param delimiter 区切り文字を与えます。
    */
    public static String toDelimiterString(BigInteger a, int p, char delimiter) {
        if (a.signum() == 0) return "0";

        char[] r = a.toString().toCharArray();
        int len  = r.length, buffLen = len + len / p + 1;
        decNumber = a.signum() > 0 ? len : len - 1;
        StringBuilder sb = new StringBuilder(buffLen);
        int top = len % p, i;

        if (top > 0) {
            sb.append(r, 0, top);
            if (r[top -1] != '-') sb.append(delimiter);   // "- abc..." とはしない。
        }
        for (i = top; i < len - p; i += p) {
            sb.append(r, i, p).append(delimiter);
        }
        sb.append(r, i, p);
        return sb.toString();
    }

    /**
    * BigInteger a を p 桁区切りの <code><b>[+|-]abcd ...xxx</b></code> 形式の文字列に変換します。
    * 10 進表現での桁数は {@link #decNumber} に入っています。
    * 区切り文字は空白' 'です。
    */
    public static String toDelimiterString(BigInteger a, int p) {
        return toDelimiterString(a, p, ' ');
    }

    /**
    * BigInteger a を p 桁区切りの <code><b>[+|-]abcd ...xxx</b></code> 形式で出力します。
    * 区切り文字は空白' 'です。出力の最後に改行します。
    *@return 10 進表現での出力桁数。
    */
    public static int print(BigInteger a, int p) {
        System.out.println(toDelimiterString(a, p));
        return decNumber;
    }

    /**
    * BigInteger a を p 桁区切りの <code><b>[+|-]abcd ...xxx</b></code> 形式で出力します。
    *出力の最後に改行します。
    *@param delimiter 区切り文字を与えます。
    *@return 10 進表現での出力桁数。
    */
    public static int print(BigInteger a, int p, char delimiter) {
        System.out.println(toDelimiterString(a, p, delimiter));
        return decNumber;
    }

    /**
    *テスト用プログラムです。
    */
    public static void main(String[] args) {
        int f;
        do { f = (int)(60 * Math.random()); } while (f < 20);
        BigInteger a = random(f);

        System.out.println("a = " + a);
        print(a, 3, ',');
        a = a.negate();
        System.out.println("a = " + a);
        print(a, 3, ',');
    }
}