package java_algorithm.algebra.linear;

/**
 *  上三角行列の逆行列を求める
 */
public final class UTMatInv {

    private UTMatInv() {}

    public static double inverse(double[][] a) {
        int n = a.length;
        double det = 1;
        for (int k = 0; k < n; k++) {
            double t = a[k][k]; det *= t;
            a[k][k] = 1 / t;
            for (int j = k - 1; j >= 0; j--) {
                double s = 0;
                for (int i = j + 1; i <= k; i++) s -= a[i][j] * a[i][k];
                a[j][k] = s * a[j][j];
            }
        }
        return det;
    }

    public static void main(String[] args) {
        double[][] a = new double[2][2];
        a[0][0] = 1; a[0][1] = 1; a[1][1] = 2;
        System.out.println("A = \n" + RMatrix.toString(a));
        double[][] a_t = RMatrix.transpose(a);
        inverse(a_t); a_t[1][0] = 0;
        System.out.println("A^-1 = \n" + RMatrix.toString(a_t));
    }
}