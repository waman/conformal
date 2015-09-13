package java_algorithm.datatype.collection.search;

/**
 *  自己組織化探索(先頭移動法)
 */

import java.io.*;

public class SelfOrganizeSearch2 {
    class Item {
        Item next;  String key, info;
        Item(Item next, String key, String info) {
            this.next = next;  this.key = key;  this.info = info;
        }
    }

    Item head;  // リストの頭

    public SelfOrganizeSearch2() {
        head = new Item(null, null, null);
        head.next = head;
    }

    public void add(String key, String info) {
        head.next =new Item(head.next, key, info);
    }

    public String getInfo(String key) {
        head.key = key;  // 番人
        Item p = head,  q;
        do {
            q = p;
            p = p.next;
        } while (!p.key.equals(key));
        if (p == head) return null;
        q.next = p.next;
        p.next = head.next;
        head.next = p;
        return p.info;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        SelfOrganizeSearch2 list = new SelfOrganizeSearch2();

        for ( ; ; ) {
            String key, info;
            System.out.print("名前? ");
            if ((key = input.readLine()) == null) break;
            if ((info = list.getInfo(key)) != null)
                System.out.println("住所: " + info);
            else {
                System.out.print("住所? ");
                if ((info = input.readLine()) != null)
                    list.add(key, info);
            }
        }
    }
}