package java_algorithm.equation.algebraic;

/*
* Cardano.java -- 3次方程式
*/
import java.io.*;
import java.util.*;

/**
* Cardano(カルダノ)の公式を使って 3 次方程式
* ax<sup>3</sup>+bx<sup>2</sup>+cx+d=0 の解を求めます。
*/
public class Cardano {

    private static double cuberoot(double x){  /* $\sqrt[3]{x}$ */
        boolean pos = true; // x > 0 か？

        if (x == 0) return 0;
        if (x < 0) {  pos = false;  x = -x;  }
        double s = (x > 1) ? x : 1, prev;

        do {
            prev = s;
            s = (x / (s * s) + 2 * s) / 3;
        } while (s < prev);
        return pos ? prev : -prev;
    }

    /**
    * 3 次方程式の解を表示します。a,b,c,d には係数を与えます。
    */
    public static void solve(double a, double b, double c, double d) {
            /* $ax^3 + bx^2 + cx + d = 0$, $a \ne 0$ */
        b /= (3 * a);
        c /= a;
        d /= a;
        double p = b * b - c / 3, q = (b * (c - 2 * b * b) - d) / 2;

        a = q * q - p * p * p;
        if (a == 0) {
            q = cuberoot(q);
            double x1 = 2 * q - b, x2 = -q - b;
            System.out.println("x = " + x1 + ", " + x2 + " (重解)");
        } else if (a > 0) {
            double a3;
            if (q > 0) a3 = cuberoot(q + Math.sqrt(a));
            else       a3 = cuberoot(q - Math.sqrt(a));
            double b3 = p / a3,
                   x1 = a3 + b3 - b,
                   x2 = -0.5 * (a3 + b3) - b,
                   x3 = Math.abs(a3 - b3) * Math.sqrt(3.0) / 2;
            System.out.println("x = " + x1 + "; " + x2 + " +- " + x3 + " i");
        } else {
            a = Math.sqrt(p);
            double t = Math.acos(q / (p * a));  a *= 2;
            double x1 = a * Math.cos( t                / 3) - b,
                   x2 = a * Math.cos((t + 2 * Math.PI) / 3) - b,
                   x3 = a * Math.cos((t + 4 * Math.PI) / 3) - b;
            System.out.println("x = " + x1 + ", " + x2 + ", " + x3);
        }
    }

    /**
    * 係数 a,b,c,d を入力すると 3 次方程式 ax<sup>3</sup>+bx<sup>2</sup>+cx+d=0 の解を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("a b c d = ");
        String s = in.readLine();
        StringTokenizer t = new StringTokenizer(s);

        double a = Double.parseDouble(t.nextToken()),
               b = Double.parseDouble(t.nextToken()),
               c = Double.parseDouble(t.nextToken()),
               d = Double.parseDouble(t.nextToken());

        if (a == 0) {
            System.out.println("3次方程式ではありません。");
            throw new IllegalArgumentException();
        }
        solve(a, b, c, d);
    }
}