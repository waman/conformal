package java_algorithm.traditional.problem;

/**
 *  騎士巡歴の問題
 */

public class KnightTour {

    static final int N = 5; // $\mathtt{N} \times \mathtt{N}$ の番面
    static int[][] board = new int[N + 4][N + 4]; // 番面
    static final int[] DX = { 2, 1,-1,-2,-2,-1, 1, 2 }; // 横変位
    static final int[] DY = { 1, 2, 2, 1,-1,-2,-2,-1 }; // 縦変位
    static int solution = 0,  count = 0;

    static void printBoard() {  // 番面を出力
        System.out.println("\n解 " + (++solution));
        for (int i = 2; i < N + 2; i++) {
            for (int j = 2; j < N + 2; j++) {
                String s = "   " + board[i][j];
                System.out.print(s.substring(s.length() - 4));
            }
            System.out.println();
        }
    }

    static void search(int x, int y) {  // 再帰的に試みる
        if (board[x][y] != 0) return;  // すでに訪れた
        board[x][y] = ++count;
        if (count == N * N) printBoard();  // 完成
        else for (int i = 0; i < 8; i++) search(x + DX[i], y + DY[i]);
        board[x][y] = 0;  count--;
    }

    public static void main(String[] args) {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++) board[i][j] = 1;
        for (int i = 2; i < N + 2; i++)
            for (int j = 2; j < N + 2; j++) board[i][j] = 0;
        search(2, 2);
    }
}