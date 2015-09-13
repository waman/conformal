package java_algorithm.algebra.linear;

/**
 *  行列クラス：
 *
 *  行列（double 型の２次元配列）に対する各種演算メソッドを提供します。
 */
public class RMatrix {

    /**
     *  行列の i 番目の行と j 番目の行を交換する
     *  @param  i, j    行番号(0,1,..)
     */
    public static void swapRow(double[][] mtx, int i, int j) {
        double[] temp = mtx[i];
        mtx[i] = mtx[j];
        mtx[j] = temp;
    }

    /**
     *  行列の i 番目の列と j 番目の列を交換する
     *  @param  i, j    列番号(0,1,..)
     */
    public static void swapCol(double[][] mtx, int i, int j) {
        for (int k = 0; k < mtx.length; k++) {
            double temp = mtx[k][i];
            mtx[k][i] = mtx[k][j];
            mtx[k][j] = temp;
        }
    }

    /**
     *  転置行列を返します。
     *  @return mtx<sup>T</sup>
     */
    public static double[][] transpose(double[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        double[][] ret = new double[m][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[j][i] = mtx[i][j];
        return ret;
    }

    /**
     *  値が (lhs + rhs) となる行列を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs + rhs)
     */
    public static double[][] add(double[][] lhs, double[][] rhs) {
        int n = lhs.length, m = lhs[0].length;

        if (n != rhs.length || m != rhs[0].length)
            throw new IndexOutOfBoundsException();

        double[][] ret = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[i][j] = lhs[i][j] + rhs[i][j];
        return ret;
    }

    /**
     *  値が (lhs - rhs) となる行列を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs - rhs)
     */
    public static double[][] subtract(double[][] lhs, double[][] rhs) {
        int n = lhs.length, m = lhs[0].length;

        if (n != rhs.length || m != rhs[0].length)
            throw new IndexOutOfBoundsException();

        double[][] ret = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[i][j] = lhs[i][j] - rhs[i][j];
        return ret;
    }
    /**
     *  値が (a * mtx) となる行列を返します。
     *  @param  a   行列にかける値
     *  @param  mtx 行列
     *  @return (a * mtx)
     */
    public static double[][] multiply(double a, double[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        double[][] ret = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                ret[i][j] = a * mtx[i][j];
        return ret;
    }

    /**
     *  値が (lhs * rhs) となる行列を返します。
     *  @param  lhs 左辺
     *  @param  rhs 右辺
     *  @return (lhs * rhs)
     */
    public static double[][] multiply(double[][] lhs, double[][] rhs) {
        int n = lhs.length, m = rhs[0].length, l = lhs[0].length;

        if (l != rhs.length)
            throw new IndexOutOfBoundsException();

        double[][] ret = new double[n][m];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++) {
                double value = 0.0;
                for (int k = 0; k < l; k++)
                    value += lhs[i][k] * rhs[k][j];
             ret[i][j] = value;
            }
        }
        return ret;
    }

    /**
     *  値が (mtx * v) となるベクトルを返します。
     *  @param  mtx 行列
     *  @param  v   ベクトル
     *  @return (mtx * v)
     */
    public static double[] multiply(double[][] mtx, double[] v) {
        int n = mtx.length, m = mtx[0].length;

        if (m != v.length)
            throw new IndexOutOfBoundsException();

        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            double value = 0.0;
            for(int j = 0; j < m; j++)
                value += mtx[i][j] * v[j];
            ret[i] = value;
        }
        return ret;
    }
    /**
     *  値が (v * mtx) となるベクトルを返します。
     *  @param  v   ベクトル
     *  @param  mtx 行列
     *  @return (v * mtx)
     */
    public static double[] multiply(double[] v, double[][] mtx) {
        int n = mtx.length, m = mtx[0].length;

        if (n != v.length)
            throw new IndexOutOfBoundsException();

        double[] ret = new double[m];
        for (int i = 0; i < m; i++) {
            double value = 0.0;
            for(int j = 0; j < n; j++)
                value += v[j] * mtx[j][i];
            ret[i] = value;
        }
        return ret;
    }

    /**
     *  行列の内容を文字列で出力します。
     *  @param  mtx 行列
     *  @return "[[mtx[0][0], mtx[0][1], ...], [mtx[1][0], ...], ...]"
     *          形式の文字列
     */
    public static String toString(double[][] mtx) {
        int n = mtx.length, m = mtx[0].length;
        String str = "[";
        for (int i = 0; i < n; i++) {
            str += "[";
            for (int j = 0; j < m; j++) {
                str += mtx[i][j];
                if (j < m - 1) str += ", ";
            }
            str += "]";
            if (i < n - 1) str += ", ";
        }
        str += "]";
        return str;
    }

/***************** 以下は印刷メソッド ***************************************/
    /**
     *  行列の全要素を印刷します。
     *  @see java_algorithm.datatype.string.NumberFormatter
     *  @param  perLine 1行に印刷する要素数
     *  @param  format  NumberFormatter に与える印刷書式
     */
    public static void print(double[][] a, int perLine, String format) {
    // a[m][n] のとき，a.length = m, a[0].length = n になる。
        print(a, a.length, a[0].length, perLine, format);
    }

    /**
     *  行列の左側 nCol 列を印刷します。
     *  @see java_algorithm.datatype.string.NumberFormatter
     *  @param  nCol 列の数
     *  @param  perLine 1行に印刷する要素数
     *  @param  format  NumberFormatter に与える印刷書式
     */
    public static void print(double[][] a, int nCol, int perLine, String format) {
        print(a, a.length, nCol, perLine, format);
    }

    /**
     *  行列の左上隅，第1行から第 nRow 行，第1列から第 nCol 列の部分を印刷します。
     *  @see java_algorithm.datatype.string.NumberFormatter
     *  @param  nRow 行の数
     *  @param  nCol 列の数
     *  @param  perLine 1行に印刷する要素数
     *  @param  format  NumberFormatter に与える印刷書式
     */
    public static void print(double[][] a, int nRow, int nCol, int perLine, String format) {
        for (int i = 0; i < nRow; i++) {
            RVector.print(0, nCol, a[i], perLine, format);
            if (nCol > perLine) System.out.println();
        }
    }
/*****************************************************************************/

    /**
     *  唯一のエントリポイント
     *  @param  args    --
     */
    public static void main(String[] args) {
        double[][]  m1 = new double[2][2];
        m1[0][0] = 1.0;
        m1[0][1] = 1.0;
        m1[1][0] = 1.0;
        m1[1][1] = 1.0;

        double[][]  m2 = m1.clone();
        double[][]  m3 = add(m1,m2), m4 = multiply(m2,m3), m5 = subtract(m3,m4);

        System.out.println(toString(m3) + "\n" + toString(m4) + "\n" + toString(m5));
    }

}