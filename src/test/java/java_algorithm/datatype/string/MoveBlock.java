package java_algorithm.datatype.string;

/*
* MoveBlock.java -- ブロック移動
* @version $Revision: 1.3 $, $Date: 2002/12/16 01:03:26 $
*/
/**
*ブロック移動のプログラムです。
*/
public class MoveBlock {
    static String s =
        "SUPERCALIFRAGILISTICEXPIALIDOCIOUS"; // Mary Poppinsの魔法の言葉
    static char a[] = s.toCharArray();

    static void reverse(int i, int j) {
        while (i < j) {
            char t = a[i];  a[i] = a[j];  a[j] = t;
            i++;  j--;
        }
    }

    static void rotate(int left, int mid, int right) {
        reverse(left, mid);
        reverse(mid + 1, right);
        reverse(left, right);
    }
    /**
    *ブロック移動の例題プログラムです。<br>
    *Mary Poppins の魔法の言葉<br>
    *<strong>&quot;SUPERCALIFRAGILISTICEXPIALIDOCIOUS&quot;</strong><br>
    *を使ってブロック移動を行った結果を表示します。
    */
    public static void main(String[] args) {
        System.out.println(a);
        for (int i = 0; i < 17; i++) {
            rotate(0, 5, 33);  System.out.println(a);
        }
    }
}