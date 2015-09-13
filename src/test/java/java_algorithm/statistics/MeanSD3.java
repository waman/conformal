package java_algorithm.statistics;

/**
 * MeanSD3.java -- 平均値・標準偏差3
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

class MeanSD3 {

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = 0;
        double s1 = 0, s2 = 0;
        try {
            while (true) { // TODO
                double x = Double.parseDouble(in.readLine());
                n++;            // 個数
                x -= s1;        // 仮平均との差
                s1 += x / n;    // 平均
                s2 += (n - 1) * x * x / n; // 平方和
            }
        } catch (Exception e) { /* Ignore */ }
        s2 = Math.sqrt(s2 / (n - 1)); // 標準偏差
        System.out.println("個数: " + n + "  平均: " + s1 + "  標準偏差: " + s2);
    }
}