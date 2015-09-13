package java_algorithm.datatype.number.integral.prime;

/**
 *  Eratosthenes (エラトステネス) のふるい
 */

public class Sieve2 {

    static final int N = 5000;
    static final int L =  100;
    static final int S = 1000;  // S = sqrt(2LN)
    static final int M =  167;  // M >= (S 以下の奇素数の数)

    /** 2LN 以下の素数の数を数える */
    public static void main(String[] args) {
        boolean[] flag = new boolean[N];
        int[] pp = new int[M],  kk = new int[M];

        for (int i = 0; i < N; i++) flag[i] = true;
        int count = 1;  // 2 は素数
        int m = 0;
        for (int i = 1; i < N; i++)
            if (flag[i]) {
                int p = i + i + 1;  // p は素数
                count++;
                if (p <= S) {
                    int k = i + p;
                    while (k < N) {
                        flag[k] = false;  k += p;
                    }
                    pp[m] = p;  kk[m] = k - N;
                    m++;
                }
            } else flag[i] = true;
        System.out.println("      1 -   10000: " + count);

        for (int n = 1; n < L; n++) {
            count = 0;
            for (int j = 0; j < m; j++) {
                int p = pp[j],  k = kk[j];
                while (k < N) {
                    flag[k] = false;  k += p;
                }
                kk[j] = k - N;
            }
            for (int i = 0; i < N; i++)
                if (flag[i]) count++;  // 2Nn+2i+1 は素数
                else flag[i] = true;
            System.out.println(n +"0001 - " + (n + 1) + "0000: " + count);
        }
    }
}