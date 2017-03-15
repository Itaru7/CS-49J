import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * The software will accept in input a text file containing a Java Class. 
 * It will recognize the presence of Instance variables, Contructors and Methods. 
 * Plus, the package name and import packages.If the file doesn't contain syntax errors, 
 * it will print in output something like "the class is OK".
 * If it contains errors, it will print the line numbers where the problems are.
 */

public class Compiler 
{
	public boolean error;
	public int lineNum;
	public String staByLine;
	public int numOfPack;
	public String CLASS_NAME;
	public String packName;
	List<String> listOfImport;
	List<String> listOfConstructor;
	List<String> listOfMethod;
	List<String> listOfivfNum;
	List<String> listOfivfBool;
	List<String> listOfivfString;
	List<String> listOfivfChar;

	Compiler()
	{
		error = false;
		lineNum = 1;
		staByLine = "";
		packName = "";
		numOfPack = 0;
		listOfImport = new ArrayList<String>();
		listOfConstructor = new ArrayList<String>();
		listOfMethod = new ArrayList<String>();
		listOfivfNum = new ArrayList<String>();
		listOfivfBool = new ArrayList<String>();
		listOfivfString = new ArrayList<String>();
		listOfivfChar = new ArrayList<String>();
		CLASS_NAME = "";
	}
	
	public void readFile(String fileName)
	{
		try
		{
			//FileReader fileReader =  new FileReader(fileName);
			//BufferedReader bufferedReader = new BufferedReader(fileReader);
			Scanner in = new Scanner(Paths.get(fileName), "UTF-8");
			while(in.hasNext())
			{
				String line = in.nextLine();
				//c.staByLine = in.nextLine();
				checkLine(line);
				lineNum++;
			}
			in.close();
			checkError();
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
	}
	
	public void checkConstructor(String line, String reg)
	{
		
	}
	
	public void checkLine(String line)
	{
		
		//trim line to omit white spaces
		String trim = "";
		for (int i = 0; i < line.length(); i++)
		{
			if(!line.substring(i,  i + 1).equals(" "))
				trim += line.substring(i, i + 1);
		}

		//get class name
		Pattern getClassName = Pattern.compile("(\\s*(public|abstract|final){1}\\s+(class){1}\\s+\\w+)");
		Matcher className = getClassName.matcher(line);
		if(className.find())
		{
			String arr[] = line.split("\\s+");
			CLASS_NAME = arr[2];
		}
	
		Pattern keywordRegex = Pattern.compile("\\s*(\\(|\\))?\\s*(\\w+|\\))"); //to consider a string with ) only
		Pattern packageRegex = Pattern.compile("\\s*package\\s+([\\w\\.]+);");
		Pattern importRegex = Pattern.compile("\\s*import\\s+([\\w\\.]+)(\\.\\*)?;");
		Pattern classRegex = Pattern.compile("\\s*(public\\s|abstract\\s|final\\s)?\\s*class\\s+(\\w+)\\s*");
		Pattern instanceVariableForNumRegex = Pattern.compile("\\s*(public\\s+|private\\s+|protected\\s+)?(int|float|double)\\s+(\\w+)(;?|(\\s*=\\s*\\-?\\s*\\d(\\.\\d\\s*([\\+\\-\\*\\/]\\s*\\-?\\d(\\.\\d\\s*)?)|\\s*([\\+\\-\\*\\/]\\s*\\-?\\d))?));");
		Pattern instanceVariableForBoolRegex = Pattern.compile("\\s*(public\\s+|private\\s+|protected\\s+)?(boolean)\\s+(\\w+)\\s*(;?|\\s*=\\s*(true|false)\\s*);");
		Pattern instanceVariableForStringRegex = Pattern.compile("\\s*(public\\s+|private\\s+|protected\\s+)?(String)\\s+(\\w+)\\s*(;?|(=\\s*\"(\\p{Punct}|\\p{Alnum})+\")\\s*);");
		Pattern instanceVariableForCharRegex = Pattern.compile("\\s*(public\\s+|private\\s+|protected\\s+)?(char)\\s+(\\w+)\\s*(;?|(=\\s*\'(\\p{Punct}|\\p{Alnum}){1}\')\\s*);");
		Pattern constructorRegex = Pattern.compile("\\s*(public\\s|private\\s|protected\\s)?" + CLASS_NAME + "\\s*\\((.)*\\)\\s*");
		Pattern parametersRegex = Pattern.compile("(int|float|char|boolean|String|double)*");//("(\\s*(int|float|char|boolean|String){1}\\s+\\b((\\w+),?)*"); // the , is actually useless
		Pattern methodRegex = Pattern.compile("\\s*(public|private|protected){1}(\\s+static)?\\s+(void|int|float|char|boolean|String|double){1}\\s+(\\w+)\\((\\s*(int|float|char|boolean|String|double)?\\s+\\w+,?)*\\)");
		//Pattern constructorParameterRegex = Pattern.compile("\\s*(public\\s|private\\s|protected\\s)?" + CLASS_NAME + 
												//	"\\s*\\((.*)\\)\\s*\\(\\s*(int|float|char|boolean|String){1}\\s+\\b((\\w+),?)*");
		Matcher pR = packageRegex.matcher(line);
		Matcher iR = importRegex.matcher(line);
		Matcher conR = constructorRegex.matcher(line);
		Matcher mR = methodRegex.matcher(line);
		Matcher ivfrR = instanceVariableForNumRegex.matcher(line);
		Matcher ivfBR = instanceVariableForBoolRegex.matcher(line);
		Matcher ivfSR = instanceVariableForStringRegex.matcher(line);
		Matcher ivfCR = instanceVariableForCharRegex.matcher(line);


		/*
		 * A class can belong to one package only and must be the 
		 * first thing specified in a file (remember that is not mandatory).
		 */
		if(pR.find()) 
			if(numOfPack < 1)
			{
				numOfPack++;
				packName = pR.group(1);
			}
			else
			{
				printError();
			}
		
		/*
		 * Only one import with the same name can be considered correct. 
		 * So, if the file is trying to import the same package twice, this is an error. 
		 */
		else if(iR.find())
		{
			boolean importCheck = false;
			
			if(listOfImport.size() > 0)
			{
				for (int i = 0; i < listOfImport.size(); i++)
				{
					if((listOfImport.get(i)).equals(line))
					{
						printError();
						importCheck = true;
					}
				}
				if(!importCheck)
					listOfImport.add(line);
			}
			else
				listOfImport.add(line);
		}
		
		/*
		 * A class can contain only one Constructor with the same signature, 
		 * but any with different signatures; also no Constructor at all.
		 */
		else if(conR.find())
		{
			String result = "";
			result += CLASS_NAME;
			boolean constCheck = false;

			Matcher n = parametersRegex.matcher(line);
			while(n.find())
			{
				if(n.group(1) != null)
					result += n.group(1);
			}
			if(listOfConstructor.size() > 0)
			{
				for(int i = 0; i < listOfConstructor.size(); i++)
				{
					if((listOfConstructor.get(i)).equals(result))
					{
						printError();
						constCheck = true;
					}
				}
				
				if(!constCheck)
					listOfConstructor.add(line);
			}
			else
				listOfConstructor.add(result);
		}
		
		/*
		 * The same with Methods, only one with the same signature, 
		 * any with different signatures, or no Methods at all.
		 */
		else if(mR.find())
		{
			boolean methodCheck = false;
			String result = "";
			result += mR.group(1);
			if(mR.group(2) != null)
				result += mR.group(2);
			result += mR.group(3);
			result += mR.group(4);
			
			String[] arr = line.split("[\\(\\)]");
			String para = "";
			if(arr.length > 1)
				para = arr[1];
			Matcher n = parametersRegex.matcher(para);
			while(n.find())
			{
				if(n.group(1) != null)
					result += n.group(1);
			}
			
			if(listOfMethod.size() > 0)
			{
				for(int i = 0; i < listOfMethod.size(); i++)
				{
					if((listOfMethod.get(i)).equals(result))
					{
						printError();
						methodCheck = true;
					}
				}
				
				if(!methodCheck)
					listOfMethod.add(result);
			}
			else
				listOfMethod.add(result);
		}
		
		/*
		 * The Instance variables can be int, float, boolean, char and String.
		 */
		//For int, float, double
		else if(ivfrR.find())
		{
			boolean check = false;
			String result = "";
			result += ivfrR.group(2);
			result += ivfrR.group(3);

			if(listOfivfNum.size() > 0)
			{
				for (int i = 0; i < listOfivfNum.size(); i++)
				{
					if((listOfivfNum.get(i)).equals(result))
					{
						printError();
						check = true;
					}
				}
				if(!check)
					listOfivfNum.add(result);
			}
			else
				listOfivfNum.add(result);
		}
		
		//for Boolean
		else if(ivfBR.find())
		{
			boolean check = false;
			String result = "";
			result += ivfBR.group(2);
			result += ivfBR.group(3);

			if(listOfivfBool.size() > 0)
			{
				for (int i = 0; i < listOfivfBool.size(); i++)
				{
					if((listOfivfBool.get(i)).equals(result))
					{
						printError();
						check = true;
					}
				}
				if(!check)
					listOfivfBool.add(result);
			}
			else
				listOfivfBool.add(result);
		}
		
		//for String
		else if(ivfSR.find())
		{
			boolean check = false;
			String result = "";
			result += ivfSR.group(2);
			result += ivfSR.group(3);

			if(listOfivfString.size() > 0)
			{
				for (int i = 0; i < listOfivfString.size(); i++)
				{
					if((listOfivfString.get(i)).equals(result))
					{
						printError();
						check = true;
					}
				}
				if(!check)
					listOfivfString.add(result);
			}
			else
				listOfivfString.add(result);
		}
		
		//for Char
		else if(ivfCR.find())
		{
			boolean check = false;
			String result = "";
			result += ivfCR.group(2);
			result += ivfCR.group(3);

			if(listOfivfChar.size() > 0)
			{
				for (int i = 0; i < listOfivfChar.size(); i++)
				{
					if((listOfivfChar.get(i)).equals(result))
					{
						printError();
						check = true;
					}
				}
				if(!check)
					listOfivfChar.add(result);
			}
			else
				listOfivfChar.add(result);
		}
	}
	
	public void printError()
	{
		System.out.println("Error found on the line #" + lineNum);
		error = true;
	}
	
	public void checkError()
	{
		if(!error)
			System.out.println("The Class, " + CLASS_NAME + " is OK.");
	}
}

