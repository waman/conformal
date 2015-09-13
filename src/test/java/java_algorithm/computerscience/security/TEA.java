package java_algorithm.computerscience.security;

/*
The Tiny Encryption Algorithm (TEA)
originally by David Wheeler and Roger Needham
(http://vader.brad.ac.uk/tea/tea.shtml)

仕様：ファイルサイズが８の倍数でないファイルをエンコードすると、
       結果のファイルのファイルサイズが８バイト増加します。
*/

import java.io.IOException;

public class TEA {

    private int[] key;

    public TEA(int k0, int k1, int k2, int k3) {
        key = new int[] {k0, k1, k2, k3};
    }

    public TEA(int[] k) {
        this(k[0], k[1], k[2], k[3]);
    }

    public void encipher(int[] v) {
        int sum = 0;
        for (int i = 0; i < 32; i++) {
            v[0] += (((v[1] << 4) ^ (v[1] >>> 5)) + v[1]) ^ (sum + key[sum & 3]);
            sum += 0x9E3779B9;
            v[1] += (((v[0] << 4) ^ (v[0] >>> 5)) + v[0]) ^ (sum + key[(sum >>> 11) & 3]);
        }
    }

    public void decipher(int[] v) {
        int sum = 0xC6EF3720;
        for (int i = 0; i < 32; i++) {
            v[1] -= (((v[0] << 4) ^ (v[0] >>> 5)) + v[0]) ^ (sum + key[(sum >>> 11) & 3]);
            sum -= 0x9E3779B9;
            v[0] -= (((v[1] << 4) ^ (v[1] >>> 5)) + v[1]) ^ (sum + key[sum & 3]);
        }
    }

    public static void toIntArray(byte[] buf, int[] vals) {
        for (int j = 0; j < 2; j++) {
            vals[j] = 0;
            for (int i = 0; i < 4; i++)
                vals[j] = (vals[j] << 8) + (0xFF&(int)buf[4*j+i]);
        }
    }

    public static void outputVals(int[] vals, int size) throws IOException {
        for (int j = 0; j < size; j++)
            System.out.write( (int)(byte)(vals[j/4] >>> ((3 - (j%4))*8)) );
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        char op;
        if (args.length != 5 ||
            ((op = args[0].charAt(0)) != 'e' && op != 'd')) {
            System.err.println("To encipher: java TEA e key0 key1 key2 key3 <in >out");
            System.err.println("To decipher: java TEA d key0 key1 key2 key3 <in >out");
            return;
        }
        int[] keys = new int[4];
        for (int i = 0; i < 4; i++)
            keys[i] = Long.valueOf(args[i + 1]).intValue();
        TEA tea = new TEA(keys);
        byte[] buf = new byte[8];
        int[] vals = new int[2];
        int inbytes;
        if (op == 'e') {
            while ((inbytes = System.in.read(buf,0,8)) != -1) {
                if (inbytes < 8)
                    for (int i = inbytes; i < 8; i++) buf[i] = 0;
                toIntArray(buf,vals);
                tea.encipher(vals);
                outputVals(vals,8);
                if (inbytes < 8) {
                    outputVals(new int[] {0,0},inbytes);  //  dummy
                    break;
                }
            }
        } else {
            int previnbytes = 0;
            while ((inbytes = System.in.read(buf,0,8)) != -1) {
                if (inbytes < 8) {
                    outputVals(vals,inbytes);
                    break;
                } else {
                    outputVals(vals,previnbytes);
                    toIntArray(buf,vals);
                    tea.decipher(vals);
                }
                previnbytes = inbytes;
            }
        }
        System.in.close();
    }
}