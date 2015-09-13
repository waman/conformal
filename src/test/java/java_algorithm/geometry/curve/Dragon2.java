package java_algorithm.geometry.curve;

/**
 *  Dragon2.java -- ドラゴンカーブ（非再帰版）
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import java.awt.*;

public class Dragon2 extends DrawOneByOnePanel {

    static final int WIDTH = 640,  HEIGHT = 400;
    static final int D = 3;
    private int order;  // 位数

    public Dragon2(int order) {
        super(WIDTH, HEIGHT, 0);  this.order = order;
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(0, HEIGHT - 1);  g.scale(1, -1);  // 第1象限に
        Plotter pt = new Plotter(g);  pt.moveTo(200, 140);

        boolean[] isRight = new boolean[1 << order];
        int p = 0;  double dx = 0, dy = 2;
        pt.drawRelative(D * dx, D * dy);

        for (int k = 1; k <= order; k++) {
            isRight[p] = false;
            for (int i = 0; i <= p; i++) {
                double dx1, dy1;
                if (isRight[p - i]) {
                    isRight[p + i] = false;  dx1 = -dy;  dy1 = dx;
                } else {
                    isRight[p + i] = true;   dx1 = dy;   dy1 = -dx;
                }
                pt.drawRelative(dx + dx1, dy + dy1);
                pt.drawRelative(D * dx1, D * dy1);
                dx = dx1;  dy = dy1;
                sleep(500 / order / order);  // 表示して待つ
            }
            p = p * 2 + 1;
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new Dragon2(10);
        p.makeFrame("非再帰版ドラゴンカーブ");
    }
}