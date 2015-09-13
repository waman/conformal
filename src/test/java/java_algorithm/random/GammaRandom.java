package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import static java.lang.Math.*;

public class GammaRandom extends UniformRandomBasedRandomGenerator<Double> {

    private final double a;

    public GammaRandom(double a) {
        this(System.currentTimeMillis(), a);
    }

    public GammaRandom(long seed, double a) {
        this(new MersenneTwister(seed), a);
    }

    public GammaRandom(UniformRandomGenerator uniform, double a) {
        super(uniform);
        this.a = a;
    }

    public double getA(){
        return this.a;
    }

    @Override
    public Double next() { // ガンマ分布の乱数，a > 0
        double x;
        if (a > 1) {
            double t = sqrt(2 * a - 1), u, y;
            do {
                do {
                    do {
                        x = 1 - nextUniform();
                        y = 2 * nextUniform() - 1;
                    } while (x * x + y * y > 1);

                    y /= x;
                    x = t * y + a - 1;
                } while (x <= 0);
                u = (a - 1) * log(x / (a - 1)) - t * y;
            } while (u < -50 || nextUniform() > (1 + y * y) * exp(u));

        } else {
            double t = E / (a + E), y;
            do {
                if (nextUniform() < t) {
                    x = Math.pow(nextUniform(), 1 / a);
                    y = exp(-x);
                } else {
                    x = 1 - log(nextUniform());
                    y = pow(x, a - 1);
                }
            } while (nextUniform() >= y);
        }
        return x;
    }
}