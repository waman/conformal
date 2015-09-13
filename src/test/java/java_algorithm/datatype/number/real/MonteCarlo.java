package java_algorithm.datatype.number.real;

/**
 *  モンテカルロ法
 */

public class MonteCarlo {

    /** 方法1 */
    static void monteCarlo1(int n) {
        int hit = 0;
        for (int i = 0; i < n; i++) {
            double x = Math.random();
            double y = Math.random();
            if (x * x + y * y < 1) hit++;
        }
        double p = (double) hit / n;
        System.out.println(
                "pi = " + (4 * p) + " +- " + (4 * Math.sqrt(p * (1 - p) / (n - 1))));
    }

    /** 方法2 */
    static void monteCarlo2(int n) {
        double sum = 0, sum2 = 0;
        for (int i = 0; i < n; i++) {
            double x = Math.random();
            double y = Math.sqrt(1 - x * x);
            sum += y;
            sum2 += y * y;
        }
        double mean = sum / n;
        double sd = Math.sqrt((sum2 / n - mean * mean) / (n - 1));
        System.out.println("pi = " + (4 * mean) + " +- " + (4 * sd));
    }

    /** 方法3 */
    static void monteCarlo3(int n) {
        final double a = (Math.sqrt(5) - 1) / 2;  // 適当な無理数
        double x = 0,  sum = 0;
        for (int i = 0; i < n; i++) {
            if ((x += a) >= 1) x--;
            sum += Math.sqrt(1 - x * x);
        }
        System.out.println("pi = " + (4 * sum / n));
    }

    public static void main(String[] args) {
        monteCarlo1(10000);
        monteCarlo2(10000);
        monteCarlo3(10000);
    }
}