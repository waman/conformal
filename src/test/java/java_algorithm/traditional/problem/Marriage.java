package java_algorithm.traditional.problem;

/*
 *  Marriage.java ("marriage.c" in page 5)
 *
 *  v1.0, 1999/11/10 :  written by M. Sugiura
 *  v1.1, 1999/11/11 :  modified by M. Sugiura
 *                      ・try - catch を throws 宣言により外した
 *                      ・input.parseNumbers() メソッド呼出しを削除
 *                        (デフォルトで同じ動作をするため)
 *  v1.2, 2003/01/06 :  modified by H. Okumura
 *                      overfull を出さないように NUMBER_OF_PAIRS を N_PAIRS にしました。
 *                      ついでに EUC に変更。
 */

import java.io.*;

public class Marriage {

    public static void main(String[] args) throws IOException {
        StreamTokenizer input = new StreamTokenizer(new InputStreamReader(System.in));
        int N_PAIRS = 3;
        int boy[] = new int[N_PAIRS + 1];
        int girl[][] = new int[N_PAIRS + 1][N_PAIRS + 1];
        int position[] = new int[N_PAIRS + 1];
        int rank[][] = new int[N_PAIRS + 1][N_PAIRS + 1];
        for (int g = 1; g <= N_PAIRS; g++) {
            for (int r = 1; r <= N_PAIRS; r++) {
                input.nextToken();
                rank[g][(int)input.nval] = r;
            }
            boy[g] = 0;  rank[g][0] = N_PAIRS + 1;
        }
        for (int b = 1; b <= N_PAIRS; b++) {
            for (int r = 1; r <= N_PAIRS; r++) {
                input.nextToken();
                girl[b][r] = (int)input.nval;
            }
            position[b] = 0;
        }
        for (int b = 1; b <= N_PAIRS; b++) {
            int s = b;
            while (s != 0) {
                int g = girl[s][++position[s]];
                if (rank[g][s] < rank[g][boy[g]]) {
                    int t = boy[g];  boy[g] = s;  s = t;
                }
            }
        }
        for (int g = 1; g <= N_PAIRS; g++)
            System.out.println("女 " + g + " - 男 " + boy[g]);
    }
}