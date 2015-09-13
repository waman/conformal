package java_algorithm.geometry;

/**
 *  Plotter.java -- プロッタシミュレーション
 *
 *  @version $Revision: 1.9 $, $Date: 2003/03/11 13:45:36 $
 */

import java.awt.*;
import java.awt.geom.Line2D;

public class Plotter {
    private Graphics2D g;
    private double x = 0, y = 0;  // ペンの現在位置
    private Line2D.Double line = new Line2D.Double();  // 実数値座標の直線

    /** プロットする Graphics2D を登録 */
    public Plotter(Graphics2D g) {  this.g = g;  }

    /** ペンの太さを設定 */
    public void setLineWidth(double lineWidth) {
        g.setStroke(new BasicStroke((float)lineWidth));
    }

    /** ペンアップで移動 */
    public void moveTo(double x, double y) {
        this.x = x;  this.y = y;
    }

    /** ペンアップで移動(相対座標) */
    public void moveRelative(double dx, double dy) {
        moveTo(x + dx, y + dy);
    }

    /** ペンダウンで移動 */
    public void drawTo(double x, double y) {
        line.setLine(this.x, this.y, x, y);  g.draw(line);
        this.x = x;  this.y = y;
    }

    /** ペンダウンで移動(相対座標) */
    public void drawRelative(double dx, double dy) {
        drawTo(x + dx, y + dy);
    }
}