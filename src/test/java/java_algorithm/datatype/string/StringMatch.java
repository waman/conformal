package java_algorithm.datatype.string;

/**
* StringMatch.java -- 文字列照合
* @version $Revision: 1.1 $, $Date: 2003/03/20 03:05:53 $
*/
/**
*文字列照合のプログラムです。
*@see KMP
*@see BoyerMoore
*/
public class StringMatch {
    // Java では char[] の最後は '\0' とは限らない。
    /**
    *text の中から pattern のある位置を探します。
    *@param text 文字列
    *@param pattern 探す文字列
    *@return pattern のある位置。先頭が 0。
    *pattern が見つからない場合は -1。
    */
    public static int position1(char[] text, char[] pattern) {
        int i = 0, c = pattern[0], n = pattern.length;

        while (i < text.length) {
            if (text[i++] == c) {
                int k = i, j = 1;
                while (j < n && text[k] == pattern[j]) {
                    k++;  j++;
                }
                if (j == n) return k - j;  /* 見つかった */
            }
        }
        return -1;  /* 見つからなかった */
    }

   //***** あるいは同じことであるが... *****
    /**
    *position1() と同様です。
    */
    public static int position2(char[] text, char[] pattern) {
        int i = 0, j = 0, n = pattern.length;

        while (i < text.length && j < n)
            if (text[i] == pattern[j]) {  i++;  j++;  }
            else {  i = i - j + 1;  j = 0;  }
        if (j == n) return i - j;  /* 見つかった */
        return -1;  /* 見つからなかった */
    }
    /**
    *<code>text = "AAAAAABAAAC", pattern[] = { "AAA", "AAB", "AAC", "AAD" }</code> の場合の
    *例題プログラムです。
    */

    public static void main(String[] args) {
        String text = "AAAAAABAAAC";
        String[] pattern = { "AAA", "AAB", "AAC", "AAD" };
        int n = pattern.length;

        for (int i = 0; i < n; i++)
            System.out.println("position1(\"" + text + "\", \"" + pattern[i] + "\") = "
                + position1(text.toCharArray(), pattern[i].toCharArray()));
        System.out.println();
        for (int i = 0; i < n; i++)
            System.out.println("position2(\"" + text + "\", \"" + pattern[i] + "\") = "
                + position2(text.toCharArray(), pattern[i].toCharArray()));
    }
}