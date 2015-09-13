package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class CauchyRandom extends UniformRandomBasedRandomGenerator<Double>{

    public CauchyRandom(){
        this(System.currentTimeMillis());
    }

    public CauchyRandom(long seed){
        this(new MersenneTwister(seed));
    }

    public CauchyRandom(UniformRandomGenerator uniform){
        super(uniform);
    }

    @Override
    public Double next() {
        double x, y;
        do {
            x = 1 - nextUniform();
            y = 2 * nextUniform() - 1;
        } while (x * x + y * y > 1);
        return y / x;
    }
}