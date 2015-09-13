package java_algorithm.datatype.number.real;

/***********************************************************
    Pi.java -- 円周率(相加相乗平均による方法)
***********************************************************/

class PiByAGM {

    public static void main(String[] args) {
        double a, b, s, t;
        a = 1;
        b = 1 / Math.sqrt(2.0);
        s = 1;
        t = 4;
        for (int i = 0; i < 3; i++) {
            double last;
            last = a;
            a = (a + b) / 2;
            b = Math.sqrt(last * b);
            s -= t * (a - last) * (a - last);
            t *= 2;
            System.out.println("pi=" + (a + b) * (a + b) / s);
        }
    }
}