package java_algorithm.datatype.number.javamath;

/*
* BDCbrt.java -- 多倍長実数の立方根
* BDTools.java を使っている。
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
*多倍長精度実数の立方根またはその逆数を求めます。
*/
public class BDCbrt {

    public static int itr;  // 反復回数を保持している。
    public static final int ITR_MAX = 20;  // 15 * 2^20 桁まで。
    public static final int DOUBLE_PREC =15; // double の有効桁数

    /**
    *立方根の逆数<code>1/<i>x</i><sup>1/3</sup></code>を求めます。
    *@param x <code>BigDecimal <i>x</i></code> を与えます。
    *@param precision 精度を与えます。
    *@return <code>1/<i>x</i><sup>1/3</sup></code>を返します。
    *そのまま10進で出力すると大きな桁数になります。その場合は {@link BDTools#roundOff}
    *を使ってください。有効桁数を2進で見積もっているためです。
    * @see BDTools
    */
    public static BigDecimal recCubeRoot(BigDecimal x, int precision) {
        final BigDecimal THREE = new BigDecimal(3.0);
        // 0.01 < |x| < 10.0 に標準化する。
        int shift = BDTools.getExp10(x) / 3;
        x = x.movePointLeft(3 * shift);
        /* x が大きいと下位に0が詰められ，下の appX = x.doubleValue(); などに
          時間がかかるため切り捨てる。
          x.movePointLeft() と順序を逆にすると appX = NaN エラーになる。
        */
        x = BDTools.roundOff(x, precision);
        double appX = x.doubleValue();
        if (appX < 0) x = x.negate();   // x = -x

        BigDecimal y = new BigDecimal(Math.pow(Math.abs(appX), -1/3.0)); // x = 0 ならばエラー
        y = BDTools.roundOff(y, DOUBLE_PREC);  // ゴミを除去
        BigDecimal dy;
        itr = 0;
        dy = x.subtract(new BigDecimal(Math.abs(appX)));  // 近似値との差
        // x = 2.00000....00000123456 のように dy = O(10^(-p)) が非常に小さい場合は ef = 2 * p にとる。
        int ef = 2 * Math.max(DOUBLE_PREC, Math.abs(BDTools.getExp10(dy)));  // 精度の初期値, dy = 0 の場合を考慮

        do {
            if (ef > precision) ef = precision;
            dy = BDMult.bdMult(y, y, ef);     // dy = y*y
            dy = BDMult.bdMult(y, dy, ef);    // dy = y^3
            dy = BDMult.bdMult(x, dy, ef);    // dy = x * y^3
            dy = BDTools.ONE.subtract(dy);    // dy = 1 - x * y^3
            dy = BDMult.bdMult(y, dy, ef);    // dy = y*(1 - x * y^3)
            dy = dy.divide(THREE, ef, BigDecimal.ROUND_HALF_EVEN); // dy /= 3;
            y = y.add(dy);   ef *= 2;   itr++;
        } while ( (dy.unscaledValue().bitLength() > 8 && itr < ITR_MAX) || ef < precision); // dy != 0 (dy.signum() != 0) ではダメ。

        if (itr == ITR_MAX) {
            System.err.println("\"recCubeRoot\" で収束しません。");
            System.exit(-1);
        }
        if (appX < 0)   y = y.negate();
        if (shift != 0) y = y.movePointLeft(shift);
        return y;
    }

    /**
    *<i>x</i> の立方根<i>x</i><sup>1/3</sup> を求めます。
    *@param x <code>BigDecimal <i>x</i></code> を与えます。
    *@param precision 精度を与えます。
    *@return <i>x</i><sup>1/3</sup> を返します。
    */
    // x^(1/3) = x * /(x*x)^(1/3) を求める
    public static BigDecimal bdCubeRoot(BigDecimal x, int precision) {
        BigDecimal y = BDMult.bdMult(x, x, precision);   // y = x * x

        y = recCubeRoot(y, precision);
        return BDMult.bdMult(x, y, precision);
    }

    /**
    *テスト用：cbrt(2) = 2^(1/3) を入力した桁数で計算します。
    */
    static void getCbrt2(int precision) {
        BigDecimal TWO = new BigDecimal(2.0), cbrt2, d;

        BDTools.precision = precision;
        System.out.println("cbrt(2) を " + precision + " 桁計算中");
        long start = System.currentTimeMillis();
        cbrt2 = bdCubeRoot(TWO, precision);
        long time = System.currentTimeMillis() - start;

        System.out.println("2^(1/3) =");
        BDTools.print(cbrt2, 10);
        System.out.print("反復回数 = " + itr + " 計算時間 " + time + " (ms)");
        System.out.println("  出力桁数 = " + BDTools.decNumber);

        d = TWO.subtract(BDPower.power(cbrt2, 3, precision));
        System.out.println("2 - {2^(1/3)}^3 の概算値 = " + BDTools.toDoubleString(d));
    }

    public static void main(String[] args) throws IOException {
        System.out.print("1 / {2^(1/3)} と 2^(1/3) を求めます．\n精度(>32) ? = ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        if (n > 32) getCbrt2(n);
    }
}