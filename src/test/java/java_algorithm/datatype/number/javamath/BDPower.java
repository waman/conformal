package java_algorithm.datatype.number.javamath;

/**
* BDPower.java -- BigDecimal x の累乗 x^n
*/

import java.math.BigDecimal;

/**
* 多倍長実数の累乗を求めます。
*@see BDTools
*@see BDtoE_Form
*/
public class BDPower {

    /**
    *多倍長実数の逆数 <i>1/x</i> を求めます。
    *@param x <code>BigDecimal</code> <i>x</i> を与えます。
    *@param precision 必要とする精度を10進の桁数で与えます。
    */
    public static BigDecimal reciprocal(BigDecimal x, int precision) {
        // 1 <= |x| < 10 にする。
        int e = BDTools.getExp10(x);
        if (e != 0) x = x.movePointLeft(e);  // x <-- x * 10^(-e)

        BigDecimal r = BDTools.ONE.divide(x, precision, BigDecimal.ROUND_HALF_UP); // r <-- 1/x
        if (e != 0) r = r.movePointLeft(e);  // r <-- r * 10^(-e)
        return r;
    }

    /**
    *多倍長実数の累乗 <i>x<sup>n</sup></i> を求めます。
    *@param x <code>BigDecimal</code> <i>x</i> を与えます。
    *@param n <i>n</i> を与えます。
    *@param precision 必要とする精度を10進の桁数で与えます。
    */
    public static BigDecimal power(BigDecimal x, int n, int precision) {
        if (n == 0) {
            if (x.signum() != 0) return BDTools.ONE;
            System.err.println("0^0 in BDPower.power().");  System.exit(-1);
        }
        if (x.signum() == 0) return BDTools.ZERO;

        BigDecimal r = BDTools.ONE;

        if (n < 0) {
            x = reciprocal(x, precision);
            n = -n;
        }
        while (n != 0) {
            if ((n & 1) != 0) r = BDMult.bdMult(r, x, precision);     // r *= x
            x = BDMult.bdMult(x, x, precision);    // x *= x:
            n >>= 1;
        }
//        return BDTools.roundOff(r, precision - 1); // 整数部が1桁あるための -1。時間がかかる。
        return r;
    }

    /**
    *テスト用プログラムです。
    */
    public static void reciprocalTest(int precision) {
        System.out.println("----- 逆数 reciprocal のテスト-----");
        System.out.println("有効桁数 = " + precision);
        System.out.println("値はすべてdouble 形式の概算値");
        BigDecimal z, y, x, d;
        long start = System.currentTimeMillis();

        for (int n = -3; n <= 3; n++) {
            z = BDTools.random(precision);
            x = z.movePointRight(n * precision);
            y = reciprocal(x, precision);
            System.out.println("  x = " + BDTools.toDoubleString(x));  //BDTools.print(x, 10, false);
            System.out.println("1/x = " + BDTools.toDoubleString(y));  //BDTools.print(y, 10, false);
            d = x.multiply(y);
            d = d.subtract(BDTools.ONE);
            System.out.println("x * (1/x) - 1 = " + BDTools.toDoubleString(d));
            int de = BDTools.getExp10(d) + precision;
            if (Math.abs(de) > 5) { // e = 1, 0, -1 のはず。
                System.out.println("error : de = " + de);   System.exit(-1);
            }
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("計算時間 : " + time + "(ms)");
    }

    public static void main(String[] args) {
        int precision = 50;
        BigDecimal x = BDTools.random(precision), y;

        reciprocalTest(precision * 10);

        System.out.println("\n------ 有効桁数 = " + precision + "------");
        x = x.negate();
        System.out.print("\nx = ");   BDTools.print(x, 10, false);
        System.out.println("BDTools.nonZeroBitLength(x) = " + BDTools.nonZeroBitLength(x));
        for (int n = -3; n <= 3; n++) {
            y = power(x, n, precision);
            System.out.print("y = x^(" + n + ")= ");
            BDTools.print(y, 10, false);
            System.out.println("BDTools.nonZeroBitLength(y) = " + BDTools.nonZeroBitLength(y));
        }
        int n = 16, exp10 = -100;
        x = BDSqrt.bdSqrt(new BigDecimal(2.0), precision);
        x = x.movePointRight(exp10);
        System.out.println("\n----- 出力桁数を " + precision + " に設定 ------");
        BDTools.precision = precision;
        System.out.println("\nx = sqrt(2) * 10^(" + exp10 + ") =");
        BDTools.print(x, 10, false);
        System.out.println("BDTools.nonZeroBitLength(x) = " + BDTools.nonZeroBitLength(x));
        y  = power(x, n, precision);    // x^n
        System.out.println("x^(" + n + ") =");
        BDTools.print(y, 10, false);
        BigDecimal iy = power(x, -n, precision);    // x^(-n)
        System.out.println("x^(" + (-n) + ") =");
        BDTools.print(iy, 10, false);
        y = BDMult.bdMult(y, iy, precision);
        System.out.println("y = x^(" + n + ") * x^(" + (-n) + ") =");
        BDTools.print(y, 10, false);
        System.out.println("BDTools.nonZeroBitLength(y) = " + BDTools.nonZeroBitLength(y) + " <-- 有効ビット数はxと同程度");
        System.out.println("\ny.unscaledValue()の16進表示\n" + y.unscaledValue().toString(16));
        y = BDTools.ONE.subtract(y);
        System.out.println("1 - y =");
        BDTools.print(y, 10, false);
    }
}