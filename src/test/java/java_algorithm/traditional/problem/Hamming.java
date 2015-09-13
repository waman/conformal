package java_algorithm.traditional.problem;

/**
 *  Hamming (ハミング) の問題
 */

public class Hamming {

    static int[] hamming(final int n) {
        int[] q = new int[n];
        int j2, j3, j5, x2, x3, x5;

        j2 = j3 = j5 = 0;  x2 = x3 = x5 = 1;
        for (int i = 0; i < n; i++) {
            int min = x2;
            if (x3 < min) min = x3;
            if (x5 < min) min = x5;
            q[i] = min;
            while (x2 <= min) x2 = 2 * q[j2++];
            while (x3 <= min) x3 = 3 * q[j3++];
            while (x5 <= min) x5 = 5 * q[j5++];
        }
        return q;
    }

    public static void main(String[] args) {
        final int N = 100;
        int[] q = hamming(N);
        for (int i = 0; i < N; i++) System.out.print(q[i]);
        System.out.println();
    }
}