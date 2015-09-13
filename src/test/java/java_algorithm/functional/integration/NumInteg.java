package java_algorithm.functional.integration;

/*
* NumInteg.java -- 数値積分
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
*数値積分（台形則，中点則，Simpson則）のプログラムです。
*@see NumInteg
*/

public class NumInteg {

    static final int N_MAX = 32;

    /**
    *被積分関数 f(x)を与えます。
    */
    public double f(double x) { // 被積分関数 $f(x)$
        return  1 / x;
    }

    /**
    *分点数を変えて(1, 2, 4,...32)３つの方法（台形則，中点則，Simpson則）
    *の結果を表示します。
    *@param a 下限
    *@param b 上限
    *@return 分点数が 32 のときの Simpson 則の計算結果
    */
    public double evaluate(double a, double b) {
        NumberFormatter fmtd = new NumberFormatter("2d"),
                        fmtf = new NumberFormatter("10.6f");
        double simpson = 0;
        double h = b - a, trapezoid = h * (f(a) + f(b)) / 2;

        System.out.println(" n       台形      中点    Simpson");
        for (int n = 1; n <= N_MAX; n *= 2) {
            double midpoint = 0;
            for (int i = 1; i <= n; i++)
                midpoint += f(a + h * (i - 0.5));
            midpoint *= h;  simpson = (trapezoid + 2 * midpoint) / 3;
            System.out.println(fmtd.toString(n) + "  "
                + fmtf.toString(trapezoid) + fmtf.toString(midpoint)
                + fmtf.toString(simpson));
            h /= 2;  trapezoid = (trapezoid + midpoint) / 2;
        }
        return simpson;
    }

    /**
    *被積分関数 f(x) = 1 / x, 下限 a = 1, 上限 b = e を与えたときのプログラムです。
    *真値は log e = 1.0 です。
    */
    public static void main(String[] args) {
        NumInteg ni = new NumInteg();
        double r = ni.evaluate(1, Math.E);
        System.out.println("\n真値 = " + Math.log(Math.E) + "  誤差 = " + (1.0 - r));
    }
}