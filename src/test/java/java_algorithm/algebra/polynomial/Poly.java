package java_algorithm.algebra.polynomial;

/**
* Poly.java -- 多項式の計算（複素数版） TODO
*/

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import static java.io.StreamTokenizer.*;

/**
* 簡単な多項式処理系のプログラムです。複素数も処理できます。
*/

public class Poly {

    static final int N_LETTER = 4;  // 最大文字数

    static class Node {    // ノード (多項式の一つの項)

        int expo[]; // 指数
        long real;  // 実部
        long imag;  // 虚部
        Node next;  // 次の項へのポインタ

        private Node() {
            this(0, 0, null);
        }

        private Node(long real, long imag, Node next) {
            this.real = real;  this.imag = imag;
            this.next = next;  expo = new int[N_LETTER];
            for (int i = 0; i < N_LETTER; i++) expo[i] = 0;
        }
    }

    static int table[] = new int[26];       // 各文字の位置
    static Node value[]  = new Node[26];    // 各文字に代入された多項式
    static char letter[] = new char[N_LETTER];  // 文字の表
    static int cells;                       // 使用中のセル数
    static int maxCells;                    // 最大使用セル数
    static StreamTokenizer in;              // 読み込み中のストリーム

    static void error(String message) { // エラー処理
        System.err.println("\n" + message + " " + maxCells + " 個のセル使用");
        System.exit(-1);
    }

    static boolean isOdd(int n){ // 奇数か
        return (n & 1) != 0;
    }

    static int alphabetOrder(int c) {  // aなら0, zなら25を返す
        return Character.digit((char)c, 36) - 10;
    }

    /** 新しいノードを作る */
    static Node newNode() {
        return newNode(0, 0, null);
    }

    static Node newNode(long real, long imag, Node next) {
        Node p = new Node(real, imag, next);
        if (++cells > maxCells) maxCells = cells;
        return p;
    }

    /** ノードを消す TODO */
    static void disposeNode(Node p) {
        cells--;
    }

    /** 多項式を消す */
    static void dispose(Node p) {
        cells--;
        while (p.next != null) {
            p = p.next;  cells--;
        }
    }

    /** 定数 */
    static Node constant(long re, long im) {
        Node p = newNode();
        if (re != 0 || im != 0) p.next = newNode(re, im, null);
        return p;
    }

    /** 多項式のコピー */
    static Node copy(Node p) {
        Node q = newNode(), r = q;

        while ((p = p.next) != null) {
            r = r.next = newNode(p.real, p.imag, null);
            System.arraycopy(p.expo, 0, r.expo, 0, N_LETTER);
        }
        r.next = null;
        return q;
    }

    /** 符号反転 */
    static void changeSign(Node p) {
        while ((p = p.next) != null) {
            p.real = -p.real;   p.imag = -p.imag;
        }
    }

    /** 微分 */
    static void differentiate(Node p) {
        if (alphabetOrder(in.ttype) < 0) error("文字がありません");
        int j = table[alphabetOrder(in.ttype)];
        if (j < 0) error("微分できません");
        Node p1 = p;  p = p.next;

        while (p != null) {
            int e = p.expo[j];
            if (e != 0) {
                p.expo[j] = e - 1;
                p.real *= e;    p.imag *= e;
                p1 = p;  p = p.next;
            } else {
                p = p.next;  disposeNode(p1.next);
                p1.next = p;
            }
        }
    }

    /** 複素共役 */
    static void complexConjugate(Node p) {
        while ((p = p.next) != null) p.imag = -p.imag;
    }

    /** p := p + q;  q は消す */
    static void add(Node p, Node q) {
        int ep = 0, eq = 0;
        Node p1 = p;  p = p.next;
        Node q1 = q;  q = q.next;  disposeNode(q1);

        while (q != null) {
            while (p != null) {
                for (int i = 0; i < N_LETTER; i++) {
                    ep = p.expo[i];  eq = q.expo[i];
                    if (ep != eq) break;
                }
                if (ep <= eq) break;
                p1 = p;  p = p.next;
            }
            if (p == null || ep < eq) {
                p1.next = q;  p1 = q;  q = q.next;
                p1.next = p;
            } else {
                p.real += q.real;   p.imag += q.imag;
                if (p.real != 0 || p.imag != 0) {
                    p1 = p;  p = p.next;
                } else {
                    p = p.next;
                    disposeNode(p1.next);
                    p1.next = p;
                }
                q1 = q;  q = q.next;  disposeNode(q1);
            }
        }
    }

    /** x, y の積を返す. x, y は不変 */
    static Node multiply(Node x, Node y) {
        int ep = 0, eq = 0;
        Node q = null, r = newNode();

        r.next = null;
        while ((y = y.next) != null) {
            Node p1 = r, p = p1.next, z = x;

            while ((z = z.next) != null) {
                if (q == null) q = newNode();
                q.real = y.real * z.real - y.imag * z.imag;
                q.imag = y.real * z.imag + y.imag * z.real;
                for (int i = 0; i < N_LETTER; i++)
                    q.expo[i] = y.expo[i] + z.expo[i];
                while (p != null) {
                    for (int i = 0; i < N_LETTER; i++) {
                        ep = p.expo[i];  eq = q.expo[i];
                        if (ep != eq) break;
                    }
                    if (ep <= eq) break;
                    p1 = p;  p = p.next;
                }
                if (p == null || ep < eq) {
                    p1.next = q;  p1 = q;  p1.next = p;
                    q = null;
                } else {
                    p.real += q.real;   p.imag += q.imag;
                    if (p.real != 0 || p.imag != 0) {
                        p1 = p;  p = p.next;
                    } else {
                        p = p.next;
                        disposeNode(p1.next);
                        p1.next = p;
                    }
                }
            }
        }
        if (q != null) disposeNode(q);
        return r;
    }

    /** x の n 乗を返す. x は捨てる */
    static Node power(Node x, int n) {
        if (n == 1) return x;
        Node p;
        if (n == 0) p = constant(1, 0);
        else {
            p = multiply(x, x);  n -= 2;
            if (n > 0) {
                Node q = p;
                if (isOdd(n)) p = multiply(q, x);
                else          p = copy(q);
                dispose(x);  x = q;  n /= 2;
                if (isOdd(n)) {
                    q = multiply(p, x);  dispose(p);  p = q;
                }
                while ((n /= 2) != 0) {
                    q = multiply(x, x);  dispose(x);  x = q;
                    if (isOdd(n)) {
                        q = multiply(p, x);  dispose(p);  p = q;
                    }
                }
            }
        }
        dispose(x);
        return p;
    }

    /** p^2 + q^2 = 1 の処理 */
    static void sincos(Node x) {
        int i;
        do {
            i = 0;  // ループ終了判定用変数
            Node p1 = x, p = p1.next, q = newNode(), r = q,
                 s = newNode(), t = s;
            while (p != null) {
                if (p.expo[1] >= 2) {
                    r = r.next = newNode();
                    r.real = -p.real;   r.imag = -p.imag;
                    r.expo[0] = p.expo[0] + 2;
                    r.expo[1] = p.expo[1] -= 2;
                    for (i = 2; i < N_LETTER; i++)
                        r.expo[i] = p.expo[i];  // ここを通ると i≠0 に
                    t = t.next = p;
                    p1.next = p = p.next;
                } else {
                    p1 = p;  p = p.next;
                }
            }
            if (i != 0) {
                r.next = t.next = null;
                add(x, q);  add(x, s);
            } else {
                disposeNode(q);  disposeNode(s);
            }
        } while (i != 0);
    }

    /** 変数 */
    static Node variable() throws IOException {
        Node p;
        int i = alphabetOrder(in.ttype), j = table[i];
        if (j >= 0) {
            p = constant(1, 0);
            int num = 1;
            if (in.nextToken() == '^') {  // ちょっとした最適化
                if (in.nextToken() != TT_NUMBER)
                    error("数がありません");
                num = (int)in.nval;  in.nextToken();
            }
            p.next.expo[j] = num;
        } else {
            if (value[i] == null) error("未定義の文字です");
            p = copy(value[i]);
            in.nextToken();
        }
        return p;
    }

    /** 因子 */
    static Node factor() throws IOException {
        Node p = null;
        if (in.ttype == '(') {
            in.nextToken();  p = expression();
            if (in.ttype != ')') error("')' がありません");
            in.nextToken();
        } else if (in.ttype == TT_NUMBER) {
            long num = (long)in.nval;
            if (Character.toUpperCase((char)in.nextToken()) != 'I')
                p = constant(num, 0);
            else {
                in.nextToken();  p = constant(0, num);
            }
        } else if (alphabetOrder(in.ttype) >= 0) p = variable();
        else error("因子の文法が間違っています");

        for ( ; ; ) {
            switch (in.ttype) {
            case '^':
                if (in.nextToken() != TT_NUMBER)
                    error("数がありません");
                p = power(p, (int)in.nval);  in.nextToken();  break;
            case ':':
                in.nextToken();  differentiate(p);
                in.nextToken();  break;
            case '!':
                in.nextToken();  sincos(p);  break;
            case '\'':
                in.nextToken();  complexConjugate(p);  break;
            default:
                return p;
            }
        }
    }

    /** 項 */
    static Node term() throws IOException {
        Node p = factor();
        while (in.ttype == '*') {
            in.nextToken();
            Node q = p, r = factor();
            p = multiply(q, r);
            dispose(q);  dispose(r);
        }
        return p;
    }

    /** 式 */
    static Node expression() throws IOException {
        Node p, q;
        if (in.ttype == '-') {
            in.nextToken();  p = term();  changeSign(p);
        } else {
            if (in.ttype == '+') in.nextToken();
            p = term();
        }

        for ( ; ; ) {
            switch (in.ttype) {
            case '+':
                in.nextToken();  q = term();  break;
            case '-':
                in.nextToken();  q = term();  changeSign(q);  break;
            default:
                return p;
            }
            add(p, q);
        }
    }

    /** 初期化 */
    static void initialize() {
        for (int i = 0; i < table.length; i++) table[i] = -1;
    }

    /** 文字宣言 */
    static void declare() throws IOException {
        for (int i = 0; i < table.length; i++) {
            table[i] = -1;
            if (value[i] != null) {
                dispose(value[i]);  value[i] = null;
            }
        }
        int j = 0;  in.nextToken();
        while (alphabetOrder(in.ttype) >= 0) {
            int i = alphabetOrder(in.ttype);
            if (table[i] < 0) {
                if (j >= N_LETTER) error("文字が多すぎます");
                table[i] = j;  letter[j] = (char)in.ttype;  j++;
            }
            if (in.nextToken() == ',') in.nextToken();
        }
    }

    /** 代入 */
    static void assign() throws IOException {
        int i = alphabetOrder(in.ttype);
        if (table[i] >= 0) error("左辺が間違っています");
        if (in.nextToken() != '=') error("'=' がありません");
        in.nextToken();
        Node p = expression();
        if (value[i] != null) dispose(value[i]);
        value[i] = p;
    }

    /** 式の印刷 */
    static void print() throws IOException {
        boolean first = true;
        in.nextToken();
        Node p = expression(), q = p;
        System.out.println();

        while ((p = p.next) != null) {
            boolean one = false;
            long re = p.real, im = p.imag;

            if (im == 0) {
                if (re >= 0) {
                    if (!first) System.out.print(" + ");
                } else {
                    System.out.print(" - ");  re = -re;
                }
                if (re == 1) one = true;
                else System.out.print(re);
            } else if (re == 0) {
                if (im >= 0) {
                    if (!first) System.out.print(" + ");
                } else {
                    System.out.print(" - ");  im = -im;
                }
                System.out.print(im + "i");
            } else {
                if (!first) System.out.print(" + ");
                System.out.print("(" + re + (im < 0 ? "" : "+")
                                 + im + "i)");
            }
            first = false;
            for (int i = 0; i < N_LETTER; i++) {
                int e = p.expo[i];
                if (e != 0) {
                    if (!one) System.out.print(" * ");
                    one = false;
                    System.out.print(letter[i]);
                    if (e != 1) System.out.print("^" + e);
                }
            }
            if (one) System.out.print("1");
        }
        if (first) System.out.print("0");
        System.out.println();
        dispose(q);
    }

    /**
    *多項式の累乗，微分などの計算を行います。
    *線形リストを使っています。<br>
    *<strong>入出力の例</strong>（下線の部分を入力する）
    *<blockquote><pre>
    * ***** 簡単な多項式処理系 *****
    *<code><u>@x,y;</u></code>
    *<code>@x,y;</code>
    *0 個のセル使用 (0 個使用中)
    *<code>
    *<u>?(x+y)^10:x;</u>
    *?(x+y)^10:x;
    *10 * x^9 + 90 * x^8 * y + 360 * x^7 * y^2 + 840 * x^6 * y^3 + 1260 * x^5 * y^4 +
    * 1260 * x^4 * y^5 + 840 * x^3 * y^6 + 360 * x^2 * y^7 + 90 * x * y^8 + 10 * y^9
    *</code>
    *26 個のセル使用 (0 個使用中)
    *</pre></blockquote>
    *表示されるのは (x+y)<sup>10</sup> をxで微分した結果です。
    *終了するには <strong>Ctrl + z</strong> を押します。<br>
    *詳しくは本文をお読み下さい。
    */
    public static void main(String[] args) throws IOException {
        in = new StreamTokenizer(new InputStreamReader(System.in));
        in.commentChar('%');
        in.ordinaryChars('a', 'z');  in.ordinaryChar('-');
        in.ordinaryChars('A', 'Z');  in.ordinaryChar('\'');
        System.out.println("***** 簡単な多項式処理系 *****");
        initialize();

        while (in.nextToken() != TT_EOF) {
            if (in.ttype == '@') declare();
            else if (in.ttype == '?') print();
            else if (alphabetOrder(in.ttype) >= 0) assign();
            else error("illegal statement");
            if (in.ttype != ';') error("';' がありません");
            System.out.println("\n" + maxCells + " 個のセル使用 ("
                               + cells + " 個使用中)");
            maxCells = cells;
        }
    }
}