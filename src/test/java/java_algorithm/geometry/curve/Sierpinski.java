package java_algorithm.geometry.curve;

/**
 *  Sierpinski.java -- Sierpi\'{n}ski (シェルピンスキー) 曲線
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import javax.swing.*;
import java.awt.*;

public class Sierpinski extends DrawOneByOnePanel {

    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    private int order;  // 位数
    private double h;  // 刻み幅
    private Plotter pt;

    public Sierpinski(int order) {
        super(WIDTH + 1, HEIGHT + 1, 0);
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    private void upRightDown(int i) {
        if (i == 0) {
            sleep(500 / order / order);  // 表示して待つ
            return;
        }
        upRightDown(i - 1);     pt.drawRelative(h, h);
        leftUpRight(i - 1);     pt.drawRelative(2 * h, 0);
        rightDownLeft(i - 1);   pt.drawRelative(h, -h);
        upRightDown(i - 1);
    }

    private void leftUpRight(int i) {
        if (i == 0) return;
        leftUpRight(i - 1);     pt.drawRelative(-h, h);
        downLeftUp(i - 1);      pt.drawRelative(0, 2 * h);
        upRightDown(i - 1);     pt.drawRelative(h, h);
        leftUpRight(i - 1);
    }

    private void downLeftUp(int i) {
        if (i == 0) return;
        downLeftUp(i - 1);      pt.drawRelative(-h, -h);
        rightDownLeft(i - 1);   pt.drawRelative(-2 * h, 0);
        leftUpRight(i - 1);     pt.drawRelative(-h, h);
        downLeftUp(i - 1);
    }

    private void rightDownLeft(int i) {
        if (i == 0) return;
        rightDownLeft(i - 1);   pt.drawRelative(h, -h);
        upRightDown(i - 1);     pt.drawRelative(0, -2 * h);
        downLeftUp(i - 1);      pt.drawRelative(-h, -h);
        rightDownLeft(i - 1);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(0, HEIGHT);  g.scale(1, -1);  // 第1象限に
        h = 1;  for (int i = 2; i <= order; i++) h = 3 * h / (6 + h);
        h *= WIDTH / 6.0;  // 拡大
        pt = new Plotter(g);    pt.moveTo(h, 0);
        upRightDown(order);     pt.drawRelative( h,  h);
        leftUpRight(order);     pt.drawRelative(-h,  h);
        downLeftUp(order);      pt.drawRelative(-h, -h);
        rightDownLeft(order);   pt.drawRelative( h, -h);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("シェルピンスキー曲線");
        Sierpinski p1 = new Sierpinski(4);
        Sierpinski p2 = new Sierpinski(5);
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