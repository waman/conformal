package java_algorithm.datatype.number.integral.prime;

/**
 *  Eratosthenes (エラトステネス) のふるい
 */
public class Sieve1 {

    /** 2n+3 以下の素数を列挙する */
    public static void sieve(int n) {
        boolean[] flag = new boolean[n + 1];  // flag[i] は 2i+3 が
        System.out.print(2);                  // 素数なら true
        int count = 1;
        for (int i = 0; i <= n; i++) flag[i] = true;
        for (int i = 0; i <= n; i++)
            if (flag[i]) {
                count++;
                int p = 2 * i + 3;
                System.out.print(" " + p);
                for (int k = i + p; k <= n; k += p) flag[k] = false;
            }
        System.out.println("\n" + count + " primes");
    }

    public static void main(String[] args) {
        sieve(8190);
    }
}