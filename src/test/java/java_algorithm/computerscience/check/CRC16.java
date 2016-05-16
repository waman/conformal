package java_algorithm.computerscience.check;

/*
* CRC16.java -- CRC
*/
/**
* 16 ビットの CRC を 2 つの方法で求めます。
*/
public class CRC16 {

    static final int CRCPOLY1 = 0x1021;  // x^{16}+x^{12}+x^5+1
    static final int CRCPOLY2 = 0x8408;  // 左右逆転
    static final int BYTE_BIT = 8;       // １バイトのビット数
    /**
    * 16 ビットの CRC を方法 1 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method1(byte c[]) {
        int r = 0xFFFF;

        for (byte ci : c) {
            r ^= (ci & 0xFF) << (16 - BYTE_BIT);
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 0x8000) != 0) r = (r << 1) ^ CRCPOLY1;
                else                   r <<= 1;
        }
        return ~r & 0xFFFF;
    }
    /**
    * 16 ビットの CRC を方法 2 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method2(byte c[]) {
        int r = 0xFFFF;

        for (byte ci : c) {
            r ^= (ci & 0xFF);
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 1) != 0) r = (r >> 1) ^ CRCPOLY2;
                else              r >>= 1;
        }
        return r ^ 0xFFFF;
    }
    /**
    * テスト用（２バイト文字のテストを含む）
    */
    public static void main(String[] args) {
                                                               // method1 / method2
        String s1 = "Java では char は16ビット符号無し整数。", // 4F4F / E456
               s2 = "Hello, world!";                           // AD2D / 1EB5
        byte[] b1 = s1.getBytes(), b2 = s2.getBytes();  // byte[] に変換

      // CRC 値を１６進数で表示する。
        System.out.println("method1(" + s1 + ") = "
            + Integer.toHexString( method1(b1) ).toUpperCase());
        System.out.println("method2(" + s1 + ") = "
            + Integer.toHexString( method2(b1) ).toUpperCase());

        System.out.println("method1(" + s2 + ") = "
            + Integer.toHexString( method1(b2) ).toUpperCase());
        System.out.println("method2(" + s2 + ") = "
            + Integer.toHexString( method2(b2) ).toUpperCase());
    }
}