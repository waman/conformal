package java_algorithm.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Hypot {

    static double hypot0(double x, double y) { // 通常の方法
        return Math.sqrt(x * x + y * y);
    }

    static double hypot1(double x, double y) { // やや念入りな方法
        if (x == 0) return Math.abs(y);
        if (y == 0) return Math.abs(x);
        if (Math.abs(y) > Math.abs(x)) {
            double t = x / y;
            return Math.abs(y) * Math.sqrt(1 + t * t);
        } else {
            double t = y / x;
            return Math.abs(x) * Math.sqrt(1 + t * t);
        }
    }

    static double hypot2(double x, double y) { // Moler-Morrison法
        x = Math.abs(x);
        y = Math.abs(y);
        if (x < y) {
            double t = x;
            x = y;
            y = t;
        }
        if (y == 0) return x;
        for (int i = 0; i < 3; i++) {  // float: 2, double: 3
            double t = y / x;
            t *= t;
            t /= 4 + t;

            x += 2 * x * t;
            y *= t;
        }
        return x;
    }

    // 以下はテスト用
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("x? ");
        double x = Double.parseDouble(in.readLine());
        System.out.print("y? ");
        double y = Double.parseDouble(in.readLine());
        System.out.println("hypot0 = " + hypot0(x, y));
        System.out.println("hypot1 = " + hypot1(x, y));
        System.out.println("hypot2 = " + hypot2(x, y));
    }
}