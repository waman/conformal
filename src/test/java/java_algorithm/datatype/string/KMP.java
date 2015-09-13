package java_algorithm.datatype.string;

/**
* KMP.java -- Knuth--Morris--Pratt法
* @version $Revision: 1.3 $, $Date: 2002/12/16 01:03:25 $
*/

/**
*Knuth, Morris and Pratt による文字列照合アルゴリズムのプログラムです。
*/

public class KMP {
    /**
    *text の中から pattern のある位置を探します。
    *@param text 文字列
    *@param pattern 探す文字列
    *@return pattern のある位置。先頭が 0。
    *pattern が長すぎる場合や見つからない場合は -1 を，
    *pattern の長さが 0 の場合は 0 を返します。
    */
    public static int position(String text, String pattern) {
        int tLen = text.length(), pLen = pattern.length();
        if (pLen > tLen) return -1;  /* エラー: {\tt pattern}が長すぎる */
        if (pLen == 0) return 0;
        int[] next = new int[pLen + 1];

        int i = 1, j = 0;  // next[1] = 0;
        while (i <  pLen) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                i++;  j++;  next[i] = j;
            } else if (j == 0) { i++;  next[i] = j; }
            else j = next[j];
        }
        /* デモンストレーション用 */
        for (int k = 0; k < pLen; k++)
            System.out.println("pattern[" + k + "] = " + pattern.charAt(k)
                    + ", next[" + (k+1) + "] = " + next[k+1]);
        /**************************/
        i = j = 0;
        while (i < tLen && j < pLen) {
            if (text.charAt(i) == pattern.charAt(j)) {  i++;  j++;  }
            else if (j == 0) i++;
            else j = next[j];
        }
        if (j == pLen) return i - j;
        return -1;  /* 見つからない */
    }
    /**
    * text = <b>&quot;supercalifragilisticexpialidocious&quot;</b>,
    * pattern = <b>&quot;ilisti&quot;</b> の場合の
    *例題プログラムです。
    *途中経過を表示します。
    */

    public static void main(String[] args) {
        String text = "supercalifragilisticexpialidocious", pattern = "ilisti";
//      String text = "ABCABCABABC", pattern = "ABCABA";

        System.out.println("position(\"" + text + "\"" + ", \"" + pattern + "\") = " + position(text, pattern));
    }
}