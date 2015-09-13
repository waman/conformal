package java_algorithm.function;

/**
 *   Newton 法で立方根を求める
 */
public class CubeRoot {

    public static double cuberoot(double x) {  // double 版 x^(1/3)
        if (x == 0) return 0;
        int sign = (x > 0) ? 1 : -1;
        if (sign == -1) x = -x;
        double s = 1, prev;
        if (x > 1) s = x;
        do {
            prev = s;  s = (x / (s * s) + 2 * s) / 3;
        } while (s < prev);
        return sign * prev;
    }

    public static int cuberoot(int x) {  // int 版 x^(1/3)
        if (x == 0) return 0;
        int sign = (x > 0) ? 1 : -1;
        if (sign == -1) x = -x;
        int s = 1, t = x;
        while (s < t) {
            s <<= 1;
            t >>= 2;
        }
        do {
            t = s;
            s = (x / (s * s) + 2 * s) / 3;
        } while (s < t);
        return sign * t;
    }

    public static void main(String args[]) { // テスト用
        if (args.length == 0) {
            System.err.println("java CubeRoot number(double or integer)");
            return;
        }
        try {
            int x = Long.valueOf(args[0]).intValue();
            System.out.println("cuberoot(" + x + ")=" + cuberoot(x));
        } catch (NumberFormatException e) {
            try {
                double x = Double.valueOf(args[0]);
                System.out.println("cuberoot(" + x + ")=" + cuberoot(x));
            } catch (Exception other) {
                System.err.println("number format error=" + args[0]);
            }
        }
    }
}