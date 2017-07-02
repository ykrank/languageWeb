class TestArg3{
	static void f(int... a){
	for (int x:a){
		System.out.print(x+" ");
	} 
	}
	public static void main(String[] args){ 
		f(1,2,3,4,5,6,7);
	} 
}