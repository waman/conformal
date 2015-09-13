package java_algorithm.datatype.number.integral.prime;

/**
 *  素数のLucasテスト 配列版
 */

public class Lucas2 {

    /** 2^p-1 が素数なら true を返す (pは3以上の素数) */
    public static boolean isMersennePrime(final int p) {
        int[] a = new int[p + 1],  x = new int[p + 1];  // +1 は番人用
        for (int i = 0; i < p; i++) a[i] = 0;
        a[2] = 1;  // a = 4

        for (int k = 2; k < p; k++) {
            System.err.print((p - k) + " \r");  // 本文からは削除
            int[] swap = x;  x = a;  a = swap;  // x = a
            for (int i = 0; i < p; i++) a[i] = 1;
            a[1] = 0;  // a = -2 mod M_p
            for (int i = 0; i < p; i++)  // a = (a + x ^ 2) mod M_p
                if (x[i] > 0) {
                    int s = 0,  h = i;
                    for (int j = 0; j < p; j++) {
                        s = s / 2 + a[h] + x[j];
                        a[h] = s % 2;  if (++h == p) h = 0;
                    }
                    if (s > 1) {  // 最後の繰り上がり
                        while (a[h] > 0) {
                            a[h] = 0;  if (++h == p) h = 0;
                        }
                        a[h] = 1;
                    }
                }
        }
        a[p] = 1 - a[0];  // 番人
        int i = 1;  while (a[i] == a[0]) i++;
        return (i == p);  // a == 0 か a == M_p で true
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("使用法: java Lucas2 p");
            return;
        }
        final int p = Integer.parseInt(args[0]);
        if (p < 3) {
            System.out.println("３以上の素数を指定して下さい");
            return;
        }
        System.out.println("2^" + p + "-1 は ...");
        if (isMersennePrime(p)) System.out.println("素数です");
        else                    System.out.println("合成数です");
    }
}