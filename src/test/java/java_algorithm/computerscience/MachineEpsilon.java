package java_algorithm.computerscience;

/*
* MachineEpsilon.java -- 機械エプシロン
*/

import java.text.DecimalFormat;

/**
* 機械エプシロンの実験をするプログラムです。
*/
public class MachineEpsilon {
    static void doubleVersion() {
        final DecimalFormat DF = new DecimalFormat(" 0.0000000000000000E00");
        System.out.println("-------------------------- double version -------------------------------");
        System.out.println("    e                       1 + e                   (1 + e) - 1");
        System.out.println("------------------------- ----------------------- -----------------------");

        double e = 1, w = 1 + e;
        while (w > 1) {
            System.out.println(DF.format(e) + " " + DF.format(w) +
                " " + DF.format(w - 1));
            e /= 2;  w = 1 + e;
        }
    }

    static void floatVersion() {
        final DecimalFormat DF = new DecimalFormat(" 0.00000000E00");
        System.out.println("----------------- float version -----------------");
        System.out.println(" e                1 + e           (1 + e) - 1");
        System.out.println("---------------- ---------------- ---------------");

        float e = 1, w = 1 + e;
        while (w > 1) {
            System.out.println(DF.format(e) + " " + DF.format(w) +
                " " + DF.format(w - 1));
            e /= 2;  w = 1 + e;
        }
    }
    /**
    * あなたの処理系の機械エプシロンを評価します。<br>
    * double と float バージョンがあります。
    *<blockquote><pre>
    * 【参考】機械エプシロン（C のfloat.hの値）
    * #define FLT_EPSILON         1.19209290E-07F               // float
    * #define DBL_EPSILON         2.2204460492503131E-16        // double
    * #define LDBL_EPSILON        1.084202172485504434e-019L    // long double
    *</pre></blockquote>
    */
    public static void main(String[] args) {
        doubleVersion();    floatVersion();
    }
}