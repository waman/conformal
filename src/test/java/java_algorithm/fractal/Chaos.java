package java_algorithm.fractal;

/**
 *  Chaos.java -- -- カオスとアトラクタ
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import java.awt.*;
import java.awt.event.*;

public class Chaos extends DrawOneByOnePanel {

    static final int WIDTH = 640,  HEIGHT = 400;
    private double kMin, kMax, pMin, pMax;

    public Chaos(double kMin, double kMax, double pMin, double pMax) {
        super(WIDTH + 1, HEIGHT + 1, 0);
        this.kMin = kMin;  this.kMax = kMax;
        this.pMin = pMin;  this.pMax = pMax;
        removeMouseListener(mouseRestart);  // super() で登録したものを削除
        addMouseListener(new MouseEnlarge());
    }

    private class MouseEnlarge extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (thread != null) return;
            synchronized(this) {  // クリックした点を中心に２倍に拡大
                double dx = kMax - kMin;
                double dy = pMax - pMin;
                kMin -= dx * (0.25 - (double)e.getX() / WIDTH);
                pMax += dy * (0.25 - (double)e.getY() / HEIGHT);
                kMax = kMin + dx / 2;
                pMin = pMax - dy / 2;
            }
            start();
        }
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        for (int ik = 0; ik <= WIDTH; ik++) {
            double k = kMin + (kMax - kMin) * ik / WIDTH;
            double p = 0.3;
            for (int i =  1; i <=  50; i++) p += k * p * (1 - p);
            for (int i = 51; i <= 100; i++) {
                int ip = (int)((p - pMax) / (pMin - pMax) * HEIGHT + 0.5);
                g.fillRect(ik, ip, 1, 1);  // 点を描く
                p += k * p * (1 - p);
            }
            sleep(3);  // 表示して待つ
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new Chaos(1.5, 3, 0, 1.5);
        p.makeFrame("カオスとアトラクタ");
    }
}