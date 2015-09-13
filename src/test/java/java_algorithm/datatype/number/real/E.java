package java_algorithm.datatype.number.real;

/*
* E.java -- 自然対数の底（double 版）
*/
/**
* 自然対数の底 e の double 版です。
* @see BinarySplitE
*/
public class E {

    /**
    * 自然対数の底 e を級数を使って計算します。
    */
    public static double getValue() {
        int n = 1;
        double e = 0, a = 1, prev;

        do {
            prev = e;
            e += a;
            a /= n;
            n++;
        } while (e != prev);
        return e;
    }

    /**
    * 自然対数の底 e を double で表示します。
    */
    public static void main(String[] args) {
        System.out.println("e = "+ getValue());
    }
}