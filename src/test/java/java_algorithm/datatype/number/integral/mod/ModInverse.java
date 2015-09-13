package java_algorithm.datatype.number.integral.mod;

/*
    m を法として与えられた a の逆元 x を求める
    つまり ax = 1 (mod m) となる x を求める
 */

import java.math.BigInteger;

class ModInverse {

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;

    public static BigInteger getValue(BigInteger a, BigInteger m) {
        BigInteger d = m, x = ZERO, s = ONE;
        while (a.compareTo(ZERO) != 0) {
            BigInteger t;
            BigInteger tmp[] = d.divideAndRemainder(a);
            d = a;
            a = tmp[1];
            t = x.subtract(tmp[0].multiply(s));
            x = s;
            s = t;
        }
        // d = gcd(a, m)
        if (d.compareTo(ONE) == 0) {
            if (x.compareTo(ZERO) < 0) x = x.add(m);
            return x;
        } else {
            throw new ArithmeticException();
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("使用法: java ModInverse a m");
            System.out.println("output: mod m での a の逆元");
            return;
        }
        BigInteger a = new BigInteger(args[0]);
        BigInteger m = new BigInteger(args[1]);
        try {
            System.out.println("native method " + a.modInverse(m));
        } catch(ArithmeticException e) {
            System.err.println("not found");
        }
        try {
            System.out.println("素朴な実装    " + getValue(a, m));
        } catch(ArithmeticException e) {
            System.err.println("not found");
        }
    }
}