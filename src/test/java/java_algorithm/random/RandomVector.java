package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import static java.lang.Math.*;

public class RandomVector extends UniformRandomBasedRandomGenerator<double[]>{

    private final int n;

    public RandomVector(int n){
        this(System.currentTimeMillis(), n);
    }

    public RandomVector(long seed, int n){
        this(new MersenneTwister(seed), n);
    }

    public RandomVector(UniformRandomGenerator uniform, int n){
        super(uniform);
        this.n = n;
    }

    @Override
    public double[] next() {
        double[] v = new double[n];
        double r;
        do {
            r = 0;
            for (int i = 0; i < n; i++) {
                v[i] = 2 * nextUniform() - 1;
                r += v[i] * v[i];
            }
        } while (r > 1);
        r = sqrt(r);
        for (int i = 0; i < n; i++) v[i] /= r;
        return v;
    }
}