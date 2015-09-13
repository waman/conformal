package java_algorithm.datatype.number.real;

import java.util.*;

/**
 * AGM - Arithmetic Geographic Mean<br>
 * <p>
 * The positive variables a and b of the series
 *     <blockquote>
 *     <code>(a, b) = ((a + b) / 2, sqrt(a * b))</code>
 *     </blockquote>
 * converge to a value and is called the arithmetic geographic mean of a and b.
 * </p>
 */
public class AGM {

    /**
     * <code>EPSILON</code> - A variable to determine the end of convergence.
     * Set the value be some times of the machine epsilon.
     */
    private static final double EPSILON = 1e-15;

    /**
     * agm() returns the arithmetic geographic mean of two double values.
     * @param a non zero positive value.
     * @param b non zero positive value.
     * @return The arithmetic geographic mean of <code>a</code> and <code>b</code>.
     */
    public static double agm(double a, double b) {
        if ((a < 0) || (b < 0))
            return(Double.NaN);
        if (a == 0 || Double.isInfinite(a) || Double.isNaN(a))
            return(a);
        if (b == 0 || Double.isInfinite(b) || Double.isNaN(b))
            return(b);

        double t;
        do {
            t = (a + b) * 0.5;
            b = Math.sqrt(a * b);
            a = t;
        } while((a / b - 1.0) > EPSILON); // a > b
        return (a + b) * 0.5;
    }

    public static void main(String[] args){
        Random a = new Random(12345);
        for (int i = 0; i < 100; i++) {
            double b = a.nextDouble() * 100.0;
            double c = a.nextDouble() * 100.0;
            System.out.println("agm("+b+", "+c+")="+agm(b, c));
        }
        System.out.println("agm(1.0, sqrt(2.0))="+agm(1.0, Math.sqrt(2.0)));
    }
}