package java_algorithm.datatype.collection.sort;

/**
 *  バブルソート
 */

public class BubbleSort {

    /** a[] を昇順に */
    public static void sort(int a[]) {
        int k = a.length - 1;
        while (k >= 0) {
            int j = -1;
            for (int i = 1; i <= k; i++)
                if (a[i - 1] > a[i]) {
                    j = i - 1;
                    int swap = a[j];
                    a[j] = a[i];
                    a[i] = swap;
                }
            k = j;
        }
    }

    public static void main(String[] args) {
        final int N = 20;
        int[] a = new int[N];

        System.out.print("Before:");
        for (int i = 0; i < N; i++) {
            a[i] = (int)(Math.random() * 100);
            System.out.print(" " + a[i]);
        }
        System.out.println();
        sort(a);
        System.out.print("After: ");
        for (int i = 0; i < N; i++) System.out.print(" " + a[i]);
        System.out.println();
    }
}