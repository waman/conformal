package java_algorithm;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

/**
 * @author waman
 */
public abstract class XYDataChartCreator extends ChartCreator{

    public XYDataChartCreator(String title, String domainLabel, String rangeLabel) {
        super(title, domainLabel, rangeLabel);
    }

    @Override
    public JFreeChart createChart() {
        XYSeriesCollection dataset = new XYSeriesCollection(createData());

        JFreeChart chart = ChartFactory.createScatterPlot(
                getTitle(), getDomainLabel(), getRangeLabel(),
                dataset, PlotOrientation.VERTICAL,
                false, false, false);

        chart.getXYPlot().getRenderer().setSeriesShape(0, new Rectangle(1, 1));
        return chart;
    }

    protected abstract XYSeries createData();
}
