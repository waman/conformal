package java_algorithm.computerscience.code;

/**
 *  Gray (グレイ) 符号
 */

public class GrayCode {

    public static int binaryToGray(int binary) {
        return binary ^ (binary >>> 1);
    }

    public static int grayToBinary(int gray) {
        int binary;
        for (binary = 0; gray != 0; gray >>>= 1) binary ^= gray;
        return binary;
    }

    public static String toString(int x, int bit) {
        String s = "";
        for (int mask = 1 << (bit - 1); mask != 0; mask >>>= 1)
            s += ((x & mask) != 0) ? "1" : "0";
        return s;
    }

    public static void main(String[] args) {
        final int N = 6;
        for (int x = 0; x < (1 << N); x++) {
            System.out.print(x + ": " + toString(x, N) + " ");
            int y = binaryToGray(x);  System.out.print(toString(y, N) + " ");    // TODO
            int z = grayToBinary(y);  System.out.println(toString(z, N));
            if (x != z) System.out.println("Error");
        }
        grayToBinary(-1);  // 不正な入力ではあるが無限ループしないことを確認
    }
}