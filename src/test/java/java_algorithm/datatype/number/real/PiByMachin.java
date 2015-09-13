package java_algorithm.datatype.number.real;

/***********************************************************
    Pi.java -- 円周率(Machinの公式)
***********************************************************/

class PiByMachin {

    static double pi() {
        int k;
        double p, t, last;

        p = 0;
        k = 1;
        t = 16.0 / 5.0;
        do {
            last = p;
            p += t / k;
            t /= -5.0 * 5.0;
            k += 2;
        } while (p != last);

        k = 1;
        t = 4.0 / 239.0;
        do {
            last = p;
            p -= t / k;
            t /= -239.0 * 239.0;
            k += 2;
        } while (p != last);
        return p;
    }

    public static void main(String[] args) {
        System.out.println("pi=" +  PiByMachin.pi());
    }
}