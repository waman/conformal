package java_algorithm.traditional.problem;

/**
* Jos2.java -- Josephus (ヨセフス) の問題
*/
import java.io.*;

/**
* Josephus (ヨセフス) の問題を方法2で解くプログラムです。
* @see Jos1
*/

public class Jos2 {

    /**
    *ヨセフスの問題の解を表示します。<br>
    * <b>人数? </b>(n)と <b>何人ごと? </b>(p)と聞いてきます。<br>
    * n と p を入力すると何番(J<sub>p</sub>(n))の人が残るかを表示します。<br>
    * 例：n = 10, p = 3, J<sub>3</sub>(10) = 4
    */

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("人数? ");     int n = Integer.parseInt(input.readLine());
        System.out.print("何人ごと? "); int p = Integer.parseInt(input.readLine());
        int k = p - 1;
        while (k < (p - 1) * n) k = (p * k) / (p - 1) + 1;
        System.out.println((p * n - k) + " 番の人が残ります");
    }
}