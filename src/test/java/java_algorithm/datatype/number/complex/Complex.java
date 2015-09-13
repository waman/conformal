package java_algorithm.datatype.number.complex;

/**
 *  複素数クラス：
 *
 *  複素数及びそれに対する各種演算メソッドを提供します。
 *  実・虚数の内部表現及び数値演算は全て double 型を用いています。
 */
public class Complex {

    private double real, imag;

    /**
     *  実数部，虚数部を与えて複素数オブジェクトを作成
     *
     *  @param  real    実数部
     *  @param  imag    虚数部
     */
    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     *  実数部，虚数部を 0 として複素数オブジェクトを作成
     */
    public Complex() {
        this(0.0, 0.0);
    }

    /**
     *  コピーコンストラクタ
     *
     *  @param  that    コピー元の複素数オブジェクト
     */
    public Complex(Complex that) {
        if (that != null) {
            real = that.real;
            imag = that.imag;
        }
    }
    /**
     *  複素数を "<code><i>re</i>+<i>im</i>i</code>" という文字列に変換します。
     *  @return 複素数の文字列表現
     */
    @Override
    public String toString() {
        if (imag >= 0) return real + "+" + imag + "i";
        return real + "-" + Math.abs(imag) + "i";
    }

    /**
     *  複素数同士の比較をします。
     *
     *  @param  rhs 比較対象オブジェクト。
     *  @return rhs が Complex を指し，かつ実数部，虚数部共に等しい場合
     *          true を返します。
     */
    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        if (rhs instanceof Complex)    // TODO
            return (real == ((Complex)rhs).real && imag == ((Complex)rhs).imag);
        return false;
    }

    /**
     *  実数部を返します。
     *  @return Re(this)
     */
    public double re() {
        return real;
    }

    /**
     *  虚数部を返します。
     *  @return Im(this)
     */
    public double im() {
        return imag;
    }

    //  以下の２つのメソッドは static でもよかったかも？

    /**
     *  絶対値を返します。
     *  @return |this|
     */
    public double abs() {
        if (real == 0) return Math.abs(imag);
        if (imag == 0) return Math.abs(real);
        if (Math.abs(imag) > Math.abs(real)) {
            double  t = real / imag;
            return Math.abs(imag) * Math.sqrt(1 + t * t);
        } else {
            double  t = imag / real;
            return Math.abs(real) * Math.sqrt(1 + t * t);
        }
    }

    /**
     *  位相を -π < θ < π の範囲で返します。
     *  @return Arg(this)
     */
    public double arg() {
        return Math.atan2(imag,real);
    }

    /**
     *  べき乗
     *  @param  z   指数
     *  @return this<sup>z</sup> = exp(z log(this))
     */
    public Complex pow(Complex z) {
        return Complex.exp(z.multiply(Complex.log(this)));
    }

    //  四則演算
    /**
     *  値が (this + rhs) の複素数を返します。
     *  @param  rhs この複素数に加える値
     *  @return this + rhs
     */
    public Complex add(Complex rhs) {
        return new Complex(real + rhs.real, imag + rhs.imag);
    }

    /**
     *  値が (this - rhs) の複素数を返します。
     *  @param  rhs この複素数から引く値
     *  @return this - rhs
     */
    public Complex subtract(Complex rhs) {
        return new Complex(real - rhs.real, imag - rhs.imag);
    }

    /**
     *  値が (this * rhs) の複素数を返します。
     *  @param  rhs この複素数にかける値
     *  @return this * rhs
     */
    public Complex multiply(Complex rhs) {
        return new Complex(
                            real * rhs.real - imag * rhs.imag,
                            real * rhs.imag + imag * rhs.real
                        );
    }

    /**
     *  値が (this / rhs) の複素数を返します。
     *  @param  rhs この複素数を割る値
     *  @return this / rhs
     */
    public Complex divide(Complex rhs) {
        if (Math.abs(rhs.real) >= Math.abs(rhs.imag)) {
            double  w = rhs.imag / rhs.real,
                    d = rhs.real + rhs.imag * w;
            return new Complex((real + imag * w) / d, (imag - real * w) / d);
        } else {
            double  w = rhs.real / rhs.imag,
                    d = rhs.real * w + rhs.imag;
            return new Complex((real * w + imag) / d, (imag * w - real) / d);
        }
    }

    /**
     *  複素共役
     *  @return this<sup>*</sup>
     */
    public Complex conj() {
        return new Complex(real,-imag);
    }

    /**
     *  -1 倍
     *  @return -this
     */
    public Complex negate() {
        return new Complex(-real,-imag);
    }

    //  初等関数
    /**
     *  指数関数
     *  @param  z   演算の対象
     *  @return exp(z) = exp(x) * (cos y + i sin y)
     */
    public static Complex exp(Complex z) {
        double  a = Math.exp(z.real);
        return new Complex(a * Math.cos(z.imag), a * Math.sin(z.imag));
    }

    /**
     *  対数関数
     *  @param  z   演算の対象
     *  @return log(z) = log r + i θ
     */
    public static Complex log(Complex z) {
        return new Complex(
                        0.5 * Math.log(z.real * z.real + z.imag * z.imag),
                        Math.atan2(z.imag,z.real)
                    );
    }

    /**
     *  正弦関数
     *  @param  z   演算の対象
     *  @return sin(z)
     */
    public static Complex sin(Complex z) {
        double  e = Math.exp(z.imag), f = 1 / e;
        return new Complex(
                        0.5 * Math.cos(z.real) * (e - f),
                        0.5 * Math.sin(z.real) * (e + f)
                    );
    }

    /**
     *  余弦関数
     *  @param  z   演算の対象
     *  @return cos(z)
     */
    public static Complex cos(Complex z) {
        double  e = Math.exp(z.imag), f = 1 / e;
        return new Complex(
                        0.5 * Math.cos(z.real) * (f - e),
                        0.5 * Math.sin(z.real) * (f + e)
                    );
    }

    /**
     *  正接関数
     *  @param  z   演算の対象
     *  @return tan(z)
     */
    public static Complex tan(Complex z) {
        double  e = Math.exp(2 * z.imag), f = 1 / e,
                d = Math.cos(2 * z.real) + 0.5 * (e + f);
        return new Complex(
                        Math.sin(2 * z.real) / d,
                        0.5 * (e - f) / d
                    );
    }

    /**
     *  双曲正弦関数
     *  @param  z   演算の対象
     *  @return sinh(z)
     */
    public static Complex sinh(Complex z) {
        double  e = Math.exp(z.real), f = 1 / e;
        return new Complex(
                        0.5 * Math.cos(z.imag) * (e - f),
                        0.5 * Math.sin(z.imag) * (e + f)
                    );
    }

    /**
     *  双曲余弦関数
     *  @param  z   演算の対象
     *  @return cosh(z)
     */
    public static Complex cosh(Complex z) {
        double  e = Math.exp(z.real), f = 1 / e;
        return new Complex(
                        0.5 * Math.cos(z.imag) * (e + f),
                        0.5 * Math.sin(z.imag) * (e - f)
                    );
    }

    /**
     *  双曲正接関数
     *  @param  z   演算の対象
     *  @return tanh(z)
     */
    public static Complex tanh(Complex z) {
        double  e = Math.exp(2 * z.real), f = 1 / e,
                d = Math.cos(2 * z.imag) + 0.5 * (e + f);
        return new Complex(
                        0.5 * (e - f) / d,
                        Math.sin(2 * z.imag) / d
                    );
    }

    private static final double SQRT05 = 0.707106781186547524;

    /**
     *  平方根
     *  @param  z   演算の対象
     *  @return z<sup>1/2</sup>
     */
    public static Complex sqrt(Complex z) {
        double  r = z.abs(), w = Math.sqrt(r + Math.abs(z.real));
        if (z.real >= 0) {
            return new Complex(SQRT05 * w, SQRT05 * z.imag / w);
        } else {
            return new Complex(
                            SQRT05 * Math.abs(z.imag) / w,
                            (z.imag >= 0) ? SQRT05 * w : -SQRT05 * w
                        );
        }
    }

    /**
     *  唯一のエントリポイント
     *  @param  args    --
     */
    public static void main(String[] args) {
        //  算術式を受け取って結果を表示するようにしてもいいかも。
        Complex z = new Complex(1.0,1.0), w = new Complex(2.0,-3.0);
        System.out.print(
                    "z = " + z + "\n" + 
                    "w = " + w + "\n" + 
                    "z + w = " + z.add(w) + "\n" + 
                    "z - w = " + z.subtract(w) + "\n" + 
                    "z * w = " + z.multiply(w) + "\n" + 
                    "z / w = " + z.divide(w) + "\n" + 
                    "|z| = " + z.abs() + "\n" + 
                    "Arg(z) = " + z.arg() + "\n" + 
                    "z^w = " + z.pow(w) + "\n" + 
                    "z^* = " + z.conj() + "\n" + 
                    "-z = " + z.negate() + "\n" + 
                    "exp(z) = " + Complex.exp(z) + "\n" + 
                    "log(z) = " + Complex.log(z) + "\n" + 
                    "sin(z) = " + Complex.sin(z) + "\n" + 
                    "cos(z) = " + Complex.cos(z) + "\n" + 
                    "tan(z) = " + Complex.tan(z) + "\n" + 
                    "sinh(z) = " + Complex.sinh(z) + "\n" + 
                    "cosh(z) = " + Complex.cosh(z) + "\n" + 
                    "tanh(z) = " + Complex.tanh(z) + "\n" + 
                    "z^(1/2) = " + Complex.sqrt(z) + "\n"
                );
    }
}