package java_algorithm.statistics;

/**
* StemLeaf.java -- 幹葉表示
* InputDoubleData クラスを使う。
*/

import java_algorithm.datatype.string.NumberFormatter;
import java_algorithm.io.InputDoubleData;

import java.io.IOException;

/**
* 実数型の複数のデータを幹葉表示するプログラムです。
*@see InputDoubleData
*@see NumberFormatter
*/
public class StemLeaf {

    static final int MAX_LINES = 60;

    static void display(double[] x) {
        int kMin, kMax, n = x.length;
        int[] histo = new int[10 * MAX_LINES];
        double xMin = x[0], xMax = xMin, factor = 1;

        for (int i = 1; i < n; i++)
            if      (x[i] < xMin) xMin = x[i];
            else if (x[i] > xMax) xMax = x[i];
        while (factor * xMax > 32767 || factor * xMin < -32767)
            factor /= 10;
        for ( ; ; ) {
            kMin = (int)(factor * xMin) / 10 - ((xMin < 0) ? 1 : 0);
            kMax = (int)(factor * xMax) / 10;
            if (kMax - kMin + 1 <= MAX_LINES) break;
            factor /= 10;
        }
        System.out.println("10 * 幹 + 葉 = " + factor + " * データ");

        for(double xi : x)
            histo[(int)(factor * xi) - ((xi < 0) ? 1 : 0) - 10 * kMin]++;
        if (kMin < 0 && kMax > 0) {
            int k = 0;
            for(double xi : x) if (xi == 0) k++;
            histo[-10 * kMin    ] -= k / 2;
            histo[-10 * kMin - 1] += k / 2;
        }
        NumberFormatter fmt5d = new NumberFormatter("5d");
        for (int k = kMin; k <= kMax; k++) {
            if (k != -1) System.out.print(fmt5d.toString(k + ((k < 0) ? 1 : 0)) + " | ");
            else         System.out.print("   -0 | ");
            for (int j = 0; j <= 9; j++) {
                int h;
                if (k >= 0) h = histo[10 * (k - kMin) + j];
                else h = histo[10 * (k - kMin) + 9 - j];
                for (int i = 0; i < h; i++) System.out.print(j);
            }
            System.out.println();
        }
    }

    /**
    *<b>データを入力してください</b> と聞いてきますから，<b>Enter</b>キーで
    *区切られた実数型データを入力してください。データの終わりでは，<b>Ctrl+z</b>
    *を押してください。<br>
    *幹葉表示を出力します。
    *<blockquote><pre>
    *<b>入力例</b>（下線の部分を入力する）
    *データを入力してください
    *<u><code>-3
    *5
    *6
    *12
    *12
    *15
    *23
    *^Z</code></u><br>
    *<b>出力</b>
    *10 * 幹 + 葉 = 1.0 * データ<code>
    *   -0 | 3
    *    0 | 56
    *    1 | 225
    *    2 | 3
    *</code></pre></blockquote>
    */

    public static void main(String[] args) throws IOException {
        System.err.println("データを入力してください");
        InputDoubleData data = new InputDoubleData();
        double[] x = data.getValue();

        display(x);
    }
}