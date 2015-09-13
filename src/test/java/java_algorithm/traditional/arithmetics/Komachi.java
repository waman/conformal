package java_algorithm.traditional.arithmetics;

/*
* Komachi.java -- 小町算
*/

/**
*小町算の解を求めます。
*/

public class Komachi {

    /**
    *小町算のすべての解(12通り)を表示するプログラムです。
    */
    public static void main(String[] args) {
        int[] sign = new int[10];

        for (int i = 1; i <= 9; i++) sign[i] = -1;
        do {
            int x = 0, n = 0, s = 1, i;
            for (i = 1; i <= 9; i++)
                if (sign[i] == 0) n = 10 * n + i;
                else {
                    x += s * n;  s = sign[i];  n = i;
                }
            x += s * n;
            if (x == 100) {
                for (i = 1; i <= 9; i++) {
                    if      (sign[i] ==  1) System.out.print(" + ");
                    else if (sign[i] == -1) System.out.print(" - ");
                    System.out.print(i);
                }
                System.out.println(" = 100");
            }
            i = 9;  s = sign[i] + 1;
            while (s > 1) {
                sign[i] = -1;  i--;  s = sign[i] + 1;
            }
            sign[i] = s;
        } while (sign[1] < 1);
    }
}