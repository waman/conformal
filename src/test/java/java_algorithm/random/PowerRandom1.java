package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class PowerRandom1 extends UniformRandomBasedRandomGenerator<Double>{

    private final int n;

    public PowerRandom1(int n){
        this(System.currentTimeMillis(), n);
    }

    public PowerRandom1(long seed, int n){
        this(new MersenneTwister(seed), n);
    }

    public PowerRandom1(UniformRandomGenerator uniform, int n){
        super(uniform);
        this.n = n;
    }

    @Override
    public Double next() {
        double p = nextUniform(), r;
        for (int i = 0; i < n; i++)
            if ((r = nextUniform()) > p) p = r;
        return p;
    }
}