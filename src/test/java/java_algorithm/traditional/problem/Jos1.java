package java_algorithm.traditional.problem;

/**
* Jos1.java -- Josephus (ヨセフス) の問題
*/
import java.io.*;

/**
* Josephus (ヨセフス) の問題を方法1で解くプログラムです。
* @see Jos2
*/

public class Jos1 {

    /**
    *ヨセフスの問題の解を表示します。<br>
    *<b>人数? </b>(n)と <b>何人ごと? </b>(p)と聞いてきます。<br>
    * n と p を入力すると何番(J<sub>p</sub>(n))の人が残るかを表示します。<br>
    * 例：n = 41, p = 3, J<sub>3</sub>(41) = 31
    */

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("人数? ");     int n = Integer.parseInt(input.readLine());
        System.out.print("何人ごと? "); int p = Integer.parseInt(input.readLine());
        int k = 1;
        for (int j = 2; j <= n; j++) {
            k = (k + p) % j;
            if (k == 0) k = j;
        }
        System.out.println(k + " 番の人が残ります");
    }
}