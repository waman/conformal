package java_algorithm.datatype.number.integral.prime;

/**
 *  素数のLucasテスト BigInteger 版
 */

import java.math.BigInteger;

public class Lucas1 {

    static final BigInteger ONE  = BigInteger.valueOf(1);
    static final BigInteger TWO  = BigInteger.valueOf(2);
    static final BigInteger FOUR = BigInteger.valueOf(4);

    /** 2^p-1 が素数なら true を返す (pは3以上の素数) */
    public static boolean isMersennePrime(int p) {
        BigInteger m = TWO.pow(p).subtract(ONE);
        BigInteger x = FOUR;
        for (int i = 2; i < p; i++) {
            System.err.print((p - i) + " \r");  // 本文からは削除({}も)
            x = x.pow(2).subtract(TWO).mod(m);
        }
        return (x.signum() == 0);  // signum() は符号に応じて +1,0,-1 を返す
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("使用法: java Lucas1 p");
            return;
        }
        final int p = Integer.parseInt(args[0]);
        if (p < 3) {
            System.out.println("３以上の素数を指定して下さい");
            return;
        }
        System.out.println("2^" + p + "-1 は ...");
        if (isMersennePrime(p)) System.out.println("素数です");
        else                    System.out.println("合成数です");
    }
}