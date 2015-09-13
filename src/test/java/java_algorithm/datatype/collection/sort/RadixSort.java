package java_algorithm.datatype.collection.sort;

/**
 *  ラディックス・ソート（基数ソート）
 */

public class RadixSort {

    /** a[] を昇順に */
    public static void sort(String a[]) {
        final int max = '9';
        final int min = '0';
        int[] count = new int[max - min + 1];
        String[] work = new String[a.length];

        for (int k = a[0].length() - 1; k >= 0; k--) {
            for (int i = 0; i <= max - min; i++) count[i] = 0;
            for (String ai : a)count[ai.charAt(k) - min]++;
            for (int i = 1; i <= max - min; i++)
                count[i] += count[i - 1];
            for (int i = a.length - 1; i >= 0; i--) {
                int j = --count[a[i].charAt(k) - min];
                work[j] = a[i];
            }
            System.arraycopy(work, 0, a, 0, a.length);
        }
    }

    public static void main(String[] args) {
        final int N = 20;
        String[] a = new String[N];

        System.out.print("Before:");
        for (int i = 0; i < N; i++) {
            a[i] = Integer.toString(1000 + (int)(Math.random() * 100));
            System.out.print(" " + a[i]);
        }
        System.out.println();
        sort(a);
        System.out.print("After: ");
        for (int i = 0; i < N; i++) System.out.print(" " + a[i]);
        System.out.println();
    }
}