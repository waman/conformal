package java_algorithm.statistics.distribution;

public class TDistribution {

    public static double lower(int df, double t) {
        double c2 = df / (df + t * t), s = Math.sqrt(1 - c2);
        if (t < 0) s = -s;
        double p = 0;
        for (int i = df % 2 + 2; i <= df; i += 2) {
            p += s;  s *= (i - 1) * c2 / i;
        }
        if ((df & 1) != 0)
            return 0.5 + (p * Math.sqrt(c2) + Math.atan(t / Math.sqrt(df)))
                          / Math.PI;
        else
            return (1 + p) / 2;
    }

    public static double upper(int df, double t) {
        return 1 - lower(df, t);
    }
}