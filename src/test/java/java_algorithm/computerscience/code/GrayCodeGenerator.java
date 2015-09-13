package java_algorithm.computerscience.code;

/**
 *  Gray (グレイ) 符号
 */

public class GrayCodeGenerator {

    public  static void printAll(final int n) {
        int[] binary = new int[n + 1];
        int[] gray = new int[n];
        for (int i = 0; i < n; i++) binary[i] = gray[i] = 0;
        binary[n] = 0;  // 番人

        System.out.println("binary  Gray");
        for ( ; ; ) {  // 出力
            int i;
            for (i = n - 1; i >= 0; i--) System.out.print(binary[i]);
            System.out.print("  ");
            for (i = n - 1; i >= 0; i--) System.out.print(gray[i]);
            System.out.println();
            for (i = 0; binary[i] == 1; i++) binary[i] = 0;
            if (i == n) break;
            binary[i] = 1;
            gray[i] ^= 1;  // 1個変えるだけで次のGray符号が得られる
        }
    }

    public static void main(String[] args) {
        printAll(6);  // 6桁
    }
}