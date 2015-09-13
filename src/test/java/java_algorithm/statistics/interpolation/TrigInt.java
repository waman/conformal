package java_algorithm.statistics.interpolation;

public class TrigInt {

    private double[] a; // 係数 a_k, b_k (本文参照)

    public TrigInt(double[] y) { // コンストラクタ（係数配列 a[] の初期化）
        int N = y.length; // 点の数
        a = new double[N];
        double s = 0, t;
        for (double yi : y) s += yi;
        a[0] = s / N;
        int i;
        for (i = 2; i < N; i += 2) {
            t = i * Math.PI / N;
            s = 0;
            for (int j = 0; j < N; j++) s += y[j] * Math.cos(t * j);
            a[i - 1] = 2 * s / N;
            s = 0;
            for (int j = 0; j < N; j++) s += y[j] * Math.sin(t * j);
            a[i] = 2 * s / N;
        }
        if (i == N) {
            s = 0;  t = i * Math.PI / N;
            for (int j = 0; j < N; j++) s += y[j] * Math.cos(t * j);
            a[i - 1] = s / N;
        }
    }

    public double interpolate(double t) { // 補間値を求める
        double s = a[0];
        int i;
        for (i = 2; i < a.length; i += 2)
            s += a[i - 1] * Math.cos((i / 2) * t) + a[i] * Math.sin((i / 2) * t);
        if (i == a.length)
            s += a[i - 1] * Math.cos((i / 2) * t);
        return s;
    }

    public static void main(String[] args) {
        double[] y = new double[] {3,5,6,8,9,12,15,19,10,5}; // 各点での y 座標
        TrigInt trigint = new TrigInt(y);
        for (String arg : args) {
            double t = Double.parseDouble(arg);
            System.out.println("t = " + t + ", y(t) = " + trigint.interpolate(t));
        }
    }
}