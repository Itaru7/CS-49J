import java.util.ArrayList;
import java.util.List;

public class Node 
{
	public String name;
	public List<String> listOfMethod;
	
	public Node()
	{
		name = "";
		listOfMethod = new ArrayList<String>();
	}
	
	public Node(String newName, List<String> newList)
	{
		name = newName;
		listOfMethod = newList;
	}
	
	public void setData(String newName, List<String> newList)
	{
		name = newName;
		listOfMethod = newList;
	}
}
