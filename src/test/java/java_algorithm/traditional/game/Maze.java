package java_algorithm.traditional.game;

/**
 *  Maze.java -- 迷路
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Maze extends DrawOneByOnePanel {

    static final int X_MAX = 80;  // 迷路の横の大きさ (偶数)
    static final int Y_MAX = 40;  // 迷路の縦の大きさ (偶数)
    static final int H = 10; // 通路と壁の幅
    static final int WIDTH = (X_MAX - 3) * H, HEIGHT = (Y_MAX - 3) * H;

    static final int WALL    = 0;  // 壁
    static final int PASSAGE = 1;  // 通路
    static final int VISITED = 2;  // 走査中の通路
    static final int ANSWER  = 3;  // 解

    static final Color color[] = {
        Color.BLACK,            // 壁の色 (Java v1.4未満では Color.black)
        Color.LIGHT_GRAY,       // 通路の色 (同 Color.lightGray)
        Color.GREEN.darker(),   // 走査中の通路の色 (同 Color.green.darker())
        Color.YELLOW,           // 解の色 (同 Color.yellow)
    };

    static final int DX[] = { 2, 0, -2,  0 };  // 変位ベクトル
    static final int DY[] = { 0, 2,  0, -2 };  // 変位ベクトル
    static final int DIR_TABLE[][] = {         // 方向表
        {0,1,2,3}, {0,1,3,2}, {0,2,1,3}, {0,2,3,1}, {0,3,1,2}, {0,3,2,1},
        {1,0,2,3}, {1,0,3,2}, {1,2,0,3}, {1,2,3,0}, {1,3,0,2}, {1,3,2,0},
        {2,0,1,3}, {2,0,3,1}, {2,1,0,3}, {2,1,3,0}, {2,3,0,1}, {2,3,1,0},
        {3,0,1,2}, {3,0,2,1}, {3,1,0,2}, {3,1,2,0}, {3,2,0,1}, {3,2,1,0},
    };

    private int[][] map = new int[X_MAX + 1][Y_MAX + 1]; // 地図
    private List<Point> site = new ArrayList<>();
    private Random random = new Random();
    private boolean made = false;
    private Graphics2D g;

    public Maze() {
        super(WIDTH, HEIGHT, 0);
    }

    private void addSite(int x, int y) {  // サイトに加える
        site.add(new Point(x, y));
    }

    private Point selectSite() {  // サイトを乱数で選ぶ
        if (site.isEmpty()) return null;  // サイトが尽きた
        int index = random.nextInt(site.size());
        return site.remove(index);
    }

    private int nextDir(int x, int y) {  // 空き地を探す
        int[] t = DIR_TABLE[random.nextInt(DIR_TABLE.length)];
        for (int d = 3; d >= 0; d--) {
            int dx = DX[t[d]],  dy = DY[t[d]];
            if (map[x + dx][y + dy] == PASSAGE) return t[d];
        }
        return -1;
    }

    private void setMap(int x, int y, int m) {  // 地図に記録
        map[x][y] = m;  g.setColor(color[m]);
        g.fillRect((x - 2) * H, (y - 2) * H, H, H);
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        this.g = g;
        if (!made) {  make();   made = true;  }
        else       {  solve();  made = false;  }
    }

    private void make() {
        for (int x = 0; x <= X_MAX; x++)   // 地図を初期化
            for (int y = 0; y <= Y_MAX; y++) setMap(x, y, WALL);
        for (int x = 3; x <= X_MAX - 3; x++)
            for (int y = 3; y <= Y_MAX - 3; y++) setMap(x, y, PASSAGE);
        setMap(2, 3, PASSAGE);  // 入り口と出口を作る
        setMap(X_MAX - 2, Y_MAX - 3,  PASSAGE);
        for (int x = 4; x <= X_MAX - 4; x += 2) {  // サイトを加える
            addSite(x, 2);  addSite(x, Y_MAX - 2);
        }
        for (int y = 4; y <= Y_MAX - 4; y += 2) {
            addSite(2, y);  addSite(X_MAX - 2, y);
        }
        sleep();

        Point p;  int d;
        while ((p = selectSite()) != null)  // サイトを選ぶ
            while ((d = nextDir(p.x, p.y)) >= 0) { // そこから延ばしていく
                setMap(p.x + DX[d] / 2, p.y + DY[d] / 2, WALL);
                p.x += DX[d];  p.y += DY[d];  addSite(p.x, p.y);
                setMap(p.x, p.y, WALL);  sleep(30);  // 表示してウエイト
            }
    }

    /*
      迷路を解く
      参考文献：河西朝雄著『Javaによるアルゴリズム入門』（技術評論社，2001年）
    */

    boolean scanned[][] = new boolean[X_MAX + 1][Y_MAX + 1];
    boolean success;

    private void solve() {
        success = false;
        for (int x = 0; x <= X_MAX ; x++)
            for (int y = 0; y <= Y_MAX; y++) {
                scanned[x][y] = false;  setMap(x, y, map[x][y]);
            }
        visit(X_MAX - 2, Y_MAX - 3);  // 出口から探索
        repaint();
    }

    private void visit(int i, int j) {  // 再帰的に探索する
        if (success || scanned[i][j] || map[i][j] != PASSAGE) return;
        setMap(i, j, VISITED);  sleep(10);  // 走査中の通路を表示
        scanned[i][j] = true;
        if (i == 2 && j == 3) success = true;  // 入口に到着
        visit(i, j - 1); // 上
        visit(i, j + 1); // 下
        visit(i + 1, j); // 右
        visit(i - 1, j); // 左
        if (success) {  setMap(i, j, ANSWER);  sleep(30);  }
    }

    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.makeFrame("迷路");
    }
}