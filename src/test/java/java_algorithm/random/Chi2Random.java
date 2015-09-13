package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class Chi2Random implements RandomGenerator<Double>{

    private final GammaRandom gamma;

    public Chi2Random(double nu) {
        this(System.currentTimeMillis(), nu);
    }

    public Chi2Random(long seed, double nu) {
        this(new MersenneTwister(seed), nu);
    }

    public Chi2Random(UniformRandomGenerator random, double nu) {
        this.gamma = new GammaRandom(random, 0.5 * nu);
    }

    @Override
    public Double next() {
        return 2 * gamma.next();
    }
}