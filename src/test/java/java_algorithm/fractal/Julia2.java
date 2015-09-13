package java_algorithm.fractal;

/**
 *  Julia2.java -- Julia (ジュリア) 集合
 *  (DrawOneByOnePanel.java を使う)
 */

import java_algorithm.geometry.DrawOneByOnePanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class Julia2 extends DrawOneByOnePanel {

    static final int WIDTH = 600,  HEIGHT = 600;
    private double xMin, xMax, yMin, yMax, p, q;
    private int iterMax;
    private Color[] colors;

    private JPanel ctrlPanel = new JPanel();
    private JComboBox<String> magList = new JComboBox<>(new
        String[] {"x16", "x8", "x4", "x2", "x1", "/2", "/4", "/8", "/16"});
    private JLabel xLabel = new JLabel("x");
    private JLabel yLabel = new JLabel("y");
    private DecimalFormat df = new DecimalFormat("0.000000");

    public Julia2(double xMin, double xMax,
                  double yMin, double yMax,
                  double p, double q, int iterMax) {
        super(WIDTH + 1, HEIGHT + 1);
        this.xMin = xMin;  this.xMax = xMax;
        this.yMin = yMin;  this.yMax = yMax;
        this.p = p;  this.q = q;  this.iterMax = iterMax;
        setColors2();

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

    public JPanel getCtrlPanel() {  return ctrlPanel;  }

    private void setColors() {
        colors = new Color[16];
        for (int i = 0; i < colors.length; i++)
            colors[i] = Color.getHSBColor(1f / 16 * (i % 16), 0.5f,
                                          (float)i / colors.length);
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
        colors = new Color[48];
        for (int i = 0; i < colors.length; i++)
            colors[i] = Color.getHSBColor(0,
                                          0,
                                          (float)i / colors.length);
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
            b = 2 * a * b - q;  a = a2 - b2 - p;
        }
        return 0;
    }

    @Override
    synchronized public void drawSlowly(Graphics2D g) {
        xLabel.setText(df.format(xMin) + " < x < " + df.format(xMax)
                       + "   p = " + df.format(p));
        yLabel.setText(df.format(yMin) + " < y < " + df.format(yMax)
                       + "   q = " + df.format(q));
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
        final Julia2[] panel = {
            // 渕上季代絵著「フラクタルCGコレクション」（サイエンス社, 1987）より
            //         xMin   xMax   yMin  yMax    p      q     iterMax  階調
            new Julia2(-1.5,  1.5,  -1.5,  1.5,  -1.0,  -0.0,    100), // +5
            new Julia2(-0.5,  0.5,  -0.5 , 0.5,   0.1,   0.845,  400), // +5
            new Julia2(-0.5,  0.5,  -0.5 , 0.5,   0.04,  0.695,  400), // +5
            new Julia2(-1.0,  1.0,  -1.0 , 1.0,   0.3,   0.63,   400), // +5
            new Julia2( 0.3,  0.7,  -0.2 , 0.2,  -0.26, -0.0,    400), // +8
            new Julia2( 0.0,  1.0,   0.0 , 1.0,  -0.3,  -0.0,    400), // +5
        };
        final boolean[] started = new boolean[panel.length];

        final JTabbedPane tab = new JTabbedPane();
        for (int i = 0; i < panel.length; i++)
            tab.addTab(String.valueOf(i + 1), panel[i]);
        final JFrame frame = new JFrame("ジュリア集合");
        final Container c = frame.getContentPane();
        tab.addChangeListener(new ChangeListener() {
            @Override
                public void stateChanged(ChangeEvent e) {
                    c.remove(1);
                    int i = tab.getSelectedIndex();
                    JPanel ctrlPanel = panel[i].getCtrlPanel();
                    c.add(ctrlPanel, BorderLayout.NORTH);
                    ctrlPanel.repaint();
                    if (!started[i]) { panel[i].start();  started[i] = true; }
                }
            });

        c.add(tab);
        c.add(panel[0].getCtrlPanel(), BorderLayout.NORTH);
        frame.pack();               // 適切な大きさに
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  // サイズ変更不可
        frame.setVisible(true);     // 表示する
        tab.setSelectedIndex(1);
    }
}