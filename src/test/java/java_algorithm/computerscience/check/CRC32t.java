package java_algorithm.computerscience.check;

/*
* CRC32t.java -- CRC
*/

import java.util.zip.CRC32;

/**
* 32 ビットの CRC を 2 つの方法で求めます。
*/

public class CRC32t {

    static final int CRCPOLY1 = 0x04C11DB7;
    /* x^{32}+x^{26}+x^{23}+x^{22}+x^{16}+x^{12}+x^{11]+
       x^{10}+x^8+x^7+x^5+x^4+x^2+x^1+1 */
    static final int CRCPOLY2 = 0xEDB88320;  // 左右逆転
    static final int BYTE_BIT = 8;       // １バイトのビット数
    static final int CRC_BIT  = 32;      // CRC のビット数
    static final int CRC_MASK = 0xFFFFFFFF;
    static final int TABLE_SIZE= 1 << BYTE_BIT; // = 256
    private static int[] crcTable = new int[TABLE_SIZE];

    /**
    *方法 1 の補助の表を作ります。
    */
    public static void makeCrcTable1() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            int r = i << (CRC_BIT - BYTE_BIT);
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 0x80000000) != 0) r = (r << 1) ^ CRCPOLY1;
                else                       r <<= 1;
            crcTable[i] = r & CRC_MASK;    // TODO
        }
    }

    /**
    * 32 ビットの CRC を方法 1 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method1(byte c[]) {
        int r = CRC_MASK, i = 0, n = c.length;

        while (--n >= 0) {
            int k = ((r >> (CRC_BIT - BYTE_BIT)) ^ c[i++]) & 0xFF;
            r = (r << BYTE_BIT) ^ crcTable[k];
        }
        return ~r & CRC_MASK;    // TODO
    }

    /**
    *方法 2 の補助の表を作ります。
    */
    public static void makeCrcTable2() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            int r = i;
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 1) != 0) r = (r >>> 1) ^ CRCPOLY2;
                else              r >>>= 1;
            crcTable[i] = r;
        }
    }

    /**
    * 32 ビットの CRC を方法 2 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method2(byte c[]) {
        int r = CRC_MASK, i = 0, n = c.length;

        while (--n >= 0){
            int k = (r ^ c[i++]) & 0xFF;
            r = (r >>> BYTE_BIT) ^ crcTable[k];
        }
        return r ^ CRC_MASK;    // TODO
    }

    /**
    * 以下はテスト用（２バイト文字のテストを含む）
    */
    public static void main(String[] args) {
                                                               // method1 / method2
        String s1 = "Java では char は16ビット符号無し整数。", // 1E28E1C1 / DAF9EBA1
               s2 = "Hello, world!";                           // 8E9A7706 / EBE6C6E6
        byte[] b1 = s1.getBytes(), b2 = s2.getBytes();  // byte[] に変換

      // CRC値を１６進数で表示する。
        makeCrcTable1();
        System.out.println("method1(" + s1 + ") = "
            + Integer.toHexString( method1(b1) ).toUpperCase());
        System.out.println("method1(" + s2 + ") = "
            + Integer.toHexString( method1(b2) ).toUpperCase());

        makeCrcTable2();
        System.out.println("method2(" + s1 + ") = "
            + Integer.toHexString( method2(b1) ).toUpperCase());
        System.out.println("method2(" + s2 + ") = "
            + Integer.toHexString( method2(b2) ).toUpperCase());

      // Java のCRC32はmethod2()と同じ値を返す。
        CRC32 javaCRC32s1 = new CRC32(), javaCRC32s2 = new CRC32();
        System.out.println("\n---------- CRC32() of Java -------------- ");
        javaCRC32s1.update(b1);
        System.out.println(s1 + " = " + Long.toHexString(javaCRC32s1.getValue()).toUpperCase());
        javaCRC32s2.update(b2);
        System.out.println(s2 + " = " + Long.toHexString(javaCRC32s2.getValue()).toUpperCase());
    }
}