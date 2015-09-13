package java_algorithm.datatype.number.rational;

/**
* ContFrac.java -- 連分数
*/
/**
* 連分数のプログラムです。
*/
public class ContFrac {

    /**
    * x を単純連分数<code>[b<sub>0</sub>,b<sub>1</sub>,b<sub>2</sub>,...b<sub>n</sub>]</code>
    *に直します。
    */
    public static void simpleCF(double x, int n, long b[]) {
        b[0] = (long)Math.floor(x);
        for (int i = 1; i <= n; i++) {
            x = 1 / (x - b[i - 1]);
            b[i] = (long)Math.floor(x);
        }
    }

    private static long gcd(long x, long y) {
        while (y != 0) {
            long t = x % y;  x = y;  y = t;
        }
        return x;
    }

    /**
    * 単純連分数<code>[b<sub>0</sub>,b<sub>1</sub>,b<sub>2</sub>,...b<sub>n</sub>]</code>
    *を普通の分数に直し，表示します。
    */
    public static void reduceCF(int n, long b[]) {
        long f = b[n], g = 1;

        for (int i = n - 1; i >= 0; i--) {
            long temp = b[i] * f + g;
            g = f;
            f = temp;

            long d = gcd(f, g);
            f /= d;
            g /= d;
        }
        System.out.println(f + " / " + g + " = " + (double)f / g);
    }

    /**
    * テストプログラムです。<br>
    * 1)自然対数の底 e を連分数展開します。<br>
    * 2)得られた連分数をふつうの分数と実数に直します。
    */
    public static void main(String[] args) {
        final int N = 20;
        long[] b = new long[N + 1];

        /* e = 2.718...の連分数展開 */
        simpleCF(Math.E, N, b);
        System.out.print("e = [");
        for (int i = 0; i <= N; i++) System.out.print(b[i] + ",");
        System.out.println("...]");

        /* ふつうの分数と実数に直す */
        reduceCF(N, b);
        // Math.E を表示
        System.out.print("Math.E = " + Math.E);
    }
}