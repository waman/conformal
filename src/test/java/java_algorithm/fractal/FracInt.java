package java_algorithm.fractal;

import java.applet.Applet;
import java.awt.*;
import java.util.Random;
import java.util.StringTokenizer;

public class FracInt extends Applet {

    private int N; // 点の数
    private double[] x, y, d, a, c, e, f;

    private static double[] parseParameter(String str) {
        if (str == null) throw new IllegalArgumentException();
        StringTokenizer tkn = new StringTokenizer(str,",");
        double[] ret = new double[tkn.countTokens()];
        for (int i = 0; tkn.hasMoreTokens() && i < ret.length; i++)
            ret[i] = Double.parseDouble(tkn.nextToken());
        return ret;
    }

    @Override
    public void init() {
        Dimension dim = getSize();
        setSize(dim.width, dim.height);
        setBackground(Color.white);
        x = parseParameter(getParameter("x_coords"));
        y = parseParameter(getParameter("y_coords"));
        d = parseParameter(getParameter("afn_params"));
        N = Math.min(x.length, y.length) - 1;
        a = new double[N];  c = new double[N];
        e = new double[N];  f = new double[N];
        double q = x[N] - x[0];
        for (int i = 0; i < N; i++) {
            a[i] = (x[i + 1] - x[i]) / q; // アフィン変換を求める
            e[i] = (x[N] * x[i] - x[0] * x[i + 1]) / q;
            c[i] = (y[i + 1] - y[i] - d[i] * (y[N] - y[0])) / q;
            f[i] = (x[N] * y[i] - x[0] * y[i + 1] -
                    d[i] * (x[N] * y[0] - x[0] * y[N])) / q;
        }
    }

    @Override
    public void paint(Graphics g) {
        Dimension dim = getSize();
        g.setClip(0, 0, dim.width, dim.height);
        g.clearRect(0, 0, dim.width, dim.height);
        g.setColor(Color.black);
        Random rdm = new Random();
        double p = x[0], q = y[0];
        for (int i = 0; i < 5000; i++) { // アトラクタをプロットする
            int j = rdm.nextInt(Integer.MAX_VALUE) /
                    (Integer.MAX_VALUE / N + 1);
            q = c[j] * p + d[j] * q + f[j];
            p = a[j] * p            + e[j];
            g.drawLine((int)p, (int)q, (int)p, (int)q);
        }
    }
}