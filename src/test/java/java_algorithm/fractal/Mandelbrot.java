package java_algorithm.fractal;

/**
 *  Mandelbrot.java -- Mandelbrot (マンデルブロート) 集合
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class Mandelbrot extends DrawOneByOnePanel {

    static final int WIDTH = 600,  HEIGHT = 480;
    private double xMin, xMax, yMin, yMax;
    private int iterMax;
    private Color[] colors;

    private JPanel ctrlPanel = new JPanel();
    private JComboBox<String> magList = new JComboBox<>(
            new String[] {"x16", "x8", "x4", "x2", "x1", "/2", "/4", "/8", "/16"});
    private JLabel xLabel = new JLabel("x");
    private JLabel yLabel = new JLabel("y");
    private DecimalFormat df = new DecimalFormat("0.000000");

    public Mandelbrot(double xMin, double xMax, double yMin, double yMax, int iterMax) {
        super(WIDTH + 1, HEIGHT + 1);
        this.xMin = xMin;  this.xMax = xMax;
        this.yMin = yMin;  this.yMax = yMax;  this.iterMax = iterMax;
        setColors();
        //setGrayColors();

        removeMouseListener(mouseRestart);  // super() で登録したものを削除
        addMouseListener(new MouseEnlarge());

        magList.setSelectedIndex(3);
        ctrlPanel.add(magList);

        xLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        yLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(xLabel);  box.add(yLabel);
        ctrlPanel.add(box);
    }

    public JPanel getCtrlPanel() {
        return ctrlPanel;
    }

    private void setColors() {
        colors = new Color[iterMax + 1];
        colors[0] = Color.BLACK;
        for (int i = 1; i < colors.length; i++) {
            double h = (double)i / colors.length;
            colors[i] = Color.getHSBColor(
                    1f / 16 * (i % 16),
                    0.5f, 1f - (float)Math.pow(h, 12));
        }
    }

    private void setColors2() {
        int grad = 5;
        colors = new Color[grad * grad * grad];
        int index = 0;
        for (int r = 0; r < grad; r++)
            for (int g = 0; g < grad; g++)
                for (int b = 0; b < grad; b++)
                    colors[index++] = new Color(255 * r / (grad - 1),
                                                255 * g / (grad - 1),
                                                255 * b / (grad - 1));
    }

    private void setMonoColors() {
        colors = new Color[iterMax + 1];
        colors[0] = Color.BLACK;
        for (int i = 1; i <= iterMax; i++) colors[i] = Color.WHITE;
    }

    private void setGrayColors() {
        colors = new Color[iterMax + 1];
        colors[0] = Color.BLACK;
        for (int i = 1; i < colors.length; i++) {
            double h = (double)i / colors.length;
            colors[i] = Color.getHSBColor(0, 0, 1f - (float)Math.pow(h, 12));
        }
    }

    private class MouseEnlarge extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (thread != null) return;
            synchronized(this) {  // クリックした点を中心に mag 倍に拡大
                double mag = Math.pow(2, 4 - magList.getSelectedIndex());
                double dx = xMax - xMin;
                double dy = yMax - yMin;
                xMin -= dx * (0.5 / mag - (double)e.getX() / WIDTH);
                yMax += dy * (0.5 / mag - (double)e.getY() / HEIGHT);
                xMax = xMin + dx / mag;
                yMin = yMax - dy / mag;
            }
            start();
        }
    }

    private int getCount(double x, double y) {
        double a = x,  b = y;
        for (int i = iterMax; i > 0; i--) {
            double a2 = a * a,  b2 = b * b;
            if (a2 + b2 > 4) return i;
            b = 2 * a * b - y;  a = a2 - b2 - x;
        }
        return 0;
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        xLabel.setText(df.format(xMin) + " < x < " + df.format(xMax));
        yLabel.setText(df.format(yMin) + " < y < " + df.format(yMax));
        for (int i = 0; i <= WIDTH; i++) {
            double x = xMin + (xMax - xMin) * i / WIDTH;
            for (int j = 0; j <= HEIGHT; j++) {
                double y = yMax + (yMin - yMax) * j / HEIGHT;
                g.setColor(colors[getCount(x, y) % colors.length]);
                g.fillRect(i, j, 1, 1);  // 点を描く
            }
            sleep(1);  // 表示して待つ
        }
    }

    public static void main(String[] args) {
        Mandelbrot p = new Mandelbrot(-0.9, 2.1, -1.2, 1.2, 120);  // 600 x 480
        //Mandelbrot p = new Mandelbrot(0.6, 1.2, 0, 0.4, 120);  // 600 x 400
        JFrame frame = new JFrame("マンデルブロート集合");
        Container c = frame.getContentPane();
        c.add(p.getCtrlPanel(), BorderLayout.NORTH);
        c.add(p);
        frame.pack();               // 適切な大きさに
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  // サイズ変更不可
        frame.setVisible(true);     // 表示する
        p.start();
    }
}