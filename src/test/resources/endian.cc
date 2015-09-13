#include <iostream>
using namespace std;
int main()
{
    int i = 1;
    if (*reinterpret_cast<char*>(&i))
        cout << "little-endian\n";
    else if (*(reinterpret_cast<char*>(&i) + (sizeof(int) - 1)))
        cout << "big-endian\n";
    else
        cout << "•s–¾\n";
}
