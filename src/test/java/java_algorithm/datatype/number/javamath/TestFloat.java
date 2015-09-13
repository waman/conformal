package java_algorithm.datatype.number.javamath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class TestFloat {

    static final double d = (double)(1 << 23);

    public static void main(String[] args) throws IOException {
        BufferedReader in =  new BufferedReader(new InputStreamReader(System.in));
        while (true) {    // TODO
            System.out.print("Input a hexadecimal number: ");
            int x = Integer.parseInt(in.readLine(), 16);
            int s = x >>> 31;   // sign (1)
            int e = (x >>> 23) & 0xff; // exponent(8)
            int f = x & ((1 << 23) - 1); // mantissa (23)
            if (s == 1) System.out.print('-');
            if (e == 0) {
                System.out.println((f / d) * Math.pow(2, -126));
            } else if (e != 255) {
                System.out.println(((f + d) / d) * Math.pow(2, e - 127));
            } else if (f == 0) {
                System.out.println("INF");
            } else {
                System.out.println("NaN");
            }
            System.out.println(Float.intBitsToFloat(x));
        }
    }
}