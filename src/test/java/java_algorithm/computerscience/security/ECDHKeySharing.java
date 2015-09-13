package java_algorithm.computerscience.security;

/*
 *  楕円曲線を用いた鍵交換
 */

import java.math.BigInteger;

class ECPoint {

    static final BigInteger ZERO = BigInteger.ZERO;
    // y^2 = x^3 + a_ x + b_ mod (p_) の楕円曲線を一つ固定
    // 位数1461501637330902918203683518218126812711137002561の群
    static final BigInteger a_ = BigInteger.valueOf(10);
    static final BigInteger b_ = new BigInteger("1343632762150092499701637438970764818528075565078");
    // p = 2^160 + 7;
    static final BigInteger p_ = new BigInteger("1461501637330902918203684832716283019655932542983");
    private BigInteger x_, y_;  // 楕円曲線の点のアフィン座標
    private boolean isZero_;    //trueなら無限遠点(このときx_, y_は意味をもたない)

    void setZero() {
        isZero_ = true;
    }

    // E={y^2=x^3+ax+b}上に点(x, y)があるかどうかをチェック
    static boolean isValid(BigInteger x, BigInteger y) {
        BigInteger diff;
        diff = x.multiply(x).add(a_).multiply(x).add(b_);
        diff = diff.subtract(y.multiply(y));
        return diff.mod(p_).compareTo(ZERO) == 0;
    }

    // コンストラクタ
    ECPoint() {
        x_ = y_ = ZERO; isZero_ = true;
    }

    ECPoint(BigInteger x, BigInteger y) {
        x_ = x.mod(p_); y_ = y.mod(p_);
        isZero_ = false;
        if (!isValid(x_, y_)) throw new IllegalArgumentException("E上にありません");
    }

    @Override
    public String toString() {
        if (isZero_) {
            return "O";
        } else {
            return "(" + x_.toString() + ", " + y_.toString() + ")";
        }
    }

    // 加法の定義
    ECPoint add(ECPoint rhs) {
        ECPoint ret = new ECPoint();
        BigInteger lambda, tmp;
        if (isZero_) {                  // P1 = 0
            ret.x_ = rhs.x_; ret.y_ = rhs.y_; ret.isZero_ = rhs.isZero_;
            return ret;
        } else if (rhs.isZero_) {       // P2 = 0
            ret.x_ = x_; ret.y_ = y_; ret.isZero_ = isZero_;
            return ret;
        } else if (x_.compareTo(rhs.x_) == 0) {
            // P1 = -P2
            if (y_.add(rhs.y_).mod(p_).compareTo(ZERO) == 0) {
                ret.isZero_ = true;
                return ret;
            }
            // P1 = P2
            lambda = x_;
            tmp = y_;
            lambda = lambda.multiply(lambda);
            lambda = lambda.multiply(BigInteger.valueOf(3));
            lambda = lambda.add(a_);
            tmp = tmp.add(tmp);
            lambda = lambda.multiply(tmp.modInverse(p_));    // (3x1^2+a)/(2y1)
        } else {
            // generic
            lambda = y_.subtract(rhs.y_);
            tmp = x_.subtract(rhs.x_);
            lambda = lambda.multiply(tmp.modInverse(p_));    // (y2-y1)/(x2-x1)
        }
        tmp = lambda.multiply(lambda).subtract(x_).subtract(rhs.x_);
        ret.y_ = x_.subtract(tmp).multiply(lambda).subtract(y_).mod(p_);
        ret.x_ = tmp.mod(p_);
        ret.isZero_ = false;
        return ret;
    }

    // マイナス記号の定義
    ECPoint negate() {
        ECPoint ret = new ECPoint();
        ret.x_ = x_; ret.y_ = y_.negate(); ret.isZero_ = isZero_;
        return ret;
    }

    // 減法の定義
    ECPoint subtract(ECPoint rhs) {
        return add(rhs.negate());
    }

    // 整数倍の定義
    ECPoint multiply(BigInteger m) {
        int len = m.bitLength();
        ECPoint ret = new ECPoint();
        for (int i = len - 1; i >= 0; i--) {
            ret = ret.add(ret);
            if (m.testBit(i)) {
                ret = ret.add(this);
            }
        }
        return ret;
    }

    ECPoint multiply(long m) {
        return multiply(BigInteger.valueOf(m));
    }
}

class ECDHKeySharing {

    // 生成元を一つ固定
    private static final ECPoint P_ = new ECPoint(
            BigInteger.ONE,
            new BigInteger("224889247379440766542528101180966881215948963699"));

    // 各ユーザの秘密鍵と公開鍵
    private final BigInteger privateKey;
    public final ECPoint publicKey;

    public ECDHKeySharing(BigInteger privateKey) {
        this.privateKey = privateKey;
        publicKey = P_.multiply(privateKey);
    }

    // 鍵共有
    public ECPoint getCommonKey(ECDHKeySharing other) {
        return other.publicKey.multiply(privateKey);
    }

    public static void main(String[] args) {
        // 位数のチェック
//      System.out.println("mP=" + P_.multiply(
//      new BigInteger("1461501637330902918203683518218126812711137002561")));
        ECDHKeySharing alice = new ECDHKeySharing(BigInteger.valueOf(1234));
        ECDHKeySharing bob   = new ECDHKeySharing(BigInteger.valueOf(5678));

        System.out.println("alice.publicKey  = " + alice.publicKey);
        System.out.println("bob.publicKey    = " + bob.publicKey);
        System.out.println("alice common key = " + alice.getCommonKey(bob));
        System.out.println("bob   common key = " + bob.getCommonKey(alice));
    }
}