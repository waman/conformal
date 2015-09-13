package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;
import static java.lang.Math.*;

public class BinormalRandom extends UniformRandomBasedRandomGenerator<double[]>{

    private final double r;

    public BinormalRandom(double r){
        this(System.currentTimeMillis(), r);
    }

    public BinormalRandom(long seed, double r){
        this(new MersenneTwister(seed), r);
    }

    public BinormalRandom(UniformRandomGenerator uniform, double r){
        super(uniform);
        this.r = r;
    }

    @Override
    public double[] next() {
        double r1, r2, s;
        do {
            r1 = 2 * nextUniform() - 1;
            r2 = 2 * nextUniform() - 1;
            s = r1 * r1 + r2 * r2;
        } while (s > 1 || s == 0);
        s = - log(s) / s;
        r1 = sqrt((1 + r) * s) * r1;
        r2 = sqrt((1 - r) * s) * r2;
        return new double[] {r1 + r2, r1 - r2};
    }
}