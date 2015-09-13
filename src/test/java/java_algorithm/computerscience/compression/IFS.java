package java_algorithm.computerscience.compression;

/**
 *  IFS.java -- フラクタルによる画像圧縮
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class IFS extends DrawOneByOnePanel {

    /** アフィン変換を表す内部クラス */
    public static class Affine implements Comparable {

        private final double a, b, c, d, e, f, prob;

        public Affine(double a, double b, double c, double d, double e, double f) {
            this.a = a;  this.b = b;  this.c = c; this.d = d;  this.e = e;  this.f = f;
            prob = Math.abs(a * d - b * c);
        }

        @Override
        public int compareTo(Object other) {
            double r = prob - ((Affine)other).prob;
            if (r > 0) return  1;
            if (r < 0) return -1;  else  return 0;
        }

        public double getX(double x, double y){
            return a * x + b * y + e;
        }

        public double getY(double x, double y){
            return c * x + d * y + f;
        }
    }

    private int width, height;
    private double left, bottom, right, top;
    private Affine[] table;

    public IFS(Affine[] affine, int width, int height,
               double left, double bottom, double right, double top) {
        super(width, height, 0);
        this.width = width;  this.height = height;
        this.left  = left;   this.bottom = bottom;
        this.right = right;  this.top    = top;
        table = makeTable(affine);
    }

    private Affine[] makeTable(Affine[] affines) {
        double sum = 0;  // 確率の合計
        for(Affine affine : affines) sum += affine.prob;
        java.util.Arrays.sort(affines);  // prob の小さいもの順に整列

        Affine[] table = new Affine[affines.length * 25];  // 表作成
        int r = table.length;
        for(Affine affine : affines) {
            int k = (int)Math.round(r * affine.prob / sum);
            sum -= affine.prob;
            do {  table[--r] = affine;  } while (--k > 0);
        }
        return table;
    }

    private int screenX(double x) {
        return (int)Math.round((x - left) / (right - left) * (width - 1));
    }

    private int screenY(double y) {
        return (int)Math.round((y - top) / (bottom - top) * (height - 1));
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        double x = 0,  y = 0;  // IFSのアトラクタをプロット
        Random random = new Random();
        for (int i = 0; i < 30000; i++) {
            int j = random.nextInt(table.length);
            double tmp = table[j].getX(x, y);
            y = table[j].getY(x, y);
            x = tmp;
            if (i >= 10) g.fillRect(screenX(x), screenY(y), 1, 1);
            if (i % 300 == 0) sleep(100);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("フラクタルによる画像圧縮");
        Container c = frame.getContentPane();

        Box left  = new Box(BoxLayout.Y_AXIS);
        Box right = new Box(BoxLayout.Y_AXIS);
        c.add(left,  BorderLayout.WEST);
        c.add(right, BorderLayout.EAST);

        IFS fern = new IFS(  // シダ(fern)のデータ
            new IFS.Affine[] {
                new IFS.Affine( 0,     0,     0,     0.16,  0,  0),
                new IFS.Affine( 0.85,  0.04, -0.04,  0.85,  0,  1.6),
                new IFS.Affine( 0.2,  -0.26,  0.23,  0.22,  0,  1.6),
                new IFS.Affine(-0.15,  0.28,  0.26,  0.24,  0,  0.44),
            },  400, 400,           // width, height
            -5.0, 0.0, 5.0, 10.0);  // left, bottom, right, top
        left.add(new JLabel("シダ"));
        left.add(fern);

        IFS sierpin = new IFS(  // Sierpinskiの三角形のデータ
            new IFS.Affine[] {
                new IFS.Affine(0.5, 0, 0,   0.5,   0,   0),
                new IFS.Affine(0.5, 0, 0,   0.5,   1,   0),
                new IFS.Affine(0.5, 0, 0,   0.5, 0.5, 0.5),
            },  400, 200,         // width, height
            0.0, 0.0, 2.0, 1.0);  // left, bottom, right, top
        left.add(new JLabel("Sierpinskiの三角形"));
        left.add(sierpin);

        IFS tree1 = new IFS(  // 樹木のデータ
            new IFS.Affine[] {
                new IFS.Affine(0,     0,     0,     0.5,   0,  0),
                new IFS.Affine(0.1,   0,     0,     0.1,   0,  0.2),
                new IFS.Affine(0.42, -0.42,  0.42,  0.42,  0,  0.2),
                new IFS.Affine(0.42,  0.42, -0.42,  0.42,  0,  0.2),
            },  400, 200,          // width, height
            -0.5, 0.0, 0.5, 0.5);  // left, bottom, right, top
        right.add(new JLabel("樹木"));
        right.add(tree1);

        IFS tree2 = new IFS(  // もっと現実的な樹木のデータ
            new IFS.Affine[] {
                new IFS.Affine(0.05,  0,     0,      0.6,   0,  0),
                new IFS.Affine(0.05,  0,     0,     -0.5,   0,  1),
                new IFS.Affine(0.46, -0.32,  0.39,   0.38,  0,  0.6),
                new IFS.Affine(0.47, -0.15,  0.17,   0.42,  0,  1.1),
                new IFS.Affine(0.43,  0.28, -0.25,   0.45,  0,  1),
                new IFS.Affine(0.42,  0.26, -0.35,   0.31,  0,  0.7),
            },  400, 400,          // width, height
            -1.0, 0.0, 1.0, 2.0);  // left, bottom, right, top
        right.add(new JLabel("もっと現実的な樹木"));
        right.add(tree2);

        frame.pack();               // 適切な大きさに
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  // サイズ変更不可
        frame.setVisible(true);     // 表示する

        fern.start();
        sierpin.start();
        tree1.start();
        tree2.start();
    }
}