package java_algorithm.random.uniform;

/**
 *  メルセンヌ・ツイスター (mt19937ar 2002/1/26)
 */

import java.text.DecimalFormat;

public class MersenneTwister extends AbstractUniformRandomGenerator {

    static final int N = 624;
    static final int M = 397;
    static final int UPPER_MASK = 0x80000000;  // 上位1ビット
    static final int LOWER_MASK = 0x7FFFFFFF;  // 下位31ビット
    static final int MATRIX_A   = 0x9908B0DF;
    int x[] = new int[N];  // 状態を記憶する配列（リングバッファ）
    int p, q, r;  // リングバッファのインデックス

    public MersenneTwister() {
        this(System.currentTimeMillis());
    }

    public MersenneTwister(long seed){
        setSeed(seed);
    }

    public MersenneTwister(int[] seeds){
        setSeed(seeds);
    }

    @Override
    public synchronized void setSeed(long seed) {
        if (x == null) return;  //  super() からの呼び出しでは何もしない
        x[0] = (int)seed;
        for (int i = 1; i < N; i++)
            x[i] = 1812433253 * (x[i - 1] ^ (x[i - 1] >>> 30)) + i;
        p = 0;  q = 1;  r = M;
    }

    public synchronized void setSeed(int[] seeds){
        setSeed(19650218);
        int i = 1,  j = 0;
        for (int k = 0; k < Math.max(N, seeds.length); k++) {
            x[i] ^= (x[i - 1] ^ (x[i - 1] >>> 30)) * 1664525;
            x[i] += seeds[j] + j;
            if (++i >= N) {  x[0] = x[N - 1];  i = 1;  }
            if (++j >= seeds.length) j = 0;
        }
        for (int k = 0; k < N - 1; k++) {
            x[i] ^= (x[i - 1] ^ (x[i - 1] >>> 30)) * 1566083941;
            x[i] -= i;
            if (++i >= N) {  x[0] = x[N - 1];  i = 1;  }
        }
        x[0] = 0x80000000;
    }

    @Override
    protected synchronized int next(int bits) {
        int y = (x[p] & UPPER_MASK) | (x[q] & LOWER_MASK);
        y = x[p] = x[r] ^ (y >>> 1) ^ ((y & 1) * MATRIX_A);
        if (++p == N) p = 0;  // リングバッファの添字を +1
        if (++q == N) q = 0;
        if (++r == N) r = 0;

        y ^= (y >>> 11);  // 多次元分布を良くする調整
        y ^= (y  <<  7) & 0x9D2C5680;
        y ^= (y  << 15) & 0xEFC60000;
        y ^= (y >>> 18);
        return (y >>> (32 - bits));
    }

    // http://www.math.keio.ac.jp/~matumoto/emt.html
    // のコードと同じ結果を出力する
    @Override
    public double nextDouble() {
        long x = next(32);
        if (x < 0) x += 1L << 32;
        return x * (1.0 / 4294967296.0);
    }

    public static void main(String[] args) {
        int[] seeds = {0x123, 0x234, 0x345, 0x456};
        MersenneTwister random = new MersenneTwister(seeds);
        System.out.println("1000 outputs of nextInt(32)");

        for (int i = 0; i < 1000; i++) {
            long x = random.nextInt();
            if (x < 0) x += 1L << 32;
            String s = "         " + x + " ";
            System.out.print(s.substring(s.length() - 11));
            if (i % 5 == 4) System.out.println();
        }
        System.out.println();

        System.out.println("1000 outputs of nextDouble()");
        DecimalFormat df = new DecimalFormat("0.00000000 ");
        for (int i = 0; i < 1000; i++) {
            System.out.print(df.format(random.nextDouble()));
            if (i % 5 == 4) System.out.println();
        }
    }
}