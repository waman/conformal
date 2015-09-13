package java_algorithm.datatype.collection.sort;

/**
 *  櫛ソート
 */

public class CombSort {

    static final double SHRINK_FACTOR = 1.3;

    /** a[] を昇順に */
    public static void sort(int a[]) {
        int gap = a.length;
        boolean swapped;
        do {
            gap = (int)(gap / SHRINK_FACTOR);
            if (gap == 0) gap = 1;  // バブルソートと同じになる
            if (gap == 9 || gap == 10) gap = 11;  // Combsort11 を実現
            swapped = false;
            for (int i = 0, j = gap; j < a.length; i++, j++)
                if (a[i] > a[j]) {
                    int swap = a[i];
                    a[i] = a[j];
                    a[j] = swap;
                    swapped = true;
                }
        } while (gap > 1 || swapped);
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