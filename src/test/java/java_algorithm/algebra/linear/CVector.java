package java_algorithm.algebra.linear;

import java_algorithm.datatype.number.complex.Complex;

/**
 *  複素ベクトルクラス：
 *
 *  複素ベクトル(Complex の配列)に対する各種演算メソッドを提供します。
 */
public class CVector {

    /**
     *  複素共役ベクトルを返します。
     *  @return v<sup>*</sup>
     */
    public static Complex[] conj(Complex[] v) {
        Complex[] ret = new Complex[v.length];
        for (int i = 0; i < v.length; i++) ret[i] = v[i].conj();
        return ret;
    }

    /**
     *  値が (lhs + rhs) となるベクトルを返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs + rhs)
     */
    public static Complex[] add(Complex[] lhs, Complex[] rhs) {
        if (lhs.length != rhs.length)
            throw new IndexOutOfBoundsException();

        Complex[] ret = new Complex[lhs.length];
        for (int i = 0; i < lhs.length; i++) ret[i] = lhs[i].add(rhs[i]);
        return ret;
    }

    /**
     *  値が (this - rhs) となるベクトルを返します。
     *  @param  rhs このベクトルに加える値
     *  @return (this - rhs)
     */
    public static Complex[] subtract(Complex[] lhs, Complex[] rhs) {
        if (lhs.length != rhs.length)
            throw new IndexOutOfBoundsException();

        Complex[] ret = new Complex[lhs.length];
        for (int i = 0; i < lhs.length; i++) ret[i] = lhs[i].subtract(rhs[i]);
        return ret;
    }

    /**
     *  値が (z * v) となるベクトルを返します。
     *  @param  z   スカラー
     *  @param  v   ベクトル
     *  @return (z * v)
     */
    public static Complex[] multiply(Complex z, Complex[] v) {
        Complex[] ret = new Complex[v.length];
        for (int i = 0; i < v.length; i++) ret[i] = z.multiply(v[i]);
        return ret;
    }

    /**
     *  lhs[ns]<sup>*</sup>*rhs[ns]+..+
     *  lhs[ne-1]<sup>*</sup>*rhs[ne-1] を返します。
     *  @param  ns  和を取る最初の要素番号
     *  @param  ne  和を取る最後の要素の次の要素番号
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return 演算の結果
     */
    public static Complex innerProduct(int ns, int ne, Complex[] lhs, Complex[] rhs) {
        if (ns < 0 || ne < 0 || ns > ne)
            throw new IndexOutOfBoundsException();

        double re = 0.0, im = 0.0;
        for (int i = ns; i < ne; i++) {
            Complex value = lhs[i].conj().multiply(rhs[i]);
            re += value.re(); im += value.im();
        }
        return new Complex(re,im);
    }

    /**
     *  ベクトルの ns 番目の要素から最後の要素までの内積を返します。
     *  @param  ns  和を取る最初の要素番号
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return 内積の値
     */
    public static Complex innerProduct(int ns, Complex[] lhs, Complex[] rhs) {
        return innerProduct(ns, lhs.length, lhs, rhs);
    }

    /**
     *  ベクトルの内積を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return 内積の値
     */
    public static Complex innerProduct(Complex[] lhs, Complex[] rhs) {
        return innerProduct(0, lhs.length, lhs, rhs);
    }
}