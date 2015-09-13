package java_algorithm.algebra;

/*
* Eval.java -- 式の評価
*/

import java.io.IOException;

/**
*式の評価を行うプログラムです。
*/
public class Eval {

    static int ch;

    static void error(String s) {  // エラー処理
        System.err.println(s);  System.exit(-1);
    }

    static void readCh() {  // 1文字を読む。空白は読み飛ばす。
        do {
            try {
                ch = System.in.read();
            } catch (IOException e) { /* Ignore */ }
        } while (ch == ' ' || ch == '\t' || ch == '\r');
    }

    static double number() { // 数
        int sign = '+';

        if (ch == '+' || ch == '-') {
            sign = ch;
            readCh();
        }
        if (! Character.isDigit((char)ch)) error("数か '(' がありません");
        double x = ch - '0';
        while (true) {
            readCh();
            if (! Character.isDigit((char)ch)) break;
            x = 10 * x + ch - '0';
        }
        if (ch == '.') {
            double a = 1;
            while (true) {
                readCh();
                if (! Character.isDigit((char)ch)) break;
                x += (a /= 10) * (ch - '0');
            }
        }
        return sign == '+' ? x : -x;
    }

    static double factor() { // 因子
        if (ch != '(') return number();
        readCh();   double x = expression();
        if (ch != ')') error("')' がありません");
        readCh();  return x;
    }

    static double term() { // 項
        double x = factor();

        while (true)
            if (ch == '*') {
                readCh();
                x *= factor();
            } else if (ch == '/') {
                readCh();
                double y = factor();
                if (y == 0) error("0 では割れません");
                x /= y;    // TODO
            } else break;

        return x;
    }

    static double expression() { // 式
        double x = term();

        while (true)
            if (ch == '+') {
                readCh();  x += term();
            } else if (ch == '-') {
                readCh();  x -= term();
            } else break;
        return x;
    }

    /**
    *例えば，3.14*(3+1)/2のように式を入力するとその値を出力します。
    */
    public static void main(String[] args) throws IOException {
        System.err.print("式を入力してください： ");
        readCh();
        double x = expression();

        if (ch != '\n') error("文法の間違いがあります");
        System.out.println("値：" + x);
    }
}