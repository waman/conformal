package java_algorithm.datatype.number.javamath;

/*
* BDtoE_Form.java -- BigDecimal 数値を E 形式に変換
* BigDecimal x を
* double "mant * 10^exp"
* Bigdecimal "bdMant * 10^exp"
* 形式に変換する。
* ただし，1.0<|mant|,|dbMant|<10.0
*/

import java.math.BigDecimal;

/**
*BigDecimal 数値を E 形式に変換します。
*/
public class BDtoE_Form {

    private int        exp;     // 指数部
    private double     mant;    // 仮数部(double)
    private BigDecimal bdMant;  // 仮数部(BigDecimal)

    /**
    * 指数部を返します。
    */
    public int exp()  {
        return exp;
    }

    /**
    * 仮数部を double 値で返します。
    */
    public double mant() {
        return mant;
    }

    /**
    * 仮数部を BigDecimal 値で返します。下位に 0 がある場合は切り捨てられます。
    */
    public BigDecimal bdMant() {
        return bdMant;
    }

    /**
    *BigDecimal <i>x</i> を E 形式 <i>m</i> ×10<sup>e</sup>
    * (1.0 ≦ |<i>m</i>| &lt; 10.0) に変換します。
    *下位の 0 は切り捨てられます。
    *@param x BigDecimal 数値を与えます。
    */
    public BDtoE_Form(BigDecimal x) {
        if (x.signum() == 0) {
            exp = 0;
            mant = 0.0;
            bdMant = BDTools.ZERO;
            return;
        }
        exp = BDTools.getExp10(x);
        mant = BDTools.mant;
        bdMant = x.movePointLeft(exp);
//      if (exp <= 0) return; // exp <=0 でも多くの0が付いていることがある。テスト・プログラム参照。
    /*
    * 下位の 0 を切り捨てる。
    * |x| が大きいときには 10 進での末尾の 0 の個数を得るのに結構時間がかかる。
    * 0 の個数を得る簡便な方法がない。
    * BigInteger の getLowestSetBit() は 2^100 などには使えない。
    */
    /* char[] s = bdMant.toString().toCharArray(); を使っても速度に変化なし。
       多分，基数変換に時間がかかっている。*/
        String s = bdMant.toString();
        int zeros = 0;  // 末尾の 0 の個数

        for (int i = s.length() - 1; s.charAt(i) == '0' ; i--) zeros++;
        if (zeros == 0) return;

        int decLen = s.length() - zeros - 2;  // s.length() > 3, [+|-]x.y0
        if (mant < 0) decLen--; // '-' 負符号
        // 小数第(decLen + 1)位以下を切り捨てる。
        bdMant = bdMant.divide(BDTools.ONE, decLen, BigDecimal.ROUND_DOWN); // 1 で割っている。
    }

    /**
    *テスト用プログラムです。
    */
    public static void main(String[] args) {
        int precision = 128, fac = 20000;

// |z| が大きいときには 10 進での末尾の 0 の個数を得るのに結構時間がかかる。
        BigDecimal z = BDTools.random(precision);
        System.out.println("z = " + z.doubleValue() + " ×10^" + fac);
        z = z.movePointRight(fac);

        long start = System.currentTimeMillis();
        BDtoE_Form bdZ = new BDtoE_Form(z);
        long time = System.currentTimeMillis() - start;
        System.out.println(" BDtoE_Form(z); 処理時間 : " + time + "(ms)");

        start = System.currentTimeMillis();
        time = System.currentTimeMillis() - start;
        System.out.println(" z.toString(); 処理時間 : " + time + "(ms)");
        System.out.println("mant = " + bdZ.mant());
        System.out.println("exp = " + bdZ.exp());
        System.out.println("bdMant =\n" + bdZ.bdMant() + "\n");

        for (int i = -5; i <= 5; i++) {
            BigDecimal x = BDTools.random(precision);
            x = x.movePointRight(precision * i / 2);
            if ((i & 1) != 0) x = x.negate();
            BDtoE_Form bdX = new BDtoE_Form(x);
            System.out.println("x =\n" + x);
            System.out.println("mant = " + bdX.mant());
            System.out.println("exp = " + bdX.exp());
            System.out.println("bdMant =\n" + bdX.bdMant() + "\n");
        }

        System.out.println("---- 短い数の場合 ----");
        BigDecimal y = new BigDecimal("0.12345");
        BDtoE_Form bdY = new BDtoE_Form(y);
        System.out.println("y = " + y);
        System.out.println("mant = " + bdY.mant());
        System.out.println("exp = " + bdY.exp());
        System.out.println("bdMant = " + bdY.bdMant());

        int k = 100;
        System.out.println("(y * 10^" + k + ")*10^(" + (-k) + ") とすると多くの0が付いたまま");
        y = bdY.bdMant().movePointRight(100);
        System.out.println("y = bdMant * 10^" + k + " = " + y);
        y = y.movePointRight(-k);
        System.out.println("y * 10^(" + (-k) + ") = " + y);
        bdY = new BDtoE_Form(y);
        System.out.println("bdMant = " + bdY.bdMant());
    }
}