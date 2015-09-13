package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import static java.lang.Math.*;

public class WeibullRandom extends UniformRandomBasedRandomGenerator<Double>{

    private final double alpha;

    public WeibullRandom(double alpha){
        this(System.currentTimeMillis(), alpha);
    }

    public WeibullRandom(long seed, double alpha){
        this(new MersenneTwister(seed), alpha);
    }

    public WeibullRandom(UniformRandomGenerator uniform, double alpha){
        super(uniform);
        this.alpha = alpha;
    }

    @Override
    public Double next() {
        return pow(- log(1 - nextUniform()), 1 / alpha);
    }
}