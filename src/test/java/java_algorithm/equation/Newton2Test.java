package java_algorithm.equation;

/*
* Newton2Test.java -- Newton法(Newton2)の使用例
*/
/**
*Newton法(Newton2)の使用例です。
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Newton2Test {

    static class Function implements Newton2.Function {

        @Override
        public double of(double x) { // 零点を求めたい関数
            return x * (x * x - 1);
        }
    }

    /**
    * 初期値を入力すると <code><i>x</i><sup>3</sup> - x = 0</code> の解を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("x^3 - x = 0 を解きます.");
        System.out.print("初期値 = ");
        double x0 = Double.parseDouble(input.readLine());

        Function f = new Function();
        double x = Newton2.solve(f, x0);

        System.out.println("x = " + x);
        System.out.println("f(x) = " + f.of(x));
    }
}