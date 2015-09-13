package java_algorithm.algebra.linear;

/*
* QR.java -- QR法
* $Revision: 1.4 $, $Date: 2003/04/28 23:23:18 $
* class Tridiagonalize, RMatrix, RVector を使う。
*/
import java_algorithm.datatype.string.NumberFormatter;

import java.io.*;
/**
*QR法を使って実対称行列の対角化を行うプログラムです。
*@see Jacobi
*@see Tridiagonalize
*@see Householder
*@see RVector
*@see RMatrix
*/
public class QR {
    static final double EPS      = 1E-6;  // 非対角成分の許容誤差
    static final int    MAX_ITER = 100;   // 最大の繰返し数
    /**
    *a[0..n-1][0..n-1]に入った対称行列 A の対角化を行います。<br>
    *d[k]に固有値が，a[k][i]には第k+1固有ベクトルの第i+1成分が入ります。
    *@return 収束したとき0，しないとき-1
    */
    public static int eigen(double[][] a, double[] d) {
        int n = d.length;
        double e[] = new double[n];  // 作業用

        Tridiagonalize.getMatrix(a, d, e);  // 3重対角化
        System.arraycopy(e, 0, e, 1, e.length - 1); // 1桁上方にコピー
        e[0] = 0;  // 番人
        for (int h = n - 1; h > 0; h--) { // 行列のサイズを小さくしていく
            int j = h;
            while (j > 0 && Math.abs(e[j]) > EPS * (Math.abs(d[j - 1]) + Math.abs(d[j])))
                j--;  // $\mbox{\tt e[$j$]} \ne 0$ のブロックの始点を見つける
            if (j == h) continue;
            int iter = 0;
            do {
                if (++iter > MAX_ITER) return -1;
                double w = (d[h - 1] - d[h]) / 2, t = e[h] * e[h],
                       s = Math.sqrt(w * w + t);
                if (w < 0) s = -s;
                double x = d[j] - d[h] + t / (w + s), y = e[j + 1];
                for (int k = j; k < h; k++) {
                    double c;
                    if (Math.abs(x) >= Math.abs(y)) {
                        t = -y / x;  c = 1 / Math.sqrt(t * t + 1);
                        s = t * c;
                    } else {
                        t = -x / y;  s = 1 / Math.sqrt(t * t + 1);
                        c = t * s;
                    }
                    w = d[k] - d[k + 1];
                    t = (w * s + 2 * c * e[k + 1]) * s;
                    d[k] -= t;  d[k + 1] += t;
                    if (k > j) e[k] = c * e[k] - s * y;
                    e[k + 1] += s * (c * w - 2 * s * e[k + 1]);
                    // 次の5行は固有ベクトルを求めないなら不要
                    for (int i = 0; i < n; i++) {
                        x = a[k][i];  y = a[k + 1][i];
                        a[k    ][i] = c * x - s * y;
                        a[k + 1][i] = s * x + c * y;
                    }
                    if (k < h - 1) {
                        x = e[k + 1];  y = -s * e[k + 2];
                        e[k + 2] *= c;
                    }
                }
            } while (Math.abs(e[h]) >
                EPS * (Math.abs(d[h - 1]) + Math.abs(d[h])));
        }
    /*
    以下は固有値の降順に整列しているだけ。必要なければ省く。
    固有ベクトルを求めないなら固有ベクトルの整列はもちろん不要。
    */
        for (int k = 0; k < n - 1; k++) {
            int h = k;
            double t = d[h];

            for (int i = k + 1; i < n; i++)
                if (d[i] > t) {  h = i;  t = d[h];  }
            d[h] = d[k];  d[k] = t;
            // 行列 a[][] の第 h + 1 行と第 k + 1 行の交換をベクトル v[] を介して行っている。
            double v[] = a[h];  a[h] = a[k];  a[k] = v;
        }
        return 0;
    }

    /**
    * 乱数を使った例題プログラムです。<br>
    *<b>"n = "</b>と聞いてきますから 2 以上の整数値(次数)を入力してください。<br>
    *元の行列 A，固有値，二乗平均誤差，固有ベクトルの直交性を出力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new
            BufferedReader(new InputStreamReader(System.in));
        System.out.print("n = ");
        int n = Integer.parseInt(in.readLine());
        double a[][] = new double[n][n], u[][] = new double[n][n],
               d[]   = new double[n];

        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                u[i][j] = a[i][j] = a[j][i] = Math.random() - Math.random();

        System.out.println("A:");   RMatrix.print(a, 7, "10.6f");
        if (eigen(u, d) < 0) System.out.println("収束しません.");
        System.out.println("固有値:");  RVector.print(d, 5, "14.6f");

        double s = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double t = 0;
                for (int k = 0; k < n; k++)
                    t += u[k][i] * d[k] * u[k][j];
                s += (a[i][j] - t) * (a[i][j] - t);
            }
        }
        System.out.println("二乗平均誤差: " + Math.sqrt(s) / n);

        NumberFormatter fmt = new NumberFormatter("14.5e");
        System.out.println("\n固有ベクトルの直交性の確認");
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                System.out.println("u[" + i + "]・u[" + j + "] = "
                    + fmt.toString(RVector.innerProduct(u[i], u[j])));
    }
}