package java_algorithm.traditional;

/*
* Change.java -- 小銭の払い方
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
*小銭の払い方を求めます。
*@see NumberFormatter
*/
public class Change {

    /**
    *n 円を k 円以下の小銭で支払う払い方が何通りあるかを求めます。<br>
    *再帰版です。
    */
    public static int howManyWays(int n, int k) {  // 再帰版
        if (n < 0) return 0;
        int s = 1 + n / 5 + howManyWays(n - 10, 10);
        if (k >=  50) s += howManyWays(n -  50,  50);
        if (k >= 100) s += howManyWays(n - 100, 100);
        return s;
    }

    /**
    *非再帰版です。
    */
    public static int howManyWays1(int n) { // 非再帰版
        int s = 0;

        for (int i = n / 100; i >= 0; i--) {    // 100円玉
            int t = n - 100 * i;
            for (int j = t / 50; j >= 0; j--) { //  50円玉
                int u = t - 50 * j;
                s += (1 + u / 5 - u / 10) * (1 + u / 10);
            }
        }
        return s;
    }

    /**
    *0 ～ 500 円を小銭で支払う払い方が何通りあるかを表示します。
    */
    public static void main(String[] args) {
        NumberFormatter fmt6d  = new NumberFormatter("6d"),
                        fmt10d = new NumberFormatter("10d");

        System.out.println("お金の払い方");
        System.out.println("  金額    再帰版  非再帰版");
        for (int i = 0; i <= 500; i += 5)
            System.out.println(fmt6d.toString(i) +
                fmt10d.toString(howManyWays(i, i)) + fmt10d.toString(howManyWays1(i)) );
    }
}