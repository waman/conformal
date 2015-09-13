package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import static java.lang.Math.*;

public class GeometricRandom1 extends UniformRandomBasedRandomGenerator<Integer>{

    private final double p;

    public GeometricRandom1(double p) {
        this(System.currentTimeMillis(), p);
    }

    public GeometricRandom1(long seed, double p) {
        this(new MersenneTwister(seed), p);
    }

    public GeometricRandom1(UniformRandomGenerator uniform, double p) {
        super(uniform);
        this.p = p;
    }

    @Override
    public Integer next() {
        return (int)ceil(log(1 - nextUniform()) / log(1 - p));
    }
}