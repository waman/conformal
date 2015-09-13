package java_algorithm.random;

/* pinkFilter: 1 / f 発生用 3dB/oct. IIR LPF
 * ＣＲラダーフィルタにより近似した 1 / f 特性の伝達関数を IIR デジタル
 * フィルタで実現し、乱数で発生したホワイトノイズにこのフィルターを施す
 * ことにより N オクターブにわたる 1 / f 雑音を得ることが出来る。
 */
/** double pinkFilter(double x)
 * 3dB/oct. over N octave IIR LPF
 */

public class PinkFilter extends RuntimeException {

    int n;
    double[] z;
    double[] k;
    double t = 0.0;

    public PinkFilter(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("PinkFilter: N must be >= 1");
        this.n = n;
        z = new double[n]; // z[] は 0 で初期化
        k = new double[n];
        k[n - 1] = 0.5;
        for (int i = n - 1; i > 0; i--)
            k[i - 1] = k[i] * 0.25;
    }

    public double pinkFilter(double in) {
        double q = in;
        // 3dB/oct. over n octave IIR LPF
        for (int i = 0; i < n; i++) {
            z[i] = (q * k[i] + z[i] * (1.0 - k[i]));
            q = (q + z[i]) * 0.5 ;
        }
        t = (q + t) * 0.5; // 6dB/oct. Nyqist frequency LPF.
        return t;
    }

    public static void main(String[] args) {
        final int N = 16; // 3dB/oct. over 16 octave.
        PinkFilter pf = new PinkFilter(N);
        for (int i = 0; i < 16384; i++) {
            System.out.println(pf.pinkFilter(Math.random() - 0.5));
        }
    }
}