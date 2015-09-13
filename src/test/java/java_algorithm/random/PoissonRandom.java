package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class PoissonRandom extends UniformRandomBasedRandomGenerator<Integer> {

    private final double lambda;

    public PoissonRandom(double lambda) {
        this(System.currentTimeMillis(), lambda);
    }

    public PoissonRandom(long seed, double lambda) {
        this(new MersenneTwister(seed), lambda);
    }

    public PoissonRandom(UniformRandomGenerator uniform, double lambda) {
        super(uniform);
        this.lambda = lambda;
    }


    @Override
    public Integer next() {
        double a = Math.exp(this.lambda) * nextUniform();
        int k = 0;
        while (a > 1) {
            a *= nextUniform();
            k++;
        }
        return k;
    }
}