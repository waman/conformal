package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class FRandom implements RandomGenerator<Double>{

    private final double nu1, nu2;
    private final Chi2Random chi1;
    private final Chi2Random chi2;

    public FRandom(double nu1, double nu2){
        this(System.currentTimeMillis(), nu1, nu2);
    }

    public FRandom(long seed, double nu1, double nu2){
        this(new MersenneTwister(seed), nu1, nu2);
    }

    public FRandom(UniformRandomGenerator uniform, double nu1, double nu2){
        this.nu1 = nu1;
        this.nu2 = nu2;
        this.chi1 = new Chi2Random(uniform, nu1);
        this.chi2 = new Chi2Random(uniform, nu2);
    }

    public Double next(){
        return (chi1.next() * nu2) / (chi2.next() * nu1);
    }
}