package java_algorithm.statistics;

/**
* Rank1.java -- 順位づけ
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
*順位づけのプログラム 1 です。
*@see Rank2
*@see NumberFormatter
*/
public class Rank1 {
    /**
    * a[i] の順位 (学力テスト)を求めます。
    * @param a 点数のデータ
    * @param i 順位を求めたい人の番号
    * @return 順位
    */
    public static int ofTest(int i, int a[]) { /* a[i] の順位 (学力テスト) */
        int r = 1, x = a[i];

        for(int ai : a){
            if(ai > x)r++;
        }
        return r;
    }
    /**
    * a[i] の順位 (統計学)を求めます。
    * @param a 点数のデータ
    * @param i 順位を求めたい人の番号
    * @return 順位(必ずしも整数にならない)
    */
    public static float ofStat(int i, int a[]) {  /* a[i] の順位 (統計学 statistics) */
        int n_eq = 1, n_lt = 0, x = a[i];

        for(int ai : a){
            if(ai < x)
                n_lt++;
            else if(ai == x)
                n_eq++;
        }
        return n_lt + 0.5F * n_eq;
    }

    /**
    *乱数を使った例題プログラムです。
    */
    public static void main(String[] args) {
        final int N   =  20;  /* 人数 */
        final int MAX = 100;  /* 満点 */
        int[] a = new int[N];
        NumberFormatter fmt2d = new NumberFormatter("2d");
        NumberFormatter fmt5d = new NumberFormatter("5d");
        NumberFormatter fmt8d = new NumberFormatter("8d");
        NumberFormatter fmtF  = new NumberFormatter("10.1f");

        for (int i = 0; i < N; i++)
            a[i] = (int)(Math.random() * MAX);
        System.out.println("      点数 学力テスト 統計学");
        for (int i = 0; i < N; i++)
            System.out.println(fmt2d.toString(i) + ": " + fmt5d.toString(a[i]) +
                fmt8d.toString(ofTest(i, a)) + fmtF.toString(ofStat(i, a)));
    }
}