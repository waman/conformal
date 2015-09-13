package java_algorithm.random;

import java_algorithm.XYDataChartCreator;

/**
 * @author waman
 */
public abstract class AbstractRandomDataChartCreator extends XYDataChartCreator{

    private final int samples;

    public AbstractRandomDataChartCreator(String title, int samples) {
        super(title, "番号", "乱数");
        this.samples = samples;
    }

    protected int getSamples(){
        return this.samples;
    }
}
