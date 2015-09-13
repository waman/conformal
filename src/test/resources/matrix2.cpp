#include <iostream>
#include <valarray>
using namespace std;

class Matrix {
    size_t r, c;                // �s���C��
    valarray<double>* v;        // �f�[�^
    void init(size_t x, size_t y) {
        r = x;  c = y;
        v = new valarray<double>(r * c);
    }
public:
    Matrix() { init(0, 0); }
    Matrix(size_t x, size_t y) { init(x, y); }
    Matrix(const Matrix& a) {   // �R�s�[�R���X�g���N�^
        init(a.r, a.c);  *v = *(a.v);
    }
    Matrix& operator=(const Matrix& a) { // ���
        if (this != &a) {
            delete v;  init(a.r, a.c);  *v = *(a.v);
        }
        return *this;
    }
    ~Matrix() { delete v; }     // �f�X�g���N�^
    double& operator()(size_t i, size_t j) { // �Y��
        return (*v)[i * c + j]; // �s����
        // �񂲂ƂȂ��� (*v)[j * r + i] �ƂȂ�
    }
    const double& operator()(size_t i, size_t j) const {
        return (*v)[i * c + j]; // ����
    }
    size_t nrow() const { return r; } // �s��
    size_t ncol() const { return c; } // ��
};

void matprint(const Matrix& a)  // �s����o��
{
    for (size_t i = 0; i < a.nrow(); i++) {
        for (size_t j = 0; j < a.ncol() - 1; j++)
            cout << a(i, j) << '\t';
        cout << a(i, a.ncol() - 1) << '\n';
    }
}

int main()
{
    Matrix a(3, 4);             // $3 \times 4$ �s��
    for (size_t i = 0; i < a.nrow(); i++)
        for (size_t j = 0; j < a.ncol(); j++)
            a(i, j) = i * 10 + j + 11;
    matprint(a);                // �o��
    Matrix b;                   // ���O�����m��
    b = a;                      // ���
    matprint(b);                // �o��
}
