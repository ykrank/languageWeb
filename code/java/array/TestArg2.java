class TestArg2{
	static void f(int[] a){
		for (int x:a){ 
			System.out.print(x+" ");
		} 
	}
	public static void main(String[] args){ 
		f(new int[]{1,2,3,4,5,6,7});
	} 
}