package java_algorithm.random.uniform;

import java_algorithm.random.AbstractRandomDataChartCreator;
import org.jfree.data.xy.XYSeries;

/**
 * @author waman
 */
public class UniformRandomDataChartCreator extends AbstractRandomDataChartCreator{

    private final UniformRandomGenerator random;

    public UniformRandomDataChartCreator(UniformRandomGenerator random, int samples){
        super(random.getClass().getSimpleName(), samples);
        this.random = random;
    }

    @Override
    protected XYSeries createData() {
        XYSeries series = new XYSeries(getTitle());
        double sum = 0.0, sum2 = 0.0;
        final int n = getSamples();

        for(int i = 0; i < n; i++){
            double d = this.random.nextDouble();
            series.add(i, d);
            sum += d;
            sum2 += d*d;
        }

        System.out.println(getTitle());
        System.out.println("平均値 : "+ sum/n );
        System.out.println("分散 : "+ (sum2 - sum*sum/n)/n);
        System.out.println();
        return series;
    }
}
