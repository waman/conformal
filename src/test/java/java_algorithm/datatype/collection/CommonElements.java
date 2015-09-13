package java_algorithm.datatype.collection;

/*
* CommonElements.java -- 共通の要素
*/

import java_algorithm.datatype.string.NumberFormatter;

import java.util.Random;

/**
* 共通の要素を表示します。
*/
public class CommonElements {

    private static final int INT_MAX = Integer.MAX_VALUE;

    /**
    * 3個の整数型配列 <code>a[], b[], c[]</code> に共通な要素を表示します。
    * @param na <code>a[]</code> に入っている要素の個数。<br>
    *ただし，<code>a[]</code> の要素は整列されていて，na &lt; a.length であるとします。
    *<code>a[na]</code>は作業用（番人）に使用します。<br>
    * <code>nb, b[], nc, c[]</code> に付いても同様です。
    */
    public static void listOfSorted( int na, int a[], int nb, int b[], int nc, int c[]) {
        int i = 0, j = 0, k = 0, t = a[0];

        a[na] = b[nb] = c[nc] = INT_MAX; /* 番人 */
        while (t < INT_MAX) {
            while (b[j] < t) j++;
            while (c[k] < t) k++;
            if (t == b[j] && t == c[k]) System.out.print(" " + t);
            do {  i++;  } while (a[i] == t);
            t = a[i];
        }
        System.out.println();
    }

    /**
    * 要素として可能な値が 0 から N までの整数の場合に flag を使う方法です。
    */
    public static void listByFlag(
            int na, int a[], int nb, int b[], int nc, int c[], int N) {
        int flag[] = new int[N + 1]; // 0 に初期化されている

        for (int i = 0; i < na; i++) flag[a[i]] |= 1;
        for (int i = 0; i < nb; i++) flag[b[i]] |= 2;
        for (int i = 0; i < nc; i++) flag[c[i]] |= 4;
        for (int i = 0; i <= N; i++)
            if (flag[i] == 7) System.out.print(" " + i);
    }

///// 以下はテスト用 /////
    static final NumberFormatter FMT3d = new NumberFormatter("3d");

    private static long seed = System.currentTimeMillis();

    private static void makeRandomArray(int n, int max, int a[]) {
        Random rand = new Random(seed++);

        for (int i = 0; i < n; i++) a[i] = rand.nextInt(max);
        for (int i = 0; i < n; i++) System.out.print(FMT3d.toString(a[i]));
        System.out.println();
    }

    private static void makeSortedArray(int n, int a[]) {
        Random rand = new Random(seed++);

        a[0] = rand.nextInt(INT_MAX) / (INT_MAX / 5);
        for (int i = 1; i < n; i++)
            a[i] = a[i - 1] + rand.nextInt(INT_MAX) / (INT_MAX / 3);
        for (int i = 0; i < n; i++) System.out.print(FMT3d.toString(a[i]));
        System.out.println();
    }

    /**
    * テスト用
    */
    public static void main(String[] args) {
        final int L = 15, M = 20, N = 25;
        int a[] = new int[L + 1];
        int b[] = new int[M + 1];
        int c[] = new int[N + 1];

        System.out.println("---- by use of sort ---- ");
        System.out.print("a:");  makeSortedArray(L, a);
        System.out.print("b:");  makeSortedArray(M, b);
        System.out.print("c:");  makeSortedArray(N, c);
        System.out.print("common elements: ");
        listOfSorted(L, a, M, b, N, c);

        final int R = 20;
        System.out.println("\n---- by use of flag ---- ");
        System.out.print("a:");  makeRandomArray(L, R, a);
        System.out.print("b:");  makeRandomArray(M, R, b);
        System.out.print("c:");  makeRandomArray(N, R, c);
        System.out.print("common elements: ");
        listByFlag(L, a, M, b, N, c, R);
    }
}