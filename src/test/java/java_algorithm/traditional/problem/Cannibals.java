package java_algorithm.traditional.problem;

/**
 *  Cannibals.java -- 宣教師と人食い人
 */

class Cannibals {

    static final int M = 3;     // 宣教師の数
    static final int C = 3;     // 人食い人の数
    static final int B = 2;     // ボートの定員

    static int np = 0;
    static int solution = 0;
    static int[] mb = new int[(B+1)*(B+2)/2];
    static int[] cb = new int[(B+1)*(B+2)/2];
    static int[] mh = new int[2*(M+1)*(C+1)];
    static int[] ch = new int[2*(M+1)*(C+1)];
    static boolean[][][] flag = new boolean[M+1][C+1][2];

    static void found(int n) {  // 解の表示
        String mmm = "          MMMMMMMMMM";
        String ccc = "          CCCCCCCCCC";
        System.out.println("解 " + (++solution));
        for (int i = 0; i <= n; i++) {
            String si = "   " + i;
            System.out.println(
                si.substring(si.length() - 4) + "  "
                + mmm.substring(mh[i], mh[i] + 10) + " "
                + ccc.substring(ch[i], ch[i] + 10) + " / "
                + mmm.substring(M - mh[i], M - mh[i] + 10) + " "
                + ccc.substring(C - ch[i], C - ch[i] + 10));
        }
    }

    static void doit(int i) {    // 再帰的に試す
        for (int j = 1; j < np; j++) {
            int m, c;
            if (i % 2 != 0) {   // 奇数回目は向こうに行く
                m = mh[i - 1] - mb[j];  c = ch[i - 1] - cb[j];
            } else {            // 偶数回目はこちらに来る
                m = mh[i - 1] + mb[j];  c = ch[i - 1] + cb[j];
            }
            if (m < 0 || c < 0 || m > M || c > C || flag[m][c][i % 2])
                continue;
            mh[i] = m;  ch[i] = c;
            if (m == 0 && c == 0) {
                found(i);
            } else {
                flag[m][c][i % 2] = true;
                doit(i + 1);
                flag[m][c][i % 2] = false;
            }
        }
    }

    public static void main(String[] args) {
        for (int m = 0; m <= B; m++) {
            for (int c = 0; c <= B - m; c++) {
                if (m == 0 || m >= c) {
                    mb[np] = m;  cb[np] = c;  np++;
                }
            }
        }
        for (int m = 0; m <= M; m++)
            for (int c = 0; c <= C; c++)
                flag[m][c][0] = flag[m][c][1] = 
                    ((m > 0 && m < c) || (m < M && M - m < C - c));
        mh[0] = M;  ch[0] = C;  flag[M][C][0] = true;
        doit(1);
        if (solution == 0) System.out.println("解はありません");
    }
}