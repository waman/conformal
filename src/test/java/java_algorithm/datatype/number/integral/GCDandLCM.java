package java_algorithm.datatype.number.integral;

/*
* GCDandLCM.java -- 最大公約数を J. Stein の方法で求める。
* ２進gcdアルゴリズム
*/

/**
* 最大公約数を J. Stein の方法（２進gcdアルゴリズム）で求めます。
* @see GCD
*/
public class GCDandLCM {

    /**
    * ２つの自然数の最大公約数を２進gcdアルゴリズムで求めます。
    * @param u 自然数
    * @param v 自然数
    * @return <code>gcd(u,v)</code>
    */
    public static long gcd(long u, long v) {
        if (u <= 0 || v <= 0) throw new Error("エラー : gcd(u,v)");
        if (u == 1 || v == 1) return 1;
        long d = 1, t;
        while ((u % 2) == 0 && (v % 2) == 0) { // u, v どちらかが奇数になるまで2で割る。
            d *= 2;  u /= 2;  v /= 2; // d に除数を記憶しておく
        }
        if ((u % 2) != 0) t = -v;
        else              t = u;
        while (t != 0) {
            while ((Math.abs(t) % 2) == 0) t /= 2;
            if (t > 0) u =  t;
            else       v = -t;
            t = u - v;
        }
        return u * d;
    }

    /**
    * ２つの自然数の最小公倍数を求めます。
    * @param a 自然数
    * @param b 自然数
    * @return <code>lcm(a,b)</code>
    */ 
    public static long lcm(long a, long b) {
        // 少しでも桁あふれを防止するために大きい方を先に割る。
        return a > b ? (a / gcd(a, b)) * b : a * (b / gcd(a, b));
    }

    /**
    * テスト用です。
    */
    public static void main(String[] args) {
        long a = 40902, b = 24140;

        System.out.println("GCD(" + a + "," + b + ") = " + gcd(a, b)); // 34 になるはず
        System.out.println("LCM(" + a + "," + b + ") = " + lcm(a, b));
    }
}