package java_algorithm.computerscience.code;

/**
* Slide.java -- LZ法
*/

import java.io.*;

/**
*スライド辞書法：LZ法によるデータ符号化のプログラムの１つです。
*@see Huffman
*@see java_algorithm.computerscience.compression.Squeeze
*/

public class Slide {

    static final int N   = 4096;  // 環状バッファの大きさ
    static final int F   =   18;  // 最長一致長
    static final int NIL =    N;  // 木の末端
    static final int EOF =   -1;  // end of file
    private int[] text = new int[N + F - 1];
    private int[] dad  = new int[N + 1],
        lSon = new int[N + 1], rSon = new int[N + 257];  // 木
    private int matchPos, matchLen;  // 最長一致位置, 一致長

    private void initTree() {  // 木の初期化
        for (int i = N + 1; i <= N + 256; i++) rSon[i] = NIL;
        for (int i = 0; i < N; i++) dad[i] = NIL;
    }

    private void insertNode(final int r) { // 節 r を木に挿入
        int cmp = 1, p = N + 1 + text[r];

        rSon[r] = lSon[r] = NIL;  matchLen = 0;
        for ( ; ; ) {
            if (cmp >= 0) {
                if (rSon[p] != NIL) p = rSon[p];
                else {  rSon[p] = r;  dad[r] = p;  return;  }
            } else {
                if (lSon[p] != NIL) p = lSon[p];
                else {  lSon[p] = r;  dad[r] = p;  return;  }
            }
            int i;
            for (i = 1; i < F; i++)
                if ((cmp = text[r + i] - text[p + i]) != 0)  break;
            if (i > matchLen) {
                matchPos = p;
                if ((matchLen = i) >= F)  break;
            }
        }
        dad[r] = dad[p];   lSon[r] = lSon[p];  rSon[r] = rSon[p];
        dad[lSon[p]] = r;  dad[rSon[p]] = r;
        if (rSon[dad[p]] == p) rSon[dad[p]] = r;
        else                   lSon[dad[p]] = r;
        dad[p] = NIL;  // p を外す
    }

    private void deleteNode(final int p) { // 節 p を木から消す
        if (dad[p] == NIL) return;  // 見つからない
        int  q;

        if (rSon[p] == NIL) q = lSon[p];
        else if (lSon[p] == NIL) q = rSon[p];
        else {
            q = lSon[p];
            if (rSon[q] != NIL) {
                do {  q = rSon[q];  } while (rSon[q] != NIL);
                rSon[dad[q]] = lSon[q];  dad[lSon[q]] = dad[q];
                lSon[q] = lSon[p];       dad[lSon[p]] = q;
            }
            rSon[q] = rSon[p];  dad[rSon[p]] = q;
        }
        dad[q] = dad[p];
        if (rSon[dad[p]] == p) rSon[dad[p]] = q;
        else                   lSon[dad[p]] = q;
        dad[p] = NIL;
    }

    /**
    *符号化を行います。
    *@param inFile 入力ファイル名
    *@param outFile 出力（符号化）ファイル名
    */
    public void encode(String inFile, String outFile) throws IOException {
        initTree();  // 木を初期化
        // 入力・出力ストリームをオープン
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        int c, len, r = N - F;

        for (int i = 0; i < r; i++) text[i] = 0;  // バッファを初期化
        for (len = 0; len < F; len++) {
            if ((c = in.read()) == EOF) break;
            text[r + len] = c;
        }
        long inCount = len;  if (inCount == 0) return;
        for (int i = 1; i <= F; i++) insertNode(r - i);
        insertNode(r);

        long outCount = 0, printCount = 0;
        int s = 0, codePtr = 1, mask = 1, code[] = new int[17];
        do {
            if (matchLen > len) matchLen = len;
            if (matchLen < 3) {
                matchLen = 1;  code[0] |= mask;
                code[codePtr++] = text[r];
            } else {
                code[codePtr++] = matchPos;
                code[codePtr++] = ((matchPos >> 4) & 0xf0) | (matchLen - 3);
            }
            mask <<= 1;  mask &= 0xff;
            if (mask == 0) {
                for (int i = 0; i < codePtr; i++) out.write(code[i]);
                outCount += codePtr;
                code[0] = 0;  codePtr = mask = 1;
            }
            int lastMatchLen = matchLen, j;
            for (j = 0; j < lastMatchLen; j++) {
                if ((c = in.read()) == EOF) break;
                deleteNode(s);  text[s] = c;
                if (s < F - 1) text[s + N] = c;
                s = (s + 1) & (N - 1);  r = (r + 1) & (N - 1);
                insertNode(r);
            }
            if ((inCount += j) > printCount) {
                System.err.print('.');  printCount += 1024;
            }
            while (j++ < lastMatchLen) {
                deleteNode(s);
                s = (s + 1) & (N - 1);  r = (r + 1) & (N - 1);
                if ((--len) != 0) insertNode(r);
            }
        } while (len > 0);
        if (codePtr > 1) {
            for (int i = 0; i < codePtr; i++) out.write(code[i]);
            outCount += codePtr;
        }
        System.out.println("\nIn : " + inCount + " bytes");  // 結果報告
        System.out.println("Out: " + outCount + " bytes");
        if (inCount != 0) {  // 符号化比を求めて報告
            long cr = (1000L * outCount + inCount / 2) / inCount;
            System.out.println("Out/In: " + (cr / 1000) + "." + (cr % 1000));
        }
        in.close();  out.close();
    }

    /**
    * 復号を行います。
    *@param inFile 符号化ファイル名
    *@param outFile 復号ファイル名
    */
    public void decode(String inFile, String outFile) throws IOException {
        // 入力・出力ストリームをオープン
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        int r = N - F, flags = 0;
        long outCount = 0;

        for (int i = 0; i < r; i++) text[i] = 0;
        for ( ; ; ) {
            int c;
            if (((flags >>= 1) & 256) == 0) {
                if ((c = in.read()) == EOF) break;
                flags = c | 0xff00;
            }
            if ((flags & 1) != 0) {
                if ((c = in.read()) == EOF) break;
                out.write(c);   outCount++;
                text[r++] = c;  r &= (N - 1);
            } else {
                int c1, c2;
                if ((c1 = in.read()) == EOF) break;
                if ((c2 = in.read()) == EOF) break;
                c1 |= ((c2 & 0xf0) << 4);  c2 = (c2 & 0x0f) + 2;
                for (int k = 0; k <= c2; k++) {
                    c = text[(c1 + k) & (N - 1)];  out.write(c);  outCount++;
                    text[r++] = c;  r &= (N - 1);
                }
            }
        }
        System.out.println("Out: " + outCount + " bytes");
        in.close();  out.close();
    }

    /**
    * 使用方法<br>
    * java Slide [e|d] infile outfile<br>
    * オプション e : 符号化，d : 復号
    */
    public static void main(String[] args) throws IOException {
        if (args.length != 3 || !(args[0].equals("e") || args[0].equals("d"))) {
            System.err.println("使用方法：java Slide [e|d] infile outfile");
            System.exit(-1);
        }
        Slide slide = new Slide();
        if (args[0].equals("e")) slide.encode(args[1], args[2]);  // 符号化
        else                     slide.decode(args[1], args[2]);  // 復号 
    }
}