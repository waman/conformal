package java_algorithm.datatype.number.integral;

/*
 *  中国(式)剰余定理(Chinese Remainder Theorem)
 */

import java.math.BigInteger;
import static java.math.BigInteger.*;

class CRT {

    // X = a[i] (mod m[i]) となる X を求める
    // m[i] > 0 が互いに素でなければ 0 が返る

    public static BigInteger solve(BigInteger a[], BigInteger m[]) {
        int k = m.length;
        // mProduct = prod_{j = 0} ^ i m[i]
        BigInteger mProduct = m[0], ret = a[0];

        for (int i = 1; i < k; i++) {
            BigInteger u, v;
            // u * mProduct = 1 (mod m[i])
            try {
                u = mProduct.modInverse(m[i]);
            } catch(ArithmeticException e) {
                System.err.println("互いに素ではありません");
                return ZERO;
            }
            u = u.multiply(mProduct);
            v = ONE.subtract(u);

            ret = ret.multiply(v).add(u.multiply(a[i]));
            mProduct = mProduct.multiply(m[i]);
            ret = ret.mod(mProduct);
        }
        return ret;
    }

    public static void main(String[] args) {
        if (args.length == 0 || (args.length & 1) == 1) {
            System.out.println("使用法: java CRT 余り1 モジュロ1 余り2 モジュロ2 ...");
            return;
        }
        int num = args.length / 2;
        BigInteger a[] = new BigInteger[num];
        BigInteger m[] = new BigInteger[num];
        System.out.println("equations");
        for (int i = 0; i < num; i++) {
            a[i] = new BigInteger(args[i*2]);
            m[i] = new BigInteger(args[i*2+1]).abs();   // positive
            System.out.println("X mod " + m[i] + " = " + a[i]);
        }
        System.out.println("X = " + solve(a, m));
    }
}