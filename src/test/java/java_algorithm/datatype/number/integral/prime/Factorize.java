package java_algorithm.datatype.number.integral.prime;

/**
 *  素因数分解
 */
public class Factorize {

    static void factorize(int x) {
        System.out.print(x + " = ");
        while (x >= 4 && x % 2 == 0) {
            System.out.print("2 * ");  x /= 2;
        }
        int i = 3;
        while (i * i <= x)
            if (x % i != 0) i += 2;
            else { System.out.print(i + " * ");  x /= i; }
        System.out.println(x);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) factorize(i);
    }
}