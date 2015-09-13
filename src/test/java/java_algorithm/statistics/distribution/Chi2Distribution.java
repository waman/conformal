package java_algorithm.statistics.distribution;

public class Chi2Distribution {

    public static double upper(int df, double chi2) { // 上側累積確率
        if ((df & 1) != 0) { // 自由度が奇数
            double chi = Math.sqrt(chi2);
            if (df == 1) return 2 * NormalDistribution.upper(chi);
            double s = chi * Math.exp(-0.5 * chi2) / Math.sqrt(2 * Math.PI),
                   t = s;
            for (int k = 3; k < df; k += 2) {
                t *= chi2 / k;  s += t;
            }
            return 2 * (NormalDistribution.upper(chi) + s);
        } else { // 自由度が偶数
            double s = Math.exp(-0.5 * chi2), t = s;
            for (int k = 2; k < df; k += 2) {
                t *= chi2 / k;  s += t;
            }
            return s;
        }
    }

    public static double lower(int df, double chi2) { // 下側累積確率
        return 1 - upper(df, chi2);
    }
}