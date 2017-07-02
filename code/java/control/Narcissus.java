public class Narcissus{
	public static void main(String[] args){
		int i,j,k,n=100,m=1; 
		while(n<1000){
			i=n/100; //获取最高位 
			j=(n-i*100)/10; //获取第 2 位
			k=n%10; //获取最低位 
			if(Math.pow(i,3)+Math.pow(j,3)+Math.pow(k,3)==n)
				System.out.println("找到第"+m++ +"个水仙花数:"+n); 
				n++;
		} 
	}
}