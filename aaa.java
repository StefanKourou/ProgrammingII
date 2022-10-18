public class aaa{
	public static void main (String [] args) {
		int b= 7;
		int c= 3;
		int d= 2;
		int result=0;

		int temp=c*d;
		for (int i=0; i<11;i++){
			result=(result-b*i)+c*d;
		}
		System.out.print (result);
	}
}