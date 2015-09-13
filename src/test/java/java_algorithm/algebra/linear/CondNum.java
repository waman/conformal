package java_algorithm.algebra.linear;

/*
* CondNum.java -- 条件数
* class RMatrix, LUMatInv, LU を使う。
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* 行列の条件数を求めます。
* @see LUMatInv
* @see RMatrix
*/
public class CondNum {

    static double infinityNorm(double[][] a) { // ∞ノルム
        double max = 0;
        for(double[] ai : a){
            double rowSum = 0;
            for(double aij : ai)
                rowSum += Math.abs(aij);

            if(rowSum > max)
                max = rowSum;
        }
        return max;
    }

    /**
    * 行列の条件数を求めます。
    * @param a 条件数を求めたい行列 <code>a[0..n-1][0..n-1]</code> を与えます。
    * @return 行列 <code>a[][]</code> の条件数を返します。
    */
    public static double getValue(double[][] a) { // 条件数
        int n = a.length;
        double[][] aInv = new double[n][n];
        double t = infinityNorm(a);

        if (LUMatInv.inverse(a, aInv) == 0)  // mathutils.LUMatInv
            throw new Error("逆行列がありません");

        return t * infinityNorm(aInv);
    }

    /**
    * 乱数を使ったテストプログラムです。<br>
    * 最初に行列の次数 n を入力します。
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new
            BufferedReader(new InputStreamReader(System.in));

        System.out.print("行列の次数 n = ");
        int n = Integer.parseInt(in.readLine());    // 行列の次数を入力
        double[][] a = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = Math.random() - Math.random();

        RMatrix.print(a, 7, "10.6f");
        System.out.println("条件数 = " + getValue(a));
    }
}