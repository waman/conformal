package java_algorithm.datatype.collection;

/**
 *  リスト
 */

public class List {

    private static class Item {

        Item next;
        int info;

        Item(Item next, int info) {
            this.next = next;
            this.info = info;
        }
    }

    private Item head = null;

    public void add(int info) {
        this.head = new Item(this.head, info);
    }

    @Override
    public String toString() {
        String s = "";
        for (Item p = this.head; p != null; p = p.next)s += " " + p.info;
        return s;
    }

    public void reverse() {
        Item q = null;
        for (Item p = head; p != null; ) {
            Item t = q;
            q = p;
            p = p.next;
            q.next = t;
        }
        this.head = q;
    }

    public static void main(String[] args) {
        List list = new List();  // 空のリスト
        for (int x = 1; x <= 9; x++) list.add(x);
        System.out.println(list);
        list.reverse();
        System.out.println(list);
    }
}