package java_algorithm.computerscience.security;

import java.io.*;
import java.util.Random;

public class Crypt {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("使用法: java Crypt infile outfile key");
            return;
        }
        try {
            InputStream in = new FileInputStream(args[0]);
            OutputStream out = new FileOutputStream(args[1]);
            Random rand = new Random(Long.parseLong(args[2]));
            int c;
            while ((c = in.read()) != -1)     
                out.write(c ^ rand.nextInt(256));  // 乱数との排他的論理和
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません: " + e);
        } catch (IOException e) {
            System.err.println("入出力エラー: " + e);
        } catch (NumberFormatException e) {
            System.err.println("キーが整数ではありません: " + e);
        }
    }
}