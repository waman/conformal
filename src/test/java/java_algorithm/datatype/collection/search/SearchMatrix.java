package java_algorithm.datatype.collection.search;

/**
 *  2次元の探索
 */

import java.io.*;

public class SearchMatrix {
    int i, j;

    boolean search(int x, int[][] a, int imin, int imax, int jmin, int jmax) {
        i = imax;  j = jmin;
        while (a[i][j] != x) {
            if (a[i][j] < x) j++;  else i--;
            if (i < imin || j > jmax) return false;  // 見つからない
        }
        return true;  // 見つかった
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int[][] a = {{1, 2, 3, 4}, {2, 4, 6, 8}, {3, 6, 9, 12}};

        System.out.print(
            "    1  2  3  4\n" +
            "A = 2  4  6  8  (i = 0, 1, 2; j = 0, 1, 2, 3)\n" +
            "    3  6  9 12\n" +
            "何を探しますか? ");
        int x = Integer.parseInt(input.readLine());

        SearchMatrix s = new SearchMatrix();
        if (s.search(x, a, 0, 2, 0, 3))
            System.out.println("(i, j) = (" + s.i + ", " + s.j + ")");
        else
            System.out.println("見つかりません");
    }
}