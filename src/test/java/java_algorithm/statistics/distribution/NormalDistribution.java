package java_algorithm.statistics.distribution;

public class NormalDistribution {

    public static double lower(double z) { // 累積確率（下側）
        double  z2 = z * z,
                t = z * Math.exp(-0.5 * z2) / Math.sqrt(2 * Math.PI),
                p = t;
        for (int i = 3; i < 200; i+= 2) {
            double prev = p;  t *= z2 / i;  p += t;
            if (p == prev) return 0.5 + p;
        }
        return (z > 0) ? 1 : 0;
    }

    public static double upper(double z) { // 累積確率（上側）
        return 1 - lower(z);
    }
}