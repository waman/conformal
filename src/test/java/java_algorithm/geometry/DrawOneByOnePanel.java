package java_algorithm.geometry;

/**
 *  DrawOneByOnePanel.java -- 逐次的な描画を実現するパネル
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;

public abstract class DrawOneByOnePanel
        extends JPanel implements Runnable {

    private int width, height, border;
    protected BufferedImage offScreen = null;
    protected volatile Thread thread = null;
    protected boolean threadSuspended = false;
    protected MouseAdapter mouseRestart;

    /** 描画領域の大きさを指定（縁飾りなし） */
    public DrawOneByOnePanel(int width, int height) {
        this(width, height, -2);
    }

    /** 描画領域と周囲の余白の大きさを指定（縁飾りあり） */
    public DrawOneByOnePanel(int width, int height, int space) {
        this.width = width;  this.height = height;  border = space + 2;
        setBorder(new EtchedBorder());  // 2ラインの縁飾りをつける
        setPreferredSize(new Dimension(width  + border * 2, height + border * 2));
        mouseRestart = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (thread == null) start();
                else threadSuspended = !threadSuspended;
            }
        };
        addMouseListener(mouseRestart);
    }

    /** 描画開始 */
    public void start() {
        threadSuspended = false;
        thread = new Thread(this);  thread.start();  // run() を呼び出す
    }

    /** 描画中断 */
    public void stop() {  thread = null;  }

    @Override
    public void run() {  // start() から別スレッドで呼び出される
        offScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = offScreen.createGraphics();
        g.setColor(getForeground());
        g.setBackground(getBackground());
        g.clearRect(0, 0, width, height);
        drawSlowly(g);  repaint();  stop();
    }

    @Override
    public void paint(Graphics g) {  // repaint() から呼び出される
        if (border > 0) super.paint(g);
        if (offScreen != null)
            g.drawImage(offScreen, border, border, this);  // 実画面に表示
    }

    /** バッファに描画（sleep() を使って時間調節する）*/
    public abstract void drawSlowly(Graphics2D g);  // サブクラスで実装する

    private int wait = 0;  // 累積待ち時間
    static final int WAIT_MIN = 100;

    /** 実画面に表示して時間待ち */
    public boolean sleep() {
        return sleep(WAIT_MIN);
    }

    /** 実画面に表示して時間待ち（累積して WAIT_MIN ミリ秒以上になれば）*/
    public boolean sleep(int msec) {
        wait += msec;  if (wait < WAIT_MIN) return true;
        repaint();  // paint(g) を呼び出す
        do {
            Thread t = Thread.currentThread();
            if (thread != t) return false;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                return false;
            }
        } while (threadSuspended);
        wait = 0;  return true;
    }
    // 参照 http://java.sun.com/j2se/1.4/ja/docs/ja/guide/misc/threadPrimitiveDeprecation.html

    /** パネルひとつだけのフレームを作る */
    public void makeFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.getContentPane().add(this);
        frame.pack();               // 適切な大きさに
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);  // サイズ変更不可
        frame.setVisible(true);     // 表示する
        start();
    }
}