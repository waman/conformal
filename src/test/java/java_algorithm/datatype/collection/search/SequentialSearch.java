package java_algorithm.datatype.collection.search;

/**
 *  逐次探索
 */

import java.io.*;
import java.util.*;

public class SequentialSearch {

    public static final int NOT_FOUND = -1;

    public static int search1(int x, int[] a, int m, int n) {
        while (m <= n && a[m] != x) m++;
        if (m <= n) return m;
        return NOT_FOUND;
    }

    public static int search2(int x, int[] a, int m, int n) {
        if (m > n) return NOT_FOUND;
        a[n + 1] = x;  // 番人
        while (a[m] != x) m++;
        if (m <= n) return m;
        return NOT_FOUND;
    }

    public static int search3(int x, int[] a, int m, int n) {
        if (m > n) return NOT_FOUND;
        int save = a[n];  a[n] = x;  // 番人
        while (a[m] != x) m++;
        a[n] = save;
        if (m < n) return m;
        if (x == save) return n;
        return NOT_FOUND;
    }

    private static String right(int num) {
        String s = "  " + num;
        return s.substring(s.length() - 3);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("***** 逐次探索デモンストレーション *****");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        final int N = 20;
        int[] a = new int[N + 1];
        Random random = new Random();
        for (int i = 0; i < N; i++) a[i] = random.nextInt(100);

        System.out.print("  i : ");
        for (int i = 0; i < N; i++) System.out.print(right(i));
        System.out.println();

        System.out.print("a[i]: ");
        for (int i = 0; i < N; i++) System.out.print(right(a[i]));
        System.out.println();

        System.out.print("\n何を探しますか? ");
        int x = Integer.parseInt(input.readLine());

        System.out.print("search1: ");
        int i = search1(x, a, 0, N - 1);
        if (i != NOT_FOUND) System.out.println("i = " + i);
        else                System.out.println("見つかりません");

        System.out.print("search2: ");
        i = search2(x, a, 0, N - 1);
        if (i != NOT_FOUND) System.out.println("i = " + i);
        else                System.out.println("見つかりません");

        System.out.print("search3: ");
        i = search3(x, a, 0, N - 1);
        if (i != NOT_FOUND) System.out.println("i = " + i);
        else                System.out.println("見つかりません");
    }
}