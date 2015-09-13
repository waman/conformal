package java_algorithm.datatype.number.integral.combination;

/**
 *  組合せの生成
 */
public class NextCombination {

    final int n, k, a[];

    public NextCombination(int n, int k) {
        this.n = n;
        this.k = k;
        a = new int[k + 1];
        for (int i = 1; i <= k; i++) a[i] = i;
    }

    public boolean next() {  // 辞書式順序で次の組合せ
        a[0] = n;  // 番人
        int i = k;
        while (a[i] == n - k + i) i--;
        if (i == 0) return false;  // 完了
        a[i]++;
        while (++i <= k) a[i] = a[i - 1] + 1;
        return true;  // 未了
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 1; i <= k; i++) s += "  " + a[i];
        return s;
    }

    public static void main(String[] args) {
        NextCombination c = new NextCombination(6, 4);
        int count = 0;
        do {
            System.out.println((++count) + ":" + c);
        } while (c.next());
    }
}