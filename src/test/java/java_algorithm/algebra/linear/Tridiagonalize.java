package java_algorithm.algebra.linear;

/*
* Tridiagonalize.java -- 3重対角化
* class RMatrix, RVector を使う。
*/
import java.io.*;
/**
*実対称行列を3重対角行列にするプログラムです。
*@see Householder
*@see QR
*@see RMatrix
*@see RVector
*/
public class Tridiagonalize {

    // ベクトル(x[k],...x[n-1]) のHouseholder 変換
    static double house(int n, double[] x, int k) {
    // 内積の平方根, すなわちベクトル $x$ の大きさ
        double s = Math.sqrt(RVector.innerProduct(k, x, x));
        if (s != 0) {
            if (x[k] < 0) s = -s;
            x[k] += s;
            double t = 1 / Math.sqrt(x[k] * s);
            for (int i = 0; i < n; i++) x[k + i] *= t;
        }
        return -s;
    }

    /**
    *行列 a[][] を3重対角化します。
    *@param a 実対称行列（n×n）
    *@param d 対角成分が<code>d[0]...d[n-1]</code>に入ります。
    *@param e 隣の成分が<code>e[0]...e[n-2]</code>に入ります。
    */
    public static void getMatrix(double[][] a, double[] d, double[] e) {
        int n = a.length;

        for (int k = 0; k < n - 2; k++) {
            // 文v = a[k];でa[][]の第k行が代入されv[]=(a[k][0],a[k][1], ...a[k][n-1])になる。
            double[] v = a[k];  d[k] = v[k];
            e[k] = house(n - k - 1, v, k + 1);
            if (e[k] == 0) continue;
            for (int i = k + 1; i < n; i++) {
                double s = 0;
                for (int j = k + 1; j < i; j++) s += a[j][i] * v[j];
                for (int j = i;     j < n; j++) s += a[i][j] * v[j];
                d[i] = s;
            }
            double t = RVector.innerProduct(k + 1, v, d) / 2;
            for (int i = n - 1; i > k; i--) {
                double p = v[i], q = d[i] - t * p;  d[i] = q;
                for (int j = i; j < n; j++)
                    a[i][j] -= p * d[j] + q * v[j];
            }
        }
        if (n >= 2) {  d[n - 2] = a[n - 2][n - 2];
                       e[n - 2] = a[n - 2][n - 1];  }
        if (n >= 1)    d[n - 1] = a[n - 1][n - 1];
        for (int k = n - 1; k >= 0; k--) {
            double[] v = a[k];
            if (k < n - 2) {
                for (int i = k + 1; i < n; i++) {
                    double[] w = a[i];
                    double t = RVector.innerProduct(k + 1, v, w);
                    for (int j = k + 1; j < n; j++)
                        w[j] -= t * v[j];
                }
            }
            for (int i = 0; i < n; i++) v[i] = 0;
            v[k] = 1;
        }
    }

    /**
    * 乱数を使った例題プログラムです。<br>
    *<b>&quot;n = &quot;</b>と聞いてきますから 3 以上の整数値(次数)を入力してください。<br>
    *元の行列 A，3重対角化後の対角成分<code>d[]</code>とその隣の成分<code>e[]</code>
    *および二乗平均誤差を出力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new
            BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double a[][]  = new double[n][n], p[][] = new double[n][n],
               d[]    = new double[n],    e[]   = new double[n],
               temp[] = new double[n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j <= i; j++)
                a[i][j] = a[j][i] = p[i][j] = p[j][i]
                    = Math.random() - Math.random();

        String fmt = "15.10f";
        System.out.println("A:");   RMatrix.print(a, 5, fmt);
        getMatrix(p, d, e);
        System.out.println("d:");   RVector.print(d, 5, fmt);
        System.out.println("e:");   RVector.print(0, n - 1, e, 5, fmt);
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, temp, 0, n);
            for (int j = 0; j < n; j++) {
                double s = 0;
                for (int k = 0; k < n; k++)
                    s += temp[k] * p[j][k];
                a[i][j] = s;
            }
        }
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++)
                temp[k] = a[k][j];
            for (int i = 0; i < n; i++) {
                double s = 0;
                for (int k = 0; k < n; k++)
                    s += p[i][k] * temp[k];
                a[i][j] = s;
            }
        }
        double s = 0, t;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i == j) {
                    t = a[i][j] - d[i];  s += t * t;
                } else if (i + 1 == j) {
                    t = a[i][j] - e[i];  s += t * t;
                } else if (i == j + 1) {
                    t = a[i][j] - e[j];  s += t * t;
                } else {
                    t = a[i][j];  s += t * t;
                }
        // 1E-16 程度になる。
        System.out.println("二乗平均誤差: " + Math.sqrt(s) / n);
    }
}