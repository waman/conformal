package java_algorithm.datatype.number.integral.combination;

/**
 *  順列
 */

public class NextPermutation {

    final int n, a[];

    public NextPermutation(int n) {
        this.n = n;
        a = new int[n + 1];
        for (int i = 1; i <= n; i++) a[i] = i;
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

        i++;  j = n;
        while (i < j) {
            swap = a[i];
            a[i] = a[j];
            a[j] = swap;
            i++;
            j--;
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
        NextPermutation p = new NextPermutation(6);
        int count = 0;
        do {
            System.out.println((++count) + ":" + p);
        } while (p.next());
    }
}