package java_algorithm.equation;

import java_algorithm.datatype.collection.search.FibonacciSearch;

/**
 * BisectionTest.java -- 2分法(bisection method)の使用例
 */

public class BisectionTest {

    public static void main(String[] args) {
        Function2 f = new Function2(1, 0, -2);  // x^2 - 2

        double x = Bisection.solve(f, 1, 2, 0);  // [1,2] で解を求める
        System.out.println("solve f(x) = 0");
        System.out.println("x = " + x);
        System.out.println("f(x) = " + f.of(x));

        System.out.println();  // [-2, 2] で最小値を求める
        x = FibonacciSearch.minimize(f, -2, 2, 1e-10);
        System.out.println("minimize f(x)");
        System.out.println("x = " + x);
        System.out.println("f(x) = " + f.of(x));
    }
}

/** 2次関数 ax^2 + bx + c */
class Function2 implements Bisection.Function, FibonacciSearch.Function {

    private double a, b, c;

    public Function2(double a, double b, double c) {
        this.a = a;  this.b = b;  this.c = c;
    }

    @Override
    public double of(double x) {
        return (a * x + b) * x + c;
    }
}