package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

public class GammaRandom1 implements RandomGenerator<Double>{

    private final int two_a;
    private final GammaRandom1Internal internal;
    private final UniformRandomGenerator uniform;
    private final NormalRandom normal;

    public GammaRandom1(int two_a){
        this.two_a = two_a;
        this.internal = (two_a % 2 == 0) ? new GammaRandom1ForEvenA() : new GammaRandom1ForOddA();
        this.uniform = new MersenneTwister();
        this.normal = new NormalRandom(this.uniform);
    }

    @Override
    public Double next() {
        return internal.generate();
    }

    private static interface GammaRandom1Internal{
        double generate();
    }

    private class GammaRandom1ForEvenA implements GammaRandom1Internal{

        @Override
        public double generate(){
            double x = 1;
            for (int i = two_a / 2; i > 0; i--) x *= uniform.nextDouble();
            x = - Math.log(x);
            return x;
        }
    }

    private class GammaRandom1ForOddA extends GammaRandom1ForEvenA{

        @Override
        public double generate(){
            double x = super.generate();
            double r = normal.next();
            x += 0.5 * r * r;
            return x;
        }
    }
}