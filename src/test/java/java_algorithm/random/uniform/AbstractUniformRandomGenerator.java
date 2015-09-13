package java_algorithm.random.uniform;

/**
 * @author waman
 */
public abstract class AbstractUniformRandomGenerator implements UniformRandomGenerator {

    protected abstract int next(int bits);

    @Override
    public int nextInt() {
        return next(32);
    }

    @Override
    public int nextInt(int n) {  // 0<n<2^31
        if (n <= 0)
            throw new IllegalArgumentException("n は正の数");

        if ((n & -n) == n)  // nが2の累乗
            return (int)((n * (long)next(31)) >> 31);

        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }

    @Override
    public long nextLong() {
        return ((long)(next(32)) << 32) + next(32);
    }

    @Override
    public float nextFloat() {
        return next(24) / ((float)(1 << 24));
    }

    @Override
    public double nextDouble() {
        return (((long)next(26) << 27) + next(27)) / (double)(1L << 53);
    }
}
