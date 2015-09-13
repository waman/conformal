package java_algorithm.function.elementary;

/*
* AtanT.java
* arctan(x) を求める。
*/

/**
* アークタンジェントを求めます。
*/
public class AtanT {
    /**
    * 加法定理 <code>arctan <i>x</i> = arctan <i>y</i> +
    * arctan {(<i>x</i>-<i>y</i>)/(1+<i>xy</i>)}</code> を
    *使って<code>arctan <i>x</i></code> を求めます。
    * @param x 実数
    * @return <code>arctan <i>x</i></code> の主値（-pi/2 ～ pi/2）
    */
    public static double atan(double x) { // tan(x)使用版
        int sign = 0;
        if (x > 1)       { sign =  1;  x = 1 / x; }
        else if (x < -1) { sign = -1;  x = 1 / x; }
        double x2 = x * x;
    // r はarctan(x)の最良近似、誤差は 1.0E-4 程度
    // さらに高次の近似式を使うと返って遅くなる。
        double r = x * (0.995354 - (0.288679 - 0.079331 * x2) * x2);
    // tan(r)は高速に求まる。計算時間のほとんどはここに使われる。
        double y = Math.tan(r); // Trig.tan(r); では５倍ほど遅い。
        double u = (x - y) / (1 + x * y);  // |u| << 1.0
    // r + arctan(u) = arctan(x) を求める。
        double u2 = - u * u;
        for (int i = 1;; i += 2) {  // このループは３回程度で終わる。
            double last = r;
            r += u / i;
            if (r == last) break;
            u *= u2;
        }
        if (sign > 0) return   Math.PI / 2 - r;
        if (sign < 0) return - Math.PI / 2 - r;
        /* else */    return r;
    }

    /**
    * <code>arctan <i>x</i></code> の速度テスト
    */
    public static void main(String[] args){
        final double x = 10 * Math.random() - 5;
        final int N = 300000;
        double sum;
        long time;

        System.out.println("          x   = " + x);
        System.out.println("Math.atan(x)  = " + Math.atan(x));
        System.out.println("Atan.atan(x)  - Math.atan(x) = " + (Atan.atan(x)  - Math.atan(x)));
        System.out.println("AtanT.atan(x) - Math.atan(x) = " + (AtanT.atan(x) - Math.atan(x)));
        System.out.println("----------");

        System.gc();
        sum = 0;
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) sum += (x + 0.01 / i);
        time = System.currentTimeMillis() - time;
        System.out.println("loop only     " + time + "(ms) " + (sum / N));

        System.gc();
        sum = 0;
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) sum += Math.atan(x + 0.01 / i);
        time = System.currentTimeMillis() - time;
        System.out.println("Math.atan(x)  " + time + "(ms) " + (sum / N));

        System.gc();
        sum = 0;
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) sum += Atan.atan(x + 0.01 / i);
        time = System.currentTimeMillis() - time;
        System.out.println("Atan.atan(x)  " + time + "(ms) " + (sum / N));

        System.gc();
        sum = 0;
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) sum += AtanT.atan(x + 0.01 / i);
        time = System.currentTimeMillis() - time;
        System.out.println("AtanT.atan(x) " + time + "(ms) " + (sum / N));
    }
}