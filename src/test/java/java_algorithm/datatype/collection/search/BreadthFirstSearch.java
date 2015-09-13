package java_algorithm.datatype.collection.search;

/**
 *  幅優先探索
 */

public class BreadthFirstSearch {

    static int n;  // 点の数
    static int[][] adjacent;  // 隣接行列

    public static void readGraph() { // グラフ入力
        n = 7;  // 点の数 (例)
        int data[] = { 1, 2, 2, 3, 1, 3, 2, 4, 5, 7 };  // 隣接リストによる
                                                        // データ (例)
        adjacent = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                adjacent[i][j] = 0;

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

    public static void main(String[] args) {
        readGraph();
        int[] distance = new int[n + 1],  prev = new int[n + 1];
        for (int i = 1; i <= n; i++) distance[i] = -1;  // 未訪問
        int[] fifo = new int[n];  // 待ち行列(FIFO)
        int in = 0,  out = 0;  // FIFOバッファ上のポインタ

        final int START =  1;
        fifo[in++] = START;  distance[START] = 0;
        do {
            int i = fifo[out++];  // 待ち行列からの取り出し
            for (int j = 1; j <= n; j++)
                if (adjacent[i][j] != 0 && distance[j] < 0) {
                    fifo[in++] = j;  // 待ち行列への挿入
                    distance[j] = distance[i] + 1;  prev[j] = i;
                }
        } while (in != out);  // 待ち行列が空なら終了

        System.out.println("点  直前の点  最短距離");
        for (int i = 1; i <= n; i++)
            if (distance[i] > 0)
                System.out.println(" " + i + "         "  + prev[i] + "         " + distance[i]);
    }
}