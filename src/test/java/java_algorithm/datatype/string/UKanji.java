package java_algorithm.datatype.string;

/**
* UKanji.java -- シフトJISをEUCに変換
*  @version $Revision: 1.1 $, $Date: 2003/04/04 11:18:23 $
*
* java UKanji < infile > outfile
*
* のように使う。
* ただし，半角カタカナが混じっていると，その部分は文字化けする。
*
* Windowsなどの"\r\n"のペアをUNIX系の'\n'に変換するようにしている。
*/

import java.io.*;

/**
* シフト JIS のファイルを EUC のものに変換します。<br>
* Windows などの &quot;\r\n&quot; のペアは UNIX 系の '\n' に変換します。<br>
* 使用方法：<code>java UKanji &lt; infile &gt; outfile</code>
* see SJIStoEUC
*/

public class UKanji {
    static boolean isKanji(int c) { // シフトJIS 1バイト目
        return ((char)((c ^ 0x20) - 0xA1) <= 0x3B); // 本文参照
//        return (c >= 0x81 && c <= 0x9F) || (c >= 0xE0 && c <= 0xFC);
    }
    static boolean isKanji2(int c) { // シフトJIS 2バイト目
        return c >= 0x40 && c <= 0xFC && c != 0x7F;
    }

    static int shiftJIStoJIS(int hi, int lo) { // シフトJISをJISに
        if (hi <= 0x9F) {
            if (lo < 0x9F)  hi = (hi << 1) - 0xE1;
            else            hi = (hi << 1) - 0xE0;
        } else {
            if (lo < 0x9F)  hi = (hi << 1) - 0x161;
            else            hi = (hi << 1) - 0x160;
        }
        if      (lo < 0x7F) lo -= 0x1F;
        else if (lo < 0x9F) lo -= 0x20;
        else                lo -= 0x7E;

        return (hi << 8)| lo;
    }

    public static void main(String[] args) throws IOException {
        int c;

        while ((c = System.in.read()) != -1) {  // 1バイトずつ読み込む
            if (isKanji(c)) {
                int d = System.in.read();
                if (isKanji2(d)) {
                    int e = shiftJIStoJIS(c, d);  // c : 上位，d : 下位バイト
                    System.out.write((e >> 8)| 0x80);  // EUC に変換して1バイトずつ書き出す
                    System.out.write((e & 0xFF)| 0x80);
                } else {
                    System.out.write(c);
                    if (d != -1) System.out.write(d);
                }
            } else if (c == '\r');  // 何もしない
            else System.out.write(c);
        }
    }
}