package java_algorithm.geometry;

/**
 *  Lissajous.java -- Lissajous(リサージュ)図形
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 *
 *  @version $Revision: 1.11 $, $Date: 2003/03/18 10:05:54 $
 */

import java.awt.*;

public class Lissajous extends DrawOneByOnePanel {
    static final int WIDTH = 400,  HEIGHT = WIDTH;
    static final double A = WIDTH  / 2,  B = 3,  C = 0;
    static final double D = HEIGHT / 2,  E = 5,  F = 0;

    public Lissajous() {  super(WIDTH + 1, HEIGHT + 1, 3);  }

    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(WIDTH / 2, HEIGHT / 2);  g.scale(1, -1);  // 原点を中心に
        Plotter pt = new Plotter(g);
        pt.moveTo(A * Math.cos(C), D * Math.sin(F));
        for (int i = 1; i <= 360; i++) {
            double t = Math.toRadians(i);
            pt.drawTo(A * Math.cos(B * t + C), D * Math.sin(E * t + F));
            sleep(10);  // 表示して待つ
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new Lissajous();
        p.makeFrame("Lissajous(リサージュ)図形");
    }
}