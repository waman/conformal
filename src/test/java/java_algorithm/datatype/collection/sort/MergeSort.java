package java_algorithm.datatype.collection.sort;

/**
 *  マージソート
 */

public class MergeSort {

    private static class Sortee {

        int key, info;

        public Sortee(int k, int i) {
            key = k;
            info = i;
        }

        @Override
        public String toString() {
            return key + ":" + info;
        }
    }

    /** a[] を昇順に */
    public static void sort(Sortee a[]) {
        Sortee[] work = new Sortee[a.length / 2];
        sort(a, 0, a.length, work);
    }

    /** a[first..last-1] を昇順に */
    private static void sort(Sortee a[], int first, int last, Sortee work[]) {
        if (last - first <= 1) return;
        final int middle = (first + last) / 2;
        sort(a, first, middle, work);
        sort(a, middle, last, work);
        while (a[first].key < a[middle].key) first++;  // 高速化

        int i = first,  j = 0;
        while (i < middle) work[j++] = a[i++];
        i = first;  j = 0;  int k = middle;
        while (j < middle - first && k < last)
            if (work[j].key <= a[k].key) a[i++] = work[j++];
            else                         a[i++] = a[k++];
        while (j < middle - first) a[i++] = work[j++];
    }

    public static void main(String[] args) {
        final int N = 20;
        Sortee[] a = new Sortee[N];

        System.out.print("Before:");
        for (int i = 0; i < N; i++) {
            a[i] = new Sortee((int)(Math.random() * 10), i);
            System.out.print(" " + a[i]);
        }
        System.out.println();
        sort(a);
        System.out.print("After: ");
        boolean stable = true;
        for (int i = 0; i < N; i++) {
            System.out.print(" " + a[i]);
            if (i > 0 && a[i - 1].key == a[i].key &&
                a[i - 1].info > a[i].info) stable = false;
        }
        System.out.println();
        if (stable) System.out.println("安定です。");
        else        System.out.println("安定ではありません。");
    }
}