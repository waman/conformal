package java_algorithm.datatype.date;

/**
 *  ユリウス日
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * 1582/10/ 4 以前をユリウス暦（アウグストゥスの改暦による）
 * 1582/10/15 以降をグレゴリオ暦で計算する。
 * 紀元4年より前は歴史とは一致しない。
 * 紀元前100年の場合は-99年として扱う。
 */
public class JulianDay {

    public int year, month, day, hour, minute;
    public double second;

    public JulianDay(int  jd) {
        toDate(jd);
    }

    public JulianDay(double jd) {
        toDate(jd);
    }

    /** 暦日からユリウス日に変換 */
    public static int toNumber(int year, int month, int day) {
        if (month <= 2) { month += 12;  year--; }
        if ((year * 12 + month) * 31 + day >=
            (1582 * 12 + 10   ) * 31 + 15)  // 1582/10/15 以降は
            day += 2 - year / 100 + year / 400;  // グレゴリオ暦
        return (int)Math.floor(365.25 * (year + 4716)) +
            (int)(30.6001 * (month + 1)) + day - 1524;
    }

    /** 暦日からユリウス日に変換 */
    public static double toNumber(
            int year, int month, int day, int hour, int minute, double second) {
        return toNumber(year, month, day) - 0.5 +
            (hour + (minute + second / 60.0) / 60) / 24;
    }

    /** ユリウス日から暦日に変換 */
    public JulianDay toDate(int jd) {
        if (jd >= 2299161) {  // 1582/10/15 以降はグレゴリオ暦
            int t = (int)((jd - 1867216.25) / 365.2425);
            jd += 1 + t / 100 - t / 400;
        }
        jd += 1524;
        int y = (int)Math.floor((jd - 122.1) / 365.25);
        jd -= (int)Math.floor(365.25 * y);
        int m = (int)(jd / 30.6001);
        jd -= (int)(30.6001 * m);
        day = jd;  month = m -1;  year = y - 4716;
        if (month > 12) { month -= 12;  year++; }
        return this;
    }

    /** ユリウス日から暦日に変換 */
    public JulianDay toDate(double jd) {
        jd += 0.5;  int i = (int)Math.floor(jd);  toDate(i);
        jd = (jd - i)      * 24;  hour   = (int)jd;
        jd = (jd - hour)   * 60;  minute = (int)jd;
        jd = (jd - minute) * 60;  second = jd;
        return this;
    }

    static final DecimalFormat DF1 = new DecimalFormat(" 0000;-");
    static final DecimalFormat DF2 = new DecimalFormat("00");
    static final DecimalFormat DF3 = new DecimalFormat("00.000");

    @Override
    public String toString() {
        return DF1.format(year) + "/" + DF2.format(month) + "/" +
            DF2.format(day) + " " + DF2.format(hour) + ":" +
            DF2.format(minute) + ":" + DF3.format(second);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("西暦何年？");
        int year = Integer.parseInt(input.readLine());
        System.out.print("何月？");
        int month = Integer.parseInt(input.readLine());        
        System.out.print("何日？");
        int day = Integer.parseInt(input.readLine());        
        System.out.print("何日後？");
        int offset = Integer.parseInt(input.readLine());        
        JulianDay jd = new JulianDay(JulianDay.toNumber(year, month, day) + offset);
        System.out.println(jd.year + "年" + jd.month + "月" + jd.day + "日です．");
    }
}