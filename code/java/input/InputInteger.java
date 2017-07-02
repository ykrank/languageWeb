
import java.io.*;
public class InputInteger{
public static void main(String[] args){ 
		int a=0;
		System.out.println("Enter a Integer please:"); 
		try{
			InputStreamReader ln=new InputStreamReader(System.in); 
			BufferedReader in=new BufferedReader(ln);
			String s=in.readLine(); 
			a=Integer.parseInt(s);
			}
		catch(IOException e){
			System.out.println(e);
		}
		System.out.println("You've entered a Integer: "+a); 
	}
}