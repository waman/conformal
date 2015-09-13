package java_algorithm.geometry.curve;

/**
 *  TreeCurve.java -- 樹木曲線
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import javax.swing.*;
import java.awt.*;

public class TreeCurve extends DrawOneByOnePanel {

    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    static final double FACTOR = 0.7,  TURN = 0.5;
    private int order;  // 位数

    public TreeCurve(int order) {
        super(WIDTH + 1, HEIGHT + 1, 0);  this.order = order;
    }

    public int getOrder() {  return order;  }

    public void draw(Plotter pt, int n, double length, double angle) {
        double dx = length * Math.sin(angle);
        double dy = length * Math.cos(angle);
        pt.drawRelative(dx, dy);
        if (n > 0) {
            draw(pt, n - 1, length * FACTOR, angle + TURN);
            draw(pt, n - 1, length * FACTOR, angle - TURN);
        } else sleep(500 / order / order);  // 表示して待つ
        pt.moveRelative(-dx, -dy);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(WIDTH / 2, HEIGHT);  g.scale(1, -1);  // 原点を真中下に
        Plotter pt = new Plotter(g);
        pt.moveTo(0, 0);  draw(pt, order, WIDTH / 4, 0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("樹木曲線");
        TreeCurve p1 = new TreeCurve(10);
        TreeCurve p2 = new TreeCurve(12);
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