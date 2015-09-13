package java_algorithm.random;

import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomGenerator;

import  static java.lang.Math.*;

public class NormalRandom extends UniformRandomBasedRandomGenerator<Double> {

    private boolean sw = true;
    private double y, s;

    public NormalRandom() {
        this(System.currentTimeMillis());
    }

    public NormalRandom(long seed) {
        this(new MersenneTwister(seed));
    }

    public NormalRandom(UniformRandomGenerator uniform) {
        super(uniform);
    }

    @Override
    public Double next() { // 正規分布３
        if (sw) {
            sw = false;
            double x;
            do {
                x = 2 * nextUniform() - 1;
                y = 2 * nextUniform() - 1;
                s = x * x + y * y;
            } while (s >= 1 || s == 0);  // s == 0 のチェックは用心のため [2003-06-07 s > 1 を s >=1 に訂正]
            s = sqrt(-2 * log(s) / s);
            return x * s;

        } else {
            sw = true;
            return y * s;
        }
    }
}