import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class testing123 
{
	Map<String, LinkedHashSet<Map<String, Integer>>> Tempgraph = new LinkedHashMap<String, LinkedHashSet<Map<String, Integer>>>();
	List<String> traversedPlacesListDFS = new LinkedList<String>();
	List<String> calculateDistanceListDFS = new LinkedList<String>();
	List<String> iterativeDeepeningList = new ArrayList<String>();
	List<String> traverseFirstList = new ArrayList<String>();
	List<String> tempList = new ArrayList<String>();
	List<String> seenList = new ArrayList<String>();
	List<String> visitedList = new ArrayList<String>();
	Map<String, String> childParentMap = new LinkedHashMap<String, String>();
	Map<String, Integer> someMap = new LinkedHashMap<String, Integer>();
	Boolean flag = false;
	Boolean iterativeFlag = false;
	int distance =0;
	int iterativeDepth =0;
	String startPlace, endPlace, choice;
	/*For scanning the file in this case Distance.txt*/
	void FileReader() throws FileNotFoundException
	{
		File filename = new File("Distance.txt");
		Scanner sc = new Scanner(filename);
		while (sc.hasNextLine()) 
		{
			String node1 = sc.next();
			String node2 = sc.next();
			int distance = sc.nextInt();
			addDualEdge(node1, node2, distance);
		}
		sc.close();
	}
	/*Fills the HashMap with key value pairs */
	private void addEdge(String node1, String node2, int distance) 
	{
		LinkedHashSet<Map<String,Integer>> adjacent = Tempgraph.get(node1);
		Map<String, Integer> innerMap = new LinkedHashMap<String, Integer>();
		if(adjacent == null)
		{
			adjacent = new LinkedHashSet<Map<String,Integer>>();
			innerMap.put(node2, distance);
			adjacent.add(innerMap);
			Tempgraph.put(node1, adjacent);
		}
		innerMap.put(node2, distance);
		adjacent.add(innerMap);
		Tempgraph.put(node1, adjacent);
	}
	/*To maintain commutative distance property*/
	private void addDualEdge(String node1, String node2, int distance) 
	{
		addEdge(node1, node2, distance);
		addEdge(node2, node1, distance);
	}


	private void DepthFirstSearch(String place1, String place2)
	{

		LinkedHashSet<Map<String,Integer>> tempHashSet = Tempgraph.get(place1);
		for(Map<String, Integer> tempMap : tempHashSet)
		{
			for(String temp : tempMap.keySet())
			{
				if(childParentMap.keySet().contains(temp)!=true && childParentMap.values().contains(temp)!=true)
				{
					childParentMap.put(temp, place1);
				}
			}
		}


		if(Tempgraph.keySet().contains(place1) && calculateDistanceListDFS.contains(place2)!=true)
		{
			while(Tempgraph.get(place1)!=null && traversedPlacesListDFS.contains(place1)!= true )
			{
				traversedPlacesListDFS.add(place1);
				for(Map<String, Integer> tempMap : tempHashSet)
				{
					for(String temp : tempMap.keySet())
					{
						if(flag!=true)
						{
							calculateDistanceListDFS.add(temp);
						}
						if(calculateDistanceListDFS.contains(temp) && traversedPlacesListDFS.contains(temp)!=true  && flag!=true)
						{
							//distance = distance + tempMap.get(temp);
						}
						if(traversedPlacesListDFS.contains(place2))
						{
							flag = true;
							//System.out.println("return 1 *****");
							//calculateDistanceListDFS.clear();
							return;
						}
						else
						{
							DepthFirstSearch(temp, place2);
						}
					}
				}
			}
		}
		else
		{
			traversedPlacesListDFS.add(place2);
			flag = true;
			//System.out.println("return 2 *****");
			calculateDistanceListDFS.clear();
			distance = getCalculatedDistance(place1, place2, place2);
			return;
		}
	}
	private void BreadthFirstSearch(String place1, String place2)
	{
		List<String> traverseList = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();
		List<String> visitedNodes = new ArrayList<String>();
		String source = " "; boolean indicator = false;
		traverseList.add(place1);
		if(Tempgraph.containsKey(place1))
		{
			while(traverseList.isEmpty()!= true)
			{
				tempList.clear();
				tempList.addAll(traverseList);
				int count = tempList.size();
				for(int i=0; i< count; i++)
				{
					String currentPlace = tempList.get(i);
					LinkedHashSet<Map<String,Integer>> tempHashSet = Tempgraph.get(currentPlace);
					for(Map<String, Integer> tempMap : tempHashSet)   /*Maintaining the childParentMap for backtracking path*/
					{
						for(String temp : tempMap.keySet())
						{
							if(childParentMap.keySet().contains(temp)!=true && childParentMap.values().contains(temp)!=true)
							{
								childParentMap.put(temp, currentPlace);
							}
						}
					}
					visitedNodes.add(currentPlace);
					traverseList.remove(currentPlace); 
					if(tempHashSet!=null)
					{
						for(Map<String, Integer> tempMap : tempHashSet)
						{
							for(String temp : tempMap.keySet())
							{
								if(visitedNodes.contains(temp)!=true)
								{
									if(temp.equals(place2))
									{
										flag = true;
										visitedNodes.add(place2);
										source = temp;
										while(source.equals(place1)!=true)
										{
											LinkedHashSet<Map<String,Integer>> distanceHashSet = Tempgraph.get(source);
											for(Map<String, Integer> distanceMap : distanceHashSet)
											{
												if(indicator)
												{
													break;
												}
												for(String temp1 : distanceMap.keySet())
												{
													if(visitedNodes.contains(temp1) && calculateDistanceListDFS.contains(temp1)!=true && childParentMap.get(source).equals(temp1))
													{
														if(calculateDistanceListDFS.contains(place2)!=true)    // Adding the destination for backtracking
														{
															calculateDistanceListDFS.add(place2);
														}
														distance = distance + distanceMap.get(temp1);
														calculateDistanceListDFS.add(temp1);
														source = temp1;
														if(source.equals(place1)== true)
														{
															indicator = true;
															break;
														}
													}
												}
											}
										}
									}
									traverseList.add(temp);
									if(flag == true)
									{
										calculateDistanceListDFS.retainAll(visitedNodes);
										Collections.reverse(calculateDistanceListDFS);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		else
		{
			System.out.println("Place not found");
		}
	}
	private void IterativeDeepening(String place1, String place2)
	{

		LinkedHashSet<Map<String,Integer>> tempHashSet = Tempgraph.get(place1);
		for(Map<String, Integer> tempMap : tempHashSet)
		{
			for(String temp : tempMap.keySet())
			{
				if(childParentMap.keySet().contains(temp)!=true && childParentMap.values().contains(temp)!=true)
				{
					childParentMap.put(temp, place1);
				}
			}
		}
		if(Tempgraph.get(place1)!= null)
		{
			if(tempHashSet!=null && traversedPlacesListDFS.contains(place1)!= true )
			{
				for(Map<String, Integer> tempMap : tempHashSet)
				{
					for(String temp : tempMap.keySet())
					{
						if(traversedPlacesListDFS.contains(temp)!=true && traverseFirstList.contains(temp)!=true)
						{
							if(iterativeDeepeningList.contains(temp)!=true)
							{
								iterativeDeepeningList.add(temp);
							}
							traverseFirstList.add(temp);
							if(tempList.contains(place2) || place1.equals(place2) || traverseFirstList.contains(place2) )
							{
								traversedPlacesListDFS.add(place1);
								traversedPlacesListDFS.add(place2);
								iterativeFlag = true;
								distance = getCalculatedDistance(place1, place2, place2);
								return;
							}
							boolean condition = checkIfAllChildrenTraversed(place1);
							if(condition == false)
							{
								IterativeDeepening(place1, place2);
							}
							else
							{
								traversedPlacesListDFS.add(place1);
								if(tempList.isEmpty()==true)
								{
									tempList.addAll(iterativeDeepeningList);
									iterativeDeepeningList.clear();
									traverseFirstList.clear();
								}
								while(tempList.isEmpty()!=true && iterativeFlag!=true)
								{
									String temp1 = tempList.get(0);
									tempList.remove(temp1);
									traverseFirstList.clear();
									IterativeDeepening(temp1, place2);
								}
							}
						}
					}
				}
			}
		}
		else
		{
			System.out.println("place not found");
		}
	}

	private int getCalculatedDistance(String place1, String source, String place2) 
	{

		boolean indicator=false;
		source = endPlace;
		//System.out.println("place1 is "+ place1+ " source is "+ source);
		while(source.equals(startPlace)!=true && indicator!=true && calculateDistanceListDFS.contains(startPlace)!=true)
		{
			//System.out.println("Entered while");
			LinkedHashSet<Map<String,Integer>> distanceHashSet = Tempgraph.get(source);
			for(Map<String, Integer> distanceMap : distanceHashSet)
			{
				if(indicator)
				{
					break;
				}
				for(String temp1 : distanceMap.keySet())
				{
					if(childParentMap.get(source)!=null)
					{
						if(traversedPlacesListDFS.contains(temp1) && calculateDistanceListDFS.contains(temp1)!=true && childParentMap.get(source).equals(temp1))
						{
							if(calculateDistanceListDFS.contains(place2)!=true)    // Adding the destination for
								// backtracking
							{
								calculateDistanceListDFS.add(place2);
							}
							distance = distance + distanceMap.get(temp1);
							calculateDistanceListDFS.add(temp1);
							source = temp1;
							if(calculateDistanceListDFS.contains(startPlace))
							{
								indicator = true;
								break;
							}
						}
					}
				}
			}
		}
		return distance;
	}
	private boolean checkIfAllChildrenTraversed(String place1)    // checks if the entire level has been traversed
	{                                                             // before jumping to new node
		if(traversedPlacesListDFS.contains(place1))
		{
			return false;
		}
		boolean condition = false;
		LinkedHashSet<Map<String,Integer>> tempHashSet = Tempgraph.get(place1);
		List<String> temporaryList = new ArrayList<String>();
		for(Map<String, Integer> tempMap : tempHashSet)
		{
			for(String temp : tempMap.keySet())
			{
				if(traversedPlacesListDFS.contains(temp)!= true && temporaryList.contains(temp)!=true )
				{
					temporaryList.add(temp);
				}
			}
		}
		if(temporaryList.equals(traverseFirstList))
		{
			condition = true;
		}
		else
		{
			condition = false;
		}
		return condition;
	}
	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException
	{
		testing123 graph = new testing123();
		graph.FileReader();
		String inputLine;
		String[] splitPlace;

		System.out.println("Search-methods are DFS, BFS, ID");   // ID stands for Iterative Deepening
		System.out.println("Enter input in *****Place1,Place2,Search_method***** format");
		Scanner sc = new Scanner(System.in);
		inputLine = sc.nextLine();

		splitPlace = inputLine.split(",");
		graph.startPlace = splitPlace[0];
		graph.endPlace = splitPlace[1];
		graph.choice = splitPlace[2];
		if(graph.startPlace.equals(graph.endPlace))
		{
			System.out.println("distance covered is "+ 0 + " since the start place and end place entered are same");
		}
		else
		{
			switch(graph.choice)
			{
			case "DFS":
				graph.DepthFirstSearch(graph.startPlace, graph.endPlace);
				//System.out.println("printing childParentMap "+ graph.childParentMap);
				//System.out.println("Path followed is "+ graph.traversedPlacesListDFS);
				Collections.reverse(graph.calculateDistanceListDFS);
				System.out.println("Path followed is "+ graph.calculateDistanceListDFS);
				System.out.println("distance covered is "+graph.distance);
				break;
			case "BFS":
				graph.BreadthFirstSearch(graph.startPlace, graph.endPlace);
				System.out.println("Path followed is "+ graph.calculateDistanceListDFS);
				System.out.println("distance covered is "+graph.distance);
				break;
			case "ID": 
				graph.IterativeDeepening(graph.startPlace, graph.endPlace);
				Collections.reverse(graph.calculateDistanceListDFS);
				System.out.println("Path followed is "+ graph.calculateDistanceListDFS);
				System.out.println("distance covered is "+graph.distance);
				break;
			}
		}
	}
}
