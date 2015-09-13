package java_algorithm.datatype.number.real;

/*
* BinarySplitPi1.java -- binary splitting method によるπの計算
* Chudnovskys の方法による。
* BinarySplit1 使用版
*/

import java_algorithm.datatype.number.javamath.BDMult;
import java_algorithm.datatype.number.javamath.BDPower;
import java_algorithm.datatype.number.javamath.BDSqrt;
import java_algorithm.datatype.number.javamath.BDTools;

import java.math.BigDecimal;

/**
* Chudnovskys の公式で円周率を計算します。<br>
* @see BinarySplit1
*/
public class BinarySplitPi1 extends BinarySplit1 {

    static final BigDecimal A = BigDecimal.valueOf(13591409L);
    static final BigDecimal B = BigDecimal.valueOf(545140134L);
    static final long      LD = 640320L;
    static final BigDecimal C = BigDecimal.valueOf((LD * LD * LD)/24L);

    BinarySplitPi1(int L, int f) {
        super(L, f);
    }

    @Override
    public BigDecimal bsA(int k) { // A(k) = A + B * k
        return A.add(B.multiply(BigDecimal.valueOf(k)));
    }

    @Override
    public BigDecimal bsB(int k) { // B(k) = - (2 * k + 1) *(6 * k + 1) * (6 * k + 5)
        long p = - (2L * k + 1L), q = 6L * k + 1L, r = 6L * k + 5L;
        BigDecimal bk = BigDecimal.valueOf(p);
        return bk.multiply(BigDecimal.valueOf(q)).multiply(BigDecimal.valueOf(r));
    }

    @Override
    public BigDecimal bsC(int k) { // C(0) = 1, C(k) = C * k^3
        if (k == 0) return BDTools.ONE;
        BigDecimal bik = BigDecimal.valueOf(k);

        return C.multiply(BDPower.power(bik, 3, N()));
    }

    public BigDecimal getValue() {
        set();  putTogether();
        BigDecimal pi = getValue(true), // BinarySplit1 class の getValue()
                    a = BDSqrt.bdSqrt(BigDecimal.valueOf(10005L), N());
        pi = BDMult.bdMult(pi, a, N());
        pi = BDMult.bdMult(pi, BigDecimal.valueOf(426880L), N());

        return pi;
    }

    public static void main(String[] args) {
        final int L = 1024 * 4;  // L = 2^p
        int f = (int)(0.8 * L);
        BinarySplitPi1 chpi = new BinarySplitPi1(L, f);
        
        long start = System.currentTimeMillis();
        BigDecimal pi = chpi.getValue();
        long time = System.currentTimeMillis() - start;

        System.out.println("Chudnovskys pi = ");
        BDTools.precision = f;
        BDTools.print(pi, 10);
        System.out.print("出力桁数 = " + BDTools.decNumber + ", f = " + f);
        System.out.print("  計算時間 : " + time + "(ms)");
        System.out.print("  精度 : " + chpi.N());
        System.out.println("  項数 : " + chpi.upTo());

/* // MultiPrecision.pi() と比較したい場合はここを生かす。
        int fm = (int)(f * Math.log(10) / Math.log(MultiPrecision.RADIX));
        MultiPrecision m = new MultiPrecision(fm);
        start = System.currentTimeMillis();
        String ms = m.toString(m.pi());  // 基数変換を含む
        time = System.currentTimeMillis() - start;
        BigDecimal mpi = new BigDecimal(ms), d;
        d = pi.subtract(mpi);
        System.out.print("MultiPrecision.pi() の計算時間 : " + time + "(ms)  ");
        System.out.println("diffrence = " + BDTools.toDoubleString(d));
*/    }
}