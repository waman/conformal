package java_algorithm.datatype.number.complex;

/**
 *  ComplexPrimes.java -- Gauss (ガウス) の整数
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import java.awt.*;

public class ComplexPrimes extends DrawOneByOnePanel {
    static final int N = 39;
    static final int SQRTN = (int)Math.floor(Math.sqrt(N));
    static final int H = 6;              // 正方形の一辺の長さ
    static final int WIDTH = (2 * N + 1) * H;
    static final int HEIGHT = (2 * N + 1) * H;
    private boolean[][] a = new boolean[N + 1][N + 1];

    public ComplexPrimes() {
        super(WIDTH, HEIGHT, 0);
    }

    @Override
    public void drawSlowly(Graphics2D g) {
        for (int i = 0; i <= N; i++)
            for (int j = 0; j <= N; j++)
                a[i][j] = true;
        a[0][0] = a[1][0] = a[0][1] = false;
        for (int i = 1; i <= SQRTN; i++)
            for (int j = 0; j <= i; j++)
                if (a[i][j]) {
                    int p = i,  q = j;
                    do {
                        int x = p,  y = q;
                        do {
                            a[x][y] = a[y][x] = false;
                        } while ((x -= j) >= 0 && (y += i) <= N);
                        x = p;
                        y = q;
                        do {
                            a[x][y] = a[y][x] = false;
                        } while ((x += j) <= N && (y -= i) >= 0);
                        p += i;
                        q += j;
                    } while (p <= N);
                    a[i][j] = a[j][i] = true;
                }
        g.translate(N * H, N * H);  // 原点を中心に
        for (int i = -N; i <= N; i++) {
            for (int j = -N; j <= N; j++)
                if (a[Math.abs(i)][Math.abs(j)] && i * i + j * j <= N * N)
                    g.fillRect(j * H, i * H, H, H);
            sleep(100);  // 表示して100ms待つ
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new ComplexPrimes();
        p.makeFrame("Gauss (ガウス) の整数");
    }
}