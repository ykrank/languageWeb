public class Sum{
	static int sum(int a[]){ int s=0;
	for(int i=0;i<a.length;i++){ 
		s=s+a[i];
	} 
	return s;
}
	public static void main(String[] args){
	int a[]={1,2,3,4,5,6,7,8,9,10};
	System.out.println("数组 a 的总和是:"+sum(a)); 
	}
	}