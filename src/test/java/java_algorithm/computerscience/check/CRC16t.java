package java_algorithm.computerscience.check;

/*
*  CRC16t.java -- CRC
*/
/**
* 表を使って 16 ビットの CRC を 2 つの方法で求めます。
*@see GetCRC
*/
public class CRC16t {

    static final int CRCPOLY1   = 0x1021; // x^{16}+x^{12}+x^5+1
    static final int CRCPOLY2   = 0x8408; // 左右逆転
    static final int BYTE_BIT   = 8;      // １バイトのビット数
    static final int TABLE_SIZE = 1 << BYTE_BIT; // = 256
    private static int[] crcTable = new int[TABLE_SIZE];

    /**
    *方法 1 の補助の表を作ります。
    */
    public static void makeCrcTable1() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            int r = i << (16 - BYTE_BIT);
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 0x8000) != 0) r = (r << 1) ^ CRCPOLY1;
                else                   r <<= 1;
            crcTable[i] = r & 0xFFFF;
        }
    }

    /**
    * 16 ビットの CRC を方法 1 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method1(byte c[]) {
        int r = 0xFFFF, i = 0, n = c.length;

        while (--n >= 0)
            r = (r << BYTE_BIT) ^
                crcTable[((r >> (16 - BYTE_BIT)) ^ c[i++]) & 0xFF];
        return ~r & 0xFFFF;
    }

    /**
    *方法 2 の補助の表を作ります。
    */
    public static void makeCrcTable2() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            int r = i;
            for (int j = 0; j < BYTE_BIT; j++)
                if ((r & 1) != 0) r = (r >> 1) ^ CRCPOLY2;
                else              r >>= 1;
            crcTable[i] = r;
        }
    }

    /**
    * 16 ビットの CRC を方法 2 で求めます。
    *@param c データを与えます。
    *@return CRC 値を返します。
    */
    public static int method2(byte c[]) {
        int r = 0xFFFF, i = 0, n = c.length;

        while (--n >= 0)
            r = (r >> BYTE_BIT) ^ crcTable[(r ^ c[i++]) & 0xFF];
        return r ^ 0xFFFF;
    }

    /**
    * 以下はテスト用（２バイト文字のテストを含む）
    */
    public static void main(String[] args) {
                                                               // crc1 / crc2
        String s1 = "Java では char は16ビット符号無し整数。", // 4F4F / E456
               s2 = "Hello, world!";                           // AD2D / 1EB5
        byte[] b1 = s1.getBytes(), b2 = s2.getBytes();  // byte[] に変換

      // crc値を１６進数で表示する。
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
    }
}