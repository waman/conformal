package java_algorithm.datatype.number.integral.series;

/**
 * Fibonacci.java -- Fibonacci (フィボナッチ) 数列
 */

public class Fibonacci {

    static void method1() {
        int a = 1,  b = 0;
        while (a < 100) {
            System.out.print(" " + a);
            int t = a + b;
            b = a;
            a = t;
        }
    }

    static void method2() {
        int a = 1,  b = 0;
        while (a < 100) {
            System.out.print(" " + a);
            a += b;  b = a - b;
        }
    }

    static final double SQRT5 = Math.sqrt(5);

    public static int term1(int n) {
        return (int)(Math.pow((1 + SQRT5) / 2, n) / SQRT5 + 0.5);
    }

    public static int term2(int n) {
        int a = 1, b = 1, c = 0, x = 1, y = 0;
        n--;
        while (n > 0) {
            if ((n % 2) != 0) {
                int x1 = a * x + b * y;
                int y1 = b * x + c * y;
                x = x1;  y = y1;
            }
            n /= 2;
            int a1 = a * a + b * b;
            int b1 = b * (a + c);
            int c1 = b * b + c * c;
            a = a1;  b = b1;  c = c1;
        }
        return x;
    }

    /**
     * 100未満の Fibonacci 数列 Fn (n = 1～11) を
     * ４通りの方法で計算し，表示します。
     */
    public static void main(String[] args) {
        method1();
        System.out.println();

        method2();
        System.out.println();

        for (int i = 1; i <= 11; i++) System.out.print(" " + term1(i));
        System.out.println();

        for (int i = 1; i <= 11; i++) System.out.print(" " + term2(i));
        System.out.println();
    }
}