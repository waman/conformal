package java_algorithm.computerscience.check;

/*
* CRC32.java -- CRC
*/

/**
* 32 ビットの CRC を 2 つの方法で求めます。
* @see java.util.zip.CRC32
*/

public class CRC32 {

    static final int CRCPOLY1 = 0x04C11DB7;
    /* x^{32}+x^{26}+x^{23}+x^{22}+x^{16}+x^{12}+x^{11]+
       x^{10}+x^8+x^7+x^5+x^4+x^2+x^1+1 */
    static final int CRCPOLY2 = 0xEDB88320;  // 左右逆転
    static final int BYTE_BIT = 8;       // １バイトのビット数
    static final int CRC_BIT  = 32;      // CRC のビット数
    static final int CRC_MASK = 0xFFFFFFFF;

    /**
    * 32 ビットの CRC を方法 1 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method1(byte c[]) {
        int r = CRC_MASK;

        for (byte ci : c) {
            r ^= (int)(ci & 0xFF) << (CRC_BIT - BYTE_BIT);
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 0x80000000) != 0) r = (r << 1) ^ CRCPOLY1;
                else                       r <<= 1;
        }
        return ~r & CRC_MASK;    // TODO
    }
    /**
    * 32 ビットの CRC を方法 2 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method2(byte c[]) {
        int r = CRC_MASK;

        for (byte ci : c) {
            r ^= (ci & 0xFF);
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 1) != 0) r = (r >>> 1) ^ CRCPOLY2;
                else              r = (r >>> 1);  //  r >>= 1;
        }
        return r ^ CRC_MASK;    // TODO
    }
    /**
    * テスト用（２バイト文字のテストを含む）
    */
    public static void main(String[] args) {
                                                               // method1 / method2
        String s1 = "Java では char は16ビット符号無し整数。", // 1E28E1C1 / DAF9EBA1
               s2 = "Hello, world!";                           // 8E9A7706 / EBE6C6E6
        byte[] b1 = s1.getBytes(), b2 = s2.getBytes();  // byte[] に変換

      // CRC値を１６進数で表示する。
        System.out.println("method1(" + s1 + ") = "
            + Integer.toHexString( method1(b1) ).toUpperCase());
        System.out.println("method2(" + s1 + ") = "
            + Integer.toHexString( method2(b1) ).toUpperCase());
        System.out.println("method1(" + s2 + ") = "
            + Integer.toHexString( method1(b2) ).toUpperCase());
        System.out.println("method2(" + s2 + ") = "
            + Integer.toHexString( method2(b2) ).toUpperCase());


      // Java のCRC32はmethod2()と同じ値を返す。
        java.util.zip.CRC32 javaCRC32s1 = new java.util.zip.CRC32();
        java.util.zip.CRC32 javaCRC32s2 = new java.util.zip.CRC32();
        System.out.println("\n---------- CRC32() of Java -------------- ");
        javaCRC32s1.update(b1);
        System.out.println(s1 + " = "
            + Long.toHexString(javaCRC32s1.getValue()).toUpperCase());
        javaCRC32s2.update(b2);
        System.out.println(s2 + " = "
            + Long.toHexString(javaCRC32s2.getValue()).toUpperCase());
    }
}