package java_algorithm.datatype.collection.sort;

/**
 *  選択ソート
 */

public class SelectSort {

    /** a[] を昇順に */
    public static void sort(int a[]) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = a[i],  k = i;
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < min) {  min = a[j];  k = j;  }
            a[k] = a[i];  a[i] = min;
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