package java_algorithm.functional.transform;

/**
 *  FFT (高速Fourier変換)
 */

public class FFT {

    int n;
    int[] bitrev;
    double[] sintbl;

    public FFT(int n) {
        this.n = n;
        sintbl = new double[n + n / 4];
        bitrev = new int[n];
        // 三角関数表を作る.
        double t = Math.sin(Math.PI / n);
        double dc = 2 * t * t;
        double ds = Math.sqrt(dc * (2 - dc));
        t = 2 * dc;
        double c = sintbl[n / 4] = 1;
        double s = sintbl[0] = 0;
        for (int i = 1; i < n / 8; i++) {
            c -= dc;  dc += t * c;
            s += ds;  ds -= t * s;
            sintbl[i] = s;  sintbl[n / 4 - i] = c;
        }
        if (n / 8 != 0) sintbl[n / 8] = Math.sqrt(0.5);
        for (int i = 0; i < n / 4; i++)
            sintbl[n / 2 - i] = sintbl[i];
        for (int i = 0; i < n / 2 + n / 4; i++)
            sintbl[i + n / 2] = - sintbl[i];
        // ビット反転表を作る.
        int i = 0;
        int j = 0;
        bitrev[0] = 0;
        while (++i < n) {
            int k = n / 2;
            while (k <= j) {  j -= k;  k /= 2;  }
            j += k;
            bitrev[i] = j;
        }
    }

    void fftsub(double[] x, double[] y, int sign) {
        for (int i = 0; i < n; i++) { // ビット反転
            int j = bitrev[i];
            if (i < j) {
                double t = x[i];  x[i] = x[j];  x[j] = t;
                t = y[i];  y[i] = y[j];  y[j] = t;
            }
        }
        for (int k = 1; k < n; k *= 2) { // 変換
            int h = 0;
            int d = n / (k * 2);
            for (int j = 0; j < k; j++) {
                double c = sintbl[h + n / 4];
                double s = sign * sintbl[h];
                for (int i = j; i < n; i += k * 2) {
                    int ik = i + k;
                    double dx = s * y[ik] + c * x[ik];
                    double dy = c * y[ik] - s * x[ik];
                    x[ik] = x[i] - dx;  x[i] += dx;
                    y[ik] = y[i] - dy;  y[i] += dy;
                }
                h += d;
            }
        }
    }

    public void fft(double[] x, double[] y) {
        fftsub(x, y, 1);
    }

    public void ifft(double[] x, double[] y) {
        fftsub(x, y, -1);
        for (int i = 0; i < n; i++) {
            x[i] /= n;  y[i] /= n;
        }
    }

    // テスト用
    public static void main(String[] args) {
        final int N = 64;
        double x1[] = new double[N];
        double y1[] = new double[N];
        double x2[] = new double[N];
        double y2[] = new double[N];
        double x3[] = new double[N];
        double y3[] = new double[N];
        for (int i = 0; i < N; i++) {
            x1[i] = x2[i] = 6 * Math.cos( 6 * Math.PI * i / N)
                          + 4 * Math.sin(18 * Math.PI * i / N);
            y1[i] = y2[i] = 0;
        }
        FFT fftN = new FFT(N);
        fftN.fft(x2, y2);
        for (int i = 0; i < N; i++) {
            x3[i] = x2[i];  y3[i] = y2[i];
        }
        fftN.ifft(x3, y3);
        System.out.println("(元データ) (フーリエ変換) (逆変換)");
        for (int i = 0; i < N; i++)
            System.out.println(i + " (" +
                               (float)x1[i] + "," +
                               (float)y1[i] + ") (" +
                               (float)x2[i] + ", " +
                               (float)y2[i] + ") (" +
                               (float)x3[i] + ", " +
                               (float)y3[i] + ")");
    }
}