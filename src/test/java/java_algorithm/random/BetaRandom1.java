package java_algorithm.random;

public class BetaRandom1 extends UniformRandomBasedRandomGenerator<Double>{

    private final double aInverse, bInverse;

    public BetaRandom1(double a, double b){
        this.aInverse = 1 / a;
        this.bInverse = 1 / b;
    }

    @Override
    public Double next() {
        double x, y;
        do {
            x = Math.pow(nextUniform(), aInverse);
            y = Math.pow(nextUniform(), bInverse);

        } while (x + y > 1);
        return x / (x + y);
    }
}