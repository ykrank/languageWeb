public class JGuess{
	public static void main(String[] args){ 
		int k;
		outer: 
		for(k=3;k<=100;k++){
		int n=k; 
		do{
			if(n%2==0) 
				n=n/2;
			else 
				n=n*3+1;
			if(n==0){ 
				System.out.print(k+"不满足角谷猜想"); 
				break outer;
			}
		}
		while (n!=1); 
		}
		if(k==101)
			System.out.print("数 3~100 之间的数满足角谷猜想");
	} 
}