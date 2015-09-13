package java_algorithm.datatype.number.integral.prime;

/**
 *  素数
 */
public class Primes {

    static int[] generatePrimes(final int n) {  // {\tt n} 個の素数を生成
        int k = 0,  prime[] = new int[n];
        prime[k++] = 2;
        for (int x = 3; k < n; x += 2) {
            int i = 0;
            while (i < k && x % prime[i] != 0) i++;
            if (i == k) prime[k++] = x;
        }
        return prime;
    }

    public static void main(String[] args) {
        final int N = 168;
        int[] prime = generatePrimes(N);

        System.out.print("素数表");
        for (int i = 0; i < N; i++) {
            if (i % 10 == 0) System.out.println();
            String s = "    " + prime[i];
            System.out.print(s.substring(s.length() - 5));
        }
        System.out.println();
    }
}