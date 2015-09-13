package java_algorithm.datatype.collection.sort;

/**
 *  マージ
 */

public class Merge {

    /** 整列済の a[] と b[] を併合して c[] に */
    public static void merge(int a[], int b[], int c[]) {
        int i = 0,  j = 0,  k = 0;
        while (i < a.length && j < b.length)
            if (a[i] <= b[j]) c[k++] = a[i++];
            else              c[k++] = b[j++];
        while (i < a.length)  c[k++] = a[i++];
        while (j < b.length)  c[k++] = b[j++];
    }

    public static void main(String[] args) {
        final int NA = 10;
        final int NB = 20;
        int a[] = new int[NA];
        int b[] = new int[NB];
        int c[] = new int[NA + NB];

        a[0] = (int)(Math.random() * 10);
        for (int i = 1; i < NA; i++)
            a[i] = a[i - 1] + (int)(Math.random() * 10);
        b[0] = (int)(Math.random() * 10);
        for (int i = 1; i < NB; i++)
            b[i] = b[i - 1] + (int)(Math.random() * 10);

        System.out.print("a:");
        for (int i = 0; i < NA; i++) System.out.print(" " + a[i]);
        System.out.println();
        System.out.print("b:");
        for (int i = 0; i < NB; i++) System.out.print(" " + b[i]);
        System.out.println();

        merge(a, b, c);

        System.out.print("c:");
        for (int i = 0; i < NA + NB; i++) System.out.print(" " + c[i]);
        System.out.println();
    }
}