package java_algorithm.computerscience.check;

/*
* GetCRC.java -- CRC
*/

import java.io.*;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

/**
* ファイルの 32 ビット CRC を方法 2 で求めます。
* このCRC値の計算は gzip や PNG ファイルで使われている手法と同じです。
* @see CRC32t
*/
public class GetCRC {

    static final int CRCPOLY2  = 0xEDB88320;    // 左右逆転
    static final int BYTE_BIT  = 8;             // １バイトのビット数
    static final int TABLE_SIZE= 1 << BYTE_BIT; // = 256
    static final int BUFF_SIZE = 1024 * 32;     // = 32KB
    private static byte[] buff = new byte[BUFF_SIZE];   // 読み込みバッファ
    private static int[] crcTable = new int[TABLE_SIZE];

    static { // CRC32t.makeCrcTable2() と同じテーブル初期化処理
        for (int i = 0; i < TABLE_SIZE; i++) {
            int r = i;
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 1) != 0) r = (r >>> 1) ^ CRCPOLY2;
                else              r >>>= 1;
            crcTable[i] = r;
        }
    }

    private static int evaluateCRC(int n, byte c[], int r) {
        int i = 0;

        while (--n >= 0)
            r = (r >>> BYTE_BIT) ^ crcTable[(r ^ c[i++]) & 0xFF];  //TODO

        return r;
    }

    /**
    * ファイルの 32 ビット CRC を求めます。
    * @param fileName ファイル名を与えます。
    */
    public static int ofFile(String fileName) throws IOException {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(fileName));
        } catch(FileNotFoundException e) {
            System.err.println("GetCRC : " + fileName + " が見つかりません");
            System.exit(-1);
        }
        int crc = 0xffffffff, readSize;
        while ((readSize = in.read(buff)) > 0)
            crc = evaluateCRC(readSize, buff, crc);
        return ~crc;    // crc ^ 0xffffffff;
    }

    private static class CrcInfo {

        private static int MAX_LEN_FN;   // ファイル名の長さの最大値
        private static long MAX_SIZE;    // ファイルサイズの最大値
        private static long TOTAL_SIZE;  // 合計サイズ

        static{
            MAX_SIZE = TOTAL_SIZE = MAX_LEN_FN = 0;
        }

        public static int getMaxLenFn(){
            return MAX_LEN_FN;
        }

        public static long getMaxSize(){
            return MAX_SIZE;
        }

        public static long getTotalSize(){
            return TOTAL_SIZE;
        }

        String fileName;        // ファイル名
        int  crc;               // CRC 値
        long size;              // ファイルサイズ
        boolean isFile;         // ファイルか？

        CrcInfo(String path) throws IOException {
            File file = new File(path);
            if (!(isFile = file.isFile())) return; // ディレクトリ

            fileName = file.getName();  // ファイル名のみを取り出す
            crc  = ofFile(path);        // フルパス名を渡す。
            size = file.length();
            int len = fileName.length();
            if (len > MAX_LEN_FN) MAX_LEN_FN = len;
            if (size > MAX_SIZE) MAX_SIZE = size;
            TOTAL_SIZE += size;
        }

        static List<CrcInfo> scanFiles(String[] path) throws IOException {
            List<CrcInfo> infoList = new LinkedList<>();
            CrcInfo cip; // 初期化

            for (String path_i : path) {
                cip = new CrcInfo(path_i);
                if (cip.isFile) infoList.add(cip);// ディレクトリはスキップ
            }
            return infoList;
        }
    }

    // 長さ w の連続した文字からなる String を作る。
    private static String makeConChar(int w, char c) {
        String s = "";
        while (w-- > 0) s += c;
        return s;
    }

    /* s の後ろに ' ' を入れて幅 w の文字列を作る。
    * "file name" を出力するのに使っているだけ。
    * 下の fileSpace を使って
    * String s = "file name";
    * s = s + fileSpace.substring(s.length());
    * としてもよいが，再利用できるかも知れないためメソッドを書いておく。
    */
    private static String stringW(String s, int w) {
        String space = makeConChar(w, ' ');
        return s + space.substring(s.getBytes().length);
    }

    /**
    * 引数に与えられたファイルの 32 ビット CRC とサイズを標準出力に出力します。<br>
    * サイズは３桁ごとにコンマ','で区切られます。<br>
    * 使用方法：javac GetCRC file1 file2 ...<br>
    * ワイルドカードも使えます。その場合，Java の仕様でファイル名は辞書順にソートされます。
    */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("使用方法：java GetCRC file1 file2 ...");
            System.exit(-1);
        }

        List<CrcInfo> crcInfo = CrcInfo.scanFiles(args);
        if (crcInfo.size() == 0) {  // class Vector の size()
            System.err.println(args[0] + " ファイルが見つかりません。");
            System.exit(-1);
        }

        NumberFormat ifmt = NumberFormat.getNumberInstance();   // 3 桁区切りで出力する format
        int sizeWidth = ifmt.format(CrcInfo.getMaxSize()).length() + 2;  // file size の出力幅
        if (sizeWidth < 5) sizeWidth = 5;
        int fnWidth   = CrcInfo.getMaxLenFn() + 1;                       // file name の出力幅
        if (fnWidth < 10) fnWidth = 10;
        String fileSpace = makeConChar(fnWidth, ' ');
        String sizeSpace = makeConChar(sizeWidth, ' ');

        System.out.println(stringW("file name", fnWidth) + "   CRC     size");
        System.out.println(makeConChar(fnWidth, '-') + " -------- " + makeConChar(sizeWidth, '-'));

        for (CrcInfo crc : crcInfo) {
//            String sFile = crc.fileName + fileSpace.substring(crc.fileName.length());
            // 尻尾に ' ' を付加。crc.fileName.length() では全角ファイル名の場合に表示位置がずれる。
            String sFile = crc.fileName + fileSpace.substring(crc.fileName.getBytes().length);
            String sCrc  = Integer.toHexString(crc.crc).toUpperCase();
            sCrc = "00000000".substring(0, 8 - sCrc.length()) + sCrc;    // 頭に 0 を付加
            String sSize = ifmt.format(crc.size);
            sSize = sizeSpace.substring(0, sizeWidth - sSize.length()) + sSize;

            System.out.println(sFile + " " + sCrc + " " + sSize);
        }
        System.out.println(makeConChar(fnWidth, '-') + " -------- " + makeConChar(sizeWidth, '-'));
        System.out.println(ifmt.format(crcInfo.size()) + " files   " +
            ifmt.format(CrcInfo.getTotalSize()) + " bytes");
    }
}