package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class ExpRandom extends UniformRandomBasedRandomGenerator<Double>{

    public ExpRandom(){
        this(System.currentTimeMillis());
    }

    public ExpRandom(long seed){
        this(new MersenneTwister(seed));
    }

    public ExpRandom(UniformRandomGenerator uniform){
        super(uniform);
    }

    @Override
    public Double next() {
        return - Math.log(1 - nextUniform());
    }
}