package java_algorithm.datatype.number;

/*
 *  Hamiltonの4元数群
 *  G = {1, i, j, k, -1, -i, -j, -k}
 *  i^2 = j^2 = k^2 = -1
 *  ij = -ji = k, jk = -kj = i, ki = -ik = j
 */

class HamiltonGroup {

    static final String[] elementTable = {"1", "i", "j", "k", "-1", "-i", "-j", "-k"};

    // 積の演算テーブル
    static final int multiplyTable[][] = {
        {0, 1, 2, 3, 4, 5, 6, 7},
        {1, 4, 3, 6, 5, 0, 7, 2},
        {2, 7, 4, 1, 6, 3, 0, 5},
        {3, 2, 5, 4, 7, 6, 1, 0},
        {4, 5, 6, 7, 0, 1, 2, 3},
        {5, 0, 7, 2, 1, 4, 3, 6},
        {6, 3, 0, 5, 2, 7, 4, 1},
        {7, 6, 1, 0, 3, 2, 5, 4}
    };

    private int element;

    // コンストラクタ(G の要素を受ける)
    HamiltonGroup(String e) {
        for (int i = 0; i < elementTable.length; i++) {
            if (elementTable[i].equals(e)) {
                element = i;
                return;
            }
        }
        throw new Error("illegal element");
    }

    @Override
    public String toString() {
        return elementTable[element];
    }

    // lhs = lhs * rhs
    public HamiltonGroup multiply(HamiltonGroup rhs) {
        this.element = multiplyTable[element][rhs.element];
        return this;
    }

    public static void main(String[] args) {
        HamiltonGroup a = new HamiltonGroup("i");
        HamiltonGroup b = new HamiltonGroup("j");
        HamiltonGroup c = new HamiltonGroup("k");
        for (int i = 0; i < 4; i++) {
            System.out.println(a + "*" + b + "=" + a.multiply(b));
        }
        for (int i = 0; i < 4; i++) {
            System.out.println(a + "*" + c + "=" + a.multiply(c));
        }
    }
}