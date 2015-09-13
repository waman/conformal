package java_algorithm.traditional;

/**
* Tetromino.java -- テトロミノの箱詰めパズル
* 使用方法
* java Tetromino < teromino.dat
* teromin.dat はデータファイル
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
* テトロミノの箱詰めパズルの解を CUI で表示します。
*/

public class Tetromino {

    static final int PIECES       = 5;  /* 駒の数 */
    static final int COL          = 5;  /* 盤の短辺の長さ */
    static final int ROW          = 8;  /* 盤の長辺の長さ */
    static final int PIECE_SIZE   = 4;  /* 駒の大きさ */
    static final int MAX_SYMMETRY = 8;  /* 駒の置き方の最大数 */
    static final int MAX_SITE     = (COL + 1) * ROW - 1;
    static final int LIM_SITE     = (COL + 1) * (ROW + 1);
    static char board[]    = new char[LIM_SITE];
    static char name[][]   = new char[2][PIECES];
    static int symmetry[]  = new int[PIECES];
    static int shape[][][] = new int[PIECES][MAX_SYMMETRY][PIECE_SIZE - 1];
    static int rest[]      = new int[PIECES];

    // 駒の形のデータファイルを読み込む
    static void initialize() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        for (int site = 0; site < MAX_SITE; site++)
            if (site % (COL + 1) == COL) board[site] = '*';
            else board[site] = '\0';
        for (int site = MAX_SITE; site < LIM_SITE - 1; site++)
            board[site] = '*';
        board[LIM_SITE - 1] = '\0';  // 番人
        for (int piece = 0; piece < PIECES; piece++) {
            rest[piece] = 2;
            String rL          = in.readLine();
            StringTokenizer sT = new StringTokenizer(rL);

            if (sT.countTokens() == 5) { // "Oo 1 1 6 7" の型
                char nc[] = sT.nextToken().toCharArray();
                name[1][piece] = nc[0];     name[0][piece] = nc[1];
                symmetry[piece]= Integer.parseInt(sT.nextToken());
            }
            for (int state = 0; state < symmetry[piece]; state++) {
                for (int site = 0; site < PIECE_SIZE - 1; site++)
                    shape[piece][state][site] = Integer.parseInt(sT.nextToken());
                if (state + 1 < symmetry[piece]) { // "6 2 18" の型
                    rL = in.readLine();     sT = new StringTokenizer(rL);
                }
            }
        }
    }

    static int count;

    static void found() { // 解の表示
        count++;
        System.out.print("\n解 " + count + "\n\n");
        for (int i = 0; i < COL; i++) {
            for (int j = i; j < MAX_SITE; j += COL + 1)
                System.out.print(board[j]);
            System.out.println();
        }
    }

    static void search(int site) { // 再帰的に試みる
        for (int piece = 0; piece < PIECES; piece++) {
            if (rest[piece] == 0) continue;
            rest[piece]--;
            for (int state = 0; state < symmetry[piece]; state++) {
                int s0 = site + shape[piece][state][0];
                if (board[s0] != '\0') continue;
                int s1 = site + shape[piece][state][1];
                if (board[s1] != '\0') continue;
                int s2 = site + shape[piece][state][2];
                if (board[s2] != '\0') continue;
                board[site] = board[s0] = board[s1] = board[s2]
                            = name[rest[piece]][piece];
                int temp = site;
                while (board[++temp] != '\0'){}
                if (temp < MAX_SITE) search(temp);  else found();
                board[site] = board[s0] = board[s1] = board[s2] = '\0';
            }
            rest[piece]++;
        }
    }

    /**
    * 標準入力からデータを読み込んで，テトロミノの箱詰めパズルの解を表示します。<br>
    * 使用方法<br>
    * <code>java Tetromino &lt; teromino.dat</code><br>
    * tetromino.dat はデータファイル，3106 個の解が表示されます。
    */
    public static void main(String[] args) throws IOException {
        initialize();
        search(0);
    }
}