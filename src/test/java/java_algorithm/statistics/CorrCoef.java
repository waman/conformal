package java_algorithm.statistics;

/**
* CorrCoef.java -- 相関係数
* テスト用に class StatData を使う。
* float → double にすれば３つの方法は一致する。
*/

/**
* 単精度実数 float を使って相関係数を3通りの方法で求めます。
* float → double にすれば3つの方法は一致します。
*/

public class CorrCoef {

    static void method1(float x[], float y[]) {   // \comment{方法1}
        int n = x.length;
        float sx =0, sy = 0, sxx = 0, syy = 0, sxy = 0;

        for (int i = 0; i < n; i++) {
            sx += x[i];  sy += y[i];
        }
        sx /= n;  sy /= n;
        for (int i = 0; i < n; i++) {
            float dx = x[i] - sx;  float dy = y[i] - sy;
            sxx += dx * dx;  syy += dy * dy;  sxy += dx * dy;
        }
        sxx = (float)Math.sqrt(sxx / (n - 1));
        syy = (float)Math.sqrt(syy / (n - 1));
        sxy /= (n - 1) * sxx * syy;
        System.out.println("標準偏差 " + sxx + " " + syy + "  相関係数 " + sxy);
    }

    static void method2(float x[], float y[]) {   // \comment{方法2}
        int n = x.length;
        float sx =0, sy = 0, sxx = 0, syy = 0, sxy = 0;

        for (int i = 0; i < n; i++) {
            sx += x[i];  sy += y[i];
            sxx += x[i] * x[i];
            syy += y[i] * y[i];
            sxy += x[i] * y[i];
        }
        sx /= n;  sxx = (sxx - n * sx * sx) / (n - 1);
        sy /= n;  syy = (syy - n * sy * sy) / (n - 1);
        if (sxx > 0) sxx = (float)Math.sqrt(sxx);  else sxx = 0;
        if (syy > 0) syy = (float)Math.sqrt(syy);  else syy = 0;
        sxy = (sxy - n * sx * sy) / ((n - 1) * sxx * syy);
        System.out.println("標準偏差 " + sxx + " " + syy + "  相関係数 " + sxy);
    }

    static void method3(float x[], float y[]) {   // \comment{方法3}
        int n = x.length;
        float sx =0, sy = 0, sxx = 0, syy = 0, sxy = 0;

        for (int i = 0; i < n; i++) {
            float dx = x[i] - sx;  sx += dx / (i + 1);
            float dy = y[i] - sy;  sy += dy / (i + 1);
            sxx += i * dx * dx / (i + 1);
            syy += i * dy * dy / (i + 1);
            sxy += i * dx * dy / (i + 1);
        }
        sxx = (float)Math.sqrt(sxx / (n - 1));
        syy = (float)Math.sqrt(syy / (n - 1));
        sxy /= (n - 1) * sxx * syy;
        System.out.println("標準偏差 " + sxx + " " + syy + "  相関係数 " + sxy);
    }

    /**
    * テスト用のプログラムです。<br>
    * 使用方法 ： <code>java CorrCoef corrcoef.dat</code>
    * @see StatData
    */
    public static void main(String[] args) {
        StatData rd = new StatData(args[0]);
        double[][] data = rd.readData();
        int n = rd.numberOfRows();
        float[] x = new float[n], y = new float[n];

        // float に変換
        for (int i = 0; i < n; i++) {
            x[i] = (float)data[0][i];
            y[i] = (float)data[1][i];
        }
        method1(x, y);
        method2(x, y);
        method3(x, y);
    }
}