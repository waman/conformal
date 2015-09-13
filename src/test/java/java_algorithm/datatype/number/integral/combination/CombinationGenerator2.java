package java_algorithm.datatype.number.integral.combination;

/**
 *  組合せの生成
 *  CombinationGenerator.java とは終了条件が異なる実装(本文未収録)
 */
public class CombinationGenerator2 {

    final private int n;
    private long x;

    public CombinationGenerator2(int n, int k) {
        this.n = n;
        //  11...100....00
        //        <- k個->
        //  <--- n個 ---->
        x = ((1L << (n-k)) - 1) << k;
    }

    public long getValue() { return x; }

    public boolean next() {
        long b = x ^ (x+1);
        long c = x - b/2;
        x = c - (c & -c) / (b+1);
        return x != 0; // false で完了
    }

    @Override
    public String toString() {
        String s = "";
        long x = this.x;
        for (int i = 1; i <= n; i++) {
            if ((x & 1) == 0) s = s + i + " ";
            x >>>= 1;
        }
        return s;
    }

    public static void main(String[] args) {
        int n, k;
        if (args.length < 2
            || (n = Integer.parseInt(args[0])) > 63
            || (k = Integer.parseInt(args[1])) > n) {
            System.err.println("java CombinationGenerator2 n k");
            System.err.println("    n < 63 and k <= n");
            return;
        }
        System.out.println("n = " + n + ", k = " + k);

        CombinationGenerator2 c = new CombinationGenerator2(n, k);
        if (c.getValue() == 0) { // n == k のときの例外処理
            System.out.println(c);
        } else {
            do {
                System.out.println(c);
            } while (c.next());
        }
    }
}