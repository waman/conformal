package java_algorithm.statistics.distribution;

public class FDistribution {

    public static double upper(int df1, int df2, double f) {
        if (f <= 0) return 1;
        if (df1 % 2 != 0 && df2 % 2 == 0)
            return 1 - upper(df2, df1, 1 / f);
        double cos2 = 1 / (1 + df1 * f / df2), sin2 = 1 - cos2;
        if (df1 % 2 == 0) {
            double prob = Math.pow(cos2, df2 / 2.0), temp = prob;
            for (int i = 2; i < df1; i += 2) {
                temp *= (df2 + i - 2) * sin2 / i;
                prob += temp;
            }
            return prob;
        }
        double  prob = Math.atan(Math.sqrt(df2 / (df1 * f))),
                temp = Math.sqrt(sin2 * cos2);
        for (int i = 3; i <= df1; i += 2) {
            prob += temp;  temp *= (i - 1) * sin2 / i;
        }
        temp *= df1;
        for (int i = 3; i <= df2; i += 2) {
            prob -= temp;  temp *= (df1 + i - 2) * cos2 / i;
        }
        return prob * 2 / Math.PI;
    }

    public static double lower(int df1, int df2, double f) {
        if (f <= 0) return 0;
        return upper(df2, df1, 1 / f);
    }
}