package java_algorithm.equation.algebraic;

/*
* QuadraticEquation.java -- 2次方程式
*/
import java.io.*;

/**
*2次方程式を解くプログラムです。
*@see Cardano
*/
public class QuadraticEquation {

    /**
    *<b>&quot;a = &quot;</b>, <b>&quot;b = &quot;</b>, <b>&quot;c = &quot;</b></code>と
    *順次聞いてきますから，入力してください。<br>
    *2次方程式 <i>ax</i><sup>2</sup>+<i>bx</i>+<i>c</i>=0 の解を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("a = ");
        double a = Integer.parseInt(input.readLine());
        System.out.print("b = ");
        double b = Integer.parseInt(input.readLine());
        System.out.print("c = ");
        double c = Integer.parseInt(input.readLine());

        if (a != 0) {
            b /= a;
            c /= a;   // $a$ で割って $x^2 + bx + c = 0$ の形にする
            if (c != 0) {
                b /= 2;        // $x^2 + 2b'x + c = 0$
                double d = b * b - c; // 判別式 $D / 4$
                if (d > 0) {
                    double x;
                    if (b > 0) x = -b - Math.sqrt(d);
                    else       x = -b + Math.sqrt(d);
                    System.out.println("x = " + x + ", " + c / x);
                } else if (d < 0)
                    System.out.println("x = " + (-b) + " +- " + Math.sqrt(-d) + " i");
                else
                    System.out.println("x = " + (-b) + "(重解)");
            } else System.out.println("x = " + (-b) + ", 0");
        } else if (b != 0) System.out.println("x = " + (-c / b));
        else if (c != 0) System.out.println("解なし");
        else System.out.println("不定");
    }
}