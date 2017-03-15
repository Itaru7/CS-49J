import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * If everything from the previous tier works, the software will recognize multiple files in input.
 *  Each file will define a different class. Plus, the class now can contain an Object type different
 *   from String. This object can be an instance of the classes defined in the other files. 
 */



public class Tier2 
{
	Compiler file1 = new Compiler();
	Compiler file2 = new Compiler();
	/*
	 * Hint 1:
	 * To recognize an Object definition as legit, the software will check for the correct import statement. 
	 * If the import is present, and the class exists, there will be no error. 
	 * Example, if you have a Class file with the name "Heroquest" that belongs to the package "tablegame",
	 * it will check in the current file if an import to "tablegame.Heroquest" is present (or "tablegame.*" )
	 *  and if this file exists without errors. 
	 */
	String importingClass = "";
	boolean check = false;
	
	public void firstHint()
	{
		
		file1.readFile("index.txt");
		file2.readFile("index2.txt");
	
		if(file1.packName.equals(file2.packName))
		{
			for(int i = 0; i < file1.listOfImport.size(); i++)
			{
				if((file1.listOfImport.get(i)).equals("import " + file1.packName + "." + file2.CLASS_NAME + ";"))
				{
					check = true;
					importingClass = file1.CLASS_NAME;
				}
			}
			
			for(int i = 0; i < file2.listOfImport.size(); i++)
			{
				if((file2.listOfImport.get(i)).equals("import " + file2.packName + "." + file1.CLASS_NAME + ";"))
				{
					check = true;
					importingClass = file2.CLASS_NAME;
				}
			}
			
			if(check)
				System.out.println("The class is successfuly imported.");
			else
				System.out.println("The class is NOT imported even though under the same package");
		}
		else
			System.out.println("The files are not under the same package.");
	}
	
	/*
	 * Hint 2:
	 * Remember that, if you want to declare an instance of a Class, 
	 * a Constructor is necessary in that Class. Otherwise, 
	 * a default constructor will be generated automatically.
	 * 
	 * Hint 3:
	 * If the code is relying on a default constructor, 
	 * an instance of the class can be created only if the constructor 
	 * is used without specifying any parameter.
	 */
	public void secondHint()
	{
		if(check)
		{
			file1 = new Compiler();
			file2 = new Compiler();
			file1.readFile("index.txt");
			file2.readFile("index2.txt");
	
			if(importingClass.equals(file2.CLASS_NAME))
			{
				try
				{
					Scanner in = new Scanner(Paths.get("index.txt"), "UTF-8");
					Pattern constRegex = Pattern.compile("\\s*" + file2.CLASS_NAME + "\\s+\\w+\\s+=\\s+new\\s+" + file2.CLASS_NAME + "\\((\\w*\\s*\\,*)*\\);");
					file1.lineNum = 1;
					while(in.hasNext())
					{
						String line = in.nextLine();
						Matcher cR = constRegex.matcher(line);
						if(cR.find())
						{
							Pattern correctRegex = Pattern.compile("\\s*" + file2.CLASS_NAME + "\\s+\\w+\\s+=\\s+new\\s+" + file2.CLASS_NAME + "\\(\\s*\\);");
							Matcher correctReg = correctRegex.matcher(line);
							if(!correctReg.find())
								file1.printError();
						}
						file1.lineNum++;
					}
					in.close();
					file1.checkError();
				}
				catch (FileNotFoundException ex)
				{
					System.out.println("Unable to open file " + "index.txt" + " .");
				}
				catch (IOException ex)
				{
					System.out.println("Error reading file " + "index.txt" + ".");
				}
				catch (Exception e)
				{
					System.out.println("Error occured.");
				}
			}
			else
			{
				try
				{
					Scanner in = new Scanner(Paths.get("index2.txt"), "UTF-8");
					Pattern constRegex = Pattern.compile("\\s*" + file1.CLASS_NAME + "\\s+\\w+\\s+=\\s+new\\s+" + file1.CLASS_NAME + "\\((\\w*\\s*\\,*)*\\);");
					file2.lineNum = 1;
					while(in.hasNext())
					{
						String line = in.nextLine();
						Matcher cR = constRegex.matcher(line);
						if(cR.find())
						{
							Pattern correctRegex = Pattern.compile("\\s*" + file1.CLASS_NAME + "\\s+\\w+\\s+=\\s+new\\s+" + file1.CLASS_NAME + "\\(\\s*\\);");
							Matcher correctReg = correctRegex.matcher(line);
							if(!correctReg.find())
								file2.printError();
						}
						file2.lineNum++;
					}
					in.close();
					file2.checkError();
				}
				catch (FileNotFoundException ex)
				{
					System.out.println("Unable to open file " + "index2.txt" + " .");
				}
				catch (IOException ex)
				{
					System.out.println("Error reading file " + "index2.txt" + ".");
				}
				catch (Exception e)
				{
					System.out.println("Error occured.");
				}
			}
		}
	}
}
