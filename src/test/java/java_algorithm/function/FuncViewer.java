package java_algorithm.function;

import java.applet.Applet;
import java.awt.*;
import java.util.StringTokenizer;

public abstract class FuncViewer extends Applet {

    //  描画領域のサイズ(ピクセル単位)
    private static final int XMAX = 640, YMAX = 400;
    //  プロットを連結するかどうか
    private static boolean CONNECT_PLOTS = false;
    //  プロットの色
    private static final Color PLOT_COLOR = Color.BLUE;
    //  軸の色
    private static final Color AXES_COLOR = Color.BLACK;
    //  グリッドを表示するかどうか
    private static boolean SHOW_GRID = false;
    //  グリッドの色
    private static final Color GRID_COLOR = Color.LIGHT_GRAY;
    //  グリッドの間隔の最低の値(ピクセル単位)
    private static final int MIN_GRIDSIZE = 20;    // TODO
    //  オフスクリーンバッファ
    private static Image OS_BUF;

    private static class AxisMetrics {

        int nSIZE; double dMIN, dMAX, dSIZE;
        int nUNIT; double dUNIT;
        int nLOWEST; double dLOWEST;
        int nORIGIN; double dORIGIN;

        AxisMetrics(int nsize, double[] range) {
            checkRange(range);
            nSIZE = nsize;
            dMIN = range[0]; dMAX = range[1]; dSIZE = dMAX - dMIN;
            calcUnit();
            nUNIT = (int)(nSIZE * dUNIT / dSIZE);

            if (dMIN > 0) dLOWEST = ((int)(dMIN / dUNIT) + 1) * dUNIT;
            else if (dMIN < 0) dLOWEST = ((int)(dMIN / dUNIT)) * dUNIT;
            else dLOWEST = 0.0;

            nLOWEST = (int)((dLOWEST - dMIN) * nSIZE / dSIZE);
            dORIGIN = dLOWEST;  nORIGIN = nLOWEST;

            if (dLOWEST < 0 && dMAX > 0) {
                //  範囲に 0 を含む場合交点を強制的に 0 にする
                while ((int)(nSIZE * (dORIGIN + dUNIT) / dSIZE) <= 0) {
                    dORIGIN += dUNIT;  nORIGIN += nUNIT;
                }
            }
        }

        private void calcUnit() {
            //  描画領域が小さすぎる
            if (nSIZE <= MIN_GRIDSIZE) throw new IllegalArgumentException();
            dUNIT = 1.0;  // デフォルトの大きさ=1.0
            nUNIT = (int)(nSIZE / dSIZE);
            while (nUNIT >= nSIZE) {  // 描画領域よりも大きい場合
                //  グリッドの間隔を 1/10 きざみで変更
                dUNIT /= 10; nUNIT = (int)(nSIZE * dUNIT / dSIZE);
            }
            //  適当な値を見つけられなかった(描画領域が小さすぎる)
            if (nUNIT == 0) throw new IllegalArgumentException();
            //  最低幅(=MIN_GRIDSIZE)よりも小さくなった
            while (nUNIT <= MIN_GRIDSIZE) {
                //  2 倍
                dUNIT *= 2;
                nUNIT = (int)(nSIZE * dUNIT / dSIZE);
                if (nUNIT > MIN_GRIDSIZE) break;
                //  5 倍
                dUNIT = ((dUNIT / 2) * 5);
                nUNIT = (int)(nSIZE * dUNIT / dSIZE);
                if (nUNIT > MIN_GRIDSIZE) break;
                //  10 倍
                dUNIT *= 2;
                nUNIT = (int)(nSIZE * dUNIT / dSIZE);
            }
            //  適当な値を見つけられなかった(描画領域が小さすぎる)
            if (nUNIT >= nSIZE) throw new IllegalArgumentException();
        }

        //  数値の範囲指定のチェックをするメソッド
        private static void checkRange(double[] range) {
            if (range == null || range[0] >= range[1])
                throw new IllegalArgumentException();
        }
    }

    private static AxisMetrics xmetrics = null, ymetrics = null;

    //  描画の範囲を設定するメソッド
    protected static void setXRange(int max, double[] range) {
        xmetrics = new AxisMetrics(max, range);
    }

    protected static void setYRange(int max, double[] range) {
        ymetrics = new AxisMetrics(max, range);
    }

    //  "min:max" 形式の文字列から数値の範囲を表す配列を得るメソッド
    private static double[] parseRange(String param) {
        StringTokenizer token = new StringTokenizer(param,":");
        if (!token.hasMoreTokens()) return null;
        double min = Double.valueOf(token.nextToken());
        if (!token.hasMoreTokens()) return null;
        double max = Double.valueOf(token.nextToken());
        return new double[] {min, max};
    }

    //  軸・グリッドを描画するメソッド
    private static void drawAxes(Graphics g) {
        int nyorig = ymetrics.nSIZE - ymetrics.nORIGIN;
        double dlabel = xmetrics.dLOWEST;
        //  draw horizontal axis' labels
        g.setColor(AXES_COLOR);
        for (int pos = xmetrics.nLOWEST;
             pos <= xmetrics.nSIZE;
             pos += xmetrics.nUNIT) {
            if (SHOW_GRID) {
                g.setColor(GRID_COLOR);
                g.drawLine(pos, 0, pos, ymetrics.nSIZE);
                g.setColor(AXES_COLOR);
            }
            g.drawLine(pos, nyorig - 4, pos, nyorig + 4);
            g.drawString(Double.toString(dlabel), pos + 2, nyorig - 8);
            dlabel += xmetrics.dUNIT;
        }
        dlabel = ymetrics.dLOWEST;
        //  draw vertical axis' labels
        for (int pos = ymetrics.nSIZE - ymetrics.nLOWEST;
             pos >= 0;
             pos -= ymetrics.nUNIT) {
            if (SHOW_GRID) {
                g.setColor(GRID_COLOR);
                g.drawLine(0, pos, xmetrics.nSIZE, pos);
                g.setColor(AXES_COLOR);
            }
            g.drawLine(xmetrics.nORIGIN - 4, pos,
                       xmetrics.nORIGIN + 4, pos);
            g.drawString(Double.toString(dlabel),
                         xmetrics.nORIGIN + 8, pos - 4);
            dlabel += ymetrics.dUNIT;
        }
        g.drawLine(0, nyorig, xmetrics.nSIZE, nyorig);
        g.drawLine(xmetrics.nORIGIN, 0, xmetrics.nORIGIN, ymetrics.nSIZE);
    }

    private void drawGraph(Graphics g) {
        g.drawString(getName(), 5, 12);
        g.setColor(PLOT_COLOR);
        int[] prev_pos = null;
        for (int xpos = 0; xpos <= xmetrics.nSIZE; xpos++) {
            double  x = xmetrics.dMIN
                      + xpos * xmetrics.dSIZE / xmetrics.nSIZE,
                    y = getValue(x);
            if (Double.isNaN(y)) continue;
            int ypos = ymetrics.nSIZE
                     - (int)(ymetrics.nSIZE
                             * (y-ymetrics.dMIN) / ymetrics.dSIZE);
            if (CONNECT_PLOTS) {
                if (prev_pos != null) {
                    if ((ypos >= 0 && ypos <= ymetrics.nSIZE) ||
                        (prev_pos[1] >= 0 && prev_pos[1] <= ymetrics.nSIZE))
                        g.drawLine(xpos, ypos, prev_pos[0], prev_pos[1]);
                    prev_pos[0] = xpos; prev_pos[1] = ypos;
                } else {
                    prev_pos = new int[] {xpos, ypos};
                }
            } else {
                if (ypos >=0 && ypos <= ymetrics.nSIZE)
                    g.drawLine(xpos, ypos, xpos, ypos);
            }
        }
    }

    //  アプレットの初期化メソッド
    @Override
    public void init() {
        Dimension d = getSize();
        setSize(d.width,d.height);
        setXRange(d.width,  parseRange(getParameter("xrange")));
        setYRange(d.height, parseRange(getParameter("yrange")));
        setBackground(Color.white);
        setFont(new Font("Monospaced",Font.PLAIN,12));
        CONNECT_PLOTS = (getParameter("connect_plots") != null);
        SHOW_GRID = (getParameter("show_grid") != null);

        //  オフスクリーンバッファの生成
        OS_BUF = this.createImage(d.width, d.height);

        //  オフスクリーンバッファへの描画
        Graphics g = OS_BUF.getGraphics();
        //  軸・グリッド線の描画
        drawAxes(g);
        //  グラフの描画
        drawGraph(g);
    }

    //  アプレットの描画メソッド
    @Override
    public void paint(Graphics g) {
        //  オフスクリーンバッファの内容を画面に描画
        g.drawImage(OS_BUF, 0, 0, null);
    }

    //  関数の名前を取得する仮想基底メソッド
    @Override
    abstract public String getName();

    //  関数値を取得する仮想基底メソッド
    abstract public double getValue(double x);
}