package java_algorithm.traditional.problem;

/*
* Collatz.java -- Collatz (コラッツ) の予想
*/
import java.io.*;

/**
* Collatz (コラッツ) の予想を確かめるプログラムです。
*/

public class Collatz {
    static final long LIMIT = (Long.MAX_VALUE - 1L) / 3;
    /**
    * Collatz (コラッツ) の予想を確かめるプログラムです。<br>
    * 最初に初期値 n を入力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        long n = Long.parseLong(in.readLine());

        while (n > 1) {
            if ((n & 1) != 0) {  /* 奇数 */
                if (n > LIMIT) {
                    System.err.print("\nOverflow\n");
                    System.exit(1);
                } else n = 3 * n + 1;
            } else n /= 2;
            System.out.print(" " + n);
        }
        System.out.println();
    }
}