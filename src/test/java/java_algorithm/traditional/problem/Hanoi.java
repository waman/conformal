package java_algorithm.traditional.problem;

/*
* Hanoi.java -- ハノイの塔
*/
import java.io.*;
/**
* ハノイの塔の問題を解くプログラムです。
*/
public class Hanoi {

    static int count = 1;

    static void moveDisk(int n, int a, int b) {
        if (n > 1) moveDisk(n - 1, a, 6 - a - b);
        System.out.println((count++) + " 手目：円盤 " + n + " を " + a + " から " + b + " に移す");
        if (n > 1) moveDisk(n - 1, 6 - a - b, b);
    }

    /**
    *円盤の枚数を入力すると手順を表示します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("円盤の枚数？ ");
        int n = Integer.parseInt(in.readLine());

        moveDisk(n, 1, 2);
        System.out.println("円盤 " + n +
            " 枚を柱 1 から柱 2 に移すには合計 " + ((1 << n) - 1) +
            " 手が必要です。");
    }
}