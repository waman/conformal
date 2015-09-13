package java_algorithm.traditional.game;

/**
 *  LifeGame.java -- ライフ・ゲーム
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import java.awt.*;

public class LifeGame extends DrawOneByOnePanel {

    static final int N = 50,  M = 72;  // 盤面の大きさ
    static final int H = 8; // 正方形の一辺の長さ
    static final int WIDTH = M * H,  HEIGHT = N * H;
    static final int GENE_MAX = 1000; // 世代の最大値
    private boolean a[][] = new boolean[N + 2][M + 2];  // 盤面
    private int b[][] = new int[N + 2][M + 2];

    public LifeGame() {  super(WIDTH - 1, HEIGHT - 1, 0);  }

    private void initialize() {
        for (int i = 0; i < N + 2; i++)
            for (int j = 0; j < M + 2; j++) {
                a[i][j] = false;  b[i][j] = 0;
            }
        a[N/2][M/2] = a[N/2-1][M/2] = a[N/2+1][M/2]
            = a[N/2][M/2-1] = a[N/2-1][M/2+1] = true;  // 初期状態
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        initialize();
        for (int gene = 1; gene <= GENE_MAX; gene++) {
            g.clearRect(0, 0, WIDTH - 1, HEIGHT - 1);
            g.drawString("Generation : " + gene + "/" + GENE_MAX, 20, 20);
            for (int i = 1; i <= N; i++)
                for (int j = 1; j <= M; j++)
                    if (a[i][j]) {
                        g.fillRect(j * H - H, i * H - H, H - 1, H - 1);
                        b[i-1][j-1]++;  b[i-1][j]++;  b[i-1][j+1]++;
                        b[i  ][j-1]++;                b[i  ][j+1]++;
                        b[i+1][j-1]++;  b[i+1][j]++;  b[i+1][j+1]++;
                    }
            for (int i = 0; i <= N + 1; i++)
                for (int j = 0; j <= M + 1; j++) {
                    if (b[i][j] != 2) a[i][j] = (b[i][j] == 3);
                    b[i][j] = 0;
                }
            sleep(100);  // 表示して100ms待つ
        }
    }

    public static void main(String[] args) {
        DrawOneByOnePanel p = new LifeGame();
        p.makeFrame("ライフ・ゲーム");
    }
}