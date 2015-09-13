package java_algorithm.datatype.number.rational;

/*
* RepeatDecimals.java -- 小数の循環節
*/
import java.io.*;
/**
* 小数の循環節を表示します。
*/

public class RepeatDecimals {
    /**
    * 入力された正の分数の循環節を表示します。<br>
    * <strong>&quot;分子 m = &quot;</strong>と聞いてきますから正の
    *整数を入力してください。<br>次に，<strong>&quot;分母 n = &quot;</strong>と聞いてきますから
    *正の整数を入力してください。n があまりに大きいとメモリ不足になるかも知れません。<br>
    *入力された分数を小数に直します。
    *循環節は &quot;<strong>{ }</strong>&quot; で囲んで出力されます。<br><br>
    *<strong>入力例</strong>(pi の近似値) m = <u>355</u>，n = <u>113</u><br>
    *<strong>出力</strong><br>
    *<code>355/113 =<br>
    *3.{14159292035398230088495575221238938053097345132743362831858407079646017699115<br>
    *04424778761061946902654867256637168}<br>
    *</code>循環節の長さ = 112
    */
    public static void main(String[] args) throws IOException {
        final int BASE = 10;  // 何進法か
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("分子 m = ");
        int m = Integer.parseInt(input.readLine());
        System.out.print("分母 n = ");
        int n = Integer.parseInt(input.readLine());
        if (n <= 0 || m <= 0) {
            System.err.println("正の整数を入力してください");
            System.exit(-1);
        }
        System.out.println(m + "/" + n + " =");
        int p[] = new int[n], k = 0, ip = m / n; // ip は整数部
        StringBuilder dp = new StringBuilder("."); // dp は小数部

        m %= n;
        do {
            p[m] = ++k;   // k は異なる m の出現回数
            m *= BASE;  dp.append(m / n);  m %= n;
        } while (p[m] == 0);  // 同じ m が現れたら停止
        System.out.print(ip + dp.substring(0, p[m]));    // p[m] は最初の m の出現位置
        if (m != 0) { // 余りが 0 でなければ循環小数
            System.out.println("{" + dp.substring(p[m]) + "}");
            // dp.substring(p[m]).length() = k - p[m] + 1;
            System.out.println("循環節の長さ = " + dp.substring(p[m]).length());
        }
    }
}