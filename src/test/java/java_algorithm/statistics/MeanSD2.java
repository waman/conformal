package java_algorithm.statistics;

/**
 * MeanSD2.java -- 平均値・標準偏差2
 */

import java.io.*;

class MeanSD2 {
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = 0;
        double s1 = 0, s2 = 0;
        try {
            while (true) {    // TODO
                double x = Double.parseDouble(in.readLine());
                n++;  s1 += x;  s2 += x * x;
            }
        } catch (Exception e) { /* Igonore */ }
        s1 /= n;                // 平均
        s2 = (s2 - n * s1 * s1) / (n - 1); // 分散
        if (s2 > 0) s2 = Math.sqrt(s2);  else s2 = 0; // 標準偏差
        System.out.println("個数: " + n + "  平均: " + s1 + "  標準偏差: " + s2);
    }
}