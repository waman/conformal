package java_algorithm.equation.algebraic;

/**
 *  LU 分解及びそれを使った連立方程式を解くルーチン
 */
public final class LU {

    private LU() {}

    /**
     *  行列 a を LU 分解する。<br>
     *  部分ピボット選択を行うが，実際に行を交換するのではなく，
     *  行の番号だけを交換し，その情報を整数の配列 ivec に入れて戻る。
     *  この ivec は後出の solve() を使って実際に連立方程式を解く際に
     *  必要になるものである。
     *
     *  @param  a   LU 分解する行列(分解の結果で上書きされる)
     *  @param  ivec    行の交換情報を格納する配列
     *  @return a の行列式。これが 0 なら LU 分解できないことを意味する。
     *  @see    #solve
     */
    public static double lu(double[][] a, int[] ivec) {
        int n = a.length;
        double[] weight = new double[n];
        for (int k = 0; k < n; k++) {   //  各行について
            ivec[k] = k;    //  行交換情報の初期値
            double u = 0;   //  その行の絶対値最大の要素を求める
            for (int j = 0; j < n; j++) {
                double t = Math.abs(a[k][j]);
                if (t > u) u = t;
            }
            if (u == 0) return 0;   //  0 なら行列は LU 分解できない
            weight[k] = 1 / u;      //  最大絶対値の逆数
        }
        double det = 1;     //  行列式の初期値
        for (int k = 0, m = n; k < n; k++) {    //  各行について
            double u = -1;
            for (int i = k; i < n; i++) {   //  より下の各行について
                int ii = ivec[i];   //  重み×絶対値 が最大の行を見つける
                double t = Math.abs(a[ii][k]) * weight[ii];
                if (t > u) { u = t; m = i; }
            }
            int ik = ivec[m];
            if (m != k) {
                ivec[m] = ivec[k];
                ivec[k] = ik;    //  行番号を交換
                det = -det;     //  行を交換すれば行列式の符号が変わる
            }
            u = a[ik][k]; det *= u;  //  対角成分
            if (u == 0) return 0;   //  0 なら行列は LU 分解できない
            for (int i = k + 1; i < n; i++) {   //  Gauss 消去法
                int ii = ivec[i];
                a[ii][k] /= u;
                double t = a[ii][k];
                for (int j = k + 1; j < n; j++)
                    a[ii][j] -= t * a[ik][j];
            }
        }
        return det;     //  戻り値は行列式
    }

    /**
     *  lu() で LU 分解した行列 a を使って連立１次方程式 a x = b を解く。
     *
     *  @param  a   LU 分解済みの行列
     *  @param  b   連立方程式の右辺
     *  @param  ivec    行の交換情報が格納された配列
     *  @param  x   解を格納するベクトル
     *  @see    #lu
     */
    public static void solve(double[][] a, double[] b, int[] ivec, double[] x) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int ii = ivec[i];
            double t = b[ii];
            for (int j = 0; j < i; j++) t -= a[ii][j] * x[j];
            x[i] = t;
        }
        for (int i = n - 1; i >= 0; i--) {
            double t = x[i];
            int ii = ivec[i];
            for (int j = i + 1; j < n; j++) t -= a[ii][j] * x[j];
            x[i] = t / a[ii][i];
        }
    }

    /**
     *  lu(), solve() を順に呼び出して連立方程式 a x = b を解く。<br>
     *  連立方程式が１組だけならこれを使えばよいが，何組もあるなら
     *  最初に１回だけ lu() を呼び出し，あとで solve() を必要なだけ
     *  呼び出す方が速い。
     *
     *  @param  a   連立方程式の係数行列
     *  @param  b   連立方程式の右辺
     *  @param  x   解を格納するベクトル
     *  @return a の行列式。これが 0 なら連立方程式は不定(解が定まらない)
     *          または不能(解が存在しない)。
     *  @see    #lu
     *  @see    #solve
     */
    public static double gauss(double[][] a, double[] b, double[] x) {
        int n = a.length; int[] ivec = new int[n];
        double det = lu(a, ivec);
        if (det != 0) solve(a, b, ivec, x);
        return det;
    }
}