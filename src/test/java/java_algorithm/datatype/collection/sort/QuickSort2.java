package java_algorithm.datatype.collection.sort;

/**
 *  クイックソート（ループ版）
 */

public class QuickSort2 {

    static final int STACKSIZE = 32;  // たかだか int のビット数程度
    static final int THRESHOLD = 10;  // 要素数がこれ以下の部分は挿入ソートに任せる

    /** a[] を昇順に */
    public static void sort(int a[]) {
        int left = 0,  right = a.length - 1,  p = 0;
        int[] leftstack = new int[STACKSIZE];
        int[] rightstack = new int[STACKSIZE];

        for ( ; ; ) {
            if (right - left <= THRESHOLD) {
                if (p == 0) break;
                p--;
                left = leftstack[p];
                right = rightstack[p];
            }
            int x = a[(left + right) / 2];
            int i = left,  j = right;
            for ( ; ; ) {
                while (a[i] < x) i++;
                while (x < a[j]) j--;
                if (i >= j) break;
                int swap = a[i];  a[i] = a[j];  a[j] = swap;
                i++;  j--;
            }
            if (i - left > right - j) {
                if (i - left > THRESHOLD) {
                    leftstack[p] = left;
                    rightstack[p] = i - 1;
                    p++;
                }
                left = j + 1;
            } else {
                if (right - j > THRESHOLD) {
                    leftstack[p] = j + 1;
                    rightstack[p] = right;
                    p++;
                }
                right = i - 1;
            }
        }
        InsertSort.sort(a);
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