package java_algorithm.datatype.number.rational;

/*
* EvalContFrac.java -- 連分数
*/
import java.io.*;

/**
* 連分数の値を２つの方法で求めます。
*/
public class EvalContFrac {
    /**
    * 連分数の値を右端の c<sub>n</sub>/b<sub>n</sub> から計算します。
    * @param c 分子を与えます。
    * @param b 分母を与えます。
    */
    public static double ascend(int n, double c[], double b[]) {
        double f = 0;

        while (n > 0) {
            f = c[n] / (b[n] + f);  n--;
        }
        return f + b[0];
    }

    /**
    * 連分数の値を漸化式の k を増やしながら計算します。収束の様子を表示します。
    * @param c 分子を与えます。
    * @param b 分母を与えます。
    */
    public static double descend(int n, double c[], double b[]) {
        double p1 = 1, q1 = 0, p2 = b[0], q2 = 1;

        System.out.println("  0: " + p2);
        for (int i = 1; i <= n; i++) {
            double t;
            t = p1 * c[i] + p2 * b[i];
            p1 = p2;
            p2 = t;

            t = q1 * c[i] + q2 * b[i];
            q1 = q2;
            q2 = t;

            if (q2 != 0) {
                p1 /= q2;
                q1 /= q2;
                p2 /= q2;
                q2 = 1;
                System.out.println((i < 10 ? "  " : " ") + i + ": " + p2);
            } else System.out.println(i + ": 無限大");
        }
        if (q2 != 0) return p2;
        else return Double.MAX_VALUE;
    }
/**
* サンプルプログラムです。<br>
* x を入力して tan x を求めます。
*/

    public static void main(String[] args) throws IOException { /* tan(x) */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int N = 15;
        double[] c = new double[N + 1], b = new double[N + 1];

        System.out.print("x = ");
        double x = Double.parseDouble(in.readLine());

        c[1] = x;  b[0] = 0;
        for (int i = 2; i <= N; i++) c[i] = -x * x;
        for (int i = 1; i <= N; i++) b[i] = 2 * i - 1;
        System.out.println("tan(x) by ascend() = " + ascend(N, c, b));
        System.out.println("tan(x) by descend() = " + descend(N, c, b));
        System.out.println("Math.tan(x) = " + Math.tan(x));
    }
}