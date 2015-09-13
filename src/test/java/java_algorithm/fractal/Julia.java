package java_algorithm.fractal;

/**
 *  Julia.java -- Julia (ジュリア) 集合：z^3 - 1 = 0
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import java.awt.*;

public class Julia extends DrawOneByOnePanel {

    static final int WIDTH = 640,  HEIGHT = 400;
    static final double C = 4.0 / WIDTH;
    static final double YA = Math.sqrt(3) / 2;
    static final double THRESHOLD = 0.05 * 0.05;

    public Julia() {
        super(WIDTH + 1, HEIGHT + 1);
    }

    private double dist2(double x, double y) {
        return x * x + y * y;
    }

    private void dot(Graphics2D g, int x, int y, Color c) {
        g.setColor(c);                             // 画面の中心を原点として
        g.fillRect(WIDTH / 2 + x, HEIGHT / 2 - y, 1, 1);  // (x,y)に点を打つ
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        for (int i = -WIDTH / 2; i <= WIDTH / 2; i++) {
            for (int j = 0; j <= HEIGHT / 2; j++) {
                double x = C * i, y = C * j;
                for (int k = 0; k <= 15; k++) {
                    double x2 = x * x, y2 = y * y, d = x2 + y2;
                    if (d < 1E-10) break;
                    d *= d;
                    double t = (1.0 / 3.0) * (2 * x + (x2 - y2) / d);
                    y = (2.0 / 3.0) * y * (1 - x / d);  x = t;
                    if (dist2(x - 1, y) < THRESHOLD) {
                        dot(g, i, +j, Color.RED);  // Java v1.4未満では
                        dot(g, i, -j, Color.RED);  // Color.red などにする
                        break;
                    }
                    if (dist2(x + 0.5, y + YA) < THRESHOLD) {
                        dot(g, i, +j, Color.GREEN);
                        dot(g, i, -j, Color.BLUE);
                        break;
                    }
                    if (dist2(x + 0.5, y - YA) < THRESHOLD) {
                        dot(g, i, +j, Color.BLUE);
                        dot(g, i, -j, Color.GREEN);
                        break;
                    }
                }
            }
            sleep(1);  // 表示して待つ
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new Julia();
        p.makeFrame("Julia (ジュリア) 集合： z^3 - 1 = 0");
    }
}