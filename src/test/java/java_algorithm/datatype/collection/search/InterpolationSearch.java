package java_algorithm.datatype.collection.search;

/**
 *  補間探索
 */

import java.io.*;
import java.util.*;

public class InterpolationSearch {

    public static int search(int x, int[] a, int left, int right) {
        if (left == right) {
            if (a[left] == x) return left;
            else throw new IllegalArgumentException("not found");
        } else if (left > right || a[left] > x || a[right] < x)
            throw new IllegalArgumentException("not found");
        for ( ; ; ) {
            int mid = (int)((long)(x - a[left]) * (right - left) / (a[right] - a[left])) + left;
            if (a[mid] < x) {
                left  = mid + 1;
                if (a[left]  > x) break;
            } else if (a[mid] > x) {
                right = mid - 1;
                if (a[right] < x) break;
            } else return mid;
        }
        throw new IllegalArgumentException("not found");
    }

    private static String right(int num) {
        String s = "  " + num;
        return s.substring(s.length() - 3);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("***** 補完探索デモンストレーション *****");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        final int N = 20;
        int[] a = new int[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) a[i] = random.nextInt(100);
        Arrays.sort(a);

        System.out.print("  i : ");
        for (int i = 0; i < N; i++) System.out.print(right(i));
        System.out.println();

        System.out.print("a[i]: ");
        for (int i = 0; i < N; i++) System.out.print(right(a[i]));
        System.out.println();

        System.out.print("\n何を探しますか? ");
        int x = Integer.parseInt(input.readLine());

        try {
            int i = search(x, a, 0, N - 1);
            System.out.println("i = " + i);
        } catch (Error e) {
            System.out.println("見つかりません");
        }
    }
}