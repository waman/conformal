package java_algorithm.io;

/**
 * BitInputStream.java -- ビット入力ストリーム
 */

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
* Huffman法などで使うビット入力ルーチンです。
* @see BitOutputStream
* @see java_algorithm.computerscience.code.Huffman
* @see java_algorithm.computerscience.code.Slide
* @see java_algorithm.computerscience.compression.Squeeze
*/
public class BitInputStream extends FilterInputStream {

    public static final int MAX_BITS = 31;  // 読み込み可能な最大ビット数
    protected int getCount = 0; // ビット入力カウンタ
    protected int bitBuf = 0;   // ビット入力バッファ

    /**
    * コンストラクタ
    * @param in 入力ストリーム
    */
    public BitInputStream(InputStream in) {
        super(in);
    }

    /**
     * x の右側 n ビットを返す
     */
    private static int rightBits(int n, int x) {
         return x & ((1 << n) - 1);
    }

    /**
    * 1ビットを読み込み
    */
    public boolean getBit() throws IOException {
        if (--getCount >= 0) return ((bitBuf >>> getCount) & 1) == 1;
        getCount = 7;  bitBuf = in.read();
        return ((bitBuf >>> 7) & 1) == 1;
    }

    /**
    * nビットを読み込み
    */
    public int getBits(int n) throws IOException {
        int x = 0;
        while (n > getCount) {
            n -= getCount;
            x |= rightBits(getCount, bitBuf) << n;
            bitBuf = in.read();  getCount = 8;
        }
        getCount -= n;
        return x | rightBits(n, bitBuf >>> getCount);
    }
}