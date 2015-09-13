package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import static java.lang.Math.*;

public class TRandom implements  RandomGenerator<Double>{

    private final double nu;
    private final TRandomInternal internal;
    private final UniformRandomGenerator uniform;
    private final NormalRandom normal;

    public TRandom(double nu){
        this(System.currentTimeMillis(), nu);
    }

    public TRandom(long seed, double nu){
        this(new MersenneTwister(seed), nu);
    }

    public TRandom(UniformRandomGenerator uniform, double nu){
        this.nu = nu;
        this.internal = (nu <= 2) ? new TRandomInternal_le2(uniform) : new TRandomInternal_gt2();
        this.uniform = uniform;
        this.normal = new NormalRandom(uniform);
    }

    @Override
    public Double next() {
        return this.internal.generate();
    }

    private static interface TRandomInternal{
        double generate();
    }

    private class TRandomInternal_le2 implements TRandomInternal{

        private final Chi2Random chi2;

        public TRandomInternal_le2(UniformRandomGenerator uniform){
            this.chi2 = new Chi2Random(uniform, nu);
        }

        @Override
        public double generate(){
            return normal.next() / sqrt(chi2.next() / nu);
        }
    }

    private class TRandomInternal_gt2 implements TRandomInternal{
        @Override
        public double generate(){
            double a, b, c;
            do {
                a = normal.next();
                b = a * a / (nu - 2);
                c = log(1 - uniform.nextDouble()) / (1 - 0.5 * nu);
            } while (exp(-b-c) > 1 - b);

            return a / sqrt((1 - 2.0 / nu) * (1 - b));
        }
    }
}