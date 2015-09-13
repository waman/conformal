package java_algorithm.computerscience;

/**
* Sum.java -- 情報落ち
*/

/**
*情報落ちのプログラムです。
*/
public class Sum {

    static float sum1(float a[]) { // 通常の方法
        float s = 0;
        for(float ai : a)s += ai;
        return s;
    }

    static float sum2(float a[]) {  // 情報落ち対策
        float r = 0, s = 0;      // s は和, r は積み残し

        for(float ai : a) {
            r += ai;    // 積み残し + 加えたい値
            float t = s;  // 前回までの和
            s += r;       // 和を更新
            t -= s;       // 実際に積まれた値の符号を変えたもの
            r += t;       // 積み残し
        }
        return s;
    }

    /**
    * デモンストレーション・プログラムです。<br>
    *"1 + 0.0001 + ... + 0.0001 = 2" (0.0001 を 10000 回加算)<br>
    *を float を使って確かめます。<br>
    *通常の方法と情報落ち対策を施した結果を表示します。
    */
    public static void main(String[] args) {
        final int N = 10000;
        final float D = 1.0f / N;
        float[] a = new float[N + 1];

        System.out.println("1 + 0.0001 + ... + 0.0001 = 2 ?");
        a[0] = 1;
        for (int i = 1; i <= N; i++) a[i] = D; // 0.0001f;
        System.out.println("方法1: " + sum1(a));
        System.out.println("方法2: " + sum2(a));
    }
}