public class Factorial{
	static int fac(int n){ 
		if(n==1)
			return 1;
		else
			return n*fac(n-1);
	}

	public static void main(String[] args){
		double s=1;
		for (int k=2;k<=10;k++ ){
			s=s+1.0/fac(k); 
		}
		System.out.println("1+1/2!+1/3!+...+1/10!="+s); 
	}
}