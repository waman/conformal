package java_algorithm.datatype.collection.sort;

/**
 *  逆写像ソート
 */

public class MapSort2 {

    /** a[] を昇順に整列して b[] に書き込む (配列使用) */
    public static void sort(int a[], int b[], int min, int max) {
        int[] next = new int[a.length];
        int[] index = new int[max - min + 1];
        for (int x = 0; x <= max - min; x++) index[x] = -1;
        for (int i = a.length - 1; i >= 0; i--) {
            int x = a[i] - min;  next[i] = index[x];  index[x] = i;
        }
        int j = 0;
        for (int x = 0; x <= max - min; x++)
            for (int i = index[x]; i >= 0; i = next[i])
                b[j++] = a[i];
    }

    public static void main(String[] args) {
        final int N = 20;
        int[] a = new int[N];
        int[] b = new int[N];

        System.out.print("Before:");
        for (int i = 0; i < N; i++) {
            a[i] = (int)(Math.random() * 100);
            System.out.print(" " + a[i]);
        }
        System.out.println();
        sort(a, b, 0, 100);
        System.out.print("After: ");
        for (int i = 0; i < N; i++) System.out.print(" " + b[i]);
        System.out.println();
    }
}