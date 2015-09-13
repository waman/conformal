package java_algorithm.datatype.string;

/**
* SKanji.java -- EUCなどをシフトJISコードに変換
*  @version $Revision: 1.1 $, $Date: 2003/04/04 11:18:23 $
*
* java SKanji < infile > outfile
*
* のように使う。
*/

import java.io.*;

/**
* EUC などを含むファイルをシフト JIS コードのものに変換に変換します。<br>
* UNIX の '\n' は MS系の &quot;\r\n&quot; のペアに変換します。<br>
* 使用方法：<code>java SKanji &lt; infile &gt; outfile</code>
* see EUCtoSJIS
*/

public class SKanji {
    static int toShiftJIS(int hi, int lo) {  // JISをシフトJISに
        if ((hi & 1) != 0) {
            if (lo < 0x60)  lo += 0x1F;
            else            lo += 0x20;
        } else              lo += 0x7E;
        if (hi < 0x5F)      hi = (hi + 0xE1) >> 1;
        else                hi = (hi + 0x161) >> 1;

        return (hi << 8)| lo;
    }

    static final int ESC = 0x1B;  // エスケープ文字

    public static void main(String[] args) throws IOException {
        int c;
        boolean jisKanji = false;

        while ((c = System.in.read()) != -1) {  // 1バイトずつ読み込む
            if (c == ESC) {
                if ((c = System.in.read()) == '$') {
                    if ((c = System.in.read()) == '@' || c == 'B') {
                        jisKanji = true;  // JIS開始
                    } else {
                        System.out.write(ESC);  System.out.write('$');  // 1バイトずつ書き出す
                        if (c != -1) System.out.write(c);
                    }
                } else if (c == '(') {
                    if ((c = System.in.read()) == 'H' || c == 'J') {
                        jisKanji = false;  // JIS終了
                    } else {
                        System.out.write(ESC);  System.out.write('(');
                        if (c != -1) System.out.write(c);
                    }
                } else if (c == 'K') {
                    jisKanji = true;   // NECJIS開始
                } else if (c == 'H') {
                    jisKanji = false;  // NECJIS終了
                } else {
                    System.out.write(ESC);
                    if (c != -1) System.out.write(c);
                }
            } else if (jisKanji && c >= 0x21 && c <= 0x7E) {
                int d = System.in.read();
                if (d >= 0x21 && d <= 0x7E) {  // JIS
                    int e = toShiftJIS(c, d);
                    c = e >> 8;  d = e & 0xFF;
                }
                System.out.write(c);
                if (d != -1) System.out.write(d);
            } else if (c >= 0xA1 && c <= 0xFE) {
                int d = System.in.read();
                if (d >= 0xA1 && d <= 0xFE) {  // EUC
                    int e = toShiftJIS(c & 0x7F, d & 0x7F);
                    c = e >> 8;  d = e & 0xFF;
                }
                System.out.write(c);
                if (d != -1) System.out.write(d);
            } else {
                if (c == '\n') System.out.write('\r');
                System.out.write(c);
            }
        }
    }
}