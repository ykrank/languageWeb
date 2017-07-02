import java.io.*;
public class Max{
	public static void main(String[] args){
		int a,b,c,max;
		String s;
		try{
			System.out.print("输入第一个整数：");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			s =br.readLine();
			a = Integer.parseInt(s);

			System.out.print("输入第二个整数：");
			s =br.readLine();
			b =Integer.parseInt(s);

			System.out.print("输入第二个整数：");
			s =br.readLine();
			c =Integer.parseInt(s);

			max =a ;
			if (b > max) max =b;
			if (c > max) max =c;
			System.out.println("三个数中最大的数是："+max);
		}
		catch(IOException e){

		}
	}

}