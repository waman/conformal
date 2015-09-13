package java_algorithm.statistics.distribution;

import java.applet.*;
import java.awt.*;
import java.util.StringTokenizer;

abstract public class RandomPointDistribution extends Applet {

    private static int XMAX = 640, YMAX = 400;
    private static Color PLOT_COLOR = Color.black;
    private static Color AVERAGE_COLOR = Color.red;
    private static Color STDDEV_COLOR = Color.blue;
    private static int NUM;
    private static int[] histgram;
    private static double[] xrange, values;

    //  "min:max" 形式の文字列から数値の範囲を表す配列を得るメソッド
    private static double[] parseRange(String param) {
        StringTokenizer token = new StringTokenizer(param,":");
        if (!token.hasMoreTokens()) return null;
        double min = Double.valueOf(token.nextToken());
        if (!token.hasMoreTokens()) return null;
        double max = Double.valueOf(token.nextToken());
        return new double[] {min, max};
    }

    private static void drawAxes(Graphics g) {
        g.setColor(PLOT_COLOR);
        g.drawLine(0, YMAX, XMAX, YMAX);
    }

    private void drawGraph(Graphics g) {
        g.setColor(PLOT_COLOR);
        g.drawString("now calculating...", XMAX/2 - 54, YMAX/2 - 6);
        for (int i = 0; i < XMAX; i++) histgram[i] = 0;
        double  average = 0;
        for (int i = 0; i < NUM; i++) {
            values[i] = random();  average += values[i];
        }
        average /= NUM;
        g.setColor(Color.white);
        g.drawString("now calculating...", XMAX/2 - 54, YMAX/2 - 6);
        g.setColor(PLOT_COLOR);
        double stddev = 0, xmin = xrange[0], xmax = xrange[1];
        for (int i = 0; i < NUM; i++) {
            stddev += (values[i] - average) * (values[i] - average);
            int j = (int)(XMAX * (values[i] - xmin) / (xmax - xmin));
            if (j >= 0 && j < XMAX) {
                histgram[j]++;
                g.drawLine(j, YMAX - (histgram[j]/10),
                           j, YMAX - (histgram[j]/10));
            }
        }
        stddev = Math.sqrt(stddev / (NUM - 1));
        int avx = (int)(XMAX * (average - xmin) / (xmax - xmin)),
            std = (int)(XMAX * stddev / (xmax - xmin));
        g.setColor(AVERAGE_COLOR);
        g.drawLine(avx, 0, avx, YMAX);
        g.drawString("avg = " + average, 5, 12);
        g.setColor(STDDEV_COLOR);
        g.drawLine(avx - std, 0, avx - std, YMAX);
        g.drawLine(avx + std, 0, avx + std, YMAX);
        g.drawString("std = " + stddev, 5, 28);
    }

    abstract public double random();

    @Override
    public void init() {
        Dimension d = getSize();
        XMAX = d.width;  YMAX = d.height;
        setBackground(Color.white);
        setFont(new Font("Monospaced",Font.PLAIN,12));
        NUM = XMAX * YMAX * 3 / 2;
        histgram = new int[XMAX];
        xrange = parseRange(getParameter("xrange"));
        values = new double[NUM];
    }

    @Override
    public void paint(Graphics g) {
        g.setClip(0, 0, XMAX, YMAX);  g.clearRect(0, 0, XMAX, YMAX);
        drawAxes(g);
        drawGraph(g);
    }
}