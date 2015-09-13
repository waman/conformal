package java_algorithm.datatype.number.integral.series;

/**
* Bernoulli.java -- Bernoulli (ベルヌーイ) 数
* BigInteger クラスを使う。
*/
import java_algorithm.datatype.number.integral.GCD;

import java.math.*;
/**
*Bernoulli (ベルヌーイ) 数を3つの方法で計算します。
*@see GCD
*/
public class Bernoulli {

    /**
    *double を使って，Bernoulli (ベルヌーイ) 数を配列<code> b[] </code>に求めます。<br>
    * <code>b[0] = 0, b[1] = |B(2)|, b[2] = |B(4)|, ... b[n-1] = |B(2*(n-1))|</code><br>
    *ここで，n = b.length です。<br>
    *例： B(40) まで求めるには n = 40/2 + 1 = 21 にします。
    */
    public static void numbers(double[] b) {
        int m = 2 * (b.length  - 1);
        double q = 1, t[] = new double[m + 1];

        t[1] = 1;
        for (int n = 2; n <= m; n++) {
            for (int i = 1; i < n; i++) t[i - 1] = i * t[i];
            t[n - 1] = 0;
            for (int i = n; i >= 2; i--) t[i] += t[i - 2];
            if (n % 2 == 0) {
                q *= 4;  b[n/2] = (n * t[0]) / (q * (q - 1));
            }
        }
    }

    /**
    *long を使って，Bernoulli (ベルヌーイ) 数を既約分数で表示します。
    *正確なのは B(22) までです。
    */
    public static void numbers(int m) {
        long q = 1;
        long t[] = new long[m + 1];

        t[1] = 1;
        for (int n = 2; n <= m; n++) {
            for (int i = 1; i < n; i++) t[i - 1] = i * t[i];
            t[n - 1] = 0;
            for (int i = n; i >= 2; i--) t[i] += t[i - 2];
            if (n % 2 == 0) {
                q *= 4;
                long b1 = n * t[0], b2 = q * (q - 1);
                long d = GCD.gcd(b1, b2);
                b1 /= d;
                b2 /= d;
                System.out.println("|B(" + n + ")| = " + b1 + "/" + b2);
            }
        }
    }

    /**
    *BigInteger を使って，Bernoulli (ベルヌーイ) 数を既約分数で表示します。
    */
    public static void biNumbers(int m) {
        BigInteger q = BigInteger.ONE;  // q = 1
        BigInteger t[] = new BigInteger[m + 1];

        for (int i = 0; i <= m; i++) t[i] = BigInteger.ZERO; // 0 で初期化

        t[1] = BigInteger.ONE;  // t[1] = 1
        for (int n = 2; n <= m; n++) {
            for (int i = 1; i < n; i++)
                t[i - 1] = t[i].multiply(BigInteger.valueOf(i)); // t[i - 1] = i * t[i]
            t[n - 1] = BigInteger.ZERO; // t[n - 1] = 0
            for (int i = n; i >= 2; i--) t[i] = t[i].add(t[i - 2]);// t[i] += t[i - 2]
            if (n % 2 == 0) {
                BigInteger b1, b2, d, q1;
                q  = q.shiftLeft(2);  // q *= 4
                b1 = t[0].multiply(BigInteger.valueOf(n)); // b1 = n * t[0];
                q1 = q.subtract(BigInteger.ONE); // q1 = q - 1
                b2 = q.multiply(q1); // b2 = q * (q - 1)
                // b1/b2 を既約分数にして表示
                d = b1.gcd(b2);  b1 = b1.divide(d);  b2 = b2.divide(d);
                System.out.println("|B(" + n + ")| = " + b1 + "/" + b2);
            }
        }
    }

    public static void main(String[] args) {
        final int N = 40;

        System.out.println("long 版 B(m)(m >= 24)は overflow する。");
        numbers(N);

        System.out.println("\ndouble[] 版");
        int bn = N / 2 + 1;
        double B[] = new double[bn];
        numbers(B);
        for (int i = 0; i < bn; i++) System.out.println("|B(" + (2 * i) + ")| = " + B[i]);

        System.out.println("\nBigInteger 版");
        biNumbers(N);
    }
}