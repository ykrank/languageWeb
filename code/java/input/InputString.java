import java.io.*;
public class InputString{
public static void main(String[] args){ 
		String s="";
		System.out.println("Enter a String please:"); 
		try{
			InputStreamReader ln=new InputStreamReader(System.in); 
			BufferedReader in=new BufferedReader(ln);
			s=in.readLine(); 
		}
		catch(IOException e){
			System.out.println(e);
		}
		System.out.println("You've entered a String: "+s);
	}
}