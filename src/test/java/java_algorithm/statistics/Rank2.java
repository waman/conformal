package java_algorithm.statistics;

/**
* Rank2.java -- 順位づけ
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
* 順位づけのプログラム 2 です。
* @see Rank1
* @see NumberFormatter
*/
public class Rank2 {

    static final int MAX = 100;  /* 満点 */

    /**
    * 一括して順位付けを行います。
    * 点数の上限を 100，下限を 0 としています。
    * @param a 点数のデータ
    * @return rank[] : a[k] の順位が rank[k] に入る。
    */
    public static int[] all(int a[]) {
        int count[] = new int[MAX + 2], n = a.length;

        for (int ai : a) count[ai]++;
        count[MAX + 1] = 1;
        for (int i = MAX; i > 0; i--) count[i] += count[i + 1];

        int rank[] = new int[n];
        for (int i = 0; i < n; i++) rank[i] = count[a[i] + 1];
        return rank;
    }

    /**
    * 乱数を使った例題プログラムです。
    *点数と順位を表示します。
    */
    public static void main(String[] args) {
        final int N   =  20;  /* 人数 */
        int[] a = new int[N];

        for (int i = 0; i < N; i++)
            a[i] = (int)(Math.random() * MAX);

        int[] rank = all(a);
        System.out.println("    点数 順位");

        NumberFormatter fmt2d = new NumberFormatter("2d");
        NumberFormatter fmt5d = new NumberFormatter("3d");
        NumberFormatter fmt4d = new NumberFormatter("5d");
        for (int i = 0; i < N; i++)
            System.out.println(
                    fmt2d.toString(i) + ": " + fmt5d.toString(a[i]) + fmt4d.toString(rank[i]));
    }
}