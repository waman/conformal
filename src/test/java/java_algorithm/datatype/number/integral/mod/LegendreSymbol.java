package java_algorithm.datatype.number.integral.mod;

/*
 *  Legendre 記号
 */
import java.math.BigInteger;

class LegendreSymbol {

    static final BigInteger ZERO = BigInteger.ZERO;
    static final BigInteger ONE = BigInteger.ONE;

    static int calc(BigInteger a, BigInteger p) throws Exception {
        BigInteger d0 = a, d1 = p;
        int z = 1;
        for (;;) {
            // (-x/y) = (-1/y)(x/y) と第一補充法則
            if (d1.compareTo(ZERO) == 0) throw new Exception("value error");
            d0 = d0.mod(d1);
            if (d0.compareTo(ZERO) == 0) return 0;
            if (d0.add(d0).compareTo(d1) > 0) {
                d0 = d1.subtract(d0);
                if ((d1.intValue() & 3) == 3) z = -z;
            }
            // (2^{2n}x/y) = (x/y)
            while ((d0.intValue() & 3) == 0) {
                d0 = d0.shiftRight(2);
            }
            if (!d0.testBit(0)) {
                d0 = d0.shiftRight(1);
                // 第二補充法則
                int tmp = d1.intValue() & 7;
                if (tmp == 3 || tmp == 5) z = -z;
            }
            if (d0.compareTo(ONE) == 0) return z;
            // 相互法則
            if ((d0.intValue() & 3) == 3 && (d1.intValue() & 3) == 3) {
                z = -z;
            }
            BigInteger tmp = d1;
            d1 = d0;
            d0 = tmp;
        }
    }

    static int calc(long a, long p) throws Exception {
        return calc(BigInteger.valueOf(a), BigInteger.valueOf(p));
    }

    static public void main(String[] argv) throws Exception {
        System.out.println("ret="+ calc(7001, 8009));
    }
}