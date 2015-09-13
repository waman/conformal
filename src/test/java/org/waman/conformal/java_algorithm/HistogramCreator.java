package java_algorithm;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author waman
 */
public abstract class HistogramCreator extends ChartCreator{

    public HistogramCreator(String title, String domainLabel, String rangeLabel) {
        super(title, domainLabel, rangeLabel);
    }

    @Override
    public JFreeChart createChart() {
        IntervalXYDataset dataset = new XYSeriesCollection(createData());
        return ChartFactory.createHistogram(
                getTitle(), getDomainLabel(), getRangeLabel(),
                dataset, PlotOrientation.VERTICAL,
                false, false, false);
    }

    protected abstract XYSeries createData();
}
