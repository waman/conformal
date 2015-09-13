package java_algorithm.algebra.linear;

/**
* OptMult.java -- 行列の積
*/

/**
* 複数の行列の積を計算するための最適なかっこの付け方と，
*そのときの (成分どうしの) 乗算回数を出力します。
* @see MatMult
* @see UTMult
*/

public class OptMult {
    static final int N = 4;                        /* 行列の数 */
    static int nRow[] = {4, 2, 6, 3, 5};  /* 例えば */
    static int[][] minCost = new int[N][N], split = new int[N][N];

    static void printResult(int left, int right) {
        if (left == right){
            System.out.print("A" + left);
        }else{
            System.out.print("(");
            printResult(left, split[left][right]);
            printResult(split[left][right] + 1, right);
            System.out.print(")");
        }
    }
    /**
    *例題プログラムです。
    */

    public static void main(String[] args) {
        for (int length = 1; length < N; length++) {
            int choice = 0;
            for (int left = 0; left < N - length; left++) {
                int right = left + length, min = Integer.MAX_VALUE;
                for (int i = left; i < right; i++) {
                    int cost = minCost[left][i]
                        + minCost[i + 1][right]
                        + nRow[left] * nRow[i + 1] * nRow[right + 1];
                    if (cost < min) {
                        min = cost;  choice = i;
                    }
                }
                minCost[left][right] = min;
                split[left][right] = choice;
            }
        }
        System.out.println("Minimum cost = " + minCost[0][N - 1]);
        printResult(0, N - 1);
    }
}