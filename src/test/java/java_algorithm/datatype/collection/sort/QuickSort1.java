package java_algorithm.datatype.collection.sort;

/**
 *  クイックソート（再帰呼出し版）
 */

public class QuickSort1 {

    /** a[] を昇順に */
    public static void sort(int a[]) {
        sort(a, 0, a.length - 1);
    }

    /** a[first..last] を昇順に */
    private static void sort(int a[], final int first, final int last) {
        final int x = a[(first + last) / 2];
        int i = first,  j = last;
        for ( ; ; ) {
            while (a[i] < x) i++;
            while (x < a[j]) j--;
            if (i >= j) break;
            int swap = a[i];
            a[i] = a[j];
            a[j] = swap;
            i++;
            j--;
        }
        if (first < i - 1) sort(a, first, i - 1);
        if (j + 1 < last)  sort(a, j + 1, last);
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