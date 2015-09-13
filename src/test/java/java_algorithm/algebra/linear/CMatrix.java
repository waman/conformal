package java_algorithm.algebra.linear;

import java_algorithm.datatype.number.complex.Complex;

/**
 *  複素行列クラス：
 *
 *  複素行列(Complex の２次元配列)に対する各種演算メソッドを提供します。
 */
public class CMatrix {

    /**
     *  行列の i 番目の行と j 番目の行を交換する
     *  @param  i, j    行番号(0,1,..)
     */
    public static void swapRow(Complex[][] mtx, int i, int j) {
        Complex[] temp = mtx[i];
        mtx[i] = mtx[j];
        mtx[j] = temp;
    }

    /**
     *  行列の i 番目の列と j 番目の列を交換する
     *  @param  i, j    列番号(0,1,..)
     */
    public static void swapCol(Complex[][] mtx, int i, int j) {
        for (int k = 0; k < mtx.length; k++) {
            Complex temp = mtx[k][i];
            mtx[k][i] = mtx[k][j];
            mtx[k][j] = temp;
        }
    }
    /**
     *  転置行列を返します。
     *  @return mtx<sup>T</sup>
     */
    public static Complex[][] transpose(Complex[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        Complex[][] ret = new Complex[m][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[j][i] = new Complex(mtx[i][j]);
        return ret;
    }

    /**
     *  複素共役行列を返します。
     *  @return mtx<sup>*</sup>
     */
    public static Complex[][] conj(Complex[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        Complex[][] ret = new Complex[n][m];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++)
            ret[i][j] = mtx[i][j].conj();
        return ret;
    }

    /**
     *  エルミート共役行列を返します。
     *  @return mtx<sup>+</sup>
     */
    public static Complex[][] hermiteConj(Complex[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        Complex[][] ret = new Complex[m][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++)
            ret[j][i] = mtx[i][j].conj();
        return ret;
    }

    /**
     *  値が (lhs + rhs) となる行列を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs + rhs)
     */
    public static Complex[][] add(Complex[][] lhs, Complex[][] rhs) {
        int n = lhs.length, m = lhs[0].length;
        if (n != rhs.length || m != rhs[0].length)
            throw new IndexOutOfBoundsException();

        Complex[][] ret = new Complex[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[i][j] = lhs[i][j].add(rhs[i][j]);

        return ret;
    }
    /**
     *  値が (lhs - rhs) となる行列を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs - rhs)
     */
    public static Complex[][] subtract(Complex[][] lhs, Complex[][] rhs) {
        int n = lhs.length, m = lhs[0].length;
        if (n != rhs.length || m != rhs[0].length)
            throw new IndexOutOfBoundsException();

        Complex[][] ret = new Complex[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[i][j] = lhs[i][j].subtract(rhs[i][j]);

        return ret;
    }
    /**
     *  値が (lhs * rhs) となる行列を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs * rhs)
     */
    public static Complex[][] multiply(Complex[][] lhs, Complex[][] rhs) {
        int n = lhs.length, m = rhs[0].length, l = rhs.length;
        if (l != lhs[0].length) throw new IndexOutOfBoundsException();

        Complex[][] ret = new Complex[n][m];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) {
            double re = 0.0, im = 0.0;
            for (int k = 0; k < l; k++) {
                Complex value = lhs[i][k].multiply(rhs[k][j]);
                re += value.re();
                im += value.im();
            }
            ret[i][j] = new Complex(re,im);
        }
        return ret;
    }

    /**
     *  値が (mtx * v) となるベクトルを返します。
     *  @param  mtx 行列
     *  @param  v   ベクトル
     *  @return (mtx * v)
     */
    public static Complex[] multiply(Complex[][] mtx, Complex[] v) {
        int n = mtx.length, m = mtx[0].length;
        if (m != v.length) throw new IndexOutOfBoundsException();

        Complex[] ret = new Complex[n];
        for (int i = 0; i < n; i++) {
            double re = 0.0, im = 0.0;
            for(int j = 0; j < m; j++) {
                Complex value = mtx[i][j].multiply(v[j]);
                re += value.re(); im += value.im();
            }
            ret[i] = new Complex(re,im);
        }
        return ret;
    }

    /**
     *  値が (v * mtx) となるベクトルを返します。
     *  @param  v   ベクトル
     *  @param  mtx 行列
     *  @return (v * mtx)
     */
    public static Complex[] multiply(Complex[] v, Complex[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        if (n != v.length) throw new IndexOutOfBoundsException();
        Complex[] ret = new Complex[m];
        for (int i = 0; i < m; i++) {
            double re = 0.0, im = 0.0;
            for(int j = 0; j < n; j++) {
                Complex value = v[j].multiply(mtx[j][i]);
                re += value.re(); im += value.im();
            }
            ret[i] = new Complex(re,im);
        }
        return ret;
    }

    /**
     *  行列の内容を文字列で出力します。
     *  @param  mtx 行列
     *  @return "[[mtx[0][0], mtx[0][1], ...], [mtx[1][0], ...], ...]"
     *          形式の文字列
     */
    public static String toString(Complex[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        String str = "[";
        for (int i = 0; i < n; i++) {
            str += "[";
            for (int j = 0; j < m; j++) {
                str += mtx[i][j];
                if (j < m - 1) str += ", ";
            }
            str += "]";
            if (i < n - 1) str += ", ";
        }
        str += "]";
        return str;
    }

    /**
     *  唯一のエントリポイント
     *  @param  args    --
     */
    public static void main(String[] args) {
        Complex[][]  m1 = new Complex[2][2];
        m1[0][0] = new Complex(1.0,1.0); m1[0][1] = new Complex(1.0,1.0);
        m1[1][0] = new Complex(1.0,1.0); m1[1][1] = new Complex(1.0,1.0);

        Complex[][]  m2 = m1.clone(); // 要素はコピーされない！
        Complex[][]  m3 = add(m1,m2), m4 = multiply(m2,m3), m5 = subtract(m3,m4);

        System.out.println(toString(m3) + "\n" + toString(m4) + "\n" + toString(m5));
    }
}