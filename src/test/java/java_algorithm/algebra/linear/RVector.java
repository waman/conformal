package java_algorithm.algebra.linear;

import java_algorithm.datatype.string.NumberFormatter;

/**
 *  ベクトルクラス：
 *
 *  ベクトル(double の配列)に対する各種演算メソッドを提供します。
 */
public class RVector {

    /**
     *  値が (lhs + rhs) となるベクトルを返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs + rhs)
     */
    public static double[] add(double[] lhs, double[] rhs) {
        if (lhs.length != rhs.length)
            throw new IndexOutOfBoundsException();
        double[] ret = new double[lhs.length];
        for (int i = 0; i < lhs.length; i++) ret[i] = lhs[i] + rhs[i];
        return ret;
    }

    /**
     *  値が (lhs - rhs) となるベクトルを返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs - rhs)
     */
    public static double[] subtract(double[] lhs, double[] rhs) {
        if (lhs.length != rhs.length)
            throw new IndexOutOfBoundsException();

        double[] ret = new double[lhs.length];
        for (int i = 0; i < lhs.length; i++) ret[i] = lhs[i] - rhs[i];
        return ret;
    }

    /**
     *  値が (a * v) となるベクトルを返します。
     *  @param  a   スカラー
     *  @param  v   ベクトル
     *  @return (a * v)
     */
    public static double[] multiply(double a, double[] v) {
        double[] ret = new double[v.length];
        for (int i = 0; i < v.length; i++) ret[i] = a * v[i];
        return ret;
    }

    /**
     *  ベクトルの ns 番目の要素から ne - 1 番目の要素までの
     *  内積を返します。
     *  @param  ns  和を取る最初の要素番号
     *  @param  ne  和を取る最後の要素の次の要素番号
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return 内積の値
     */
    public static double innerProduct(int ns, int ne, double[] lhs, double[] rhs) {
        if (ns < 0 || ne < 0 || ns > ne)
            throw new IndexOutOfBoundsException("error in RVector\n");

        double  s = 0.0;
        int n5 = (ne - ns) % 5;
        for (int i = ns; i < ns + n5; i++) s += lhs[i] * rhs[i];
        for (int i = ns + n5; i < ne; i += 5)
            s += lhs[i] * rhs[i] + lhs[i+1] * rhs[i+1]
               + lhs[i+2] * rhs[i+2] + lhs[i+3] * rhs[i+3]
               + lhs[i+4] * rhs[i+4];
        return s;
    }

    /**
     *  ベクトルの ns 番目の要素から最後の要素までの内積を返します。
     *  @param  ns  和を取る最初の要素番号
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return 内積の値
     */
    public static double innerProduct(int ns, double[] lhs, double[] rhs) {
        return innerProduct(ns, lhs.length, lhs, rhs);
    }

    /**
     *  ベクトルの内積を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return 内積の値
     */
    public static double innerProduct(double[] lhs, double[] rhs) {
        return innerProduct(0, lhs.length, lhs, rhs);
    }

/***************** 以下は印刷メソッド ***************************************/
    /**
     *  ベクトルの ns 番目の要素から ne - 1 番目の要素を
     *  印刷します。
     *  @see NumberFormatter
     *  @param  ns  印刷を始める最初の要素番号
     *  @param  ne  印刷する最後の要素の次の要素番号
     *  @param  perLine 1行に印刷する要素数
     *  @param  format  NumberFormatter に与える印刷書式
     */
    public static void print(int ns, int ne, double[] v,
                                    int perLine, String format) {
        if (ns < 0 || ne < 0 || ns > ne) throw new IndexOutOfBoundsException();
        NumberFormatter fmt = new NumberFormatter(format);
        int k = 0;

        for (int j = ns; j < ne; j++) {
            System.out.print(fmt.toString(v[j]));
            if (++k >= perLine) {  k = 0;  System.out.println();  }
        }
        if (k != 0) System.out.println();
    }

    /**
     *  ベクトルの ns 番目の要素から最後の要素までを印刷します。
     *  @see NumberFormatter
     *  @param  ns      印刷を始める最初の要素番号
     *  @param  perLine 1行に印刷する要素数
     *  @param  format  NumberFormatter に与える印刷書式
     */
    public static void print(int ns, double[] v, int perLine, String format) {
        print(ns, v.length, v, perLine, format);
    }

    /**
     *  ベクトルの全要素を印刷します。
     *  @see NumberFormatter
     *  @param  perLine 1行に印刷する要素数
     *  @param  format  NumberFormatter に与える印刷書式
     */
    public static void print(double[] v, int perLine, String format) {
        print(0, v.length, v, perLine, format);
    }
}