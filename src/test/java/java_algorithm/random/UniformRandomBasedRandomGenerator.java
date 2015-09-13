package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public abstract class UniformRandomBasedRandomGenerator<N>
        implements RandomGenerator<N>{

    private final UniformRandomGenerator uniform;

    public UniformRandomBasedRandomGenerator(){
        this(new MersenneTwister());
    }

    public UniformRandomBasedRandomGenerator(UniformRandomGenerator uniform){
        this.uniform = uniform;
    }

    protected UniformRandomGenerator getUniform(){
        return this.uniform;
    }

    protected double nextUniform(){
        return this.uniform.nextDouble();
    }
}
