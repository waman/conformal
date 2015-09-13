package java_algorithm.random;

/**
 *  Ｍ系列乱数
 */
public class MSequenceRandom {

    static final int N = 521, M = 32;
    int x[] = new int[N], p;

    public MSequenceRandom() {
        this((int)System.currentTimeMillis());
    }

    public MSequenceRandom(int seed) {
        setSeed(seed);
    }

    private void shake() {
        for (int i = 0; i < M; i++) x[i] ^= x[i - M + N];
        for (int i = M; i < N; i++) x[i] ^= x[i - M];
    }

    synchronized public void setSeed(int seed) {
        for (int i = 0; i <= 16; i++) {
            int u = 0;
            for (int j = 0; j < 32; j++) {
                seed = seed * 1566083941 + 1;
                u = (u >>> 1) | (seed & (1 << 31));
            }
            x[i] = u;
        }
        x[16] = (x[16] << 23) ^ (x[0] >>> 9) ^ x[15];
        for (int i = 17; i < N; i++)
            x[i] = (x[i - 17] << 23) ^ (x[i - 16] >>> 9) ^ x[i - 1];
        shake();  shake();  shake();  shake();  // warm up
        p = 0;
    }

    synchronized public int nextInt() {
        if (p >= N) {  shake();  p = 0;  }
        return x[p++];
    }

    public static void main(String[] args) {
        MSequenceRandom random = new MSequenceRandom();
        for (int i = 0; i < 10; i++)
            System.out.println(random.nextInt());
    }
}