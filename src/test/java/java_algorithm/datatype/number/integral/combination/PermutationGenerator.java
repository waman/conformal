package java_algorithm.datatype.number.integral.combination;

/**
 *  順列生成
 */

/** 共通の定義 */
public class PermutationGenerator {

    static int n, a[], count;

    public static void init(int num) {  // 共通の初期化
        n = num;
        a = new int[n];
        count = 0;
    }

    public static void show() {
        count++;
        System.out.print(count + ": ");
        for (int i = 0; i < n; i++) System.out.print(" " + a[i]);
        System.out.println();
    }

    /** 各方法のテスト */

    public static void main(String[] args) {
        System.out.println("方法 1");
        PermutationGenerator1.generate(4);

        System.out.println("方法 2");
        PermutationGenerator2.generate(4);

        System.out.println("方法 3");
        PermutationGenerator3.generate(4);

        System.out.println("方法 4");
        PermutationGenerator4.generate(4);
    }
}


/** 方法 1 */
class PermutationGenerator1 extends PermutationGenerator {

    static boolean used[];

    public static void generate(int n) {
        init(n);  // 共通の初期化
        used = new boolean[n + 1];
        for (int k = 1; k <= n; k++) used[k] = false;  // 未使用の標識
        put(0);
    }

    static void put(int pos) {
        if (pos == n) {  show();  return;  }
        for (int k = 1; k <= n; k++)
            if (!used[k]) {
                used[k] = true;
                a[pos] = k;
                put(pos + 1);
                used[k] = false;
            }
    }
}

/** 方法 2 */
class PermutationGenerator2 extends PermutationGenerator {

    public static void generate(int n) {
        init(n);  // 共通の初期化
        for (int pos = 0; pos < n; pos++) a[pos] = 0;  // 未使用の標識
        put(1);
    }

    static void put(int k) {
        if (k == n + 1) {
            show();
            return;
        }
        for (int pos = 0; pos < n; pos++)
            if (a[pos] == 0) {
                a[pos] = k;  put(k + 1);
                a[pos] = 0;
            }
    }
}


/** 方法 3 */
class PermutationGenerator3 extends PermutationGenerator {

    public static void generate(int n) {
        init(n);  // 共通の初期化
        for (int i = 0; i < n; i++) a[i] = i + 1;
        perm(n - 1);
    }

    static void perm(int pos) {
        if (pos == 0) {
            show();
            return;
        }
        for (int i = pos; i >= 0; i--) {
            int swap = a[pos];
            a[pos] = a[i];
            a[i] = swap;

            perm(pos - 1);
            swap = a[pos];
            a[pos] = a[i];
            a[i] = swap;
        }
    }
}

/** 方法 4 */
class PermutationGenerator4 extends PermutationGenerator {

    public static void generate(int n) {
        init(n);  // 共通の初期化
        int c[] = new int [n + 1];
        for (int i = 0; i < n; i++) a[i] = i + 1;
        for (int i = 1; i <= n; i++) c[i] = i;  // c[n]≠0 は番人

        for (int k = 1; k < n; ) {
            int i;
            if ((k % 2) == 0) i = 0;  else i = c[k];
            int swap = a[k];
            a[k] = a[i];
            a[i] = swap;
            show();

            for (k = 1; c[k] == 0; k++) c[k] = k;
            c[k]--;
        }
    }
}