package java_algorithm.random;

public class NormalRandom1 extends UniformRandomBasedRandomGenerator<Double> {

    @Override
    public Double next() { // 正規分布１
        double d = 0.0;
        for(int i = 0; i < 12; i++){
            d += nextUniform();
        }
        return d - 6;
    }
}