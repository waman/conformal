package java_algorithm.algebra.linear;

import java_algorithm.equation.algebraic.LU;

/**
 *  行列の逆行列を求める(LU 法)
 */
public class LUMatInv {
    private LUMatInv() {}
    public static double inverse(double[][] a, double[][] a_inv) {
        int n = a.length;
        int[] ivec = new int[n];
        double det = LU.lu(a, ivec);
        if (det == 0) return 0;

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                int ii = ivec[i]; double t = (ii == k) ? 1 : 0;
                for (int j = 0; j < i; j++)
                    t -= a[ii][j] * a_inv[j][k];
                a_inv[i][k] = t;
            }
            for (int i = n - 1; i >= 0; i--) {
                double t = a_inv[i][k]; int ii = ivec[i];
                for (int j = i + 1; j < n; j++)
                    t -= a[ii][j] * a_inv[j][k];
                a_inv[i][k] = t / a[ii][i];
            }
        }
        return det;
    }

    public static void main(String[] args) {
        double[][] a = new double[2][2];
        double theta = Math.PI / 3;
        a[0][0] = Math.cos(theta); a[0][1] = - Math.sin(theta);
        a[1][0] = Math.sin(theta); a[1][1] = Math.cos(theta);
        System.out.println("A = \n" + RMatrix.toString(a));
        double[][] a_inv = new double[2][2];
        double det = inverse(a, a_inv);
        System.out.println("det A = " + det + "\nA^-1 = \n" + RMatrix.toString(a_inv));
    }
}