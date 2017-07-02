class TestArray22 {
    public static void main(String[] args) {
        int k = 1;
        int[][] a = new int[3][];
        a[0] = new int[3];
        a[1] = new int[3];
        a[2] = new int[3];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = k;
                k++;
            }
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }
}