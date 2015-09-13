package java_algorithm.datatype.collection.tree;

/**
 *  ハッシュ法
 */

import java.io.*;

public class HashAndTree {

    static final int HASH_SIZE = 101;  // ハッシュ表の大きさ（素数）
    static final int CHAR_BIT = 8;  // 256進法の整数とみる

    class Node {           // 2分木のノード
        Node left, right;  // 左右の子
        String key;        // キー（文字列）
    }

    Node zero, hashTable[];

    public HashAndTree() {
        zero = new Node();  // 番人
        hashTable = new Node[HASH_SIZE];  // ハッシュ表
        for (int i = 0; i < HASH_SIZE; i++) hashTable[i] = zero;
    }

    private int hash(String s) {  // 簡単なハッシュ関数
        int v = 0;
        for (int i = 0; i < s.length(); i++)
            v = ((v << CHAR_BIT) + s.charAt(i)) % HASH_SIZE;
        return v;
    }

    boolean insert(String word) {  // 挿入（登録）
        int cmp;
        boolean isLeftChild = false;
        Node parent = null, child = hashTable[hash(word)];
        zero.key = word;  // 番人
        while ((cmp = child.key.compareTo(word)) != 0) {
            parent = child;
            if (cmp < 0) {
                child = child.left;
                isLeftChild = true;
            }else{
                child = child.right;
                isLeftChild = false;
            }
        }
        if (child != zero) return false;  // すでに登録されている
        Node q = new Node();
        q.key = word;
        q.left = zero;
        q.right = child;

        if (parent == null)
            hashTable[hash(word)] = q;
        else if (isLeftChild)
            parent.left  = q;
        else
            parent.right = q;
        return true;  // 登録成功
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer input = new StreamTokenizer(new InputStreamReader(System.in));

        HashAndTree tree = new HashAndTree();
        int words = 0,  newWords = 0;
        while (input.nextToken() != StreamTokenizer.TT_EOF) {
            if (input.ttype != StreamTokenizer.TT_WORD) continue;
            words++;
            if (tree.insert(input.sval.toLowerCase())) newWords++;
        }
        System.out.println(words + " words, " + newWords + " different words");
    }
}