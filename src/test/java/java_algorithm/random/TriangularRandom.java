package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class TriangularRandom extends UniformRandomBasedRandomGenerator<Double>{

    public TriangularRandom(){
        this(System.currentTimeMillis());
    }

    public TriangularRandom(long seed){
        this(new MersenneTwister(seed));
    }

    public TriangularRandom(UniformRandomGenerator uniform){
        super(uniform);
    }

    @Override
    public Double next() {
        return nextUniform() - nextUniform();
    }
}