package java_algorithm.datatype.collection.sort;

/**
 *  挿入ソート
 */

public class InsertSort {

    /** a[] を昇順に */
    public static void sort(int a[]) {
        for (int i = 1; i < a.length; i++) {
            int j,  x = a[i];
            for (j = i - 1; j >= 0 && a[j] > x; j--)
                a[j + 1] = a[j];
            a[j + 1] = x;
        }
    }

    /** a[0..a.length-2] を昇順に */
    public static void sort2(int a[]) {
        a[a.length - 1] = Integer.MAX_VALUE;  // 番人
        for (int i = a.length - 2; i >= 0; i--) {
            int j,  x = a[i];
            for (j = i + 1; a[j] < x; j++)
                a[j - 1] = a[j];
            a[j - 1] = x;
        }
    }

    public static void main(String[] args) {
        final int N = 20;
        int[] a = new int[N];  // 番人を使うなら new int[N + 1]

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