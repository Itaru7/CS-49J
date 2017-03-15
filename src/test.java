
public class test
{
	public static void main(String args[])
	{
		String s = "public class Compiler extends test";
		String arr[] = s.split(" ");
		for(int i = 0; i < arr.length; i++)
			System.out.println(arr[i]);
	}
}
