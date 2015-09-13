// matrix1.cpp -- C++ Ç≈ÇÃçsóÒÇÃó· 1
// $Revision: 1.3 $, $Date: 2003/04/29 00:04:17 $

#include <iostream>
using namespace std;

const int N = 3, M = 4;

void matprint(double a[N][M])
{
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M-1; j++) cout << a[i][j] << '\t';
        cout << a[i][M-1] << '\n';
    }
}

int main()
{
    double a[N][M] = {{11,12,13,14},{21,22,23,24},{31,32,33,34}};
    matprint(a);
}
