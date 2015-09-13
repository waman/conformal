package java_algorithm.datatype.number.javamath;

/*
* BDMult.java -- 多倍長実数の積
* BDTools.java と BITools.java を使っている。
*/

import java.math.BigDecimal;
import java.math.BigInteger;

public class BDMult {

    /**
    *BigDecimal a, b の積を上位ビットと下位ビットに分けて計算します。
    *下位同士の乗算が不要のため理論的には3/4の計算量で済みます。<br>
    * 10進での桁数は長いままです。切り捨てるには <code>{@link BDTools#roundOff}</code>
    * を使用します。あるいは，{@link BDTools#precision} に出力桁数を指定し，BDToolsの出力関数を呼び出せば
    *自動的に切り捨てられます。<br>
    *2 進で計算しているため，たとえば，1.0*10<sup>1000</sup> * 1.0*10<sup>-500</sup> は
    *正確に 1.0*10<sup>500</sup> にはなりません。
    *@see BDTools
    *@see BITools
    */
    public static BigDecimal bdMult(final BigDecimal x, final BigDecimal y, int precision) {
        if (x.signum() == 0 || y.signum() == 0) return BDTools.ZERO;
        // 有効桁数のビット数。
        int bp = (int)(precision * Math.log(10) / Math.log(2)) + 1;
        BigInteger m = x.unscaledValue(), n = y.unscaledValue(), r;
        int u = bp / 2;
        int scale = x.scale() + y.scale();
        int fm = m.bitLength(), fn = n.bitLength();

        if (fm <= u && fn <= u) { // 短い数同士
            r = BITools.biMult(m, n);
            return new BigDecimal(r, scale);
        }
        if (fm < fn) return bdMult(y, x, precision); // 再帰, a のビット数>=bのビット数にする
        if (m.signum() < 0) m = m.negate();
        if (n.signum() < 0) n = n.negate();
        int shift = 0;
        // 下位の不要なビットを切り捨てる。aまたはbが求めたい精度以上の桁数を持つ場合に必要。
        if (fm > bp) {
            m  = m.shiftRight(fm - bp);  shift += fm - bp;
            fm = m.bitLength();
        }
        if (fn > bp) {
            n  = n.shiftRight(fn - bp);  shift += fn - bp;
            fn = n.bitLength();
        }
        // ここでは，fm >= fn, fm > u
        // mとnを，上位uビット，下位を残りのビットに分ける。
        if (fn <= u) { // 長い数と短い数の積, fm > u の判定は要らない。
            r = BITools.biMult(m, n);
        } else { // 長い数同士の積
            BigInteger pm = BigInteger.ONE.shiftLeft(fm - u); // pm = 1 << (fm - u);
            pm = pm.subtract(BigInteger.ONE);                 // pm -= 1; pm = 0xfff...f
            BigInteger mL = m.and(pm), mH = m.subtract(mL);
            if (x.equals(y)) { // 2乗の場合
                r = BITools.biMult(mH, mH);
                BigInteger lh = BITools.biMult(mL, mH);
                r = r.add(lh.shiftLeft(1)); // r <-- mH^2 + 2 * mL * mH
            } else { // a != b
            // m = mH + mL, n = nH + nL の形にする。
                BigInteger pn = BigInteger.ONE.shiftLeft(fn - u); // pn = 1 << (fn - u);
                pn = pn.subtract(BigInteger.ONE);                 // pn -= 1; pn = 0xfff...f
                BigInteger nL = n.and(pn), nH = n.subtract(nL);
            // 下位の多くのビットが0であってもBITools.biMult()が処理する。
                r = BITools.biMult(mH, nH);
                r = r.add(BITools.biMult(mL, nH));
                r = r.add(BITools.biMult(mH, nL));
            }
        }
        // 不要な下位ビットを0にする。
        int sr = r.bitLength() - bp;
        if (sr > 0) { r = r.shiftRight(sr);  r = r.shiftLeft(sr); } // 切り捨て
        if (shift != 0) r = r.shiftLeft(shift);  // 必ず必要。上とまとめるのは不可。
        if (x.signum() * y.signum() < 0) r = r.negate();
        return new BigDecimal(r, scale); // 10進での桁数は長いが2進ではbpビット程度。
    }
}