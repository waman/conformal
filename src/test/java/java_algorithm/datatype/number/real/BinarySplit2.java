package java_algorithm.datatype.number.real;

/**
* BinarySplit2.java -- binary splitting method 抽象クラス
* 再帰版。
*/

import java_algorithm.datatype.number.javamath.BDMult;

import java.math.BigDecimal;

/**
* 再帰版 binary splitting 法の抽象クラス(abstract class)です。
* @see BDMult
* @see BinarySplit1
* @see BinarySplitPi2
*/
public abstract class BinarySplit2 {

    private final int A = 0, B = 1, C = 2;  // enum{} の代用。 TODO
    private int L, N;
    BigDecimal X[], result;

    /**
    * 10 進での精度（有効桁数）を返します。
    */
    public int N(){
        return N;
    }

    /**
    * L の値を返します。本文参照。
    */
    public int upTo() {
        return L;
    }

    /**
    * ユーザは Xyz(int n，int f) { super(n, f); } のようなコンストラクタを
    * 準備する必要があります。
    * @see BinarySplitPi2
    * @param upto 級数の項数を指定します。
    * @param N 有効桁数を指定します。
    */
    public BinarySplit2(int upto, int N) {
        L = upto;
        this.N = N;
        X = new BigDecimal[3];
    }

    /**
    * A[k] を返す抽象メソッドです。<code><br></code>
    * サブクラスでオーバーライドする必要があります。
    */
    public abstract BigDecimal bsA(int k);

    /**
    * B[k] を返す抽象メソッドです。<code><br></code>
    * サブクラスでオーバーライドする必要があります。
    */
    public abstract BigDecimal bsB(int k);

    /**
    * C[k] を返す抽象メソッドです。<code><br></code>
    * サブクラスでオーバーライドする必要があります。
    */
    public abstract BigDecimal bsC(int k);

    /**
    * 集約を行います。
    */
    public void putTogether() { // 集約
        recursive(0, L, X);
    }

    private void recursive(int n0, int n1, BigDecimal[] Q) {
        if (n1 - n0 == 1) {
            Q[A] = bsA(n0); Q[B] = bsB(n0); Q[C] = bsC(n0);
            return;
        }
        BigDecimal[] left  = new BigDecimal[3];
        BigDecimal[] right = new BigDecimal[3];
        int midN = (n0 + n1) / 2;

        recursive(n0, midN, left);  // 再帰呼び出し，左側
        recursive(midN, n1, right); // 右側

        BigDecimal p = BDMult.bdMult(left[A], right[C], N);

        Q[A] = p.add(BDMult.bdMult(left[B], right[A], N));// A(2k) * C(2k+1) + B(2k) * A(2k+1)
        Q[B] = BDMult.bdMult(left[B], right[B], N);       // B(2k) * B(2k+1)
        Q[C] = BDMult.bdMult(left[C], right[C], N);       // C(2k) * C(2k+1)
    }

    /**
    * 級数の和を BigDecimal で返します。
    * @param inv C[0]/A[0]を得たい場合には true を，A[0]/C[0]の場合は false を与えます。
    */
    public BigDecimal getValue(boolean inv) {
        if (inv) result = X[C].divide(X[A], N, BigDecimal.ROUND_HALF_EVEN); // C[0]/A[0]
        else     result = X[A].divide(X[C], N, BigDecimal.ROUND_HALF_EVEN); // A[0]/C[0]
        return result;
    }
}