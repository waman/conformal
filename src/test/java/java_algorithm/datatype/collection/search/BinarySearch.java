package java_algorithm.datatype.collection.search;

/**
 *  2分探索
 */

import java.io.*;
import java.util.*;

public class BinarySearch {

    public static final int NOT_FOUND = -1;

    public static int search(int x, int[] a, int left, int right) {
        while (left < right) {
            int mid = (left + right) / 2;
            if (a[mid] < x) left = mid + 1;  else right = mid;
        }
        if (a[left] == x) return left;
        return NOT_FOUND;
    }

    public static int searchOrBigger(int x, int[] a, int left, int right) {
        right++;
        while (left < right) {
            int mid = (left + right) / 2;
            if (a[mid] < x) left = mid + 1;  else right = mid;
        }
        return left;
    }

    public static int searchOrSmaller(int x, int[] a, int left, int right) {
        right++;
        while (left < right) {
            int mid = (left + right) / 2;
            if (a[mid] <= x) left = mid + 1;  else right = mid;
        }
        return left - 1;
    }

    private static String right(int num) {
        String s = "  " + num;
        return s.substring(s.length() - 3);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("***** ２分探索デモンストレーション *****");
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

        final int N = 20;
        int[] a = new int[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) a[i] = random.nextInt(100);
        Arrays.sort(a);

//          double r = 1.0;
//          for (int i = N - 1; i >= 0; i--) {
//              // 1未満0以上の一様乱数をrに降順に作る
//              r *= Math.pow(random.nextUniform(), 1.0 / (i + 1));
//              // 0以上100未満の整数に直す
//              a[i] = (int)(100.0 * r);
//          }

        System.out.print("  i : ");
        for (int i = 0; i < N; i++) System.out.print(right(i));
        System.out.println();

        System.out.print("a[i]: ");
        for (int i = 0; i < N; i++) System.out.print(right(a[i]));
        System.out.println();

        System.out.print("\n何を探しますか? ");
        int x = Integer.parseInt(input.readLine());

        System.out.print("search:          ");
        int i = search(x, a, 0, N - 1);
        if (i != NOT_FOUND) System.out.println("i = " + i);
        else                System.out.println("見つかりません");

        System.out.print("searchOrBigger:  ");
        i = searchOrBigger(x, a, 0, N - 1);
        System.out.println("i = " + i);

        System.out.print("searchOrSmaller: ");
        i = searchOrSmaller(x, a, 0, N - 1);
        System.out.println("i = " + i);
    }
}