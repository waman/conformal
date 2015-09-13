package java_algorithm.datatype.date;

/**
 *  曜日
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DayOfWeek {

    /** 曜日を返す (0=日, 1=月, ..., 6=土) */
    public static int dayOfWeek(int year, int month, int day) {
        if (month < 3) {  year--;  month += 12;  }
        return (year + year / 4 - year / 100 + year / 400
                + (13 * month + 8) / 5 + day) % 7;
    }

    public static void main(String[] args) throws IOException {
        String name[] = { "日", "月", "火", "水", "木", "金", "土" };
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("西暦 ? ");
        int year = Integer.parseInt(in.readLine());
        System.out.print("月 ? ");
        int month = Integer.parseInt(in.readLine());
        System.out.print("日 ? ");
        int day = Integer.parseInt(in.readLine());
        int dayofweek = dayOfWeek(year, month, day);
        System.out.println("この日は" + name[dayofweek] + "曜日です");
    }
}