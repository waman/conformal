package java_algorithm.datatype.number.javamath;

/*
* BDTools.java -- BigDecimal 小道具集
* 本文には出力関係などは載せず。
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
*BigDecimal の小道具集を提供します。
* @see BDtoE_Form
*/
public class BDTools {

    /**
    *10 進での出力桁数を保持しています。
    */
    public static int decNumber;

    /**
    *10 進での出力桁数が指定できます。すなわち，出力するとき，precision + 1 桁目を四捨五入します。
    */
    public static int precision = -1;

    /**
    * {@link #getExp10}が呼ばれたときの仮数部を保持しています。
    */
    public static double mant;

    /**
    *BigDecimal 定数 0.0 です。
    */
    public static final BigDecimal ZERO = new BigDecimal(0.0);

    /**
    *BigDecimal 定数 1.0 です。
    */
    public static final BigDecimal ONE  = new BigDecimal(1.0);

    /**
    *BigDecimal 定数 10.0 です。
    */
    public static final BigDecimal TEN = new BigDecimal(10.0);
    private static long seed = System.currentTimeMillis();

    /**
    * dec 桁で正かつ 1.0 より小さい乱数を返します。
    */
    public static BigDecimal random(int dec) {
        int bits = (int)(dec * Math.log(10.0) / Math.log(2.0));
        Random     rnd  = new Random(seed++);
        BigInteger mant = new BigInteger(bits, rnd);
        BigDecimal bd   = new BigDecimal(mant, biFig10(mant));

        while (bd.compareTo(ONE) > 0) bd = bd.movePointLeft(1); // まれにしか実行されない。
        return bd;
    }

    /**
    * 上位の 0 でないビット数を返します。<br>
    * <b>例</b><code> 1100 0101 1010 0000 0000</code> では 11 を返します。
    */
    public static int nonZeroBitLength(BigDecimal x) {
        BigInteger u = x.unscaledValue();
        return u.bitLength() - u.getLowestSetBit();
    }

    /**
    * 仮数部の小数第(place + 1)位で丸めます。
    */
    public static BigDecimal roundOff(BigDecimal x, int place, int roundingMode) {
        int e = getExp10(x);

        x = x.movePointLeft(e);
        x = x.divide(ONE, place, roundingMode);  // 1 で割っている。
        return x.movePointRight(e);
    }

    /**
    * 小数第(place + 1)位で四捨五入します。
    */
    public static BigDecimal roundOff(BigDecimal x, int place) {
        return roundOff(x, place, BigDecimal.ROUND_HALF_UP);
    }

    /**
    * 小数第(place + 1)位以下を切り捨てます。
    */
    public static BigDecimal roundDown(BigDecimal x, int place) {
        return roundOff(x, place, BigDecimal.ROUND_DOWN);
    }

    /**
    * BigInteger r の 10 進表現での桁数の概算値を求めます。
    */
    public static int biFig10(BigInteger r) {
        return (int)(r.bitLength() * Math.log(2) / Math.log(10)) + 1;
    }

    /**
    * BigDecimal x の 10 進表現での有効桁数の概算値を求めます。下位の 0 も含みます。
    */
    public static int bdEffFig10(BigDecimal x) {
        return biFig10(x.unscaledValue());
    }

    private static final int DOUBLE_PREC = 15; // double の有効桁数

    /**
    * BigDecimal x を double m (1.0≦|m|＜10.0)を用いて<b>m×10<sup>e</sup></b>
    *形式に変換したときの指数部 e の値を返します。仮数部 m は {@link #mant} が保持しています。<br>
    *BigDecimal の仮数部が必要な場合はクラス {@link BDtoE_Form} を使います。
    * @return 指数部 e
    */
    public static int getExp10(BigDecimal x) {
        if (x.signum() == 0) {
            mant = 0;
            return 0;
        }
        int exp = biFig10(x.unscaledValue()) - x.scale() - 1;
        BigDecimal mx = x.movePointLeft(exp), tm = mx;  // mx ：x の仮の仮数部
    // 桁数が多いと下のdoubleValue()に時間がかかるため切り捨てる。
        tm = tm.divide(ONE, DOUBLE_PREC * 2, BigDecimal.ROUND_HALF_UP);  // 1 で割っている。
        mant = tm.doubleValue();
        // 指数部の調整
        double p = Math.abs(mant);
        if (p == 1.0 || p == 10.0) { // |x| が非常に1または10に近い値を持つ場合。
            if (x.signum() < 0) mx = mx.negate();
            while (mx.compareTo(ONE) < 0) {
                mant *= 10.0;  mx = mx.movePointRight(1);  exp--;
            }
            while (mx.compareTo(TEN) >= 0) {
                mant /= 10.0;  mx = mx.movePointLeft(1);   exp++;
            }
        } else {
            while (Math.abs(mant) < 1.0)  { mant *= 10.0; exp--; }
            while (Math.abs(mant) > 10.0) { mant /= 10.0; exp++; }
        }
        return exp;
    }

    /**
    *BigDecimal a, b の積を上位ビットと下位ビットに半分に分けて計算します。<br>
    *{@link BDMult#bdMult}を呼び出しています。
    */
    public static BigDecimal bdMult(BigDecimal a, BigDecimal b, int precision) {
        return BDMult.bdMult(a, b, precision);
    }

    /**
    * BigDecimal x を <code><b>[+|-]a.bcd...e[+|-]xxx</b></code> 形式に変換した文字列を返します。
    * 概算値を出力させたい場合などに使います。
    * x.doubleValue(); ではアンダー／オーバーフローする場合があるために装備しています。
    */
    public static String toDoubleString(BigDecimal x) {
        if (x.signum() == 0) return "0.0";
        int exp = getExp10(x);

        return Double.toString(mant) + "e" + Integer.toString(exp);
    }

    /**
    * BigDecimal x を p 桁区切りの <code><b>[+|-]a.bcd...E[+|-]xxx</b></code> 形式で出力します。
    * 2進でのビット数が短くても10進でのそれが長い場合は出力の前に<br>
    *<code>x = BDTools.roundOff(x, precision);</code><br>
    *を実行してください。
    * @param newLine 小数部を改行して出力する場合 true を与ます。
    * @return 10 進表現での出力桁数を返します。
    */
    public static int print(BigDecimal x, int p, boolean newLine) {
        System.out.println(toE_FormString(x, p, newLine));
        return decNumber;
    }

    /**
    * BigDecimal x を p 桁区切りの <code><b>[+|-]a.bcd...E[+|-]xxx</b></code> 形式で出力します。
    * 整数部を出力したあと改行します。
    * @return 10 進表現での出力桁数を返します。
    */
    public static int print(BigDecimal x, int p) {
        return print(x, p, true);
    }

    /**
    * BigDecimal x を p 桁区切りの <code><b>[+|-]a.bcd... e[+|-]xxx</b></code> 形式の文字列に変換します。
    * 末尾の 0 は除かれ，小数部を出力したあと改行します。<br>
    * ただし，p ≦ 0 にすると空白や改行を入れず，そのまま BigDecimal のコンストラクタ
    * public BigDecimal(String val) の val に使用できます。
    * 10 進表現での桁数は BDTools.decNumber に入っています。
    */
    public static String toE_FormString(BigDecimal x, int p) {
        return toE_FormString(x, p, true);
    }

    /**
    * BigDecimal x を p 桁区切りの <code><b>[+|-]a.bcd...E[+|-]xxx</b></code> 形式の文字列に変換します。
    * 末尾の 0 は除かれます。p ≦ 0 にすると空白や改行を入れず，そのまま BigDecimal のコンストラクタ
    * public BigDecimal(String val) の val に使用できます。
    * @param newLine 小数部を改行して出力する場合 true を与ます。<br>
    * ただし，p が大きい場合や x が短い数の場合は p と newLine の指定は無視されます。
    */
    public static String toE_FormString(BigDecimal x, int p, boolean newLine) {
        decNumber = 0;
        if (x.signum() == 0) return "0.0";
        if (precision > 0) x = roundOff(x, precision);

        int notNum = x.signum() > 0 ? 1 : 2;   // 負符号 '-' の有無と小数点 '.'
        BDtoE_Form bdE = new BDtoE_Form(x); // BDtoE_Form.java にある。末尾の 0 を除く処理を含んでいる。
        int    exp  = bdE.exp();    // 指数部
        double mant = bdE.mant();   // 仮数部(double)
        BigDecimal xMant = bdE.bdMant();    // 仮数部(BigDecimal)

        if (xMant.compareTo(new BigDecimal(mant)) == 0) { // bdEffFig10(x) < 16 の判定はだめ。
            // x == mant, "2.0" など改行が不要な短い数の場合。
            String s = Double.toString(mant);   // 1.0 <= mant < 10.0 であるから s には指数部 Ennn は付いていない。
            decNumber = s.length() - notNum;

            return s + "E" + Integer.toString(exp);
        }

        String s = xMant.toString();
        if (p > s.length() || p <= 0) {
            decNumber = s.length() - notNum;
        // BigDecimal のコンストラクタ BigDecimal(String) に使用可能にするため " E" とはしない。
            return s + "E" + Integer.toString(exp);
        }
        char r[] = s.toCharArray();
        final char DELMT = ' ';  // 区切り文字
        int decLen = r.length;
        StringBuilder sb = new StringBuilder(decLen); // 自動的に容量が増加
        int i = 0;

        do {    // 負符号，整数部，小数点
            sb.append(r, i, 1);
            decLen--;
        } while (r[i++] != '.');

        if (newLine) sb.append("+\n");

        int iRem = decLen % p, iMax = i + decLen - iRem;    // (iMax - i) は p の倍数

        for (; i < iMax; i += p) {
            sb.append(r, i, p).append(DELMT);
        }
        if (iRem != 0){
            sb.append(r, i, iRem);
            i += iRem;
        }
        decNumber = i - notNum;
        int last = sb.length() -1;
        if (sb.charAt(last) == DELMT) sb.deleteCharAt(last);  // 最後の文字が区切り文字なら削除する。
        // 指数部を追加，こちらは見やすいように " E" とする。
        // 下の readNumber() で読み込み可能。
        return sb.toString() + " E" + Integer.toString(exp);
    }

    /**
    * ２つのBigDecimal a, b が何桁一致するかを返します。
    */
    public static int agreeUpTo(BigDecimal a, BigDecimal b) {
        BDtoE_Form bdeA = new BDtoE_Form(a);
        BDtoE_Form bdeB = new BDtoE_Form(b);
        if (bdeA.exp() != bdeB.exp()) return 0; // if ( a.scale() != b.scale()) return 0; ではだめ。

        char[] sa = bdeA.bdMant().toString().toCharArray();
        char[] sb = bdeB.bdMant().toString().toCharArray();
        int num = 0, iMax = Math.min(sa.length, sb.length);

        for (int i = 0; i < iMax && sa[i] == sb[i]; i++)
            if (Character.isDigit(sa[i])) num++;
        return num;
    }

    private static boolean skip(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '+';
    }

    /**
    * BigDecimal 数値をファイルから読み込みます。precision には 10 進での桁数を与えます。
    * 実際に読み込んだ桁数は {@link #decNumber} に入っています。
    * precision が大きすぎる場合は decNumber &lt; precision となります。<br>
    * 数値の途中に改行，空白や'+'記号が入っていてもかまいません。
    *@param fileName 入力ファイル名を与えます。
    *@return 読み込んだ BigDecimal 数値を返します。ファイルが見つからない場合は <code>null</code> を返します。
    */
    public static BigDecimal readNumber(String fileName, int precision) throws IOException {
        BufferedReader in;
        try { in = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) { return null; }
        StringBuilder sb = new StringBuilder();
        int c, len = 0;

        do { // 整数部 "[+|-]a."
            if ((c = in.read()) == -1) break;
            sb.append((char)c);
            if (Character.isDigit((char)c)) len++;
        } while (len < precision && c != '.');

        while (c != -1 && len < precision) { // 小数部 "bcdef..."
            c = in.read();
            if (c == 'e' || c == 'E') break;
            if (Character.isDigit((char)c)) {
                sb.append((char)c); len++;
            }
        }

        if (c != -1) { // 指数部 "E[+|-]xyz" を探す。
            while (c != 'e' && c != 'E' && c != -1) c = in.read();
            while (c != -1 && !skip(c)) {
                sb.append((char)c); c = in.read();
            }
        }
        in.close();
        decNumber = len;
        return new BigDecimal(sb.toString());
    }
}