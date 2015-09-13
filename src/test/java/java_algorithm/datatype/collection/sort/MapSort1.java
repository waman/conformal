package java_algorithm.datatype.collection.sort;

/**
 *  逆写像ソート
 */

import java.util.ArrayList;
import java.util.List;

public class MapSort1 {

    /** a[] を昇順に整列 (Vector 使用) */
    public static void sort(int a[], int min, int max) {
        List<List<Integer>> index = new ArrayList<>(max - min + 1);

        for (int x = 0; x <= max - min; x++)
            index.set(x, new ArrayList<Integer>());

        for(int ai : a)
            index.get(ai - min).add(ai);

        int j = 0;
        for (int x = 0; x <= max - min; x++)
            for (int i = 0; i < index.get(i).size(); i++)
                a[j++] = index.get(x).get(i);
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
        sort(a, 0, 100);
        System.out.print("After: ");
        for (int i = 0; i < N; i++) System.out.print(" " + a[i]);
        System.out.println();
    }
}