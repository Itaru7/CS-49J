import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main 
{
	public static void main(String args[])
	{
		Compiler c = new Compiler();
		String fileName = "index.txt";
		try
		{
			//FileReader fileReader =  new FileReader(fileName);
			//BufferedReader bufferedReader = new BufferedReader(fileReader);
			Scanner in = new Scanner(Paths.get(fileName), "UTF-8");
			
			while(in.hasNext())
			{
				String line = in.nextLine();
				//c.staByLine = in.nextLine();
				c.checkLine(line);
				c.lineNum++;
			}
			in.close();
			c.checkError();
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Unable to open file " + fileName + " .");
		}
		catch (IOException ex)
		{
			System.out.println("Error reading file " + fileName + ".");
		}
		catch (Exception e)
		{
			System.out.println("Error occured.");
		}
		
		Tier2 t2 = new Tier2();
		t2.firstHint();
		t2.secondHint();
		
		Tier3 t3 = new Tier3();
		t3.firstHint(fileName);
		t3.secondHint(fileName);
		t3.thirdHint(fileName);
		t3.fourthHint(fileName);
	}
}
