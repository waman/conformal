package java_algorithm.random.uniform;

/**
 *  乱数
 *
 *  (by JavaRandom)
 */
public class JavaUniformRandomGenerator extends AbstractUniformRandomGenerator {

    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long MASK = (1L << 48) - 1;
    private static final long ADDEND = 0xBL;
    private volatile long seed;

    public JavaUniformRandomGenerator() {
        this(System.currentTimeMillis());
    }

    public JavaUniformRandomGenerator(long seed) {
        setSeed(seed);
    }

    @Override
    public synchronized void setSeed(long seed){  // 0 <= seed < 2^48
        this.seed = (seed ^ MULTIPLIER) & MASK;
    }

    @Override
    protected synchronized int next(int bits) {  // 0 < bits <= 32
        seed = (seed * MULTIPLIER + ADDEND) & MASK;
        return (int)(seed >>> (48 - bits));
    }

    public static void main(String[] args) {
        JavaUniformRandomGenerator r = new JavaUniformRandomGenerator(12345);
        for (int i = 0; i < 10; i++) System.out.println(r.nextInt());
    }
}