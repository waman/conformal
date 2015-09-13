package java_algorithm.geometry.curve;

/**
 *  Hilbert.java -- Hilbert (ヒルベルト) 曲線
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;
import java_algorithm.geometry.Plotter;

import java.awt.*;

public class Hilbert extends DrawOneByOnePanel {

    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    private int order;  // 位数
    private double h;  // 刻み幅
    private Plotter pt;

    public Hilbert(int order) {
        super(WIDTH + 1, HEIGHT + 1, 2);
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    private void rightUpLeft(int i) {
        if (i == 0) {
            sleep(500 / order / order);  // 表示して待つ
            return;
        }
        upRightDown(i - 1);  pt.drawRelative( h, 0);
        rightUpLeft(i - 1);  pt.drawRelative( 0, h);
        rightUpLeft(i - 1);  pt.drawRelative(-h, 0);
        downLeftUp(i - 1);
    }

    private void downLeftUp(int i) {
        if (i == 0) return;
        leftDownRight(i - 1);  pt.drawRelative( 0, -h);
        downLeftUp(i - 1);     pt.drawRelative(-h,  0);
        downLeftUp(i - 1);     pt.drawRelative( 0,  h);
        rightUpLeft(i - 1);
    }

    private void leftDownRight(int i) {
        if (i == 0) return;
        downLeftUp(i - 1);     pt.drawRelative(-h,  0);
        leftDownRight(i - 1);  pt.drawRelative( 0, -h);
        leftDownRight(i - 1);  pt.drawRelative( h,  0);
        upRightDown(i - 1);
    }

    private void upRightDown(int i) {
        if (i == 0) return;
        rightUpLeft(i - 1);    pt.drawRelative( 0,  h);
        upRightDown(i - 1);    pt.drawRelative( h,  0);
        upRightDown(i - 1);    pt.drawRelative( 0, -h);
        leftDownRight(i - 1);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        g.translate(0, HEIGHT);  g.scale(1, -1);  // 第1象限に
        h = 1;  for (int i = 2; i <= order; i++) h = h / (2 + h);
        h *= WIDTH;  // 拡大
        pt = new Plotter(g);  pt.moveTo(0, 0);  rightUpLeft(order);
    }

    public static void main(String[] args) {
        Hilbert p = new Hilbert(5);
        p.makeFrame("ヒルベルト曲線（位数 = " + p.getOrder() + "）");
    }
}