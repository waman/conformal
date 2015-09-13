package java_algorithm.computerscience.security;

/*
 *  RSA のサンプル
 *  DES の秘密鍵を RSA を使って暗号化する
 */

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

public class RSA {

    RSAPrivateKey privateKey;   // Alice の秘密鍵
    RSAPublicKey publicKey;     // Alice の公開鍵

    public RSA() throws Exception {
        // RSA 用鍵生成
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 乱数生成アルゴリズム選択
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // 乱数の種を設定
        random.setSeed("Alice 090-1234-5678".getBytes());
        // 1024 bit の鍵サイズ
        keyPairGen.initialize(1024, random);
        KeyPair keyPair = keyPairGen.genKeyPair();
        privateKey = (RSAPrivateKey)keyPair.getPrivate();
        publicKey = (RSAPublicKey)keyPair.getPublic();
        verify(privateKey, publicKey);
    }

    void verify(RSAPrivateKey priv, RSAPublicKey pub) {
        // 確認のためにn=pq と ed = 1 mod φ(n), φ(n) = (p-1)(q-1) をチェック
        System.out.println("verify RSA parameter------");
        RSAPrivateCrtKey crt = (RSAPrivateCrtKey)priv;
        BigInteger p = crt.getPrimeP(), q = crt.getPrimeQ();
        System.out.println("p=" + p + "\nq=" + q);
        System.out.println("n=" + priv.getModulus());
        BigInteger n = p.multiply(q);
        System.out.println("pq is equal to n:" + n.equals(priv.getModulus()));
        BigInteger phi_n = p.subtract(BigInteger.ONE);
        phi_n = phi_n.multiply(q.subtract(BigInteger.ONE));
        BigInteger e = priv.getPrivateExponent();
        BigInteger d = pub.getPublicExponent();
        System.out.println("e=" + e + "\nd=" + d);
        System.out.println("ed mod (p-1)(q-1)=" + e.multiply(d).mod(phi_n));
        System.out.println("--------------------------");
    }

    // DES による暗号化と復号
    static public void useDES(int mode, Key key, InputStream in,
                              OutputStream out) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(mode, key);
        byte[] data = new byte[1024];
        int len;
        while ((len = in.read(data)) != -1) {
            out.write(cipher.update(data, 0, len));
        }
        byte[] bytes = cipher.doFinal();
        if (bytes != null) out.write(bytes);
        out.flush();
    }

    public static void main(String args[]) throws Exception {
        // DES 初期化
        SecretKeyFactory keyFactory
                = SecretKeyFactory.getInstance("DES");

        // Alice が RSA の鍵を生成し publicKey のみを公開する
        RSA test = new RSA();
        RSAPublicKey pub = test.publicKey;

        // Bob は Alice に暗号文を送る -------------------------
        // Bob は適当な数字(secretNum)を選ぶ
        System.out.println("Bob 側");
        Random rand = new Random(1234);
        BigInteger secretNum = new BigInteger(100, rand);
        System.out.println("secretNum =" + secretNum);

        // それを元に DES の鍵を生成する(先頭8byteのみが使用される)
        DESKeySpec keySpec = new DESKeySpec(secretNum.toByteArray());
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        FileInputStream in = new FileInputStream("RSA.java");
        FileOutputStream out = new FileOutputStream("RSA.java.encoded");

        // DES で暗号化する
        useDES(Cipher.ENCRYPT_MODE, secretKey, in, out);
        in.close(); out.close();

        // Bob は Alice の公開鍵(pub)を用いて secretNum を暗号化する
        BigInteger encodedNum = secretNum.modPow(pub.getPublicExponent(), pub.getModulus());
        System.out.println("encodedNum=" + encodedNum);

        // Alice は Bob からの暗号文を復号する　----------------
        System.out.println("Alice 側");
        in = new FileInputStream("RSA.java.encoded");
        out = new FileOutputStream("RSA.java.decoded");

        // 秘密鍵(priv)を用いて secretNum を復号する
        RSAPrivateKey priv = test.privateKey;
        BigInteger decodedNum = encodedNum.modPow(priv.getPrivateExponent(), priv.getModulus());
        System.out.println("decodedNum=" + decodedNum);

        // Alice は復号した数字(decodedNum)を用いて DES の鍵を復元する
        keySpec = new DESKeySpec(decodedNum.toByteArray());
        secretKey = keyFactory.generateSecret(keySpec);

        // DES で復号する
        useDES(Cipher.DECRYPT_MODE, secretKey, in, out);
        in.close(); out.close();
    }
}