package java_algorithm.computerscience.compression;

/**
* Squeeze.java -- LZ法
* BitOutputStream.java と BitInputStream.java を使う。
*/

import java_algorithm.io.BitInputStream;
import java_algorithm.io.BitOutputStream;

import java.io.*;

/**
* 動的辞書法：LZ法によるデータ符号化のプログラムの１つです。
* @see BitInputStream
* @see BitOutputStream
* @see java_algorithm.computerscience.code.Huffman
* @see java_algorithm.computerscience.code.Slide
*/

public class Squeeze {

    static final int N        =  256;    // 文字の種類 (文字 = 0..N-1)
    static final int MAX_DICT = 4096;    // 辞書サイズ 4096, 8192, ...
    static final int MAX_MATCH = 100;    // 最大一致長
    static final int NIL =  MAX_DICT;    // ノード番号として存在しない値
    private char character[] = new char[MAX_DICT];
    private int[] parent= new int[MAX_DICT], lChild = new int[MAX_DICT]; // 親, 左の子
    private int[] rSib  = new int[MAX_DICT], lSib   = new int[MAX_DICT]; // 右左のきょうだい
    private int[] newer = new int[MAX_DICT], older  = new int[MAX_DICT]; // 待ち行列ポインタ
    private int[] match = new int[MAX_MATCH];  // 一致文字列
    private int dictSize = N;           // 現在の辞書サイズ
    private int qIn = NIL, qOut = NIL;  // 待ち行列の入口, 出口
    private int bitLen = 1;   // 現在の符号語の長さ
    private int bitMax = 2;   // 1 << bitLen

    private static void error(String msg) {
        System.err.println(msg);  System.exit(-1);
    }

    // ノード p を LRU 待ち行列から外す (size > 1; p は最後でない)
    private void dequeue(int p) {
        if (p == qOut) {  // 先頭の場合
            qOut = newer[p];  older[qOut] = NIL;
        } else {
            int o = older[p], n = newer[p];
            newer[o] = n;  older[n] = o;
        }
    }

    // ノード p を待ち行列の要素 q の後ろに挿入 (q が NIL なら最初に)
    private void enqueue(int p, int q) {
        if (qIn == NIL) {  // 待ち行列が空
            older[p] = newer[p] = NIL;  qIn = qOut = p;
        } else if (q == NIL) {  // 待ち行列の最初に付ける
            older[p] = NIL;  newer[p] = qOut;
            qOut = older[qOut] = p;
        } else if (q == qIn) {  // 待ち行列の最後に付ける
            older[p] = qIn;  newer[p] = NIL;
            qIn = newer[qIn] = p;
        } else {  // 待ち行列の途中に割り入る
            older[p] = q;
            newer[p] = newer[q];
            newer[q] = older[newer[p]] = p;
        }
    }

    // ノード p の文字 c に当たる子を返す (なければ NIL)
    private int child(int p, int c) {
        p = lChild[p];
        while (p != NIL && c != character[p]) p = rSib[p];
        return p;
    }

    // 親ノード parp の文字 c に当たる子として葉ノード p を挿入
    private void addLeaf(int parp, int p, int c) {
        character[p] = (char)c;
        parent[p] = parp;
        lChild[p] = lSib[p] = NIL;
        int q = lChild[parp];  rSib[p] = q;
        if (q != NIL) lSib[q] = p;
        lChild[parp] = p;
    }

    // 葉ノード p を削除
    private void deleteLeaf(int p) {
        int left = lSib[p], right = rSib[p];

        if (left != NIL) rSib[left] = right;
        else      lChild[parent[p]] = right;
        if (right != NIL) lSib[right] = left;
    }

    // 辞書木の初期化
    private void initTree() {
        dictSize = N;  qIn = NIL;  qOut = NIL;  bitLen   = 1;  bitMax = 2; // 一度使うだけなら不要
        for (int i = 0; i < N; i++) {
            character[i] = (char)i;
            parent[i] = lChild[i] = lSib[i] = rSib[i] = NIL;
        }
    }

    // 木の更新
    private void update(int[] match, int curLen, int prevPtr, int prevLen) {
        if (prevPtr == NIL) return;

        for (int i = 0; i < curLen; i++) {
            if (++prevLen > MAX_MATCH) return;
            int c = match[i], p;
            if ((p = child(prevPtr, c)) == NIL) {
                if (dictSize < MAX_DICT) p = dictSize++;  /* dictSize < NIL */
                else {
                    if (prevPtr == qOut) return;
                    p = qOut;  dequeue(p);  deleteLeaf(p);
                }
                addLeaf(prevPtr, p, c);
                if (prevPtr < N) enqueue(p, qIn);
                else             enqueue(p, older[prevPtr]);
            }
            prevPtr = p;
        }
    }

    private void output(BitOutputStream out, int p) throws IOException {
        if (p < N) {
            out.putBit(false);  out.putBits(8, p);
        } else {
            while ((dictSize - N) >= bitMax) {
                bitLen++;  bitMax <<= 1;
            }
            out.putBit(true);  out.putBits(bitLen, p - N);
        }
    }

    private int input(BitInputStream in) throws IOException {
        if ((dictSize - N) >= bitMax) {
            bitMax <<= 1;  bitLen++;
        }
        if (! in.getBit()) return in.getBits(8);
        return in.getBits(bitLen) + N;
    }

    /**
    *符号化を行います。
    *@param inFile 入力ファイル名
    *@param outFile 出力（符号化）ファイル名
    */
    public void encode(String inFile, String outFile) throws IOException {
        // 入力・出力ストリームをオープン
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
        BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
        int size = (int)(new File(inFile)).length();// 入力ファイルのバイト数
        out.putBits(BitOutputStream.MAX_BITS, size);          // ファイルヘッダにバイト数を書き込む

        int curPtr = NIL, curLen = 0, prevPtr, prevLen;
        int c = in.read();
        long inCount = 0, printCount = 0;

        initTree();
        while (c != -1) {
            prevPtr = curPtr;  prevLen = curLen;  curLen = 0;
            int  p = c, q = qIn;
            do {
                if (p >= N)
                    if (p == q) q = older[p];
                    else {  dequeue(p);  enqueue(p, q);  }
                match[curLen++] = c;  curPtr = p;
                c = in.read();  p = child(curPtr, c);
            } while (p != NIL);
            output(out, curPtr);
            update(match, curLen, prevPtr, prevLen);
            if ((inCount += curLen) > printCount) {
                System.err.print('.');   printCount += 1024;  // 状況報告
            }
        }
        System.out.println("\nIn : " + inCount +  " bytes");
        System.out.println("Out: " + out.outCount() +  " bytes");
        if (inCount != 0) {
            long cr = (1000L * out.outCount() + inCount / 2) / inCount;
            System.out.println("Out/In: " + (cr / 1000) + "." + (cr % 1000));
        }
        in.close();  out.close(); // 入出力ファイルをクローズ
    }

    /**
    * 復号を行います。
    *@param inFile 符号化ファイル名
    *@param outFile 復号ファイル名
    */
    public void decode(String inFile, String outFile) throws IOException {
        BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inFile)));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        int curPtr = NIL, curLen = 0, prevPtr, prevLen;
        int[] base = new int[MAX_MATCH];
        long size = in.getBits(BitInputStream.MAX_BITS); // 元のバイト数
        long outCount = 0, printCount = 0;

        initTree();
        while (outCount < size) {
            int p = input(in);
            if (p >= dictSize) error("入力エラー");
            prevPtr = curPtr;  prevLen = curLen;
            curPtr = p;  curLen = 0;
            while (p != NIL) {
                if (p >= N && p != qIn) {
                    dequeue(p);  enqueue(p, qIn);
                }
                curLen++;
                match[MAX_MATCH - curLen] = character[p];
                p = parent[p];
            }
            int bs = MAX_MATCH - curLen;
            System.arraycopy(match, bs, base, 0, match.length - bs);
            for (int i = 0; i < curLen ; i++) out.write(base[i]);
            update(base, curLen, prevPtr, prevLen);
            if ((outCount += curLen) > printCount) {
                System.err.print('.');  printCount += 1024;
            }
        }
        System.out.println("\nOut: " + outCount + " bytes"); // 復号したバイト数
        in.close();  out.close(); // 入出力ファイルをクローズ
    }

    /**
    * 使用方法<br>
    * <code>java Squeeze [e|d] infile outfile</code><br>
    * オプション e : 符号化，d : 復号
    */
// Huffman.java と同じ
    public static void main(String[] args) throws IOException {
        if (args.length != 3 || !(args[0].equals("e") || args[0].equals("d"))) {
            System.err.println("使用方法：java Squeeze [e|d] infile outfile");
            System.exit(-1);
        }
        Squeeze sqz = new Squeeze();
        if (args[0].equals("e")) sqz.encode(args[1], args[2]);  // 符号化
        else                     sqz.decode(args[1], args[2]);  // 復号
    }
}