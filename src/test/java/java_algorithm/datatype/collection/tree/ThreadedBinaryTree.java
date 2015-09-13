package java_algorithm.datatype.collection.tree;

/**
 *  ひも付き2分木
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

public class ThreadedBinaryTree {

    private static class Node {  // 木のノード

        Node left, right;  // 左右の子へのポインタ

        boolean isLeftThread, isRightThread;  // ポインタがひもかどうか
        String key;        // 探索のキー（登録文字列）
        int count;         // 参照回数カウンタ

        // 新しいノードを生成
        Node(String key, Node left, boolean isLeftThread,
                         Node right, boolean isRightThread) {
            this.key = key;  // キーをコピーする
            count = 1;         // 参照回数を1にする
            this.left  = left;   this.isLeftThread =  isLeftThread;
            this.right = right;  this.isRightThread = isRightThread;
        }

        private void insertLeft(String key) {  // このノードの左に挿入
            left = new Node(key,        // 新しいノードを生成
                     left, isLeftThread,  // 左の子は親の左の子を受け継ぐ
                     this, true);         // 右の子は親を指すひも
            isLeftThread = false;    // {\tt q} はひもでない
        }

        private void insertRight(String key) {  // このノードの右に挿入
            right = new Node(key, this, true, right, isRightThread);
            isRightThread = false;  // 上の {\tt insertLeft()} 参照
        }

        private Node successor() {  // 昇順でこの直後のノード
            // ${\tt right} \leftrightarrow {\tt left}$, ${\tt isRightThread} \leftrightarrow {\tt isLeftThread}$ とすれば直前のノードになる
            Node node = right;
            if (!isRightThread)
                while (!node.isLeftThread) node = node.left;
            return node;
        }
    }

    Node root;  // 木の根

    ThreadedBinaryTree() {
        root = new Node(null, null, true, null, true);
        root.left = root.right = root;
    }

    public void insertNode(String key) {  // 挿入(登録)
        Node node = root;
        int cmp = 1;  // 比較結果（最初の子は親の右に）
        do {
            if (cmp < 0) {    // 小さければ左に登録
                if (!node.isLeftThread) node = node.left;
                else {  node.insertLeft(key);  return;  }
            } else {          // 大きければ右に登録
                if (!node.isRightThread) node = node.right;
                else {  node.insertRight(key);  return;  }
            }
        } while ((cmp = key.compareTo(node.key)) != 0);
        node.count++;           // 等しければ参照回数を増すだけ
    }

    void printNode() {  // 昇順で全キーを出力
        Node node = root;
        while ((node = node.successor()) != root)
            System.out.println(node.key + " " + node.count);
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer input = new StreamTokenizer(new InputStreamReader(System.in));

        ThreadedBinaryTree tree = new ThreadedBinaryTree();
        while (input.nextToken() != StreamTokenizer.TT_EOF)
            if (input.ttype == StreamTokenizer.TT_WORD)  // 標準入力から
                tree.insertNode(input.sval);  // 単語を読み登録
        tree.printNode();    // 各単語と出現回数を昇順に出力
    }
}