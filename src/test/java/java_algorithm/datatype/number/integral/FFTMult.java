package java_algorithm.datatype.number.integral;

/*
* FFTMult.java -- FFT 乗算
* 基数10,000の多倍長実数クラス SInteger 使用
*/

import java_algorithm.datatype.number.javamath.BDTools;
import java_algorithm.datatype.number.javamath.BITools;
import java_algorithm.functional.transform.FFT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
* FFT 乗算のテスト・プログラムです。
* @see BDTools
* @see SInteger
*/
public class FFTMult {

    static final double EPS = 0.25;

    /**
    * SInteger a, b を与えると a と b の積を返します。
    * @see SInteger
    */
    public static SInteger getValue(SInteger a, SInteger b) {
        int N = SInteger.ceilpow2(Math.max(a.size, b.size)) * 2, i;
        FFT fftN = new FFT(N);
        double xr[] = new double[N];    // 0 に初期化される
        double xi[] = new double[N];
        double yr[] = new double[N];
        double yi[] = new double[N];
        double zr[] = new double[N];
        double zi[] = new double[N];

        for (i = 0; i < a.size ; i++) xr[i] = a.f[i];
        for (i = 0; i < b.size ; i++) yr[i] = b.f[i];
        fftN.fft(xr, xi);   fftN.fft(yr, yi);  // FFT

        /* 積の計算
        * メモリ節約のために xr[], xi[] に上書きすることもできる。
        * double xR = xr[i] * yr[i] - xi[i] * yi[i];
        * double xI = xr[i] * yi[i] + xi[i] * yr[i];
        * xr[i] = xR;   xi[i] = xI;
        */
        for (i = 0; i < N; i++) {
            zr[i] = xr[i] * yr[i] - xi[i] * yi[i];
            zi[i] = xr[i] * yi[i] + xi[i] * yr[i];
        }
        fftN.ifft(zr, zi);  // 逆FFT

        SInteger r = new SInteger(N);
        // 正規化 「Java の long の桁数 > double の有効桁数」であることを使っている。
        // long が 32 ビットの場合は double を使う必要がある。
        for (i = 0; i < N - 1; i++) {
            long w = (long)(zr[i] + EPS); // double を整数値に丸める
            r.f[i] = (short)(w % SInteger.RADIX); // 剰余をその桁に残す
            zr[i + 1] += w / SInteger.RADIX; // 繰り上がり
        }
        r.f[i] = (short)(zr[i] + EPS);  // 最上位の桁
        return r;
    }

    /**
    *テストプログラムです。<br>
    *基数10,000での桁数 n を入力すると，FFT 乗算と BigInteger 乗算の計算結果と
    *速度の比較を行います。n = 10,000 程度にすると FFT 乗算の方が数倍速いことが
    *分かります。
    * @see BITools
    */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("桁数／４ = ");
        int fig = Integer.parseInt(in.readLine());  fig = SInteger.ceilpow2(fig);
        if (fig <= 0) {
            System.err.println("正の数値を入力してください。");
            throw new IllegalArgumentException();
        }
        SInteger siA = new SInteger(fig);
        SInteger siB = new SInteger(fig);
        SInteger siC;

        System.out.println( (fig * SInteger.FIGURES) + " 桁の乱数作成中");
        siA.setRandom(fig);     siB.setRandom(fig);
        System.out.println("SInteger --> BigInteger 基数変換中");

        BigInteger a = siA.toBigInteger();
        BigInteger b = siB.toBigInteger();

        System.out.println("\na = ");   BITools.print(a, 10);
        System.out.println("\nb = " + BITools.toDelimiterString(b, 10) + '\n');

        System.out.println("BigInteger 乗算");
        long start = System.currentTimeMillis();
        BigInteger c = a.multiply(b); // c = a * b
        long tBD = System.currentTimeMillis() - start;
        System.out.println("result of a.multiply(b) =\n");
        BITools.print(c, SInteger.FIGURES);

        System.out.println("\nFFT 乗算");
        start = System.currentTimeMillis();
        siC = getValue(siA, siB);
        long tFFT = System.currentTimeMillis() - start;
        System.out.println("result of getValue(a, b) =");
        siC.print();

        System.out.println("SInteger --> BigInteger 基数変換中");
        BigInteger d = siC.toBigInteger();
        int dig = BDTools.biFig10(d);

        d = d.subtract(c);  // d -= c
        System.out.println("c - d = " + d + " <== 0 になるはず");

        System.out.println("乗算後の 10 進の桁数の概算値 : " + dig +
            "\n計算時間 BigInteger 乗算 : " + tBD + "(ms)" + "  FFT 乗算 : " + tFFT + "(ms)");
    }
}