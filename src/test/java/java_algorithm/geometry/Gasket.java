package java_algorithm.geometry;

/**
 *  Gasket.java -- Sierpi\'{n}ski (シェルピンスキー) の三角形
 *  (DrawOneByOnePanel.java, Plotter.java を使う)
 *
 *  @version $Revision: 1.13 $, $Date: 2003/03/18 10:05:53 $
 */

import java.awt.*;

public class Gasket extends DrawOneByOnePanel {
    static final int N = 65,  SCALE = 5;
    static final int WIDTH = 2 * N * SCALE,  HEIGHT = (N + 1) * SCALE;

    public Gasket() {  super(WIDTH + 1, HEIGHT + 1, 0);  }

    private void drawTriangle(Plotter pt, int x, int y) {
        pt.moveTo(x, y);  pt.drawTo(x - SCALE, y + SCALE);
        pt.drawTo(x + SCALE, y + SCALE);  pt.drawTo(x, y);
    }

    synchronized public void drawSlowly(Graphics2D g) {
        Plotter pt = new Plotter(g);
        boolean[] a = new boolean[2 * N + 1];
        boolean[] b = new boolean[2 * N + 1];  // false に初期化されている
        a[N] = true;
        for (int j = 1; j < N; j++) {
            for (int i = N - j; i <= N + j; i++)
                if (a[i]) drawTriangle(pt, i * SCALE, j * SCALE);
            for (int i = N - j; i <= N + j; i++)
                b[i] = (a[i - 1] != a[i + 1]);
            for (int i = N - j; i <= N + j; i++) a[i] = b[i];
            sleep(100);  // 表示して100ms待つ
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new Gasket();
        p.makeFrame("シェルピンスキーの三角形");
    }
}