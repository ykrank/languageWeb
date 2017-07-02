import java.io.*; 
class SwitchTest{
	public static void main(String[] args)throws IOException{
		int numberOfDigits=0,numberOfSpaces=0,numberOfOthers=0; 
		char c;
		while((c=(char)System.in.read())!='\n'){
			switch(c){ 
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9': numberOfDigits++;break; //数字
				case ' ': numberOfSpaces++;break; //空格
				default: numberOfOthers++;break;  //othrer
				} 
		}

		System.out.println("number Of Digits="+numberOfDigits+""); 
		System.out.println("number Of spaces="+numberOfSpaces+""); 
		System.out.println("number Of others="+numberOfOthers+"");
	} 
}