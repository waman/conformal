package java_algorithm.algebra.polynomial;

/*
* Horner.java -- Horner (ホーナー) 法
*/

/**
* Horner (ホーナー) 法を使って多項式の値<code><br>
* <i>f</i>(<i>x</i>) = <i>a<sub>n-1</sub> x <sup>n-1</sup></i> +
*<i>a<sub>n-2</sub> x <sup>n-2</sup></i> +...+ <i>a</i><sub>0</sub><i>x</i><sup>0</sup>
*<br></code>
* を計算します。
*/

public class Horner {
    /**
    * @param a 係数<code><i>a</i><sub>0</sub>,...,<i>a<sub>n-2</sub>,a<sub>n-1</sub></i>
    *</code>の <i>n</i> 個の数値を与えます。
    *ただし，<code><i>n</i> = a.lenght, a[0] = <i>a</i><sub>0</sub></code>とします。
    *@param x <code><i>x </i></code>の数値を与えます。
    *@return 
    * <code><i>a<sub>n-1</sub> x <sup>n-1</sup></i> +
    *<i>a<sub>n-2</sub> x <sup>n-2</sup></i> +...+ <i>a</i><sub>0</sub><i>x</i><sup>0</sup>
    *</code>
    * の数値を返します。
    */
    public static double getValue(double a[], double x) {
        int n = a.length;
        double p = a[n - 1];

        for (int i = n - 2; i >= 0; i--) p = p * x + a[i];
        return p;
    }
    /**
    * テスト用プログラムです。
    */
    public static void main(String[] args) {
        double a[] = { 1, 2, 3, 4, 5 }, x = 10;

        System.out.println("f(" + x + ") = " + getValue(a, x));
    }
}