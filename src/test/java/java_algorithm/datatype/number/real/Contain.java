package java_algorithm.datatype.number.real;

/**
 * Contain.java -- 区間の包含関係
 */

public class Contain {

    /** 区間を表す内部クラス */
    public static class Interval implements Comparable {

        int left, right;

        public Interval(int left, int right) {
            this.left = left;   this.right = right;
        }

        @Override
        public int compareTo(Object object) {  // 比較関数
            Interval x = (Interval)object;
            if (left  != x.left)  return left  - x.left;
            if (right != x.right) return right - x.right;
            /*
              // double 型であればこちらを使う。
              if (left  > x.left)  return  1;
              if (left  < x.left)  return -1;
              if (right > x.right) return  1;
              if (right < x.right) return -1;
            */
            return 0;
        }

        @Override
        public String toString() {
            return "(" + left + ", " + right + ")";
        }
    }

    public static void markContained(Interval[] a, boolean[] contained) {
        java.util.Arrays.sort(a); // 標準ライブラリのマージソート $O(n \log n)$
        int maxRight = a[0].right;
        contained[0] = false;

        for (int i = 1; i < a.length; i++) {
            if (a[i].right <= maxRight){
                contained[i] = true;
            }else {
                maxRight = a[i].right;
                contained[i] = false;
            }
        }
    }

    /**
     * 乱数を使って複数個の区間を作り，それらの包含関係を表示します。<br>
     * 他の区間に含まれているものには * が付きます。
     */
    public static void main(String[] args) {
        final int N = 20;
        Contain.Interval[] a = new Contain.Interval[N];  // a[i] は null
        boolean[] contained = new boolean[N];

        int i = 0;
        while (i < N) {
            int x0 = (int)(Math.random() * 100);
            int x1 = (int)(Math.random() * 100);
            if (x0 < x1) {
                a[i] = new Contain.Interval(x0, x1);
                i++;
            }
        }

        Contain.markContained(a, contained);
        for (i = 0; i < N; i++)
            System.out.println(a[i] + (contained[i] ? " *" : ""));
    }
}