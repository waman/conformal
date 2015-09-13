package java_algorithm.datatype.string;

/**
* SBoyerMoo.java -- Boyer--Moore法
* @version $Revision: 1.1 $, $Date: 2003/03/20 03:05:51 $
*/
/* 簡略Boyer-Moore法(String + charAt(i) 版)*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
*簡略Boyer-Moore法による文字列照合アルゴリズムのプログラムです。
*@see KMP
*@see StringMatch
*/

public class BoyerMoore {
    static final int TABLE_SIZE = 255;
    /**
    *text の中から pattern のある位置を探します。
    *@param text テキスト文字列
    *@param pattern 照合文字列
    *@return pattern のある位置。先頭が 0。
    *pattern が長すぎる場合や見つからない場合は -1 を，
    *pattern の長さが 0 の場合は 0 を返します。
    */
    public static int position(String text, String pattern) {
        int[] skip = new int[TABLE_SIZE + 1];
        int len = pattern.length(); /* 文字列の長さ */

        if (len == 0) return -1;        /* エラー: 長さ0 */
        char tail = pattern.charAt(len - 1);        /* 最後の文字 */
        if (len == 1) {                 /* 長さ1なら簡単! */
            for (int i = 0; i < text.length(); i++)
                if (text.charAt(i) == tail) return i;
        } else {                       /* 長さ2以上なら表を作って… */
            int i;
            for (i = 0; i <= TABLE_SIZE; i++) skip[i] = len;
            for (i = 0; i < len - 1; i++)
                skip[pattern.charAt(i)] = len - 1 - i;
            /* i = len - 1; */          /* いよいよ照合 */
            while (i < text.length()) {
                char c = text.charAt(i);
        /* デモンストレーション用 */
                System.out.print("テ: " + text);
                System.out.print("\n照: ");
                for (int j = i + 1 - len; j > 0; j--) System.out.print(" ");
                System.out.println(pattern);
        /**************************/
                if (c == tail) {
                    int j = len - 1, k = i;
                    while (pattern.charAt(--j) == text.charAt(--k))
                        if (j == 0) return k;  /* 見つかった */
                }
                i += skip[c];
            }
        }
        return -1;  /* 見つからなかった */
    }

    /*
    * len = 0 : テキスト文字列の入力（長さ制限なし)
    * len > 0 : 照合文字列の入力。長さは len 以下。
    */
    static String readInString(String s, int len) throws IOException {
        BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in));

        if (len == 0) {
            String t = in.readLine();
            if (t.length() == 0) return s; // t が空
            return t;
        }

        String t = in.readLine();
        if (t.length() > len) throw new Error("照合文字列が長すぎます。");
        return t;
    }
    /**
    * <code>text = "supercalifragilisticexpialidocious"</code> の場合の
    *例題プログラムです。<br>
    *<strong>&quot;テキスト文字列&quot;</strong>と<strong>&quot;照合文字列&quot;</strong>を
    *聞いてきますから入力してください。<br>
    *途中経過が表示されます。
    */

    public static void main(String[] args) throws IOException {
        String text = "supercalifragilisticexpialidocious";
        String pattern = "";

        for ( ; ; ) {
            System.out.print("テキスト文字列 (Enter: " + text + ")\n  ? ");
            text = readInString(text, 0);
            System.out.print("照合文字列 (Enter: 終了)\n  ? ");
            pattern = readInString(pattern, text.length());
            if (pattern.length() == 0) break;
            int p = position(text, pattern);
            if (p >= 0) System.out.print("位置 = " + p + "\n\n");
            else        System.out.print("見つかりません.\n\n");
        }
    }
}