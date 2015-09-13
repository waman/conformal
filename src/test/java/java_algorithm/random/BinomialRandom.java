package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class BinomialRandom extends UniformRandomBasedRandomGenerator<Integer>{

    private final int n;
    private final double p;

    public BinomialRandom(int n, double p){
        this(System.currentTimeMillis(), n, p);
    }

    public BinomialRandom(long seed, int n, double p){
        this(new MersenneTwister(seed), n, p);
    }

    public BinomialRandom(UniformRandomGenerator uniform, int n ,double p){
        super(uniform);

        if(!(0 < p && p < 1))
            throw new IllegalArgumentException("p must be in 0 < p < 1.");

        this.n = n;
        this.p = p;
    }

    @Override
    public Integer next() {
        int r = 0;
        for (int i = 0; i < n; i++)
            if (nextUniform() < p) r++;
        return r;
    }
}