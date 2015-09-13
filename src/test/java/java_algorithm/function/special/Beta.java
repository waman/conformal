package java_algorithm.function.special;

public class Beta {

    public static double beta(double x, double y) { // ベータ関数

        // Gamma.loggamma(x) = log Γ(x) を使う
        return Math.exp(
                    Gamma.loggamma(x) + Gamma.loggamma(y) - Gamma.loggamma(x + y));
    }
}