// ���`�����@����
// $Revision: 1.3 $, $Date: 2003/04/29 00:04:17 $

#include <iostream>
using namespace std;

class CongruRnd {
    unsigned seed;              // 32�r�b�g
public:
    CongruRnd(unsigned x) { seed = x; } // �R���X�g���N�^
    unsigned irnd() {           // 32�r�b�g����
        return seed = seed * 1566083941 + 1;
    }
    double rnd() {              // 0�ȏ�1�����̗���
        return (1.0 / (0xffffffff + 1.0)) * irnd();
    }
};

// �e�X�g�p�h���C�o
int main()
{
    CongruRnd r(12345);
    for (int i = 0; i < 10; i++) cout << r.rnd() << '\n';
}
