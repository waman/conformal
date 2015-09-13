package java_algorithm.datatype.number.integral.mod;

/**
 *  原始根
 */

public class PrimitiveRoot {

    PrimitiveRoot(int n) {
        prime = generatePrimes(n);
    }

    private int[] prime;  // 最初の{\tt n}個の素数をコンストラクタで代入する

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

    private int modPower(int n, int r, final int m) {  // $n^r \bmod m$ の計算
        int t = 1;
        while (r != 0) {
            if (r % 2 == 1) t = (int)(((long)t * n) % m);
            n = (int)(((long)n * n) % m);  r /= 2;
        }
        return t;
    }

    public int primitiveRoot(int k) {  // {\tt prime[k]} の一つの原始根を返す
        final int p = prime[k],  n = p - 1;
        for (int i = 0; i < k; i++) {
            final int a = prime[i];  int j;
            for (j = 0; j < k; j++)
                if (n % prime[j] == 0 &&
                    modPower(a, n / prime[j], p) == 1) break;
            if (j == k) return a;
        }
        return 0;  // 見つからない
    }

    private static String right(int num, int len) {
        String s = "    " + num;
        return s.substring(s.length() - len);
    }

    public static void main(String[] args) {
        final int N = 168;
        PrimitiveRoot pr = new PrimitiveRoot(N);
        System.out.println("素数と原始根");
        System.out.print("    2( -)");
        for (int i = 1; i < N; i++) {
            if (i % 7 == 0) System.out.println();
            System.out.print(
                    right(pr.prime[i], 5) + "(" + right(pr.primitiveRoot(i), 2) +")");
        }
        System.out.println();
    }
}