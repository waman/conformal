package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class BetaRandom implements RandomGenerator<Double>{

    private final GammaRandom aGamma, bGamma;

    public BetaRandom(double a, double b){
        this(System.currentTimeMillis(), a, b);
    }

    public BetaRandom(long seed, double a, double b){
        this(new MersenneTwister(seed), a, b);
    }

    public BetaRandom(UniformRandomGenerator uniform, double a, double b){
        this.aGamma = new GammaRandom(uniform, a);
        this.bGamma = new GammaRandom(uniform, b);
    }

    @Override
    public Double next() {
        double temp = aGamma.next();
        return temp / (temp + bGamma.next());
    }
}