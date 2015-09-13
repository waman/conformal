package java_algorithm.computerscience.security;

/*
 *    DHP(Diffie-Hellman problem)の困難さを利用した鍵共有法
 */
import java.math.BigInteger;

class DHKeySharing {

    // 全ての鍵ユーザに共通
    private static final BigInteger g;  // 原始元
    private static final BigInteger p;  // 素数

    // 各ユーザの秘密鍵と公開鍵
    private final BigInteger privateKey;
    public final BigInteger publicKey;

    public DHKeySharing(BigInteger privateKey) {
        this.privateKey = privateKey;
        publicKey = g.modPow(privateKey, p);
    }
/*
    // base^index mod g を計算する
    // modPow の一つの実装
    private static BigInteger power(BigInteger base, BigInteger index) {
        int bitLength = index.bitLength();
        BigInteger ret = BigInteger.ONE;
        for (int i = 0; i < bitLength; i++) {

            if ((index.intValue() & 1) == 1) {
                ret = ret.multiply(base);
                if (ret.compareTo(p) >= 0) {
                    ret = ret.mod(p);
                }
            }
            index = index.shiftRight(1);
            base = base.multiply(base);
            if (base.compareTo(p) >= 0) {
                base = base.mod(p);
            }
        }
        return ret;
    }
*/
    // 鍵共有
    public BigInteger getCommonKey(DHKeySharing other) {
        return other.publicKey.modPow(privateKey, p);
    }

    // mod p での原始根 g を1つ固定
    static {
        g = new BigInteger("3");
        p = new BigInteger("1000000000000000000000000000057");
    }

    public static void main(String[] args) {
        DHKeySharing alice = new DHKeySharing(BigInteger.valueOf(1234));
        DHKeySharing bob   = new DHKeySharing(BigInteger.valueOf(5678));

        System.out.println("alice.publicKey  = " + alice.publicKey);
        System.out.println("bob.publicKey    = " + bob.publicKey);
        System.out.println("alice common key = " + alice.getCommonKey(bob));
        System.out.println("bob   common key = " + bob.getCommonKey(alice));
    }
}