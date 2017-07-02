public class SwapTest{
	static void swap(int x,int y){ 
		int temp;
		temp=x;
		x=y;
		y=temp; System.out.println("x="+x+",y="+y);
	}

	public static void main(String[] args){
		int n=7,m=5;
		swap(n,m);
		System.out.println("n="+n+",m="+m); 
	}
}