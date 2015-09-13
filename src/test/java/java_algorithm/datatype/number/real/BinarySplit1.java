package java_algorithm.datatype.number.real;

/**
* BinarySplit1.java -- binary splitting method 抽象クラス
* 配列を使った素直な方法。
* 再帰呼び出版は BinarySplit2.java。
* BDTools.java（BigDecimal 小道具集）を使用している。
*/

import java_algorithm.datatype.number.javamath.BDMult;

import java.math.*;  // BigDecimal クラスを使う。

/**
* binary splitting 法の抽象クラス(abstract class)です。
* 配列を使った素直な方法です。<br>
* <b>使用方法</b><br>
* {@link #bsA}などでA<sub>k</sub>, B<sub>k</sub>, C<sub>k</sub>を準備して，<br>
* １．{@link #set}<br>
* ２．{@link #putTogether}<br>
* ３．{@link #getValue}<br>
*の順に呼び出してください。各ステップの処理時間を計測したい場合があるため，分割しています。
* @see java_algorithm.datatype.number.javamath.BDTools
* @see BinarySplitE
* @see BinarySplit2
* @see BinarySplitPi2
*/
public abstract class BinarySplit1 {

/////// 本文と照らし合わせて読みやすいように L,N,A,B,C は大文字を使っている。 ///////
    private int L;  // 項数 L = 2^p であること。
    private int N;
    BigDecimal[] A, B, C;
    BigDecimal result;

    static int ceilpow2(int sz) { // sz <= k = 2^n を満たす最小の k を返す。sz <= 0 の場合は0を返す。
        if (sz < 2) return sz > 0 ? 1 : 0;
        int k = 2;

        while (k < sz) k *= 2;
        return k;
    }

    /**
    * 10 進での精度（有効桁数）を返します。
    */
    public int N() {
        return N;
    }

    /**
    * 実際の項数を返します。
    */
    public int upTo() {
        return L;
    }

    /**
    * ユーザは <code>BinarySplitE(int n，int f) { super(n, f); }</code> のようなコンストラクタを
    * 準備する必要があります。
    * @see BinarySplitE
    * @param upto 級数の項数を指定します。upto = 2<sup><i>n</i></sup> になるように調整されます。
    * @param N 有効桁数を指定します。
    */
    public BinarySplit1(int upto, int N) {
        L = ceilpow2(upto); // 本文では ceilpow2() は使っていない。
        this.N = N;
        A = new BigDecimal[L];  B = new BigDecimal[L];  C = new BigDecimal[L];
    }

    /**
    * A<sub>k</sub> を返す抽象メソッドです。<br>
    * ユーザがオーバーライドする必要があります。
    */
    public abstract BigDecimal bsA(int k);

    /**
    * B<sub>k</sub> を返す抽象メソッドです。<br>
    * ユーザがオーバーライドする必要があります。
    */
    public abstract BigDecimal bsB(int k);

    /**
    * C<sub>k</sub> を返す抽象メソッドです。<br>
    * ユーザがオーバーライドする必要があります。
    */
    public abstract BigDecimal bsC(int k);

    /**
    * <code>A[0..L-1], B[0..L-1], C[0..L-1]</code> をセットします。
    */
    public void set() {
        for (int i = 0; i < L; i++){
            A[i] = bsA(i);  B[i] = bsB(i);  C[i] = bsC(i);
        }
    }

    /**
    * 集約を行います。
    */
    public void putTogether() {
        int n = L / 2;
        while (n != 0){
            for (int k = 0; k < n; k++){
                BigDecimal p = BDMult.bdMult(A[2 * k], C[2 * k + 1], N);

                A[k] = p.add(BDMult.bdMult(B[2 * k], A[2 * k + 1], N));
                B[k] = BDMult.bdMult(B[2 * k], B[2 * k + 1], N);
                C[k] = BDMult.bdMult(C[2 * k], C[2 * k + 1], N);
            }
            n /= 2;
        }
    }

    /**
    * 級数の和を BigDecimal で返します。
    * @param inv <code>C[0]/A[0]</code>を得たい場合には <code>true</code> を，
    *<code>A[0]/C[0]</code> の場合は <code>false</code> を与えます。
    */
    public BigDecimal getValue(boolean inv) {
        if (inv) result = C[0].divide(A[0], N, BigDecimal.ROUND_HALF_EVEN); // C[0]/A[0]
        else     result = A[0].divide(C[0], N, BigDecimal.ROUND_HALF_EVEN); // A[0]/C[0]

        return result;
    }
}