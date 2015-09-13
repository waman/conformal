package java_algorithm.datatype.collection.search;

/**
 *  深さ優先探索
 */

public class DepthFirstSearch {

    static int n;  // 点の数
    static int[][] adjacent;  // 隣接行列
    static boolean[] visited;  // 訪れたなら \texttt{true}

    public static void readGraph() { // グラフ入力
        n = 7;  // 点の数 (例)
        int data[] = { 1, 2, 2, 3, 1, 3, 2, 4, 5, 7 };  // 隣接リストによる
                                                        // データ (例)
        adjacent = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++) adjacent[i][j] = 0;

        for (int k = 0; k + 1 < data.length; k += 2) {
            int i = data[k],  j = data[k + 1];
            adjacent[i][j] = adjacent[j][i] = 1;
        }
        System.out.println("隣接行列:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                System.out.print(" " + adjacent[i][j]);
            System.out.println();
        }
    }

    public static void visit(int i) {  // 点 \texttt{i} を訪れる (再帰的)
        System.out.print(" " + i);  visited[i] = true;
        for (int j = 1; j <= n; j++)
            if (adjacent[i][j] != 0 && !visited[j]) visit(j);
    }

    public static void main(String[] args) {
        readGraph();  // 隣接行列を読む
        visited = new boolean[n + 1];
        for (int i = 1; i <= n; i++) visited[i] = false;  // まだ訪れていない

        System.out.println("連結成分:");
        int count = 0;  // 連結成分を数える
        for (int i = 1; i <= n; i++)
            if (!visited[i]) {
                System.out.print((++count) + ":");
                visit(i);  System.out.println();
            }
    }
}