package java_algorithm.computerscience.code;

/*
    RangeCoder test
 */
import java.io.*;

class RangeEncoder {

    private int low, range;
    private final BufferedOutputStream out;
    private final int TOP, BOTTOM, totfreq;

    public RangeEncoder(BufferedOutputStream out, int totfreq, int range, int top, int bottom) {
        low = 0;
        this.range = range; TOP = top; BOTTOM = bottom;
        this.totfreq = totfreq;
        this.out = out;
    }

    public void doFinal() throws IOException {
        for (int i = 0; i < 4; i++) {
            out.write(low >> 24);
            low <<= 8;
        }
        out.flush();
    }

    public void encode(int cumfreq, int freq) throws IOException {
        range /= totfreq;
        low += cumfreq * range;
        range *= freq;
        // low と low+range の上位8ビットが同じになればその8ビットを出力
        while ((low ^ (low + range)) < TOP) {
            out.write(low >> 24);
            range <<= 8;
            low <<= 8;
        }
        // low と low+range の上位8ビットが異なるが range がBOTTOM未満のとき
        // 強制的に range を縮める
        while (range < BOTTOM) {
            out.write(low >> 24);
            range = ((-low) & (BOTTOM - 1)) << 8;
            low <<= 8;
        }
    }
}

class RangeDecoder {
    private int low, code, range;
    private final BufferedInputStream in;
    private final int TOP, BOTTOM, totfreq;

    public RangeDecoder(BufferedInputStream in, int totfreq, int range, int top, int bottom) throws IOException {
        low = code = 0;
        this.range = range; TOP = top; BOTTOM = bottom;
        this.totfreq = totfreq;
        this.in = in;
        for (int i = 0; i < 4; i++) {
            code = (code << 8) | in.read();
        }
    }

    public int getfreq() throws IOException {
        range /= totfreq;
        int tmp = (code - low) / range;
        if (tmp >= totfreq) throw new IOException("Input data corrupt.");
        return tmp;
    }

    public void decode(int cumfreq, int freq) throws IOException {
        low += cumfreq * range;
        range *= freq;
        while ((low ^ (low + range)) < TOP) {
            code = (code << 8) | in.read();
            range <<= 8;
            low <<= 8;
        }
        while (range < BOTTOM) {
            code = (code << 8) | in.read();
            range = ((-low) & (BOTTOM - 1)) << 8;
            low <<= 8;
        }
    }
}

class RangeCoder {
    // TOP, BOTTOM, RANGE は unsigned int の使えない Java
    // ではC++ 版に比べて1ビットずつ小さくなっている
    static final int TOP = 1 << 23;
    static final int BOTTOM = 1 << 15;
    static final int RANGE = 0x7FFFFFFF;
    private int freq[] = new int[257];
    private int cumfreq[] = new int[258];

    // 累積頻度を計算
    private void calcCumulativeFrequency() {
        cumfreq[0] = 0;
        for (int i = 1; i < 258; i++) {
            cumfreq[i] = cumfreq[i - 1] + freq[i - 1];
        }
    }

    public void encode(String inName, String outName) throws IOException {
        // 度数分布作成
        BufferedInputStream in;
        in = new BufferedInputStream(new FileInputStream(inName));
        int c;
        while ((c = in.read()) != -1) freq[c]++;
        int hmax = 0;
        for (int i = 0; i < 256; i++) {
            if (freq[i] > hmax) hmax = freq[i];
        }
        in.close();
        // 度数の最大値が 255 になるよう調整
        if (hmax >= 256) {
            for (int i = 0; i < 256; i++) {
                freq[i] = (freq[i] * 255 + hmax - 1) / hmax;
            }
        }
        freq[256] = 1;
        calcCumulativeFrequency();
        // 度数分布を出力
        BufferedOutputStream out;
        out = new BufferedOutputStream(new FileOutputStream(outName));
        for (int i = 0; i < 256; i++) out.write(freq[i]);

        // 入力ファイルの先頭に戻り度数分布に基づいて
        // レンジコーダで各バイトを符号化する
        in = new BufferedInputStream(new FileInputStream(inName));
        RangeEncoder enc = new RangeEncoder(out, cumfreq[257], RANGE, TOP, BOTTOM);
        while ((c = in.read()) != -1) {
            enc.encode(cumfreq[c], freq[c]);
        }
        enc.encode(cumfreq[256], freq[256]); // EOF
        enc.doFinal();
    }

    public void decode(String inName, String outName) throws IOException {
        // 度数分布を読み込む
        BufferedInputStream in;
        in = new BufferedInputStream(new FileInputStream(inName));
        for (int i = 0; i < 256; i++) {
            freq[i] = in.read();
            if (freq[i] == -1) throw new IOException("no input data");
        }
        freq[256] = 1;
        calcCumulativeFrequency();

        RangeDecoder dec = new RangeDecoder(in, cumfreq[257], RANGE, TOP, BOTTOM);
        BufferedOutputStream out;
        out = new BufferedOutputStream(new FileOutputStream(outName));
        // レンジコーダで復号する
        for (;;) {
            int f = dec.getfreq();
            // 2分探索で cumfreq[i] <= f < cumfreq[i+1] となる
            // i を求める
            int i = 0, j = 256;
            while (i < j) {
                int k = (i + j) / 2;
                if (cumfreq[k+1] <= f) {
                    i = k + 1;
                } else {
                    j = k;
                }
            }
            if (i == 256) break; // EOF
            out.write(i);
            dec.decode(cumfreq[i], freq[i]);
        }
        out.flush();
    }
}

public class RangeCoderTest {
    public static void main(String args[]) throws IOException {
        RangeCoder rangeCoder = new RangeCoder();
        if (args.length == 3 && args[0].equals("-e")) {
            rangeCoder.encode(args[1], args[2]);
        } else
        if (args.length == 3 && args[0].equals("-d")) {
            rangeCoder.decode(args[1], args[2]);
        } else {
            System.err.println("to encode: java RangeTest -e input-file output-file");
            System.err.println("to decode: java RangeTest -d input-file output-file");
        }
    }
}