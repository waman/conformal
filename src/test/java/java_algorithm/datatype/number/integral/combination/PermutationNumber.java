package java_algorithm.datatype.number.integral.combination;

/**
 *  順列 -- 自然数との相互変換
 */

public class PermutationNumber {

    final int n, a[];

    public PermutationNumber(int n) {
        this.n = n;
        a = new int[n + 1];
        for (int i = 1; i <= n; i++) a[i] = i;
    }

    public int encode() {
        int c = 0;
        for (int i = n; i > 1; i--) {
            int k = 1;
            for (int j = 2; j <= i; j++)
                if (a[j] > a[k]) k = j;

            int swap = a[i];
            a[i] = a[k];
            a[k] = swap;

            c = c * i + k - 1;
        }
        return c;
    }

    public void decode(int c) {
        for (int i = 2; i <= n; i++) {
            int k = c % i + 1;
            c /= i;

            int swap = a[i];
            a[i] = a[k];
            a[k] = swap;
        }
    }

    public boolean next() {  // 辞書式順序で次の順列
        a[0] = 0;  // 番人
        int i = n - 1;
        while (a[i] >= a[i + 1]) i--;
        if (i == 0) return false;  // 完了
        int j = n;
        while (a[i] >= a[j]) j--;

        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;

        i++;
        j = n;
        while (i < j) {
            swap = a[i];  a[i] = a[j];  a[j] = swap;  i++;  j--;
        }
        return true;  // 未了
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 1; i <= n; i++) s += "  " + a[i];
        return s;
    }

    public static void main(String[] args) {
        PermutationNumber p = new PermutationNumber(4);
        int count = 0;
        do {
            System.out.println((++count) + ":" + p);
            int c = p.encode();
            System.out.println("code = " + c);
            System.out.println((count) + ":" + p);
            p.decode(c);
            System.out.println((count) + ":" + p);
            System.out.println();
        } while (p.next());
    }
}