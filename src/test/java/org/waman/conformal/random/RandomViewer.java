package java_algorithm.random;

import java_algorithm.ChartCreator;
import java_algorithm.random.uniform.JavaUniformRandomGenerator;
import java_algorithm.random.uniform.MersenneTwister;
import java_algorithm.random.uniform.UniformRandomDataChartCreator;
import java_algorithm.random.uniform.UniformRandomGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.ModerateSkin;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author waman
 */
public class RandomViewer {

    private static final int SAMPLES = 1000;
    private static final List<ChartCreator> CHART_CREATORS = new LinkedList<>();

    public static void main(String... args){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SubstanceLookAndFeel.setSkin(new ModerateSkin());

                JFrame frame = new JFrame("乱数");

                addCharts();

                JTabbedPane pane = new JTabbedPane();
                for(ChartCreator cc : CHART_CREATORS){
                    JFreeChart chart = cc.createChart();
                    pane.add(chart.getTitle().getText(), new ChartPanel(chart));
                }

                frame.setContentPane(pane);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private static void addCharts(){
        // Uniform random
        addRandom(new JavaUniformRandomGenerator());
        addRandom(new MersenneTwister());

        // Normal random
        addRandom(new NormalRandom1());
        addRandom(new NormalRandom2());
        addRandom(new NormalRandom());

        // Elementary
        addRandom(new ExpRandom());
        addRandom(new LogisticRandom());
        addRandom(new PowerRandom(3));
        addRandom(new PowerRandom1(3));
        addRandom(new TriangularRandom());
        addRandom(new WeibullRandom(1.5));

        // Gamma, Beta
        addRandom(new GammaRandom(1.0));
        addRandom(new GammaRandom1(1));
        addRandom(new BetaRandom(1.0, 2.0));
        addRandom(new BetaRandom1(1.0, 2.0));

        //
        addRandom(new TRandom(4));
        addRandom(new FRandom(2.0, 4.0));
        addRandom(new Chi2Random(4));
        addRandom(new Chi2Random1(4));
        addRandom(new CauchyRandom());

        // Integral
        addRandom(new BinomialRandom(10, 0.1));
        addRandom(new GeometricRandom(0.2));
        addRandom(new GeometricRandom1(0.2));
        addRandom(new PoissonRandom(5.0));
    }

    private static void addRandom(UniformRandomGenerator random){
        CHART_CREATORS.add(new UniformRandomDataChartCreator(random, SAMPLES));
    }

    private static void addRandom(RandomGenerator<? extends Number> random){
        CHART_CREATORS.add(new RandomDataChartCreator(random, SAMPLES));
    }
}
