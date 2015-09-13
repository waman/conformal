package java_algorithm.datatype.number.integral;

/*
* GCD.java -- 最大公約数
*/

import java.io.*;

/**
* 最大公約数を求めます。
* @see GCDandLCM
*/
public class GCD {
    /**
    * 再帰版
    * @param a 自然数
    * @param b 自然数
    * @return <code>gcd(x,y)</code>
    */
    public static long gcdRec(long a, long b){
        if (b == 0) return a;
        else return gcdRec(b, a % b);
    }

    /**
    * 非再帰版
    *
    * @param x 自然数
    * @param y 自然数
    * @return <code>gcd(x,y)</code>
    */
    public static long gcd(long x, long y) {
        long a = x, b = y;
        while (b != 0) {
           long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    /**
    * n 個の自然数 a[0], ..., a[n-1] の最大公約数を求めます。
    * @return <code>gcd(a[0], ..., a[n-1])</code>
    */
    public static long ngcd(int n, long a[]){
        long d = a[0];

        for (int i = 1; i < n && d != 1; i++)
            d = gcd(a[i], d);
        return d;
    }

    /**
    * テスト用です。
    * <b>「整数を入力してください（０で終了）。」</b>と聞いてきますから複数の正の整数を入力してください。
    *<b>0または負の数値</b>を入力すると，最大公約数を表示します。<br>
    *個数が2の場合は <code>gcdRec(a,b)</code> と <code>gcd(a,b)</code> を呼び出します。2より大きい場合は
    *<code>ngcd(n,a[])</code> を呼び出します。
    */
    public static void main(String[] args) throws IOException {
        long a[] = new long[100];
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("整数を入力してください（０で終了）。");
        int i;
        for (i = 0; ; i++) {
            System.out.print("a["+ i +"]= ");
            long t;
            try {
                t = Long.parseLong(in.readLine());
            } catch(NumberFormatException e) { t = -1; }
            if (t < 1) break;
            a[i] = t;
        }
        if (i > 2) System.out.println("最大公約数 = " + ngcd(i, a));
        if (i == 2) {
            System.out.println("gcdRec("+ a[0] + "," + a[1] + ") = " + gcdRec(a[0], a[1]));
            System.out.println("gcd("+ a[0] + "," + a[1] + ") = " + gcd(a[0], a[1]));
        }
   }
}