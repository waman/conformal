package java_algorithm.geometry.curve;

/**
 *  Dragon.java -- ドラゴンカーブ（再帰版）
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import javax.swing.*;
import java.awt.*;

public class Dragon extends DrawOneByOnePanel {

    static final int WIDTH = 400,  HEIGHT = 300;
    private int order;  // 位数

    public Dragon(int order) {
        super(WIDTH, HEIGHT, 0);  this.order = order;
    }

    public int getOrder() {  return order;  }

    private void draw(Plotter pt, int i, double dx, double dy, int sign) {
        if (i == 0) {
            pt.drawRelative(dx, dy);
            sleep(500 / order / order);  // 表示して待つ
            return;
        }
        draw(pt, i - 1, (dx - sign * dy) / 2, (dy + sign * dx) / 2,  1);
        draw(pt, i - 1, (dx + sign * dy) / 2, (dy - sign * dx) / 2, -1);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(0, HEIGHT - 1);  g.scale(1, -1);  // 第1象限に
        Plotter pt = new Plotter(g);
        pt.moveTo(100, 100);  draw(pt, order, 200, 0, 1);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ドラゴンカーブ");
        Dragon p1 = new Dragon(10);
        Dragon p2 = new Dragon(12);
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