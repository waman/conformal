#include <iostream>
#include <valarray>
using namespace std;

class Matrix {
    size_t r, c;                // 行数，列数
    valarray<double>* v;        // データ
    void init(size_t x, size_t y) {
        r = x;  c = y;
        v = new valarray<double>(r * c);
    }
public:
    Matrix() { init(0, 0); }
    Matrix(size_t x, size_t y) { init(x, y); }
    Matrix(const Matrix& a) {   // コピーコンストラクタ
        init(a.r, a.c);  *v = *(a.v);
    }
    Matrix& operator=(const Matrix& a) { // 代入
        if (this != &a) {
            delete v;  init(a.r, a.c);  *v = *(a.v);
        }
        return *this;
    }
    ~Matrix() { delete v; }     // デストラクタ
    double& operator()(size_t i, size_t j) { // 添字
        return (*v)[i * c + j]; // 行ごと
        // 列ごとなら上は (*v)[j * r + i] となる
    }
    const double& operator()(size_t i, size_t j) const {
        return (*v)[i * c + j]; // 同上
    }
    size_t nrow() const { return r; } // 行数
    size_t ncol() const { return c; } // 列数
};

void matprint(const Matrix& a)  // 行列を出力
{
    for (size_t i = 0; i < a.nrow(); i++) {
        for (size_t j = 0; j < a.ncol() - 1; j++)
            cout << a(i, j) << '\t';
        cout << a(i, a.ncol() - 1) << '\n';
    }
}

int main()
{
    Matrix a(3, 4);             // $3 \times 4$ 行列
    for (size_t i = 0; i < a.nrow(); i++)
        for (size_t j = 0; j < a.ncol(); j++)
            a(i, j) = i * 10 + j + 11;
    matprint(a);                // 出力
    Matrix b;                   // 名前だけ確保
    b = a;                      // 代入
    matprint(b);                // 出力
}
