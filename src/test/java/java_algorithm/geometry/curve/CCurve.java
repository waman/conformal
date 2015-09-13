package java_algorithm.geometry.curve;

/**
 *  CCurve.java -- C曲線
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import javax.swing.*;
import java.awt.*;

public class CCurve extends DrawOneByOnePanel {

    static final int WIDTH = 400,  HEIGHT = WIDTH * 5 / 8;
    private int order;  // 位数

    public CCurve(int order) {
        super(WIDTH + 1, HEIGHT + 1, 0);  this.order = order;
    }

    public int getOrder() {
        return order;
    }

    private void draw(Plotter pt, int i, double dx, double dy) {
        if (i == 0) {
            pt.drawRelative(dx, dy);
            sleep(500 / order / order);  // 表示して待つ
            return;
        }
        draw(pt, i - 1, (dx + dy) / 2, (dy - dx) / 2);
        draw(pt, i - 1, (dx - dy) / 2, (dy + dx) / 2);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(WIDTH / 2, HEIGHT);  g.scale(1, -1);  // 原点を真中下に
        Plotter pt = new Plotter(g);
        pt.moveTo(-WIDTH / 4, WIDTH / 2);  draw(pt, order, WIDTH / 2, 0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("C 曲線");
        CCurve p1 = new CCurve(10);
        CCurve p2 = new CCurve(12);
        Container c = frame.getContentPane();

        Box b = new Box(BoxLayout.Y_AXIS);
        b.add(new JLabel("位数 = " + p1.getOrder()));
        b.add(p1);
        c.add(b, BorderLayout.WEST);

        b = new Box(BoxLayout.Y_AXIS);
        b.add(new JLabel("位数 = " + p2.getOrder()));
        b.add(p2);
        c.add(b, BorderLayout.EAST);

        frame.pack();               // 適切な大きさに
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  // サイズ変更不可
        frame.setVisible(true);     // 表示する
        p1.start();
        p2.start();
    }
}