public class text2 {
    public static void main(String[] args) {
        int a1, a2, a3;
        if (args.length < 2) {
            System.out.println("运行本程序应该提供两个命令行参数");
            System.exit(0);
        }
        a1 = Integer.parseInt(args[0]);
        a2 = Integer.parseInt(args[1]);
        a3 = a1 * a2;
        System.out.println(a1 + "与" + a2 + "相乘的积为:" + a3);
    }
}