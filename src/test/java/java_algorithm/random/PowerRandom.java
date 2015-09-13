package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class PowerRandom extends UniformRandomBasedRandomGenerator<Double>{

    private final int n;

    public PowerRandom(int n){
        this(System.currentTimeMillis(), n);
    }

    public PowerRandom(long seed, int n){
        this(new MersenneTwister(seed), n);
    }

    public PowerRandom(UniformRandomGenerator uniform, int n){
        super(uniform);
        this.n = n;
    }

    @Override
    public Double next() {
      return Math.pow(nextUniform(), 1.0 / (n + 1));
    }
}