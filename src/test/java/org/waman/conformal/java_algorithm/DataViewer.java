package java_algorithm;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;

import javax.swing.*;
import java.awt.*;

/**
 * @author waman
 */
public abstract class DataViewer extends JFrame {

    static{
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
    }

    public DataViewer(String title){
        super(title);
    }

    public void visualize(){
        JFreeChart chart = createChart();
//        chart.getXYPlot().getDomainAxis().setUpperBound(count);
//        chart.getXYPlot().getRangeAxis().setUpperBound(1.0);

        setContentPane(new ChartPanel(chart));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    protected abstract JFreeChart createChart();
    protected abstract String getChartTitle();
}
