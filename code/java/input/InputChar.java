import java.io.*;
public class InputChar{
public static void main(String[] args){ 
	char c=' ';
	System.out.println("Enter a character please:"); 
	try{
		c=(char)System.in.read(); 
	}
	catch(IOException e){ 
		System.out.println(e);
	}
	System.out.println("You've entered a character:"+c); 
	}
}