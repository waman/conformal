package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class LogisticRandom extends UniformRandomBasedRandomGenerator<Double>{

    public LogisticRandom(){
        this(System.currentTimeMillis());
    }

    public LogisticRandom(long seed){
        this(new MersenneTwister(seed));
    }

    public LogisticRandom(UniformRandomGenerator uniform){
        super(uniform);
    }

    @Override
    public Double next() {
        double r = nextUniform();
        return Math.log(r / (1 - r));
    }
}