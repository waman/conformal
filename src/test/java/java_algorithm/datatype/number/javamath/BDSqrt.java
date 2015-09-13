package java_algorithm.datatype.number.javamath;

/*
* BDSqrt.java
* BDTools.java と BDMult.java を使っている。
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
*多倍長精度実数の平方根またはその逆数を求めます。<br>
*@see BDMult
*@see BDTools
*/

public class BDSqrt {

    public static int itr;
    public static final int ITR_MAX = 20;  // 15 * 2^20 桁まで。
    public static final int DOUBLE_PREC = 15; // double の有効桁数

    /**
    *多倍長精度実数の平方根の逆数 <code>1/sqrt(<i>x</i>)</code> を求めます。<br>
    *戻り値の10進での桁数はprecisionより長いままです（{@link BDMult}）。
    *切り捨てるには <code>BDTools.roundOff(r, precision);</code>
    * を使用します。あるいは，{@link BDTools#precision} に出力桁数を指定し，
    *BDToolsの出力関数を呼び出せば自動的に切り捨てられます。
    *@param x <code>BigDecimal <i>x</i></code> を与えます。
    *@param precision 10進での精度を与えます。
    *@return <code>1/sqrt(<i>x</i>)</code> を返します。
    */
    public static BigDecimal recSqrt(BigDecimal x, int precision) {
        final BigDecimal TWO = new BigDecimal(2.0);
        // 1 <= x < 100 に標準化する。
        int shift = BDTools.getExp10(x) / 2;
        x = x.movePointLeft(2 * shift);
        /* x が大きいと下位に0が詰められ，下の appX = x.doubleValue(); などに
          時間がかかるため切り捨てる。*/
        x = BDTools.roundOff(x, precision);
        double appX  = x.doubleValue();  // double の近似値
        BigDecimal y = new BigDecimal(1.0 / Math.sqrt(appX)), dy; // x <= 0 のときエラー
        y = BDTools.roundOff(y, DOUBLE_PREC);  // ゴミを除去
        itr = 0;
        dy = x.subtract(new BigDecimal(appX));  // 近似値との差
        // x = 2.00000....00000123456 のように dy = O(10^(-p)) が非常に小さい場合は ef = 2 * p にとる。
        int ef = 2 * Math.max(DOUBLE_PREC, Math.abs(BDTools.getExp10(dy)));  // 精度の初期値, dy = 0 の場合を考慮

        do {
            if (ef > precision) ef = precision;
            // dy = y*(1-x*y^2)
            dy = BDMult.bdMult(y, y, ef);     // dy = y*y
            dy = BDMult.bdMult(x, dy, ef);    // dy *= x
            dy = BDTools.ONE.subtract(dy);    // dy = 1 - dy
            dy = BDMult.bdMult(y, dy, ef);    // dy *= y
         // dy /= 2 ［注意］dy *= 0.5 と乗算にすると精度指定できないため dy = 0 にならない。
         // また，上の dy の計算で有効桁数を数桁(4～8桁程度)増やさないと誤差で振動して収束しない。
            dy = dy.divide(TWO, ef, BigDecimal.ROUND_HALF_EVEN);
            y = y.add(dy);   ef *= 2;  itr++;
        } while ( (dy.unscaledValue().bitLength() > 8 && itr < ITR_MAX) || ef < precision); // dy != 0 (dy.signum() != 0) ではダメ。

        if (itr == ITR_MAX) {
            System.err.println("\"recSqrt\" で収束しません。");
            System.exit(-1);
        }
        if (shift != 0) y = y.movePointLeft(shift);
        return y;
    }

    /**
    *多倍長精度実数の平方根 <code>sqrt(<i>x</i>)</code> を求めます。
    *@param x <code>BigDecimal <i>x</i></code> を与えます。
    *@param precision 精度を与えます。
    *@return <code>sqrt(<i>x</i>)</code> を返します。
    */
    public static BigDecimal bdSqrt(BigDecimal x, int precision) {
        return BDMult.bdMult(recSqrt(x, precision), x, precision);
    }

    /**
    *テスト用：sqrt(2) を入力した桁数で計算します。
    */
    static void getSqrt2(int precision) {
        BigDecimal TWO = new BigDecimal(2.0), sqrt2, d;

        BDTools.precision = precision;
        System.out.println("sqrt(2) を " + precision + " 桁計算中");
        long start = System.currentTimeMillis();
        sqrt2 = bdSqrt(TWO, precision);
        long time = System.currentTimeMillis() - start;

        System.out.println("sqrt(2) =");
        BDTools.print(sqrt2, 10);
        System.out.print("反復回数 = " + itr + " 計算時間 " + time + " (ms)");
        System.out.println("  出力桁数 = " + BDTools.decNumber);
        d = TWO.subtract(BDMult.bdMult(sqrt2, sqrt2, precision));
        System.out.println("2 - sqrt(2)^2 の概算値 = " + BDTools.toDoubleString(d));
    }

    public static void main(String[] args) throws IOException {
        System.out.print("1 / sqrt(2) と sqrt(2) を求めます．\n精度(>32) ? = ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        if (n > 32) getSqrt2(n);
    }
}