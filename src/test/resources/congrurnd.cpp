// 線形合同法乱数
// $Revision: 1.3 $, $Date: 2003/04/29 00:04:17 $

#include <iostream>
using namespace std;

class CongruRnd {
    unsigned seed;              // 32ビット
public:
    CongruRnd(unsigned x) { seed = x; } // コンストラクタ
    unsigned irnd() {           // 32ビット乱数
        return seed = seed * 1566083941 + 1;
    }
    double rnd() {              // 0以上1未満の乱数
        return (1.0 / (0xffffffff + 1.0)) * irnd();
    }
};

// テスト用ドライバ
int main()
{
    CongruRnd r(12345);
    for (int i = 0; i < 10; i++) cout << r.rnd() << '\n';
}
