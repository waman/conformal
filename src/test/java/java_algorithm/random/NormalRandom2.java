package java_algorithm.random;

import static java.lang.Math.*;

public class NormalRandom2 extends UniformRandomBasedRandomGenerator<Double> {

    private boolean parity = true;
    private double r, theta;

    @Override
    public Double next() { // 正規分布２
        if (parity) {
            parity = false;
            r = sqrt(-2 * log(1 - nextUniform()));
            theta = 2 * PI * nextUniform();
            return r * cos(theta);
        } else {
            parity = true;
            return r * sin(theta);
        }
    }
}