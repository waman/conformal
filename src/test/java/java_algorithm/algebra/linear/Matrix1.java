package java_algorithm.algebra.linear;

// Matrix1.java -- Java での行列の例 1

class Matrix1 {
    static void matprint(double[][] x) {
        for(double[] xi : x){
            for(double xij : xi){
                System.out.print(xij + "\t");
            }
            System.out.println(xi[xi.length-1]);
        }
    }
    public static void main(String[] args) {
        double[][] a = new double[2][3];
        a[1][2] = 4;
        matprint(a);
        double[][] b = {{11,12,13,14}, {21,22,23,24}, {31,32,33,34}};
        matprint(b);
    }
}