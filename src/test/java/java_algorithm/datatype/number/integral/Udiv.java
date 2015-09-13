package java_algorithm.datatype.number.integral;

/*
    Program to determine algorithm, multiplier, and shift factor to be
    used to accomplish unsigned division by a constant divisor.
    original code is "AMD Athlon Processor x86 Code Optimization Guide,"
    No. 22007, Rev. K, Date Feb. 2002, pp. 144--147.
    http://www.amd.com/us-en/Processors/TechnicalResources/
*/

class Udiv {

     // x ( > 0) を超えない2のべきの最大指数
    static int log2(int x) {
        int shift = 0;
        while ((1 << shift) <= x) shift++;
        return shift - 1;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("java Udiv [unsigned number]");
            return;
        }
        int dividend = Long.valueOf(args[0]).intValue();
        if (dividend <= 0) {
            System.err.println("正の数を指定してください");
            return;
        }
        System.out.println("// return x / " + dividend + " (assume x >= 0)");
        System.out.println("static int udiv" + dividend + "(int x) {");

        // dividend = odd × 2^exponent と分解する．
        int exponent = 0, odd = dividend;
        while ((odd & 1) == 0) {
            odd >>= 1; exponent++;
        }
//      System.out.println(dividend + " = " + odd + " * (1<<" + exponent + ")");
        if (odd == 1) { // trivial case
            System.out.println("    return x >>> " + exponent + ";\n}");
            System.exit(1);
        }
/*
    Granlund, T., Montgomery, P. L. Division by Invariant Integers using
    Multiplication SIGPLAN Notices, Vol. 29, June 1994, p. 61のアルゴリズム0の
    m，len を求める．
*/
        long mLow, mHigh;
        int len = log2(odd) + 1;
        {
            long roundUp = 1L << (32 + len);
            long k = roundUp / (0xFFFFFFFFL - (0xFFFFFFFFL % odd));
            mLow = roundUp / odd;
            mHigh = (roundUp + k) / odd;
        }

        while (((mLow >> 1) < (mHigh >> 1)) && (len > 0)) {
            mLow >>= 1; mHigh >>= 1; len--;
        }

        long m; int a;
        if ((mHigh >> 32) == 0) {
            m = mHigh; a = 0;
        } else {
/*
    Magenheimer, D. J., et al Integer Multiplication and Division on the HP
    Precision Architecture, IEEE Transactions on Computers, Vol. 37, No. 8,
    August 1988, p. 980のアルゴリズム1の m，len を求める．
*/
            len = log2(odd);
            long roundDown = 1L << (32 + len);
            mLow = roundDown / odd;
            int r = (int)(roundDown % odd);
            m = (r <= (odd >> 1)) ? mLow : mLow + 1;
            a = 1;
        }
        // m の2のべきの部分を取り除く．
        while ((m & 1) == 0) {
            m >>= 1; len--;
        }
        // 2のべきの補正を行う．
        len += exponent;
        System.out.print("    long tmp = (long)x * " + m + "L");
        System.out.println((a != 0) ? " + " + m + "L;" : ";");
        System.out.println("    return (int)(tmp >>> " + (len + 32) + ");\n}");
    }
}