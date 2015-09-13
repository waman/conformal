package java_algorithm.geometry;

/**
 *  ThreeDimGraph.java -- 3次元グラフ
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 *
 *  @version $Revision: 1.7 $, $Date: 2003/03/18 10:05:54 $
 */

import java.awt.*;

public class ThreeDimGraph extends DrawOneByOnePanel {
    /** 描く関数 */
    public interface Function {  public double of(double x, double z);  }
    static final int WIDTH = 720,  HEIGHT = 390;
    private double xMin, yMin, zMin;  // 座標の下限
    private double xMax, yMax, zMax;  // 座標の上限
    private double[] lower = new double[241];
    private double[] upper = new double[241];
    private Function f;

    public ThreeDimGraph(Function f, double xMin, double xMax,
           double yMin, double yMax, double zMin, double zMax) {
        super(WIDTH + 1, HEIGHT + 1, 0);  this.f = f;
        this.xMin = xMin;  this.yMin = yMin;  this.zMin = zMin;
        this.xMax = xMax;  this.yMax = yMax;  this.zMax = zMax;
    }

    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(0, HEIGHT);  g.scale(1, -1);  // 第1象限に
        Plotter pt = new Plotter(g);
        for (int i = 0; i <= 240; i++) {
            lower[i] = Double.POSITIVE_INFINITY;  // 正の無限大
            upper[i] = Double.NEGATIVE_INFINITY;  // 負の無限大
        }
        for (int iz = 0; iz <= 20; iz++) {
            double fz = zMin + (zMax - zMin) / 20 * iz;
            boolean wasOutside = false;
            for (int ix = 0; ix <= 200; ix++) {
                double fx = xMin + (xMax - xMin) / 200 * ix;
                int    sx = ix + 2 * (20 - iz);  // 表示ためのx座標 (0～240)
                double sy = 30 * (f.of(fx, fz) - yMin) / (yMax - yMin)
                            + 5 * iz;            // 表示ためのy座標 (0～130)
                boolean outside = false;
                if (sy < lower[sx]) {  lower[sx] = sy;  outside = true;  }
                if (sy > upper[sx]) {  upper[sx] = sy;  outside = true;  }
                if (outside && wasOutside) pt.drawTo(sx * 3, sy * 3);
                else                       pt.moveTo(sx * 3, sy * 3);
                wasOutside = outside;
            }
            sleep(100);  // 表示して100ms待つ
        }
    }

    public static void main(String[] args) {
        ThreeDimGraph.Function f = new ThreeDimGraph.Function() {
                public double of(double x, double z) {  // 描く関数の例
                    double r2 = x * x + z * z;
                    return Math.exp(-r2) * Math.cos(10 * Math.sqrt(r2));
                }
            };
        DrawOneByOnePanel p = new ThreeDimGraph(f,  -1, 1,  -1, 1,  -1 ,1);
        p.makeFrame("3次元グラフ");
    }
}