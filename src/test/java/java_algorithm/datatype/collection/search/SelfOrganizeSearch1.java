package java_algorithm.datatype.collection.search;

/**
 *  自己組織化探索(置換法)
 */

import java.io.*;
import java.util.*;

public class SelfOrganizeSearch1 {

    public static final int NOT_FOUND = -1;

    public static int search(int x, int[] a, int imin, int imax) {
        if (imin > imax) return NOT_FOUND;
        a[imax + 1] = x;  // 番人
        int i = imin;  while (a[i] != x) i++;
        if (i > imax) return NOT_FOUND;
        if (i != imin) {
            a[i] = a[i - 1];  i--;  a[i] = x;  // 一つ前と交換
        }
        return i;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("***** 自己組織化探索デモンストレーション *****");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        final int N = 8;
        int[] a = new int[N + 1];
        Random random = new Random();
        for (int i = 0; i < N; i++) a[i] = random.nextInt(100);

        for ( ; ; ) {
            for (int i = 0; i < N; i++) System.out.print(" " + a[i]);
            System.out.print("\nどれを探しますか? ");
            String s = input.readLine();  if (s == null) break;
            int x = Integer.parseInt(s);
            int i = search(x, a, 0, N - 1);
            if (i != NOT_FOUND) System.out.println("i = " + i);
            else System.out.println("見つかりません.");
        }
    }
}