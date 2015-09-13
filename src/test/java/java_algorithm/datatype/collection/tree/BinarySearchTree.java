package java_algorithm.datatype.collection.tree;

/**
 *  2分探索木
 */

import java.io.*;

public class BinarySearchTree {

    private static class Node {

        Node left, right;
        String key;

        Node(Node left, Node right, String key) {
            this.left = left;
            this.right = right;
            this.key = key;
        }
    }

    private final Node zero = new Node(null, null, null),  // 各葉の子(番人用)
                               rootParent = new Node(null, zero, null);  // {\tt rootParent.right} が根
    private Node parent;  private boolean isChildLeft;  // 検索成功時の親


    public boolean isEmpty() {
        return (rootParent.right == zero);
    }

    public Node searchNode(String key) {  // 検索（未登録なら {\tt null} を返す）
        zero.key = key;  // {\tt zero} は番人
        Node child = rootParent;
        int cmp = 1;  // 最初の子は {\tt rootParent.right}
        do {
            parent = child;
            if (cmp < 0) { child = child.left;  isChildLeft = true; }
            else         { child = child.right; isChildLeft = false; }
        } while ((cmp = key.compareTo(child.key)) != 0);
        return (child == zero) ? null : child;
    }

    public Node insertNode(String key) {  // 挿入（登録）
        if (searchNode(key) != null) return null;  // すでに登録されている

        Node newChild = new Node(zero, zero, key);
        if (isChildLeft) parent.left  = newChild;
        else             parent.right = newChild;
        return newChild;
    }

    public boolean deleteNode(String key) {  // 削除できれば {\tt true} を返す
        final Node target = searchNode(key);
        if (target == null) return false;  // 未登録

        Node newChild;  // {\tt target} の代わりに {\tt parent} の子になる
        if      (target.left  == zero) newChild = target.right;
        else if (target.right == zero) newChild = target.left;
        else {
            Node s = target.left;  // {\tt s} を {\tt target} の次に小さいものに
            if (s.right != zero) {
                Node p;  // {\tt s} の親
                do {  p = s;  s = s.right;  } while (s.right != zero);
                p.right = s.left;  s.left = target.left;
            }
            s.right = target.right;  newChild = s;
        }
        if (isChildLeft) parent.left  = newChild;
        else             parent.right = newChild;
        return true;  // 削除成功, C++ では {\tt delete target;} も必要
    }

    public void printNode() {
        if (!isEmpty()) printNode(rootParent.right);
    }

    private int depth = 0;

    private void printNode(Node p) { // 深さ優先探索,中間順でキーを表示
        if (p.left != zero) {
            depth++;  printNode(p.left);  depth--;
        }
        for (int i = 0; i < depth; i++) System.out.print("     ");
        System.out.println(p.key);
        if (p.right != zero) {
            depth++;  printNode(p.right);  depth--;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BinarySearchTree tree = new BinarySearchTree();

        System.out.print("命令 Iabc:  abcを挿入\n"
                       + "     Dabc:  abcを削除\n"
                       + "     Sabc:  abcを検索\n");
        for ( ; ; ) {
            System.out.print("命令? ");
            String s = input.readLine();
            if (s == null) break;
            if (s.length() <= 1) s = "help";
            String key = s.substring(1),  message;
            switch (Character.toUpperCase(s.charAt(0))) {
            case 'I':
                if (tree.insertNode(key) != null)
                     message = "登録しました.";
                else message = "登録ずみです.";
                break;
            case 'D':
                if (tree.deleteNode(key))
                     message = "削除しました.";
                else message = "登録されていません.";
                break;
            case 'S':
                if (tree.searchNode(key) != null)
                     message = "登録されています.";
                else message = "登録されていません";
                break;
            default:
                message = "使えるのは I, D, S です.";
                break;
            }
            System.out.println(message);  tree.printNode();
        }
    }
}