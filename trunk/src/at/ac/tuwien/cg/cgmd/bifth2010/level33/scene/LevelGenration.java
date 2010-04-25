package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;



import java.util.ArrayList;
import java.util.Random;


import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;

public class LevelGenration {
	
	Random rg = new Random();
	int[] levelField;
	int[] createdWays;
	int columns=0;
	int rows=0;
		
	//shortestPath
	int[][] mapInfo[];
	int[] goodiesIndex;
	int[] mapWayIndex;
	int numberOfGoodies=0;
	int mapCount=0;
	
	//WallInformation
	WallInformation wallInfo = null;
	static ArrayList<int[]> walls = new ArrayList<int[]>();
	
	
	int wayOffset=0;
	double percentOfWay=0.0;
	
	//Verhältniss Goodies/Wege
	double goodiesWayRatio = 0.3;
	
	int numberOfMaps=2;
	int numberOfStone=6;
	int numberOfBarrel=4;
	int numberOfTrashes=3;
	int numberOfSpring=3;
	
	
	public LevelGenration(int columnRowSize, int wayOffset, double percentOfWay,
			int numberOfMaps, int numberOfStone, int numberOfBarrel, int numberOfTrashes,
			int numberOfSpring){
		
		this.columns = columnRowSize;
		this.rows = columnRowSize;
		this.wayOffset = wayOffset; // zwischen 1 und 3
		this.percentOfWay = percentOfWay; // 40% ist recht gut
		this.numberOfMaps = numberOfMaps; //2
		this.numberOfStone = numberOfStone; //6
		this.numberOfBarrel = numberOfBarrel; //4
		this.numberOfTrashes = numberOfTrashes; //3
		this.numberOfSpring = numberOfSpring; //3
				
		checkLevelSettings();
		
	}
	
	public LevelGenration(int columnRowSize){
		
		this.columns= columnRowSize;
		this.rows=columnRowSize;
		int levelSize=rows*columns;
		
		setDefaultLevelParameters(levelSize);
		
	}
	
	/**
	 * Default Level-Parameter
	 */
	private void setDefaultLevelParameters(int levelSize){
		
		//relative Werte anhand Lvl-Größe
		double percentOfWay=0.2;
		double percentOfMaps=0.04;
		double percentOfStone=0.10;
		double percentOfBarrel=0.07;
		double percentOfTrashes=0.06;
		double percentOfSprings=0.04;
		
		this.wayOffset=3;
		this.percentOfWay = percentOfWay;
		int numberOfWays =new Double(levelSize*percentOfWay).intValue();
		this.numberOfMaps = new Double(numberOfWays*percentOfMaps).intValue();
		this.numberOfStone = new Double(numberOfWays*percentOfStone).intValue();
		this.numberOfBarrel = new Double(numberOfWays*percentOfBarrel).intValue();
		this.numberOfTrashes = new Double(numberOfWays*percentOfTrashes).intValue();
		this.numberOfSpring = new Double(numberOfWays*percentOfSprings).intValue();
		
		checkLevelSettings();
	}
	
	/**
	 * Checks if all Parameters are correct.
	 */
	private void checkLevelSettings(){
		
		int levelSize=rows*columns;
		if(percentOfWay >0.50)
			percentOfWay=0.50;
		int numberOfWays =new Double(levelSize*percentOfWay).intValue();
		int allGoodiesTogether= numberOfMaps+numberOfStone+numberOfBarrel+
								numberOfTrashes+numberOfSpring;
		
		if(allGoodiesTogether/numberOfWays>=goodiesWayRatio)
		{
			//Wrong Parameter
			setDefaultLevelParameters(levelSize);
		}
	}
	
	/**
	 * Creates a random Level-Matrix
	 * 
	 */
	public int[] startCreation() {
		
		int levelSize=rows*columns;
		levelField = new int[levelSize];
				
		if(percentOfWay >0.50)
			percentOfWay=0.50;
		int numberOfWays =new Double(levelSize*percentOfWay).intValue();
		createdWays = new int[numberOfWays];
		
		//init Lvl
		for(int i=0; i<levelSize; i++)
			levelField[i]=SceneGraph.GEOMETRY_WALL;
		int startField = rg.nextInt(levelSize);
		levelField[startField]=1;
		createdWays[0]=startField;
				
		numberOfGoodies = numberOfBarrel+numberOfSpring+numberOfStone;
		goodiesIndex = new int[numberOfGoodies];
		mapWayIndex = new int[numberOfMaps];
		int goodieCount=0;
		
		//only Way
		for(int i=1; i<numberOfWays;i++)
		{
			creatWay(i);
		}
		
		//Find the right wall-models
		wallInfo = new WallInformation(levelField,columns);
		wallInfo.calculatesWallInformation();
		walls = wallInfo.getNewWorldArray();
		
		
		//goodies
		for(int i=0; i<numberOfMaps;i++)
		{
			creatGoodiesWayPoints("Map",goodieCount);
			mapCount++;
		}
		
		for(int i=0; i<numberOfStone;i++)
		{
			creatGoodiesWayPoints("Stone",goodieCount);
			goodieCount++;
		}
		
		for(int i=0; i<numberOfTrashes;i++)
		{
			creatGoodiesWayPoints("Trash",goodieCount);
		}
		
		for(int i=0; i<numberOfSpring;i++)
		{
			creatGoodiesWayPoints("Spring",goodieCount);
			goodieCount++;
		}
		
		for(int i=0; i<numberOfBarrel;i++)
		{
			creatGoodiesWayPoints("Barrel",goodieCount);
			goodieCount++;
		}
		
		mapInfo = new int[mapCount][goodieCount][];
		
		//find the shortest way
		solveLabyrinth();
		
		//calculates wall-information
		//wallGeneration();
		
		return levelField;
	
	}
	
	/**
	 * Creates random ways 
	 */
	private void creatWay(int wayPointCount)
	{
		int seedWayPointIndex=0;
		int[] allNeighbourIndex;
		
		boolean foundNewWay = false;
		do
		{
			do
			{
			seedWayPointIndex = rg.nextInt(wayPointCount);
			allNeighbourIndex=checkWayPossibility(createdWays[seedWayPointIndex]);
			}while(allNeighbourIndex[0]!=1);
			//int newWayDirection = rg.nextInt(4);
			//0 up
			//1 right
			//2 down
			//3 left
			int newWayDirection = findGoodWay(allNeighbourIndex);
			if(newWayDirection==-1)
			{
				foundNewWay=false;
			}
			else if(levelField[allNeighbourIndex[newWayDirection]]!=1)
			{
				foundNewWay=true;
				levelField[allNeighbourIndex[newWayDirection]]=SceneGraph.GEOMETRY_WAY;
				createdWays[wayPointCount]=allNeighbourIndex[newWayDirection];
			}
						
		}while(!foundNewWay);
		
	}
	
	/**
	 * All waypoint`s neighbours will be checked and calculated
	 */
	private int[] checkWayPossibility(int seedPoint){
		
		int[] wayNeighbourPreferences = new int[5];
		wayNeighbourPreferences[0]=1;
		
		//0 up
		if(seedPoint-columns<0)
		{
			wayNeighbourPreferences[1]=seedPoint+(rows-1)*columns;
		}
		else
		{
			wayNeighbourPreferences[1]=seedPoint-columns;
		}
		//1 right
		if((seedPoint+1)%columns==0)
		{
			wayNeighbourPreferences[2]=seedPoint-(columns-1);
		}
		else
		{
			wayNeighbourPreferences[2]=seedPoint+1;
		}
		//2 down
		if(seedPoint+columns>columns*rows-1)
		{
			wayNeighbourPreferences[3]=seedPoint-(rows-1)*columns;
		}
		else
		{
			wayNeighbourPreferences[3]=seedPoint+columns;
		}
		//3 left
		if((seedPoint-1)%columns==columns-1 ||seedPoint==0)
		{
			wayNeighbourPreferences[4]=seedPoint+(columns-1);
		}
		else
		{
			wayNeighbourPreferences[4]=seedPoint-1;
		}
		
		//checkAvailable
		if(levelField[wayNeighbourPreferences[1]]==1 && levelField[wayNeighbourPreferences[2]]==1 &&
		   levelField[wayNeighbourPreferences[3]]==1 && levelField[wayNeighbourPreferences[4]]==1)
			wayNeighbourPreferences[0]=-1;
		
		return wayNeighbourPreferences;
	}
	
	/**
	 * Calculates the best ways based on a waypoint so that the labyrinth wont be too easy
	 */
	private int findGoodWay(int[] allWayNeighbours)
	{
		ArrayList<Integer> possibleGoodDirections= new ArrayList<Integer>();
		
		for(int i=1; i<5; i++)
		{
			int[] tempNeighbours = checkWayPossibility(allWayNeighbours[i]);
			int counter=0;
			
			for(int j=1; j<5; j++)
			{
				if(levelField[tempNeighbours[j]]==0)
					counter++;
			}
			
			if(counter >= wayOffset)
			{
				possibleGoodDirections.add(i);
			}
		}
		
		if(possibleGoodDirections.size()==0)
			return -1;
		//Choose good direction
		int index = rg.nextInt(possibleGoodDirections.size());
		return possibleGoodDirections.get(index);
	}
	
	/**
	 * Random waypoints will be transformed into some special waypoints/goodies
	 */
	private void creatGoodiesWayPoints(String goodieMode, int goodieCount)
	{
		//0 Wall
		//1 Way
		//2 Stone
		//3 Barrel
		//4 Trash
		//5 Map
		//6 Spring
		
		int wayPointIndex =0;
		int levelFieldIndex=-1;
		do
		{
			wayPointIndex=rg.nextInt(createdWays.length);
			levelFieldIndex=createdWays[wayPointIndex];
		}while(levelFieldIndex==-1 || wayPointIndex==0);
		

		if(goodieMode.equals("Stone"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_STONE;
			walls.get(levelFieldIndex)[0]=SceneGraph.GEOMETRY_STONE;
			goodiesIndex[goodieCount]=levelFieldIndex;
		}else
		if(goodieMode.equals("Map"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_MAP;
			walls.get(levelFieldIndex)[0]=SceneGraph.GEOMETRY_MAP;
			mapWayIndex[mapCount]=levelFieldIndex;
		}else
		if(goodieMode.equals("Trash"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_TRASH;
			walls.get(levelFieldIndex)[0]=SceneGraph.GEOMETRY_TRASH;
		}else
		if(goodieMode.equals("Spring"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_SPRING;
			walls.get(levelFieldIndex)[0]=SceneGraph.GEOMETRY_SPRING;
			goodiesIndex[goodieCount]=levelFieldIndex;
		}else
		if(goodieMode.equals("Barrel")){
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_BARREL;
			walls.get(levelFieldIndex)[0]=SceneGraph.GEOMETRY_BARREL;
			goodiesIndex[goodieCount]=levelFieldIndex;
		}
		createdWays[wayPointIndex]=-1;
		
	}
	
	
	/**
	 * Calculates all neighbours-index of a point
	 */
	public  int[] checkAllNeighbours(int seedPoint){
		
		/*
		 * 	7	0	1
		 * 	6	SP	2
		 * 	5	4	3
		 */
		
		int[] allNeighbours = new int[8];
		int[] mainNeighbours = checkWayPossibility(seedPoint);
		
		allNeighbours[0]=mainNeighbours[1];
		allNeighbours[2]=mainNeighbours[2];
		allNeighbours[4]=mainNeighbours[3];
		allNeighbours[6]=mainNeighbours[4];
		
		//1
		if(mainNeighbours[2]-columns<0)
		{
			allNeighbours[1]=mainNeighbours[2]+(rows-1)*columns;
		}
		else
		{
			allNeighbours[1]=mainNeighbours[2]-columns;
		}
		
		//3
		if(mainNeighbours[2]+columns>columns*rows-1)
		{
			allNeighbours[3]=mainNeighbours[2]-(rows-1)*columns;
		}
		else
		{
			allNeighbours[3]=mainNeighbours[2]+columns;
		}
		
		//5
		if(mainNeighbours[4]+columns>columns*rows-1)
		{
			allNeighbours[5]=mainNeighbours[4]-(rows-1)*columns;
		}
		else
		{
			allNeighbours[5]=mainNeighbours[4]+columns;
		}
		
		//7
		if(mainNeighbours[4]-columns<0)
		{
			allNeighbours[7]=mainNeighbours[4]+(rows-1)*columns;
		}
		else
		{
			allNeighbours[7]=mainNeighbours[4]-columns;
		}				
		
		return allNeighbours;
	}
	
	/**
	 * Solves the labyrinth and find a correct way
	 */
	public void solveLabyrinth() {

		// Für alle Mpas im Laby
		for (int i = 0; i < mapCount; i++) {
			// mapInformation = new HashMap<Integer, int[]>(numberOfGoodies);
			for (int j = 0; j < numberOfGoodies; j++) {
				// Node lastNode = null;
				int[] waySequence = calculateShortestPath(mapWayIndex[i], goodiesIndex[j]);
				mapInfo[i][j] = waySequence;
			}
		}
	}
	
	/**
	 * Calculates the way-sequence of a shortest way
	 */
	public int[] calculateShortestPath(int startPoint,int targetPoint) {

		ArrayList<int[]> possibleList = new ArrayList<int[]>();
		ArrayList<int[]> closedList = new ArrayList<int[]>();

		// add startPoint to list
		possibleList.add(new int[] { startPoint, walls.get(startPoint)[2], 0 });

		while (!possibleList.isEmpty()) {
			// Finde besten Wegpunkt
			int bestNodeIndex = findBestNode(possibleList, targetPoint);

			// alle Richtungen
			int[] mainNeighbours = checkWayPossibility(possibleList.get(bestNodeIndex)[0]);
			for (int i = 1; i < mainNeighbours.length; i++) {
				if (walls.get(mainNeighbours[i])[0] > 0) {
					// Node steht generell zur Verfügung

					int newWayCost = possibleList.get(bestNodeIndex)[2] + 1;

					if (mainNeighbours[i] == targetPoint) {
						int[] newWayPoint = new int[] { mainNeighbours[i],possibleList.get(bestNodeIndex)[0], newWayCost };
						// Pfad-Berechnung

						int[] shortestWaySequenze = calculateShortesWayIndex(newWayPoint);
						return shortestWaySequenze;
					}

					boolean addToList = true;
					for (int j = 0; j < possibleList.size(); j++) {
						if (possibleList.get(j)[0] == mainNeighbours[i] && possibleList.get(j)[2] <= walls.get(mainNeighbours[i])[3])
							addToList = false;
					}
					for (int j = 0; j < closedList.size(); j++) {
						if (closedList.get(j)[0] == mainNeighbours[i] && closedList.get(j)[2] <= walls.get(mainNeighbours[i])[3])
							addToList = false;
					}

					if (addToList) {
						walls.get(mainNeighbours[i])[2] = possibleList.get(bestNodeIndex)[0];
						walls.get(mainNeighbours[i])[3] = newWayCost;
						possibleList.add(new int[] { mainNeighbours[i],possibleList.get(bestNodeIndex)[0],newWayCost });
					}
				}
			}
			// Aus der Liste der verfügbaren Wegpunkte entfernen
			closedList.add(possibleList.get(bestNodeIndex));
			possibleList.remove(bestNodeIndex);
		}

		int[] shortestWaySequenze = null;
		return shortestWaySequenze;
	}
	
	/**
	 * Finds the best Node to find the shortest path
	 */
	public int findBestNode(ArrayList<int[]> possibleList,
			int targetPoint) {

		int bestNodeIndex = -1;

		for (int i = 0; i < possibleList.size(); i++) {

			if (bestNodeIndex == -1) {
				bestNodeIndex = i;
			} else {
				if (getWayCost(possibleList.get(i), targetPoint) < getWayCost(possibleList.get(bestNodeIndex), targetPoint))
					bestNodeIndex = i;
			}
		}

		return bestNodeIndex;
	}
	
	public float getWayCost(int[] wayPoint, int targetFieldNumber) {

		int distX = Math.abs(wayPoint[0] % columns - targetFieldNumber
				% columns);
		int distY = Math.abs(wayPoint[0] / columns - targetFieldNumber
				/ columns);
		float costNode = (float) Math.sqrt(distX * distX + distY * distY);
		costNode = wayPoint[2] + costNode;
		return costNode;
	}
	
	
	public int[] calculateShortesWayIndex(int[] lastWayPoint) {

		int wayIndex = 0;
		int wayIndexCount = lastWayPoint[2];
		int[] wayIndexSequence = new int[wayIndexCount];

		for (int i = wayIndexCount - 1; i >= 0; i--) {
			if (i == wayIndexCount - 1) {
				wayIndexSequence[i] = lastWayPoint[0];
				wayIndex = lastWayPoint[1];
			} else {
				wayIndexSequence[i] = wayIndex;
				wayIndex = walls.get(wayIndex)[2];
			}
		}

		return wayIndexSequence;
	}
	
	public int getMapIndex(int mapField) {

		for (int i = 0; i < mapCount; i++) {
			if (mapField == mapWayIndex[i])
				return i;
		}

		// Error
		return -1;
	}

	public int getGoodiesIndex(int goodyField) {

		for (int i = 0; i < numberOfGoodies; i++) {
			if (goodyField == goodiesIndex[i])
				return i;
		}

		// Error
		return -1;
	}
	
	/**
	 * Get the shortest way to a target
	 */
	public int[] getWayToTarget(int startField, int targetField){
		int[] wayToTarget = mapInfo[getMapIndex(startField)][getGoodiesIndex(targetField)];
		return wayToTarget;
	}

	/**
	 * Get the shortest way to the nearest target
	 */
	public int[] getWayToNearestTarget(int startField) {

		int[] wayToTarget = null;
		int mapIndex = getMapIndex(startField);
		int sizeG = mapInfo[mapIndex].length;

		for (int i=0; i<sizeG; i++)
		{	
			if(wayToTarget==null || mapInfo[mapIndex][i].length<wayToTarget.length)
				wayToTarget = mapInfo[mapIndex][i];
		}

		return wayToTarget;
	}

	/**
	 * Get the shortest way to a target
	 */
	public int[] getWayToFarthestTarget(int startField) {

		int[] wayToTarget = null;
		int mapIndex = getMapIndex(startField);
		int sizeG = mapInfo[mapIndex].length;

		for (int i=0; i<sizeG; i++)
		{	
			if(wayToTarget==null || mapInfo[mapIndex][i].length>wayToTarget.length)
				wayToTarget = mapInfo[mapIndex][i];
		}

		return wayToTarget;

	}
		
	/**
	 *Get the start position of the whole labyrinth
	 */
	public Vector2i getStartPosition(){
		
		
		return new Vector2i(createdWays[0]%columns,createdWays[0]/columns);
	}
	
	
	/**
	 * Print the created labyrinth
	 */
	private void testCreation(boolean all){
		
		System.out.println("Labyrinth: Startpunkt bei "+createdWays[0]+"\n");
		System.out.println("------------------------");
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				System.out.print(" "+levelField[10*i+j]);
				//System.out.print(" "+(10*i+j));
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Print the shortest way to all goodies for each map
	 */
	/*
	public void testSolvedLabyrinth(){
		
		for(int i=0; i<mapCount;i++)
		{
			System.out.println("Map: " +i+" on field: "+mapWayIndex[i]);
			System.out.println("************");
			Node n = allNodesHandler.get(mapWayIndex[i]);
			for(int j=0; j<numberOfGoodies; j++)
			{
				
				int hashMapIndex = calculateHashMapIndex(goodiesIndex[j]);
				int[] testWay = n.getWayToTarget(hashMapIndex);
				for(int l=0; l<testWay.length; l++)
				{
					System.out.print(testWay[l]+" ");
				}
				System.out.print("\n");
			}
			System.out.println("------------");
			System.out.println("Kürzester Weg:");
			int[] testShortWay = n.getWayToNearestTarget();
			for(int l=0; l<testShortWay.length; l++)
			{
				System.out.print(testShortWay[l]+" ");
			}
			System.out.print("\n");
			
			System.out.println("------------");
			System.out.println("Längster Weg:");
			int[] testFarWay = n.getWayToFarthestTarget();
			for(int l=0; l<testFarWay.length; l++)
			{
				System.out.print(testFarWay[l]+" ");
			}
			System.out.print("\n");
			
			System.out.println("************");
		}
		
	}*/

}
