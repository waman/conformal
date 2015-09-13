package java_algorithm.function.elementary;

/*
* LogE.java
* log(x) を求める。
* @version $Revision: 1.9 $, $Date: 2003/02/26 15:30:26 $
* 要 Exp, Logクラス (Exp.ldexp(x,k) = 2^k x を求めるために利用)
*/
/**
* 自然対数 log(x) を求めます。
* @see Exp
* @see Log
*/
public class LogE {
    private static final double LOG2  = 0.6931471805_5994530941_7232121458; // log_e 2
    private static final double SQRT2 = 1.4142135623_7309504880_168872421; // sqrt(2)

    /**
    * 恒等式 <code>log x = r + log(1 - x') (x' = 1 - x * e<sup>-r</sup>)</code> を
    * 使ってlog(x) を求めます。
    * @param x 実数
    * @return log(x)
    */
    public static double log_exp(double x) { // 自然対数（exp(x)使用版）
        if (x <= 0) return Double.NaN;
        Log.Frexp frexp = new Log.Frexp(x / SQRT2);
        x = Exp.ldexp(x, -frexp.k); // sqrt(0.5) < x < sqrt(2)

        double r = 2 * (x - 1) / (x + 1);    // log(x) の近似値 誤差は1%以下
        double xp = 1.0 - x * Math.exp(-r); // |xp| << 1.0, exp(-r)は高速に求まる

        // log(1 - xp) を求める。
        double u = xp / (xp - 2), d = u, u2 = u * u, s = u, last;
        int i = 1;
        do{ // このループは数回で終わる。
            d *= u2;
            i += 2;
            last = s;
            s += d / i;
        } while (last != s);
        return LOG2 * frexp.k + r + 2 * s;  // 2 * s = log(1 - xp)
    }

    /**
    * テストプログラムです。<br>
    * log(x) の計算速度の比較をします。
    */
    public static void main(String[] args){
        final int N = 500_000;
        double x, y = 0.0, y_cf = 0.0, y_exp = 0.0, y_log = 0.0;    // TODO

        do {
            x = Math.random() * 10;
        } while( x == 0.0);
        System.out.println("log(" + x + ") の値を " + N + " 回加算して速度と結果を比較\n");

        long start = System.currentTimeMillis();
        for(int i = 0; i < N; i++) y += Math.log(x);
        long time = System.currentTimeMillis() - start;
        System.out.println("CPU time of Math.log(x) " + time + "(ms)");

        start = System.currentTimeMillis();
        for(int i = 0; i < N; i++) y_cf += Log.log_cf(x);
        time = System.currentTimeMillis() - start;
        System.out.println("CPU time of Log.log_cf(x) " + time + "(ms)");

        start = System.currentTimeMillis();
        for(int i = 0; i < N; i++) y_exp += LogE.log_exp(x);
        time = System.currentTimeMillis() - start;
        System.out.println("CPU time of Log.log_exp(x) " + time + "(ms)");

        start = System.currentTimeMillis();
        for(int i = 0; i < N; i++) y_log += Log.log(x);
        time =  System.currentTimeMillis() - start;
        System.out.println("CPU time of Log.log(x) " + time + "(ms)");

        System.out.println("Math.log(x) = " + y);
        System.out.println("log_cf(x) = " + y_cf);
        System.out.println("log_exp(x) = " + y_exp);
        System.out.println("error =" + (y - y_exp));
    }
}