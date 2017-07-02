import java.io.*;
public class InputChar2{
	public static void main(String[] args){ 
		char c=' ';
		System.out.println("Enter a character please:"); 
		try{
			InputStreamReader ln=new InputStreamReader(System.in); 
			c=(char)ln.read();
		}
		catch(IOException e){
			System.out.println(e); 
		}
		System.out.println("You've entered a character:"+c);
	}
}