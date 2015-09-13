package java_algorithm.random;

public class Chi2Random1 implements RandomGenerator<Double>{

    private final int nu;
    private final NormalRandom normal;

    public Chi2Random1(int nu){
        this.nu = nu;
        this.normal = new NormalRandom();
    }

    @Override
    public Double next(){
        double s = 0;
        for (int i = 0; i < nu; i++) {
            double t = normal.next();
            s += t * t;
        }
        return s;
    }
}