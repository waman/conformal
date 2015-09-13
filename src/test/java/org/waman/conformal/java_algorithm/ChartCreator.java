package java_algorithm;

import org.jfree.chart.JFreeChart;

/**
 * @author waman
 */
public abstract class ChartCreator {

    private final String title;
    private final String domainLabel;
    private final String rangeLabel;

    public ChartCreator(String title, String domainLabel, String rangeLabel){
        this.title = title;
        this.domainLabel = domainLabel;
        this.rangeLabel = rangeLabel;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDomainLabel() {
        return this.domainLabel;
    }

    public String getRangeLabel() {
        return this.rangeLabel;
    }

    public abstract JFreeChart createChart();
}
