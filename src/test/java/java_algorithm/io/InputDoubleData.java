package java_algorithm.io;

/**
* InputDoubleData.java
* キーボード（標準入力）から double 型のデータを配列に読み込む。
* データの個数に制限はない。
* ラッパー・クラスの使用例
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
* キーボード（標準入力）から複数個の double 型のデータを配列に読み込みます。
* データの個数に制限はありません。ラッパー・クラスの使用例です。
* @see java_algorithm.statistics.MeanSD1
*/

public class InputDoubleData {

    private List<Double> keyBuffer;
    private boolean showNumber;      // データ番号を表示するか

    public InputDoubleData() {
        this(false);
    }

    /**
    *@param showNumber データ番号を表示するかどうかを指定します。
    */
    public InputDoubleData(boolean showNumber) {
        this.showNumber = showNumber;
    }

    private void read() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int dataNum = 1;

        this.keyBuffer = new LinkedList<>();   // データの個数に制限はない。
        while (true) {
            if (this.showNumber)System.err.print( (dataNum++) + " : ");

            String s;
            if ( (s = in.readLine()) == null ) break;

            try {
                double t = Double.parseDouble(s);
                this.keyBuffer.add(t);
            } catch(NumberFormatException e) {
                break;   // 数字以外 <enter>のみなど。
            }
        }
    }

    /**
    * キーボードから enter キーで区切られた複数個の double 型のデータを配列に読み込みます。
    * 空のまま enter キーを押すか，数値に使われない文字を読み込むとデータの終わりと見なします。
    * ファイルからリダイレクト機能を使って読み込ませることもできます。
    * @return 入力されたデータを返します。
    */
    public double[] getValue() throws IOException {
        if (this.keyBuffer == null) read();
        double[] data = new double[this.keyBuffer.size()];

        for (int i = 0, n = this.keyBuffer.size(); i < n; i++) {
        // Vector(Object[]) → double[] への変換
            data[i] = this.keyBuffer.get(i);
//          Double dv = (Double)keyBuffer.get(i);
//          data[i] = dv.doubleValue();
        }
/*
// Iterator インタフェースを使う方法
        Iterator itr = keyBuffer.iterator();
        for (int i = 0; itr.hasNext(); i++) {
            data[i] = ((Double)itr.next()).doubleValue();
        }
*/
        keyBuffer = null;   // 次回の呼び出しに備えて keyBuffer を空にする。
        return data;
    }

    /**
    * テスト用プログラムです。
    */
    public static void main(String[] args) throws IOException {
        System.err.println("データを入力してください");
        InputDoubleData data = new InputDoubleData();
        double[] x = data.getValue();

        for (int i = 0; i < x.length; i++) System.out.println("x[" + i + "] = " + x[i]);
        // ２回目
        System.err.println("データを入力してください");
        x = data.getValue();
        for (int i = 0; i < x.length; i++) System.out.println("x[" + i + "] = " + x[i]);
    }

}