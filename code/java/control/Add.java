public class Add{
	public static void main(String[] args){
		long sum=0;
		int k=1; 
		do{
		sum=sum+k;
		k++; 
		}
		while (k<=100);
		System.out.println("1+2+3+...+100="+sum); 
	}
}