import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * If everything from the previous tiers is working fine, the software will also recognize 
 * inheritance (super classes and subclasses), plus Interfaces and Abstract Classes.
 */
public class Tier3 
{
	//A subclass can inherit only from one superclass.
	public void firstHint(String fileName)
	{
		try
		{
			Scanner in = new Scanner(Paths.get(fileName), "UTF-8");
			Compiler c = new Compiler();
			Pattern inheritanceRegex = Pattern.compile("\\s*(public|protected|private){1}\\s+class\\s+\\w+\\s+extends(\\s+\\w+)*");
			while(in.hasNext())
			{
				String line = in.nextLine();
				Matcher iR = inheritanceRegex.matcher(line);
				if(iR.find())
				{
					Pattern correctInheritanceRegex = Pattern.compile("\\s*(public|protected|private){1}\\s+class\\s+\\w+\\s+extends((\\s+\\w+)\\,*){2,}");
					Matcher correctReg = correctInheritanceRegex.matcher(line);
					if(correctReg.find())
						c.printError();
				}
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
	}
	
	//A Class can implement as many Interfaces it wants.
	public void secondHint(String fileName)
	{
		try
		{
			Scanner in = new Scanner(Paths.get(fileName), "UTF-8");
			Compiler c = new Compiler();
			Pattern implementsRegex = Pattern.compile("\\s*(public|protected|private){1}\\s+class\\s+\\w+\\s+implements((\\s+\\w+)\\,*)+");
			while(in.hasNext())
			{
				String line = in.nextLine();
				Matcher iR = implementsRegex.matcher(line);
				if(iR.find())
				{
					System.out.println("Interface found on the line #" + c.lineNum);
				}
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
	}
	
	//An Abstract Class cannot create instance of an object. 
	public void thirdHint(String fileName)
	{
		try
		{
			Scanner in = new Scanner(Paths.get(fileName), "UTF-8");
			Compiler c = new Compiler();
			Pattern abstractRegex = Pattern.compile("\\s*(public|protected|private){1}\\s+abstract\\s+(void|int|String|double|char|float|boolean){1}\\s+\\(\\s*\\)\\s;");
			while(in.hasNext())
			{
				String line = in.nextLine();
				Matcher aR = abstractRegex.matcher(line);
				if(aR.find())
				{
					System.out.println("Abstract class found on the line #:");
				}
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
	}
	
	//To implement an Interface the class have to define all the methods from that Interface.
	List<String> listOfInterface;
    LinkedList<Node> linkedlist = new LinkedList<Node>();

	public void fourthHint(String fileName)
	{
		try
		{
			Scanner in = new Scanner(Paths.get(fileName), "UTF-8");
			Compiler c = new Compiler();
			Pattern interfaceRegex = Pattern.compile("\\s*interface\\s+(\\w+)");
			Pattern parametersRegex = Pattern.compile("(int|float|char|boolean|String|double)*");
			while(in.hasNext())
			{
				String line = in.nextLine();
				Matcher iR = interfaceRegex.matcher(line);
				Pattern methodRegex = Pattern.compile("\\s*(void|int|String|double|float|char|boolean){1}\\s+(\\w+)\\s*\\(\\s*((int|double|char|String|float|boolean){1}\\s+\\w+\\,*\\s*)*\\);");
				Matcher mR = methodRegex.matcher(line);
				if(iR.find())
				{
					boolean start = false;
					boolean end = false;
					boolean check = false;
					listOfInterface = new ArrayList<String>();
					String nameOfInterface = iR.group(1);
					while(in.hasNext() && !start && !end && !check)
					{
						for(int i = 0; i < line.length(); i++)
							if((line.charAt(i)) == '{')
								start = true;
						line = in.nextLine();
						if(!start)
							check = true;
						if(start && !check)
						{
							int i = 0;
							while(in.hasNext() && !end)
							{
								if(Pattern.matches("\\s*(void|int|String|double|float|char|boolean){1}\\s+(\\w+)\\s*\\(\\s*((int|double|char|String|float|boolean){1}\\s+\\w+\\,*\\s*)*\\);", line))
								{
									String result = "";
									String arr[] = line.split("\\(");
									String arr1[] = arr[0].split("\\s+");
									for(int z = 0; z < arr1.length; z++)
										result += arr1[z];
									String arr2[] = arr[1].split("[\\s+\\,]");
									for(int z = 0; z < arr2.length; z++)
									{
										if(arr2[z].equals("int")||arr2[z].equals("double")||
												arr2[z].equals("String")||arr2[z].equals("char")||
												arr2[z].equals("float")||arr2[z].equals("boolean"))
										{
											result += arr2[z];
										}
									}
									listOfInterface.add(result);
								}
								for(int z = 0; z < line.length(); z++)
									if(line.charAt(i) == '}')
										end = true;
								i++;
								line = in.nextLine();
								Node newNode = new Node(nameOfInterface, listOfInterface);
								linkedlist.add(newNode);
							}
						}
						
					}
					if(check)
						c.printError();
					Node newNode = new Node(nameOfInterface, listOfInterface);
					linkedlist.add(newNode);
				}
				//Pattern implementsRegex = Pattern.compile("\\s*(public|protected|private)*class\\s+\\w+\\s+implements\\s+(\\w+)");
				//Matcher imR = implementsRegex.matcher(line);
				if(Pattern.matches("\\s*(public|protected|private)*class\\s+\\w+\\s+implements\\s+(\\w+)", line))
				{
					String nameOfImplements = "";
					String ar[] = line.split("\\s+");
					for(int z = 0; z < ar.length; z++)
					{
						if(ar[z].equals("implements"));
							nameOfImplements = ar[z + 1];
					}
					
					for(int i = 0; i < linkedlist.size(); i++)
					{
						if((linkedlist.get(i)).name.equals(nameOfImplements))
						{
							boolean start = false;
							boolean end = false;
							boolean check = false;
							while(in.hasNext() && !start && !end && !check)
							{
								for(int j = 0; j < line.length(); j++)
									if((line.charAt(j)) == '{')
										start = true;
								if(!start)
									check = true;
								if(start && !check)
								{
									int k = 0;
									//while(k < line.length() && !end)
									//{
									if(Pattern.matches("\\s*(void|int|String|double|float|char|boolean){1}\\s+(\\w+)\\s*\\(\\s*((int|double|char|String|float|boolean){1}\\s+\\w+\\,*\\s*)*\\);", line))
									{
										String result = "";
										String arr[] = line.split("\\(");
										String arr1[] = arr[0].split("\\s+");
										for(int z = 0; z < arr1.length; z++)
											result += arr1[z];
										String arr2[] = arr[1].split("[\\s+\\,]");
										for(int z = 0; z < arr2.length; z++)
										{
											if(arr2[z].equals("int")||arr2[z].equals("double")||
													arr2[z].equals("String")||arr2[z].equals("char")||
													arr2[z].equals("float")||arr2[z].equals("boolean"))
											{
												result += arr2[z];
											}
										}
											boolean found = false;
											for(int q = 0; q < linkedlist.get(i).listOfMethod.size(); q++)
											{
												if((linkedlist.get(i).listOfMethod.get(q)).equals(result));
													 found = true;
											}
											if(!found)
											{
												c.printError();
											}
											
										}
									for(int z = 0; z < line.length(); z++)
										if(line.charAt(i) == '}')
											end = true;
									k++;
									//}
								}
							}
						}
						else
							System.out.println("Ther is no such interface called: " + nameOfImplements);
					}
				}
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
	}

}
