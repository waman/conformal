package java_algorithm.algebra.linear;

/*
* Jacobi.java -- Jacobi (ヤコビ) 法
* class RMatrix, RVector を使う。
*/
import java.io.*;
/**
*Jacobi (ヤコビ) 法のプログラムです。
*@see Tridiagonalize
*@see RVector
*@see RMatrix
*/

public class Jacobi {

    static final double EPS      = 1E-6;  /* 許容誤差 */
    static final double TINY     = 1E-20; /* 0 と見なしてよい値 */
    static final int    MAX_ITER = 100;   /* 最大の繰返し数 */

    static void rotate(double[][] a, int i, int j, int k, int l, double c, double s) {
        double x = a[i][j], y = a[k][l];
        a[i][j] = x * c - y * s;
        a[k][l] = x * s + y * c;
    }

    /**
    *<code>a[0..n-1][0..n-1]</code> に入った対称行列 A の固有値と
    *固有ベクトルをJacobi法で求めます。<br>
    *<code>a[k][k]</code>に固有値が，それに対応する固有ベクトルは 
    *<code>w[k][0..n-1]</code> に上書きされます。
    *@return 成功 true，収束せず false
    */
    public static boolean solve(double[][] a, double[][] w) {
        int n = a.length;
        double s = 0, offdiag = 0;

        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) w[j][k] = 0;
            w[j][j] = 1;  s += a[j][j] * a[j][j];
            for (int k = j + 1; k < n; k++)
                offdiag += a[j][k] * a[j][k];
        }
        double tolerance = EPS * EPS * (s / 2 + offdiag);
        int iter;

        for (iter = 1; iter <= MAX_ITER; iter++) {
            offdiag = 0;
            for (int j = 0; j < n - 1; j++)
                for (int k = j + 1; k < n; k++)
                    offdiag += a[j][k] * a[j][k];
// テスト用 //////収束の様子を表示////////
                System.out.println( iter + " : " +
                    Math.sqrt(2 * offdiag / (n * (n - 1))));
//////////////////
            if (offdiag < tolerance) break;
            for (int j = 0; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (Math.abs(a[j][k]) < TINY) continue;
                    double t = (a[k][k] - a[j][j]) / (2 * a[j][k]);
                    if (t >= 0) t = 1 / (t + Math.sqrt(t * t + 1));
                    else        t = 1 / (t - Math.sqrt(t * t + 1));
                    double c = 1 / Math.sqrt(t * t + 1), u = t * c;
                    t *= a[j][k];
                    a[j][j] -= t;  a[k][k] += t;  a[j][k] = 0;
                    for (int i = 0; i < j; i++)     rotate(a, i, j, i, k, c, u);
                    for (int i = j + 1; i < k; i++) rotate(a, j, i, i, k, c, u);
                    for (int i = k + 1; i < n; i++) rotate(a, j, i, k, i, c, u);
                    for (int i = 0; i < n; i++)     rotate(w, j, i, k, i, c, u);
                }
            }
        }
        if (iter > MAX_ITER) return false;  /* 収束せず */
        for (int i = 0; i < n - 1; i++) {
            int k = i;  double t = a[k][k];
            for (int j = i + 1; j < n; j++)
                if (a[j][j] > t) {  k = j;  t = a[k][k];  }
            a[k][k] = a[i][i];  a[i][i] = t;
            double[] v = w[k];  w[k] = w[i];  w[i] = v;
        }
        return true;  /* 成功 */
    }

    /**
    * 乱数を使った例題プログラムです。<br>
    *<b>&quot;n = &quot;</b>と聞いてきますから 2 以上の整数値(次数)を入力してください。<br>
    *元の行列 A，収束の様子，固有値と固有ベクトル，固有ベクトルの直交性，二乗平均誤差を
    *出力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double[][] a = new double[n][n], b = new double[n][n],
                   w = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j <= i; j++)
                a[i][j] = a[j][i] = b[i][j] = b[j][i] = Math.random() - Math.random();
        System.out.println("元の行列 A = ");
        RMatrix.print(a, 7, "10.6f");
        System.out.println("収束の様子");
        if (! solve(a, w)) System.out.println("収束しません");

        double[] lambda = new double[n];
        for (int i = 0; i < n; i++) lambda[i] = a[i][i];
//        System.out.println("固有値:");    RVector.print(lambda, 7, "10.6f");
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.println("固有値 lambda[" + i +"] = " + lambda[i]);
            System.out.println("固有ベクトル w[" + i + "] =");
            RVector.print(w[i], 7, "10.6f");
        }
        System.out.println("\n固有ベクトルの直交性の確認");
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                System.out.println("w[" + i + "]・w[" + j + "] = "
                    + RVector.innerProduct(w[i], w[j]));

        double err = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                double s = b[i][j];
                for (int k = 0; k < n; k++) s -= lambda[k] * w[k][i] * w[k][j];
                err += s * s;
            }
        }
        System.out.println("\n二乗平均誤差: " + Math.sqrt(err / (n * n)));
    }
}