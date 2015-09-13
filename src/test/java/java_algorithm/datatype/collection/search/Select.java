package java_algorithm.datatype.collection.search;

/**
 *  選択
 */

import java.io.*;

public class Select {
    /** k+1 番目に小さい要素を返す */
    static int select(int a[], int k) {
        int first = 0,  last = a.length - 1;
        while (first < last) {
            final int x = a[k];
            int i = first,  j = last;
            for ( ; ; ) {
                while (a[i] < x) i++;
                while (x < a[j]) j--;
                if (i >= j) break;  // i==j か i==j+1 で抜ける
                int swap = a[i];  a[i] = a[j];  a[j] = swap;
                i++;  j--;
            }
            if (i <= k) first = j + 1;
            if (k <= j) last  = i - 1;
        }
        return a[k];
    }

    public static void main(String[] args) throws IOException {
        final int N = 10;
        int[] a = new int[N];

        for (int i = 0; i < N; i++) {
            a[i] = (int)(Math.random() * 100);
            System.out.print(" " + a[i]);
        }
        System.out.println();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for ( ; ; ) {
            System.out.print("小さい方から数えて何番目のものを求めますか? ");
            int k = Integer.parseInt(in.readLine());
            if (k < 1 || k > N) break;
            System.out.println(k + "番目 = " + select(a, k - 1));
        }
    }
}