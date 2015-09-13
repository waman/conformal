package java_algorithm.datatype.collection.sort;

/**
 *  Shellソート
 */

public class ShellSort {

    /** a[] を昇順に */
    public static void sort(int a[]) {
        int h = 13;
        while (h < a.length) h = 3 * h + 1;
        h /= 9;
        while (h > 0) {
            for (int i = h; i < a.length; i++) {
                int j,  x = a[i];
                for (j = i - h; j >= 0 && a[j] > x; j -= h)
                    a[j + h] = a[j];
                a[j + h] = x;
            }
            h /= 3;
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