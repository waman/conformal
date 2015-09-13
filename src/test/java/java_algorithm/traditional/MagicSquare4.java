package java_algorithm.traditional;

/**
 *  魔方陣(４次)
 */
public class MagicSquare4 {

    static boolean used[];

    private static boolean begin(int x) {
        return !used[11 + x] && (used[11 + x] = true);
    }

    private static void end(int x) {
        used[11 + x] = false;
    }

    private static String fm(int x) {
        String s = " " + x + " ";
        return s.substring(s.length() - 3);
    }

    public static void main(String[] args) {
        int count = 0;
        used = new boolean[40];
        for (int x = -11; x <= 28; x++) begin(x);
        for (int x = 1; x <= 16; x++) end(x);

        for (int a = 1;     a <= 16; a++) {   begin(a);
        for (int d = a + 1; d <= 16; d++) {   begin(d);
        for (int m = d + 1; m <= 16; m++) {   begin(m);
        int p = 34 - a - d - m;  if (a < p && begin(p)) {
        for (int b = 1; b <= 16; b++)     if (begin(b)) {
        int c = 34 - a - b - d;           if (begin(c)) {
        for (int f = 1; f <= 16; f++)     if (begin(f)) {
        int k = 34 - a - f - p;           if (begin(k)) {
        for (int g = 1; g <= 16; g++)     if (begin(g)) {
        int j = 34 - d - g - m;           if (begin(j)) {
        int n = 34 - b - f - j;           if (begin(n)) {
        int o = 34 - c - g - k;           if (begin(o)) {
        for (int e = 1; e <= 16; e++)     if (begin(e)) {
        int i = 34 - a - e - m;           if (begin(i)) {
        int h = 34 - e - f - g;           if (begin(h)) {
        int l = 34 - i - j - k;           if (begin(l)) {
            System.out.println("\n解 " + (++count));
            System.out.println(fm(a) + fm(b) + fm(c) + fm(d));
            System.out.println(fm(e) + fm(f) + fm(g) + fm(h));
            System.out.println(fm(i) + fm(j) + fm(k) + fm(l));
            System.out.println(fm(m) + fm(n) + fm(o) + fm(p));
        end(l); } end(h); } end(i); } end(e); } end(o); } end(n); }
        end(j); } end(g); } end(k); } end(f); } end(c); } end(b); }
        end(p); } end(m); } end(d); } end(a); }
    }
}