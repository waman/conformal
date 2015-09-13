package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class GeometricRandom extends UniformRandomBasedRandomGenerator<Integer>{

    private final double p;

    public GeometricRandom(double p) {
        this(System.currentTimeMillis(), p);
    }

    public GeometricRandom(long seed, double p) {
        this(new MersenneTwister(seed), p);
    }

    public GeometricRandom(UniformRandomGenerator uniform, double p) {
        super(uniform);
        this.p = p;
    }

    @Override
    public Integer next() {
        int n = 1;
        while (nextUniform() > p) n++;
        return n;
    }
}