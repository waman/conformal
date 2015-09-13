package java_algorithm.statistics;

/*
* MaxMin.java -- 最大値・最小値
*/

/**
*int 型配列の中から最大値・最小値を求めます。
*/
public class MaxMin {

    /**
    *最大値を求めます。
    *@param a 配列
    *@return 最大値
    */
    public static int findMax(int a[]) {
        int max = a[0];
        for (int i = 1; i < a.length; i++)
            if (a[i] > max) max = a[i];
        return max;
    }

    /**
    *最小値を求めます。
    *@param a 配列
    *@return 最小値
    */
    public static int findMin(int a[]) {
        int min = a[0];
        for (int i = 1; i < a.length; i++)
            if (a[i] < min) min = a[i];
        return min;
    }

    /**
    *最大値と最小値を求めます。
    *@param a 配列
    *@return n[2]：n[0] に最大値，n[1] に最小値
    */
    public static int[] findMaxMin(int a[]) {
        int max, min, n = a.length;

        if ((n % 2) != 0)        max = min = a[0];
        else if (a[0] > a[1]) {  max = a[0];  min = a[1];  }
        else                  {  max = a[1];  min = a[0];  }
        for (int i = 2 - (n % 2); i < n; i += 2) {
            int a1 = a[i], a2 = a[i + 1];
            if (a1 > a2) {
                if (a1 > max) max = a1;
                if (a2 < min) min = a2;
            } else {
                if (a2 > max) max = a2;
                if (a1 < min) min = a1;
            }
        }
        return new int[] {max, min};
    }

    /**
    *乱数を使った例題プログラムです。
    */
    public static void main(String[] args) {
        int N = 15, a[] = new int[N];

        for (int i = 0; i < N; i++) a[i] = (int)(100 * Math.random()); // a[i] < 100
        System.out.print("a[] = { ");
        for (int i = 0; i < N - 1; i++) System.out.print(a[i] + ", ");
        System.out.println(a[N - 1] + "};");

        System.out.println("findMax(a) = " + findMax(a));
        System.out.println("findMax(3, 1, 7, 4) = " + findMax(new int[] {3, 1, 7, 4}));
        System.out.println("findMin(a) = " + findMin(a));

        int f[] = findMaxMin(a);
        System.out.print("findMaxMin(a) : ");
        System.out.println("max = " + f[0] + ", min = " + f[1]);

        f = findMaxMin(new int[] {3, 1, 7, 4});
        System.out.print("findMaxMin(3, 1, 7, 4) : ");
        System.out.println("max = " + f[0] + ", min = " + f[1]);
    }
}