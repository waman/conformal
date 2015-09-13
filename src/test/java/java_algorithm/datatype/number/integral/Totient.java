package java_algorithm.datatype.number.integral;

/**
 *  Totient.java -- Euler (オイラー) の関数
 */
public class Totient {

    public static int phi(int x) {
        int t = x;
        if (x % 2 == 0) {
            t /= 2;
            do {  x /= 2;  } while (x % 2 == 0);
        }
        int d = 3;
        while (x / d >= d) {
            if (x % d == 0) {
                t = t / d * (d - 1);
                do {  x /= d;  } while (x % d == 0);
            }
            d += 2;
        }
        if (x > 1) t = t / x * (x - 1);
        return t;
    }

    public static void main(String[] args) {
        System.out.println("オイラーの関数 φ(1),…,φ(200)");
        System.out.print("     ");
        for (int j = 1; j <= 10; j++) System.out.print("   +" + j);
        System.out.println();
        System.out.print("      ");
        for (int j = 1; j <= 10; j++) System.out.print("-----");
        System.out.println();
        for (int i = 0; i < 20; i++) {
            String s = "  " + (i * 10) + " |";
            System.out.print(s.substring(s.length() - 5));
            for (int j = 1; j <= 10; j++) {
                s = "    " + phi(10 * i + j);
                System.out.print(s.substring(s.length() - 5));
            }
            System.out.println();
        }
    }
}