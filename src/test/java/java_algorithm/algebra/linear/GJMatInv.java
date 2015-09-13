package java_algorithm.algebra.linear;

/**
 *  行列の逆行列を求める(Gauss-Jordan 法)
 */
public final class GJMatInv {

    private GJMatInv() {}

    public static double inverse(double[][] a) {
        int n = a.length;
        double det = 1;
        for (int k = 0; k < n; k++) {
            double t = a[k][k];
            det *= t;
            for (int i = 0; i < n; i++) a[k][i] /= t;
            a[k][k] = 1 / t;
            for (int j = 0; j < n; j++)
                if (j != k) {
                    double u = a[j][k];
                    for (int i = 0; i < n; i++)
                        if (i != k) a[j][i] -= a[k][i] * u;
                        else        a[j][i]  = - u / t;
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
        double det = inverse(a);
        System.out.println("det A = " + det + "\nA^-1 = \n" + RMatrix.toString(a));
    }
}