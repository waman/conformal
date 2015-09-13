package java_algorithm.random.uniform;

/**
 * @author waman
 */
public interface UniformRandomGenerator {

    void setSeed(long seed);

    int nextInt();
    int nextInt(int n);
    long nextLong();

    float nextFloat();
    double nextDouble();
}
