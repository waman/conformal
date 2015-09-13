package java_algorithm.datatype.collection.tree;

/**
 *  B木
 */

import java.io.*;

public class BTree {

    private static final int M = 2;  // 1ページのデータ数の上限の半分
    private int insertKey;
    private Page insertPage;  // 挿入用
    private boolean undersize;  // 削除用

    private class Page {
        int n = 0;                            // データ数
        int[] key = new int[2 * M];           // キー
        Page[] branch = new Page[2 * M + 1];  // 他ページへのポインタ

        /** 木を再帰的にたどって \texttt{searchKey} を探す */
        private boolean searchNode(int searchKey) {
            int i = 0;
            while (i < n && key[i] < searchKey) i++;
            if (i < n && key[i] == searchKey) return true;
            if(branch[i] == null) return false;    // TODO
            return branch[i].searchNode(searchKey);
        }

        /** \texttt{newKey} を \texttt{key[i]} に，
            \texttt{newPage} を \texttt{branch[i + 1]} に挿入 */
        private void insertItem(int i, int newKey, Page newPage) {
            for (int j = n; j > i; j--) {
                branch[j + 1] = branch[j];
                key[j] = key[j - 1];
            }
            key[i] = newKey;  branch[i + 1] = newPage;  n++;
        }

        /** \texttt{insertKey} を \texttt{key[i]} に挿入し，このページを割る */
        private void split(int i) {
            final int m;
            if (i <= M) m = M;  else m = M + 1;
            Page q = new Page();
            for (int j = m + 1; j <= 2 * M; j++) {
                q.key[j - m - 1] = key[j - 1];
                q.branch[j - m] = branch[j];
            }
            q.n = 2 * M - m;  n = m;
            if (i <= M) insertItem(i, insertKey, insertPage);
            else      q.insertItem(i - m, insertKey, insertPage);
            insertKey = key[n - 1];
            q.branch[0] = branch[n];  n--;
            insertPage = q;
        }

        /** 木を再帰的にたどって \texttt{insertKey} を挿入 */
        private boolean insertNode() {
            int i = 0;
            while (i < n && key[i] < insertKey) i++;
            if (i < n && key[i] == insertKey) {
                message = "もう登録されています";  return true;
            }
            if (branch[i] != null && branch[i].insertNode())
                return true;
            if (n < 2 * M) {  // ページが割れない場合
                insertItem(i, insertKey, insertPage);  return true;
            } else {         // ページが割れる場合
                split(i);  return false;
            }
        }

        /** \texttt{key[i]}, \texttt{branch[i+1]} を外し，
            ページが小さくなりすぎたら \texttt{undersize} フラグを立てる */
        private void deleteItem(int i) {
            while (++i < n) {
                key[i - 1] = key[i];
                branch[i] = branch[i + 1];
            }
            branch[n] = null;
            undersize = (--n < M);
        }

        /** \texttt{branch[i - 1]} の最右要素を
            \texttt{key[i - 1]} 経由で \texttt{branch[i]} に動かす */
        private void moveRight(int i) {
            Page left = branch[i - 1],  right = branch[i];
            right.insertItem(0, key[i - 1], right.branch[0]);
            key[i - 1] = left.key[left.n - 1];
            right.branch[0] = left.branch[left.n];
            left.n--;  // \texttt{left.deleteItem(left.n)} と同じ
        }

        /** \texttt{branch[i]} の最左要素を
            \texttt{key[i - 1]} 経由で \texttt{branch[i - 1]} に動かす */
        private void moveLeft(int i) {
            Page left = branch[i - 1],  right = branch[i];
            left.insertItem(left.n, key[i - 1], right.branch[0]);
            key[i - 1] = right.key[0];
            right.branch[0] = right.branch[1];
            right.deleteItem(0);
        }

        /** \texttt{branch[i - 1]}, \texttt{branch[i]} を結合する */
        private void combine(int i) {
            Page left = branch[i - 1],  right = branch[i];
            left.insertItem(left.n, key[i - 1], right.branch[0]);
            for (int j = 1; j <= right.n; j++)
                left.insertItem(left.n,
                                right.key[j - 1], right.branch[j]);
            deleteItem(i - 1);  // C++ では {\tt delete right;} も必要
        }

        /** 小さくなりすぎたページ \texttt{branch[i]} を修復する */
        private void restore(int i) {
            undersize = false;
            if (i > 0) {
                if (branch[i - 1].n > M) moveRight(i);
                else combine(i);
            } else {
                if (branch[1].n > M) moveLeft(1);
                else combine(1);
            }
        }

        /** 再帰的に木をたどり \texttt{deleteKey} を削除する */
        private boolean deleteNode(int deleteKey) {
            int i = 0;  boolean deleted = false;
            while (i < n && key[i] < deleteKey) i++;
            if (i < n && key[i] == deleteKey) {  // 見つかった
                deleted = true;
                Page q = branch[i + 1];
                if (q != null) {
                    while (q.branch[0] != null) q = q.branch[0];
                    key[i] = deleteKey = q.key[0];
                    branch[i + 1].deleteNode(deleteKey);
                    if (undersize) restore(i + 1);
                } else deleteItem(i);
            } else {
                if (branch[i] != null) {
                    deleted = branch[i].deleteNode(deleteKey);
                    if (undersize) restore(i);
                }
            }
            return deleted;
        }

        /** 再帰的に木をたどり表示する */
        private void print() {
            System.out.print("(");
            for (int i = 0; i <= n; i++) {
                if (branch[i] == null) System.out.print(".");
                else branch[i].print();  // 再帰呼出し
                if (i < n) System.out.print(key[i]);
            }
            System.out.print(")");
        }
    }

    private Page root = new Page();        // B木の根
    static String message = "";            // 関数の返すメッセージ

    /** キー \texttt{key} をB木から探す */
    public void searchNode(int key) {
        if (root.searchNode(key)) message = "見つかりました";
        else                      message = "見つかりません";
    }

    /** キー \texttt{key} をB木に挿入する */
    public void insertNode(int key) {
        message = "登録しました";
        insertKey = key;  insertPage = null;
        if (root != null && root.insertNode()) return;
        Page p = new Page();  p.branch[0] = root;  root = p;
        p.insertItem(0, insertKey, insertPage);
    }

    /** キー \texttt{key} をB木から外す */
    public void deleteNode(int key)  {
        undersize = false;
        if (root.deleteNode(key)) {  // 根から再帰的に木をたどり削除する
            if (root.n == 0) {  // 根が空になった場合
                /* Page p = root; */
                root = root.branch[0];  /* delete p; */
            }  // C++ では {\tt /*..*/} の処理も必要
            message = "削除しました";
        } else message = "見つかりません";
    }

    /** デモ用にB木全体を表示する */
    public void print() {
        if (root != null) root.print();  else System.out.print(".");
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));
        BTree tree = new BTree();

        for ( ; ; ) {
            System.out.print("挿入 In, 検索 Sn, 削除 Dn (n:整数)? ");
            String s = input.readLine();
            if (s == null) break;
            int key = Integer.parseInt(s.substring(1));
            switch (Character.toUpperCase(s.charAt(0))) {
            case 'I':  tree.insertNode(key);  break;
            case 'S':  tree.searchNode(key);  break;
            case 'D':  tree.deleteNode(key);  break;
            default :  message = "???";   break;
            }
            System.out.println(message);
            tree.print();
        }
    }
}