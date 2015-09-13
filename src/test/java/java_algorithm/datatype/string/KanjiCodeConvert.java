package java_algorithm.datatype.string;

/**
 *  KanjiCodeConvert.java -- 漢字コードを変換
 *
 *  @version $Revision: 1.3 $, $Date: 2003/04/01 13:22:14 $
 */

import java.io.*;

public class KanjiCodeConvert {
    private static void cat(String encode, String eol) throws IOException {
        BufferedReader in = new BufferedReader(new
            InputStreamReader(System.in, "JISAutoDetect"));
        BufferedWriter out = new BufferedWriter(new
            OutputStreamWriter(System.out, encode));
        String s;
        while ((s = in.readLine()) != null) out.write(s + eol);
        in.close();  out.close();
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            if (args[0].equals("-w")) {  cat("SJIS", "\r\n");  return;  }
            if (args[0].equals("-m")) {  cat("SJIS",   "\r");  return;  }
            if (args[0].equals("-u")) {  cat("EUC-JP", "\n");  return;  }
        }
        System.err.println("usage : java KanjiCodeConvert"
                           + " -{w|m|u} < in_file > out_file");
        System.err.println("   -w (for Windows)  SJIS,  \\r\\n");
        System.err.println("   -m (for Mac)      SJIS,    \\r");
        System.err.println("   -u (for Unix)     EUC-JP,  \\n");
        System.err.println("The encoding of your system is "
                           + System.getProperty("file.encoding") + ".");
    }
}