package java_algorithm.datatype.number.integral.combination;

/**
 *  順列 -- 階乗進法との相互変換
 */
public class PermutationFactorial {

    final int n, a[];

    public PermutationFactorial(int n) {
        this.n = n;
        a = new int[n + 1];
        for (int i = 0; i < n; i++) a[i] = n - i - 1;
    }

    public void encode() {
        for (int j = n - 1; j > 0; j--)
            for (int k = 0; k < j; k++)
                if (a[k] > a[j]) a[k]--;
    }

    public void decode() {
        for (int j = 1; j < n; j++)
            for (int k = j - 1; k >= 0; k--)
                if (a[k] >= a[j]) a[k]++;
    }

    public boolean next() {  // 辞書式順序で次の順列
        a[n] = -1;  // 番人
        int i = 1;
        while (a[i] >= a[i - 1]) i++;
        if (i == n) return false;  // 完了
        int j = 0;
        while (a[i] >= a[j]) j++;

        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;

        i--;  j = 0;
        while (i > j) {
            swap = a[i];
            a[i] = a[j];
            a[j] = swap;
            i--;
            j++;
        }
        return true;  // 未了
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = n - 1; i >= 0; i--) s += "  " + a[i];
        return s;
    }

    public String toString2() {
        String s = "";
        for (int i = n - 1; i > 0; i--) s += "  " + a[i];
        return s;
    }

    public static void main(String[] args) {
        PermutationFactorial p = new PermutationFactorial(4);
        int count = 0;
        do {
            System.out.println((++count) + ":" + p);
            p.encode();
            System.out.println((count) + ":" + p.toString2());
            p.decode();
            System.out.println((count) + ":" + p);
            System.out.println();
        } while (p.next());
    }
}