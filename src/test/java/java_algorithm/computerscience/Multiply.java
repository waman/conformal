package java_algorithm.computerscience;

/**
 *  整数の積
 */

public class Multiply {

    public static long multiply(int a, int b) {
        long c = 0, longb = b;
        while (a != 0) {
            if ((a & 1) == 1) c += longb;
            longb <<= 1; a >>>= 1;
        }
        return c;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("使用法: java Multiply a b");
            return;
        }
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println(a + " * " + b + " = " + multiply(a, b));
    }
}