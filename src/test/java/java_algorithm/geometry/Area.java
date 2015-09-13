package java_algorithm.geometry;

/**
 * Area.java -- 面積
 */

import java.text.DecimalFormat;

public class Area {

    /** 点を表す内部クラス */
    public static class Point {
        public double x, y;
        public Point(double x, double y) {  this.x = x;  this.y = y;  }
    }

    /** 三角形 OAB の面積 */
    public static double getValue(Point a, Point b) {
        return (a.x * b.y - b.x * a.y) / 2;
    }

    /** 多角形の面積 */
    public static double getValue(Point[] p) {
        double sum = getValue(p[p.length - 1], p[0]);
        for (int i = 1; i < p.length; i++) sum += getValue(p[i - 1], p[i]);
        return sum;
    }


    /*
     * 参考用
     * 半径 1 の円に内接する正 n 角形の面積
     */
    static double areaOfRegularPolygon(int n) {
        double d = 2 * Math.PI / n;  // = 2π/n
        Area.Point[] p = new Area.Point[n];

        for (int k = 0; k < n; k++)
            p[k] = new Area.Point(Math.cos(d * k), Math.sin(d * k));
        return Area.getValue(p);
    }

    /**
     * 例題プログラムです。<br>
     * 半径1の円に内接する正n角形の面積を求めて，
     * <code>0.5*n*sin(2*pi/n)</code>と比較しています。
     */
    public static void main(String[] args) {
        Area.Point[] pt = {
            new Area.Point(1, 1), new Area.Point(3, 2),
            new Area.Point(2, 4), new Area.Point(0, 2),
        };
        System.out.println("左回りの面積 = " + Area.getValue(pt));

        int n = pt.length;
        for (int i = 0; i < n / 2; i++) {
            Area.Point swap = pt[n - 1 - i];
            pt[n - 1 - i] = pt[i];
            pt[i] = swap;
        }
        System.out.println("右回りの面積 = " + Area.getValue(pt));

        final DecimalFormat DF = new DecimalFormat(" 0.0000000000000");
        System.out.println();
        System.out.println("正 n 角形の面積");
        System.out.println(" n       getValue()     0.5*n*sin(2π/n)");
        for (n = 3; n <= 20; n++)
            System.out.println((n < 10 ? "  " : " ") + n + "  "
                               + DF.format(areaOfRegularPolygon(n)) + "  "
                               + DF.format(0.5 * n * Math.sin(2 * Math.PI / n)));
    }
}