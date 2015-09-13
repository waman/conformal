package java_algorithm.computerscience.code;

/**
 * Huffman.java -- Huffman符号化
 * BitInputStream, BitOutputStreamを使う
 */

import java_algorithm.io.BitInputStream;
import java_algorithm.io.BitOutputStream;

import java.io.*;

public class Huffman {

    static final int N = 256;   // アルファベットのサイズ (文字 = 0..N-1)
    private int heapSize, avail;
    private int[] heap = new int[2 * N - 1]; // 優先待ち行列用ヒープ
    private int[] parent = new int[2 * N - 1]; // Huffman木のデータ構造
    private int[] left = new int[2 * N - 1]; // 〃
    private int[] right = new int[2 * N - 1]; // 〃
    private int[] freq = new int[2 * N - 1]; // 各文字の出現頻度

    /*
     * 優先待ち行列に挿入
     */
    private void downHeap(int i) {
        int j, k = heap[i];

        while ((j = 2 * i) <= heapSize) {
            if (j < heapSize && freq[heap[j]] > freq[heap[j + 1]])
                j++;
            if (freq[k] <= freq[heap[j]]) break;
            heap[i] = heap[j];  i = j;
        }
        heap[i] = k;
    }

    /*
     * Huffman木の出力
     */
    private void writeTree(BitOutputStream out, int i) throws IOException {
        if (i < N) {            // 葉
            out.putBit(false);
            out.putBits(8, i);  // 文字そのもの
        } else {                // 節
            out.putBit(true);
            writeTree(out, left[i]); // 左の枝
            writeTree(out, right[i]); // 右の枝
        }
    }

    /*
     * 符号化
     */
    public void encode(String infile, String outfile) throws IOException {
        int i, j, k;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(infile));
        BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)));
        boolean[] codeBit = new boolean[N]; // 符号語

        for (i = 0; i < N; i++) freq[i] = 0; // 頻度の初期化
        while ((i = in.read()) >= 0) freq[i]++; // 頻度数え
        heap[1] = 0;            // 長さ0のファイルに備える
        heapSize = 0;
        for (i = 0; i < N; i++)
            if (freq[i] != 0) heap[++heapSize] = i; // 優先待ち行列に登録
        for (i = heapSize / 2; i >= 1; i--) downHeap(i); // ヒープ作り
        k = heap[1];            // 以下のループが1回も実行されない場合に備える
        avail = N;              // 以下のループでHuffman木を作る
        while (heapSize > 1) {  // 2個以上残りがある間
            i = heap[1];        // 最小の要素を取り出す
            heap[1] = heap[heapSize--];  downHeap(1); // ヒープ再構成
            j = heap[1];        // 次に最小の要素を取り出す
            k = avail++;        // 新しい節を生成する
            freq[k] = freq[i] + freq[j];    // 頻度を合計
            heap[1] = k;    downHeap(1);    // 待ち行列に登録
            parent[i] = k;  parent[j] = -k; // 木を作る
            left[k] = i;    right[k] = j;   // 〃
        }
        parent[k] = 0;            // 根
        out.putBits(BitOutputStream.MAX_BITS, freq[k]); // 入力ファイルのバイト数を出力
        writeTree(out, k);        // 木を出力
        int tableSize = out.outCount(); // 表の大きさ
        int inCount = 0;
        in.close();              // 最初に戻る
        in = new BufferedInputStream(new FileInputStream(infile));
        while ((j = in.read()) >= 0) {
            k = 0;
            while ((j = parent[j]) != 0)
                if (j > 0) codeBit[k++] = false;
                else {     codeBit[k++] = true;  j = -j;  }
            while (--k >= 0) out.putBit(codeBit[k]);
            if ((++inCount & 1023) == 0) System.err.print('.'); // 状況報告
        }
        System.err.println("\nIn : " + inCount +  " bytes");  // 結果報告
        System.err.println("Out: " + out.outCount() +
                           " bytes (table: " + tableSize + " bytes)");
        if (inCount != 0) {     // 圧縮比を求めて報告
            long cr = (1000L * out.outCount() + inCount / 2) / inCount;
            System.err.println("Out/In: " + (cr / 1000) + "." + (cr % 1000));
        }
        in.close();  out.close(); // 入出力ファイルをクローズ
    }

    /*
     * Huffman木を読む
     */
    private int readTree(BitInputStream in) throws IOException {
        if (in.getBit()) { // bit=1: 葉でない節
            int i;
            if ((i = avail++) >= 2 * N - 1) {
                System.err.println("表が間違っています");
                System.exit(-1);
            }
            left [i] = readTree(in); // 左の枝を読む
            right[i] = readTree(in); // 右の枝を読む
            return i;           // 節を返す
        } else return in.getBits(8); // 文字
    }

    /*
     * 復号
     */
    public void decode(String infile, String outfile) throws IOException {
        BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(infile)));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outfile));
        int size = in.getBits(BitInputStream.MAX_BITS); // 元のバイト数
        avail = N;
        int root = readTree(in); // 木を読む
        for (int k = 0; k < size; k++) { // 各文字を復号
            int j = root;  // 根
            while (j >= N)
                if (in.getBit()) j = right[j];  else j = left[j];
            out.write(j);
            if ((k & 1023) == 0) System.err.print('.');
        }
        System.err.println("\nOut: " + size + " bytes"); // 復号したバイト数
        in.close();  out.close(); // 入出力ファイルをクローズ
    }

    /*
     * メイン
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 3 || !(args[0].equals("e") || args[0].equals("d"))) {
            System.err.println("使用法: java Huffman [e|d] infile outfile");
            System.exit(-1);
        }
        Huffman huf = new Huffman();
        if (args[0].equals("e")) huf.encode(args[1], args[2]); // 符号化
        else                     huf.decode(args[1], args[2]); // 復号
    }
}