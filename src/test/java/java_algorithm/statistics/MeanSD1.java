package java_algorithm.statistics;

/**
 * MeanSD1.java -- 平均値・標準偏差1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

class MeanSD1 {
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        double[] a = new double[1000];
        int n = 0;
        try {
            while (true) {    // TODO
                a[n] = Double.parseDouble(in.readLine());
                n++;
            }
        } catch (Exception e) { /* Ignore */ }
        double s1 = 0, s2 = 0;
        for (int i = 0; i < n; i++) s1 += a[i]; // 1回目の走査
        s1 /= n;                // 平均
        for (int i = 0; i < n; i++) {
            double x = a[i] - s1;
            s2 += x * x; // 2回目の走査
        }
        s2 = Math.sqrt(s2 / (n - 1)); // 標準偏差
        System.out.println("個数: " + n + "  平均: " + s1 + "  標準偏差: " + s2);
    }
}