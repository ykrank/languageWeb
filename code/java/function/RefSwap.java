public class RefSwap{ 
	int value=0;
	static void swap(RefSwap x,RefSwap y){ 
		int temp;
		temp=x.value;
		x.value=y.value;
		y.value=temp;
		System.out.println("in calling method:"); 
		System.out.println("x.value="+x.value+",y.value="+y.value);
	}

	public static void main(String[] args){
		RefSwap n=new RefSwap();
		RefSwap m=new RefSwap();
		n.value=4;
		m.value=5;
		System.out.println("before calling:"); 
		System.out.println("n.value="+n.value+",m.value="+m.value);
		swap(n,m); 
		System.out.println("after called:");
		System.out.println("n.value="+n.value+",m.value="+m.value); 
	}
}