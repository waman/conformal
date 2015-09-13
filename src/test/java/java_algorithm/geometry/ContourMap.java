package java_algorithm.geometry;

/**
 *  ContourMap.java -- 等高線
 *  (Plotter.java を使う)
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;

public class ContourMap extends JPanel {

    /** 描く関数 */
    public interface Function {
        abstract double f(double x, double y);
        abstract double fx(double x, double y);
        abstract double fy(double x, double y);
    }

    static final int WIDTH = 400,  HEIGHT = 400;
    static final double SCALE = 10;
    static final double EPS = 0.001;  // 許容誤差 $\varepsilon$

    public ContourMap() {
        setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));
    }

    private double square(double x) {
        return x * x;
    }

    private double x, y, fx, fy, grad2;

    /** Newton法 (x,y,fx,fy,grad2 に値をセットする) */
    private void newton(Function func, double fc, double x, double y) {
        double t;
        do {
            double f = func.f(x, y);
            fx = func.fx(x, y);  fy = func.fy(x, y);
            grad2 = square(fx) + square(fy);
            if (grad2 < 1e-10) throw new Error("grad2 が小さすぎます");
            t = (fc - f) / grad2;
            x += fx * t;  y += fy * t;
        } while (t * t * grad2 > EPS * EPS);
        this.x = x;  this.y = y;
    }

    /** 等高線を描く */
    public void draw(Function func, double fc, double x0, double y0, double step) {
        Graphics2D g = (Graphics2D)getGraphics();
        g.translate(WIDTH / 2, HEIGHT / 2);  g.scale(1, -1);  // 原点を中心に
        Plotter pt = new Plotter(g);

        x = x0;  y = y0;
        for (int i = 0; ; i++) {
            newton(func, fc, x, y);  // x,y,fx,fy,grad2 に値が入る
            if (Math.abs(x) + Math.abs(y) > 1e10) return;
            if (i == 0) {
                   pt.moveTo(x * SCALE, y * SCALE);  x0 = x;  y0 = y;
            } else pt.drawTo(x * SCALE, y * SCALE);
            if (i >= 2 && square(x - x0) + square(y - y0) < square(step))
                break;
            double t = step / Math.sqrt(grad2);
            x += fy * t;  y -= fx * t;
        }
        pt.drawTo(x0 * SCALE, y0 * SCALE);
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("等高線");
        ContourMap cm = new ContourMap();
        frame.getContentPane().add(cm);
        frame.pack();               // 適切な大きさに
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  // サイズ変更不可
        frame.setVisible(true);     // 表示する

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        ContourMap.Function func = new ContourMap.Function() {
            @Override
            public double f(double x, double y) {
                return x * x + 4 * y * y;  // $f(x, y) = x^2 + 4y^2$
            }

            @Override
            public double fx(double x, double y) {
                return 2 * x;  // $x$ で微分したもの: $f_x(x, y) = 2x$
            }

            @Override
            public double fy(double x, double y) {
                return 8 * y;  // $y$ で微分したもの: $f_y(x, y) = 8y$
            }
        };

        for(;;) {
            System.out.print("f(x, y) = ");    // 等高線の関数値
            double fc = Double.parseDouble(input.readLine());
            System.out.print("initial x = ");  // $x$ の初期値
            double x = Double.parseDouble(input.readLine());
            System.out.print("initial y = ");  // $y$ の初期値
            double y = Double.parseDouble(input.readLine());
            System.out.print("step = ");       // ステップサイズ
            double step = Double.parseDouble(input.readLine());
            cm.draw(func, fc, x, y, step);
        }
    }
}