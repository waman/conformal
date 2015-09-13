package java_algorithm.datatype.number.integral.combination;

/**
* Isomer.java -- 異性体の問題
*/
/**
*異性体の問題を解きます。
*/
public class Isomer {

    static final int C  = 17;   // 炭素原子の数の上限
    static final int L  = 2558; // 生成する基の個数の上限
    static int size[]   = new int[L];
    static int length[] = new int[L];
    static int count[]  = new int[C + 1];

    /**
    *炭素原子の数の上限を 17 として，異性体の数を表示します。
    */
    public static void main(String[] args) {
        int len = 0, n = 0;

//        n = size[0] = length[0] = 0;
        for (int i = 0; i < L; i++) {
            len = length[i] + 1;
            if (len > C / 2) break;
            int si = size[i] + 1;
            if (si + len > C) continue;

            for (int j = 0; j <= i; j++) {
                int sj = si + size[j];
                if (sj + len > C) continue;

                for (int k = 0; k <= j; k++) {
                    int sk = sj + size[k];
                    if (sk + len > C) continue;
                    if (++n >= L) System.exit(-1);
                    size[n] = sk;
                    length[n] = len;
                }
            }
        }
        if (len <= C / 2) System.exit(-1);
        for (int i = 0; i <= n; i++) {
            int si = size[i];
            for (int j = 0; j <= i; j++) {
                if (length[i] != length[j]) continue;
                int sj = si + size[j];
                if (sj > C) continue;
                count[sj]++;  // 偶数
                for (int k = 0; k <= j; k++) {
                    int sk = sj + size[k] + 1;
                    if (sk > C) continue;
                    for (int h = 0; h <= k; h++) {
                        int sh = sk + size[h];
                        if (sh <= C) count[sh]++;  // 奇数
                    }
                }
            }
        }
        for (int i = 1; i <= C; i++)
            System.out.println("炭素原子が " + i + " 個のものは " + count[i] + " 種類");
    }
}