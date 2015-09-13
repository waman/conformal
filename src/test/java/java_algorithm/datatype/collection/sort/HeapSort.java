package java_algorithm.datatype.collection.sort;

/**
 *  ヒープソート
 */

public class HeapSort {

    /** a[1..a.length-1] を昇順に */
    public static void sort(int a[]) {
        int n = a.length - 1;
        for (int i = n / 2; i >= 1; i--) down(a, n, i);
        while (n > 1) {
            int swap = a[n];  a[n] = a[1];  a[1] = swap;
            down(a, --n, 1);
        }
    }

    /** a[i..n]を「親が子より大きく」なるまで子と入れ換える */
    private static void down(int a[], final int n, int i) {
        int j,  x = a[i];
        while ((j = i * 2) <= n) {
            if (j + 1 <= n && a[j] < a[j + 1]) j++;  // j = (i の子で大きい方)
            if (a[j] <= x) break;
            a[i] = a[j];  i = j;
        }
        a[i] = x;
    }

    public static void main(String[] args) {
        final int N = 20;
        int[] a = new int[N];

        System.out.print("Before:");
        for (int i = 1; i < N; i++) {
            a[i] = (int)(Math.random() * 100);
            System.out.print(" " + a[i]);
        }
        System.out.println();
        sort(a);
        System.out.print("After: ");
        for (int i = 1; i < N; i++) System.out.print(" " + a[i]);
        System.out.println();
    }
}