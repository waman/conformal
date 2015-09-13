package java_algorithm.computerscience.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// MessageDigest の MD5 を使うサンプル
// java MD5 文字列 : 文字列の MD5 を出力する
// java MD5 -f ファイル名 : ファイルの MD5 を出力する

class MD5 {

    // byte[] を 16進数の文字列に変換する
    static String convertByteToString(byte[] src) {
        String msg = "";
        for (byte b : src) {
            int num = b & 0xFF;
            if (num < 0x10) msg += "0";    // 1桁の場合0を追加
            msg += Integer.toHexString(num);
        }
        return msg;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        boolean doReadFile = false;
        if (args.length == 2 && args[0].compareTo("-f") == 0) {
            doReadFile = true;
        } else if (args.length != 1) {
            System.err.println("usage: MD5 characters");
            System.err.println("usage: MD5 -f filename");
            return;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        if (doReadFile) {
            FileInputStream file = new FileInputStream(args[1]);
            byte[] data = new byte[512];
            while (true) {
                int len = file.read(data);
                if (len < 0) break;
                md5.update(data, 0, len);
            }
            file.close();
        } else {
            md5.update(args[0].getBytes());
        }
        byte digest[] = md5.digest();
        System.out.println(convertByteToString(digest));
    }
}