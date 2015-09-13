package java_algorithm.geometry.curve;

/**
 *  KochCurve.java -- Koch(コッホ)曲線
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import java.awt.*;

public class KochCurve extends DrawOneByOnePanel {

    static final double D_MAX = 5; // 小さくすると一辺の長さが短くなる。
    static final int WIDTH = 600,  HEIGHT = 200;
    static final double[] COS = new double[6], SIN = new double[6];

    public KochCurve() {
        super(WIDTH + 1, HEIGHT + 1, 3);
        for (int i = 0; i < 6; i++) {
            COS[i] = Math.cos(i * Math.PI / 3);
            SIN[i] = Math.sin(i * Math.PI / 3);
        }
    }

    private void draw(Plotter pt, int a, double d) {
        if (d <= D_MAX) {
            pt.drawRelative(d * COS[a % 6], d * SIN[a % 6]);
            sleep((int)D_MAX);  // 表示して待つ
            return;
        }
        d /= 3;  draw(pt, a, d);  a++;  draw(pt, a, d);
        a += 4;  draw(pt, a, d);  a++;  draw(pt, a, d);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(0, HEIGHT);  g.scale(1, -1);  // 第1象限に
        Plotter pt = new Plotter(g);
        pt.moveTo(0, 0);  draw(pt, 0, WIDTH);
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new KochCurve();
        p.makeFrame("Koch(コッホ)曲線");
    }
}