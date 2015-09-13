package java_algorithm.datatype.number.integral.series;

/*
 * Stirling.java -- Stirling (スターリング) 数
*/

import java_algorithm.datatype.string.NumberFormatter;

/**
*Stirling (スターリング) 数を求めます。
*@see NumberFormatter
*/

public class Stirling {

    /**
     *第1種Stirling数を返します。
     */
    public static int Stirling1(int n, int k) { // n > 0
        if (k < 1 || k > n) return 0;
        if (k == n) return 1;
        return (n - 1) * Stirling1(n - 1, k)
                + Stirling1(n - 1, k - 1);
    }

    /**
    *第2種Stirling数を返します。
    */
    public static int Stirling2(int n, int k) { // n > 0
        if (k < 1 || k > n) return 0;
        if (k == 1 || k == n) return 1;
        return k * Stirling2(n - 1, k)
             + Stirling2(n - 1, k - 1);
    }

    /**
    *漸化式を使って第2種，第1種Stirling数を計算し，表示します。
    */
    public static void main(String[] args) {
        NumberFormatter fmt6d = new NumberFormatter("6d");

        System.out.println("Stirling numbers of the 2nd kind");
        System.out.print("  k");
        for (int k = 0; k <= 8; k++) System.out.print(fmt6d.toString(k));
        System.out.print("\nn  ");
        for (int k = 0; k <= 8; k++) System.out.print("------");
        System.out.println();
        for (int n = 0; n <= 8; n++) {
            System.out.print(n + " |");
            for (int k = 0; k <= n; k++)
                System.out.print(fmt6d.toString(Stirling2(n, k)));
            System.out.println();
        }

        System.out.print("\nStirling numbers of the 1st kind\n");
        System.out.print("  k");
        for (int k = 0; k <= 8; k++) System.out.print(fmt6d.toString(k));
        System.out.print("\nn  ");
        for (int k = 0; k <= 8; k++) System.out.print("------");
        System.out.println();
        for (int n = 0; n <= 8; n++) {
            System.out.print(n + " |");
            for (int k = 0; k <= 8; k++)
                System.out.print(fmt6d.toString(Stirling1(n, k)));
            System.out.println();
        }
    }
}