package java_algorithm.random;

/**
 *  CongruRandom.java -- 線形合同法による乱数の発生
 */
public class CongruRandom {

    private long seed = 1;

    public CongruRandom(long x){   // コンストラクタによる乱数の初期化
        seed = x % 0x100000000L;
    }

    public void init_rnd(long x){ // メソッドによる乱数の初期化
        seed = x % 0x100000000L;
    }

    public long irnd(){    // 32ビット整数 乱数
        seed = (seed * 1566083941L + 1) % 0x100000000L;

        return seed;
    }

    public double rnd(){   // Double 乱数
        return ( 1.0 / 0x100000000L ) * irnd();
    }

    public static void main(String[] args){
        CongruRandom r = new CongruRandom(12345);

        for ( int i = 0 ; i < 10 ; i++)
               System.out.println(r.rnd());
    }
}