package java_algorithm.datatype.string;

import java.text.DecimalFormat;

/**
 *  ANSI C printf 関数と互換の数値フォーマットを行う
 *
 *  @version $Revision: 1.10 $, $Date: 2003/04/03 12:55:46 $
 */
public class NumberFormatter {
    static final byte ALIGN_LEFT       = 0x01;  // '-'
    static final byte HEADER_SIGNATURE = 0x02;  // '+'
    static final byte HEADER_SPACE     = 0x04;  // ' '
    static final byte PADDING_ZERO     = 0x08;  // '0'
    static final byte EX_STYLE         = 0x10;  // '#'

    /**
     *  書式指定のパースを行うサブクラス
     */
    static class Format {
        char type;              //  変換文字
        byte flags;             //  フラグ
        int  minlength;         //  最小のフィールド幅
        int  precision = -1;    //  精度(= -1 は無指定)
        Format(final String fmt) {
            int fmtlen = fmt.length();
            if (fmtlen == 0) return;
            int i = 0;
            byte flags = 0;
            _flags:
            for ( ; i < fmtlen; i++) {
                switch (fmt.charAt(i)) {
                case '-':
                    flags = (byte)(ALIGN_LEFT |
                                   (flags & ~PADDING_ZERO));
                    break;
                case '0':
                    if ((flags & ALIGN_LEFT) == 0)
                        flags |= PADDING_ZERO;
                    break;
                case '+':
                    flags = (byte)(HEADER_SIGNATURE |
                                   (flags & ~HEADER_SPACE));
                    break;
                case ' ':
                    if ((flags & HEADER_SIGNATURE) == 0)
                        flags |= HEADER_SPACE;
                    break;
                case '#':
                    flags |= EX_STYLE;
                    break;
                default:
                    break _flags;   //  break loop
                }
            }
            this.flags = flags;
            if (i == fmtlen) return;
            int j = i;
            for ( ; j < fmtlen; j++)
                if (!Character.isDigit(fmt.charAt(j))) break;
            if (j > i)
                this.minlength = Integer.parseInt(fmt.substring(i,j));
            if (fmt.charAt(j) == '.') {
                this.precision = 0; //  ピリオドだけの場合 0 指定と見なす
                for (i = ++j ; j < fmtlen; j++)
                    if (!Character.isDigit(fmt.charAt(j))) break;
                if (j > i)
                    this.precision
                        = Integer.parseInt(fmt.substring(i,j));
            }
            this.type = fmt.charAt(j);
        }
        boolean isG() { return type == 'g' || type == 'G'; }
    }
    /**
     *  整数型をラップするサブクラス
     */
    static class IntValue {
        byte type;
        long val;
        IntValue(int x) { val = x; type = 4; }
        IntValue(long x) { val = x; type = 8; }
        String toOctalString() {
            switch (type) {
            case 4: return Integer.toOctalString((int)val);
            case 8: return Long.toOctalString(val);
            }
            return "";
        }
        String toHexString() {
            switch (type) {
            case 4: return Integer.toHexString((int)val);
            case 8: return Long.toHexString(val);
            }
            return "";
        }
        public String toString() {
            switch (type) {
            case 4: return Integer.toString((int)val);
            case 8: return Long.toString(val);
            }
            return "";
        }
    }

    //  フォーマット指定オブジェクト
    private Format fmt;

    /**
     *  フォーマット指定文字列を与えてオブジェクトを生成
     *  @param  fmt フォーマット指定子<br>
     *              (printf 関数のフォーマット指定を参照("%" は含まない))
     */
    public NumberFormatter(String fmt) { this.fmt = new Format(fmt); }
    /**
     *  現在のフォーマット指定に従って int を変換
     *  @param  x   変換対象の数値
     *  @return フォーマットされた数値文字列
     */
    public String toString(int x)
    { return toString(fmt,new IntValue(x)); }
    /**
     *  現在のフォーマット指定に従って long を変換
     *  @param  x   変換対象の数値
     *  @return フォーマットされた数値文字列
     */
    public String toString(long x)
    { return toString(fmt,new IntValue(x)); }
    /**
     *  現在のフォーマット指定に従って double を変換
     *  @param  x   変換対象の数値
     *  @return フォーマットされた数値文字列
     */
    public String toString(double x) { return toString(fmt,x); }
    /**
     *  フォーマット指定文字列に従って int を変換
     *  @param  fmt フォーマット指定文字列
     *  @param  x   変換対象の数値
     *  @return フォーマットされた数値文字列
     */
    public static String toString(String fmt, int x) {
        return toString(new Format(fmt),new IntValue(x));
    }
    /**
     *  フォーマット指定文字列に従って long を変換
     *  @param  fmt フォーマット指定文字列
     *  @param  x   変換対象の数値
     *  @return フォーマットされた数値文字列
     */
    public static String toString(String fmt, long x) {
        return toString(new Format(fmt),new IntValue(x));
    }
    /**
     *  フォーマット指定文字列に従って double を変換
     *  @param  fmt フォーマット指定文字列
     *  @param  x   変換対象の数値
     *  @return フォーマットされた数値文字列
     */
    public static String toString(String fmt, double x) {
        return toString(new Format(fmt),x);
    }
    //  整数変換の実体
    static String toString(final Format fmt, IntValue x) {
        String header = "", number = null;
        switch (fmt.type) {
        case 'o':
            if ((fmt.flags & EX_STYLE) != 0) header = "0";
            number = paddingIntZero(fmt.precision,x.toOctalString());
            break;
        case 'x':
        case 'X':
            if ((fmt.flags & EX_STYLE) != 0) header = "0" + fmt.type;
            number = paddingIntZero(fmt.precision,x.toHexString());
            if (fmt.type == 'X') number = number.toUpperCase();
            break;
        default:
            if (x.val < 0) { header = "-"; x.val = - x.val; }
            else if ((fmt.flags & HEADER_SIGNATURE) != 0) header = "+";
            else if ((fmt.flags & HEADER_SPACE) != 0) header = " ";
            number = paddingIntZero(fmt.precision,x.toString());
            break;
        }
        return align(fmt, header, number);
    }
    //  浮動小数点数変換の実体
    static String toString(final Format fmt, double x) {
        if (Double.isNaN(x))
            return ((fmt.flags & (HEADER_SPACE|HEADER_SIGNATURE)) != 0)
                    ? " NaN" : "NaN";
        String header = "";
        if (x < 0.0 || x == 0.0 && 1/x < 0.0) { header = "-"; x = -x; }
        else if ((fmt.flags & HEADER_SIGNATURE) != 0) header = "+";
        else if ((fmt.flags & HEADER_SPACE) != 0) header = " ";
        if (Double.isInfinite(x)) return header + "Infinity";
        final String number = (fmt.type == 'f') ?
                              toFPString(fmt,x) : toExpString(fmt,x);
        return align(fmt, header, number);
    }
    //  精度指定に従って整数(文字列)の左側に 0 を詰める
    private static String paddingIntZero(final int precision,
                                         final String num) {
        if (num.length() >= precision) return num;
        StringBuffer temp = new StringBuffer();
        for (int i = num.length(); i < precision; i++) temp.append('0');
        temp.append(num);
        return temp.toString();
    }
    //  符号文字と数値文字列をアライメント指定に従って連結する
    private static String align(final Format fmt, final String header,
                                final String number) {
        StringBuffer    buf = new StringBuffer();
        if (header.length() + number.length() >= fmt.minlength)
            buf.append(header).append(number);
        else if ((fmt.flags & ALIGN_LEFT) != 0) {
            buf.append(header).append(number);
            for (int len = buf.length(); len < fmt.minlength; len++)
                buf.append(' ');
        } else if ((fmt.flags & PADDING_ZERO) != 0) {
            buf.append(header);
            for (int len = header.length() + number.length();
                 len < fmt.minlength; len++) buf.append('0');
            buf.append(number);
        } else {
            for (int len = header.length() + number.length();
                 len < fmt.minlength; len++) buf.append(' ');
            buf.append(header).append(number);
        }
        return buf.toString();
    }

    //  浮動小数点数を文字列に変換するための static オブジェクト
    private static DecimalFormat dfmt = new DecimalFormat();
    static {    //  dfmt の初期化
        dfmt.setGroupingUsed(false);
        dfmt.setDecimalSeparatorAlwaysShown(true);
    }

    private static String toFPString(final Format fmt, double x) {
        int precision = (fmt.precision < 0) ? 6 : fmt.precision;
        if (fmt.isG()) {
            int i = 0;
            if (x != 0.0) {
                double y = 1;
                for (i = 0; i < 5; i++, y *= 10)
                    if ((long)(x * y) != 0) break;
            }
            precision += i - 1;
            if (precision < 0) precision = 0;
        }
        dfmt.setMinimumFractionDigits(precision);
        dfmt.setMaximumFractionDigits(precision);
        //  ２回目以降の format() の呼出しの際に、
        //  精度 0 で 1.0 以下の値を変換すると
        //  "1" を返す場合がある(JDK1.2.2)。
        StringBuffer buf = new StringBuffer(
                (precision == 0 && x < 1.0) ? "0.0" : dfmt.format(x)
            );
        final int spos = buf.toString().indexOf('.'); // 小数点の位置
        if (spos == buf.length() - 1) buf.append('0');
        boolean hasfrac = false;
        for (int i = spos + 1; i < buf.length(); i++)
            if (buf.charAt(i) != '0') { hasfrac = true; break; }
        if (!hasfrac) {
            if (fmt.isG()) {
                if ((fmt.flags & EX_STYLE) == 0) buf.setLength(spos);
                //  GNU C 互換にしたい時は直後の1行をコメントアウトする。
                else if (precision == 0) buf.setLength(spos + 2);
                else buf.setLength(spos + precision + 1);
            } else if (precision == 0)
                buf.setLength(spos
                          + ((fmt.flags & EX_STYLE) != 0 ? 1 : 0));
        }
        return buf.toString();
    }

    private static final Format expfmt = new Format("+03.2d");
    private static final double log10_2 = Math.log(2) / Math.log(10);

    private static String toExpString(final Format fmt, double x) {
        final int precision = (fmt.precision < 0) ? 6 : fmt.precision;
        final long bits = Double.doubleToLongBits(x);
        int e = (int)((bits >> 52) & 0x7ffL);
        final long m = (e == 0) ?
                       (bits & 0xfffffffffffffL) << 1 :
                       (bits & 0xfffffffffffffL) | 0x10000000000000L;
        if (m == 0) e = 1075;
        final double exp = (e - 1075) * log10_2;
        e = (int)Math.ceil(exp); // 指数部
        if (fmt.isG() && (m == 0 || (e >= -4 && e < precision)))
            return toFPString(fmt, x);
        final double val = m * Math.pow(10,exp - e); // 仮数部
        String sval = null;
        if (m == 0) sval = toFPString(fmt,0E0);
        else {
            e = 0;
            double d = 1.0;
            if (x < 1.0) {
                while (x * d < 1.0) {
                    d *= 10.0;
                    e--;
                }
                x = x * d;
            } else {
                while (x / d > 1.0) {
                    d *= 10.0;
                    e++;
                }
                x = x / d;
            }
            sval = toFPString(fmt, x);
        }
        StringBuffer buf = new StringBuffer(sval);
        buf.append((fmt.type == 'e' || fmt.type == 'g') ? 'e' : 'E')
           .append(NumberFormatter.toString(expfmt, new IntValue(e)));
        return buf.toString();
    }
}