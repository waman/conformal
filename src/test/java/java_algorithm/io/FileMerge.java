package java_algorithm.io;

/**
 *  マージ
 */

import java.io.*;

public class FileMerge {
    public static void merge(BufferedReader in1, BufferedReader in2, BufferedWriter out)
            throws IOException {
        String s1 = in1.readLine();
        String s2 = in2.readLine();
        for ( ; ; )
            if (s1 != null && (s2 == null || s1.compareTo(s2) <= 0)) {
                out.write(s1);  out.newLine();  s1 = in1.readLine();
            } else if (s2 != null) {
                out.write(s2);  out.newLine();  s2 = in2.readLine();
            } else break;
        out.flush();
    }

    public static void merge(String inFile1, String inFile2,
                             String outFile) throws IOException {
        BufferedReader in1 = new BufferedReader(new FileReader(inFile1));
        BufferedReader in2 = new BufferedReader(new FileReader(inFile2));
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        merge(in1, in2, out);
        in1.close();  in2.close();  out.close();
    }

    public static void main(String[] args) throws IOException {
        final String file1 = "FileMerge.test1";
        final String file2 = "FileMerge.test2";
        final String file3 = "FileMerge.test3";
        final int n1 = (int)(Math.random() * 20);
        final int n2 = (int)(Math.random() * 20);

        System.out.print("file1:");
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file1));
        for (int i = 0, x = 100; i < n1; i++) {
            x += (int)(Math.random() * 10);
            out1.write(String.valueOf(x));  out1.newLine();
            System.out.print(" " + x);
        }
        out1.close();  System.out.println();

        System.out.print("file2:");
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
        for (int i = 0, x = 100; i < n2; i++) {
            x += (int)(Math.random() * 10);
            out2.write(String.valueOf(x));  out2.newLine();
            System.out.print(" " + x);
        }
        out2.close();  System.out.println();

        FileMerge.merge(file1, file2, file3);

        System.out.print("file3:");
        BufferedReader in3 = new BufferedReader(new FileReader(file3));
        String s;
        while ((s = in3.readLine()) != null) System.out.print(" " + s);
        in3.close();  System.out.println();
    }
}