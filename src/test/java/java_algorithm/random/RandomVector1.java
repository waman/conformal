package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import static java.lang.Math.sqrt;

public class RandomVector1 implements RandomGenerator<double[]>{

    private final NormalRandom normal;
    private final int n;

    public RandomVector1(int n){
        this(System.currentTimeMillis(), n);
    }

    public RandomVector1(long seed, int n){
        this(new MersenneTwister(seed), n);
    }

    public RandomVector1(UniformRandomGenerator uniform, int n){
        this.normal = new NormalRandom(uniform);
        this.n = n;
    }

    @Override
    public double[] next() {
        double[] v = new double[n];
        double r = 0;
        for (int i = 0; i < n; i++) {
            v[i] = normal.next();
            r += v[i] * v[i];
        }
        r = sqrt(r);
        for (int i = 0; i < n; i++) v[i] /= r;
        return v;
    }
}