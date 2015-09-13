package java_algorithm.datatype.number.rational;

/*
* EgyptianFractions.java -- エジプトの分数
*/

import java.io.*;
/**
* エジプトの分数を表示します。
*/

public class EgyptianFractions {
    /**
    * 分子・分母を入力すると単位分数の和の形にして出力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(" 分子 m = ");
        int m = Integer.parseInt(in.readLine());
        System.out.print(" 分母 n = ");
        int n = Integer.parseInt(in.readLine());

        System.out.print(m + "/" + n + " = ");
        while (n % m != 0) {
            int q = n / m + 1;
            System.out.print("1/" + q + " + ");
            m = m * q - n;  n *= q;
        }
        System.out.println("1/" + n / m);
    }
}