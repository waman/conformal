package java_algorithm.algebra;

/*
*   Postfix.java -- 後置記法
*/

import java.io.IOException;

/**
*後置記法のプログラムです。
*@see java_algorithm.algebra.polynomial.Poly
*/
public class Postfix {
    static int ch;

    static void readCh() {  // 1文字を読む. 空白は読み飛ばす.
        do {
            try {
                if ( (ch = System.in.read()) < 0) return;
                if (ch == '\r'){   // "\r\n" は '\n' に置き換える。
                    ch = System.in.read(); // '\n' を収める。
                }
            } catch (IOException e) { ch = -1;  return; }
        } while (ch == ' ' || ch == '\t');
    }

    static void factor() { // 因子
        if (ch < 0) return;
        if (ch == '(') {
            readCh();
            expression();
            if (ch == ')') readCh();
            else System.out.print("?");

        } else if ( ('!' <= ch) && (ch <= '~') ) { // 表示可能文字 0x21 <= ch <= 0x7e
            System.out.print((char)ch);
            readCh();

        } else System.out.print("?");
    }

    static void term() { // 項
        factor();
        while (true) {
            if (ch == '*') {
                readCh();  factor();   System.out.print("*");
            } else if (ch == '/') {
                readCh();  factor();   System.out.print("/");
            } else break;
        }
    }

    static void expression() { // 式
        term();
        while (true)
            if (ch == '+') {
                readCh();  term();  System.out.print("+");
            } else if (ch == '-') {
                readCh();  term();  System.out.print("-");
            } else break;
    }

    /**
    *通常の記法で入力された式を後置記法に直します。<br>
    *<strong>入力例</strong>（下線の部分を入力する）<br>
    *<code><u>(a+b)*(c-d)</u><br><br></code>
    *<strong>出力</strong><br>
    *<code>ab+cd-*</code>
    */
    public static void main(String[] args) throws IOException {
        System.out.println("<ctrl> + z で終了します。");
        do {
            readCh();
            expression();
            while (ch != '\n' && ch > 0) {
                System.out.print("?");
                readCh();
            }
            System.out.println();
        } while (ch > 0);
    }
}