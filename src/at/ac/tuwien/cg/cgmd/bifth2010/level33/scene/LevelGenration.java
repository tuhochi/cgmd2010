package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;

/**
 * 
 * A random labyrinth-field will be generated. How the labyrinth looks like is
 * depending of the labyrinth's size. There are some types of labyrinths possible, so there could
 * be only normal ways (with a way-width of 1) or there could be some places. The result of the
 * algorithm will always be a valid labyrinth.
 * After the labyrinth is created the additional items will be randomly set. The numbers of all items
 * can be set, too.
 * Last the walls of the whole labyrinth will be considered to determine the right wall-models.
 *
 *The result is an array. For each point in the labyrinth a specify value will be stored
 *in the array.
 */
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
	
	/** Number of maps */
	public static int numberOfMaps=3;
	
	/** Number of stones */
	public static int numberOfStone=20;
	
	/** Number of barrels */
	public static int numberOfBarrel=5;
	
	/** Number of trashes */
	public static int numberOfTrashes=4;
	
	/** Number of springs */
	public static int numberOfSpring=4;
	//double percentOfWay=0.2;
	
	/**
	 * 
	 * The properties of the chosen labyrinth will be set. If the properties 
	 * are not possible the random-settings will be used.
	 * 
	 * @param columnRowSize				Size of the whole labyrinth's side.
	 * @param wayOffset					How should the labyrinth looks like.
	 * @param percentOfWay				Ratio how many way-elments should be in the labyrinth.
	 * @param numberOfMaps				Ratio of maps in the labyrinth depended of the number of way-elements.
	 * @param numberOfStone				Ratio of stones in the labyrinth depended of the number of way-elements.
	 * @param numberOfBarrel			Ratio of barrels in the labyrinth depended of the number of way-elements.
	 * @param numberOfTrashes			Ratio of trashes in the labyrinth depended of the number of way-elements.
	 * @param numberOfSpring			Ratio of springs in the labyrinth depended of the number of way-elements.
	 */
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
	
	/**
	 * 
	 * The default-properties of the labyrinth will be set.
	 * 
	 * @param columnRowSize				Size of the labyrinth's side.
	 */
	public LevelGenration(int columnRowSize){
		
		this.columns= columnRowSize;
		this.rows=columnRowSize;
		int levelSize=rows*columns;
		
		setDefaultLevelParameters(levelSize);
		
	}
	
	/**
	 * 
	 * The default properties will be set and the real numbers of items will be calculated.
	 * 
	 * @param levelSize					Size of the whole labyrinth.
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
	 * The chosen properties of a labyrinth will be checked. If the properties are not possible, the
	 * default-settings will be used.
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
	 * 
	 * The labyrinth will be initialized and the creation will be started.
	 * After the ways are calculated the wall-models will be calculated. Then all items
	 * will be randomly set.
	 * 
	 * @return levelField[]				The labyrinth-array will be returned.
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
		//solveLabyrinth();
				
		return levelField;
	
	}
	
	/**
	 * 
	 * The ways will be generated. A way-element which was set before will be randomly chosen.
	 * This element is the connection to the new way-element. All neighbours of this element will
	 * be considered to get the possible new directions.
	 * The new directions will also be randomly chosen.
	 * 
	 * @param wayPointCount				Number of ways which are already set.
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
	 * 
	 * The neighbours of a point in the labyrinth will be calculated and also the
	 * availability will be considered.
	 * 
	 * @param seedPoint				The center of the neighbours.
	 * @return neighbours[]			The neighbours and the availability will be returned.
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
	 * 
	 * Calculates the best ways based on a waypoint so that the labyrinth wont be too easy. For each
	 * possible new direction the neighbours will be calculated. There should be as least as possible way-neighbours, 
	 * so the labyrinth will be more difficult. The possible new directions will be stored and one will
	 * be randomly chosen.
	 * 
	 * @param allWayNeighbours			All neighbours of the center-point.
	 * @return goodWayIndex				Returns the index of the new direction.
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
	 * 
	 * The items will be randomly set on the way-elements. Additional the way-element's index of some
	 * goodies will be stored. This is important for the map to find on specify item.
	 * 
	 * @param goodieMode			Specifies which item should be inserted.
	 * @param goodieCount			The counts of items which are already set.
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
	 * Solves the labyrinth and find a correct way. For each map the ways to all other items
	 * will be caclulated, but only the shortest way. Therefore all neighbours will be considered to
	 * find a correct possible way.
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
	 * 
	 * Calculates the shortest path from start to the target. For each possible way all neighbours will be considered.
	 * Also the number of steps will be considered and every way point is allowed to be visited only one time.
	 * The path, which has the lowest amount of steps, is the correct one.
	 * 
	 * @param startPoint				The start point.
	 * @param targetPoint				The target point which should be reached.
	 * @return waySequenze[]			The order of way-elements will be returned.
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
	 * 
	 * For each possible new direction the distance to the target-point will be calculated. The direction
	 * with the lowest distance will be chosen first to test if this one heads to the target.
	 * 
	 * @param possibleList			Contains all possible directions.
	 * @param targetPoint			The point which should be reached.
	 * @return bestNodeIndex		Returns the index of the best new direction.
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
	
	/**
	 * 
	 * Calculates the distance from a point to the target and additional the amount of steps which has been
	 * gone before will be considered to determine the probably costs.
	 * 
	 * @param wayPoint					The acutely point.
	 * @param targetFieldNumber			The target which should be reached.
	 * @return cost						The probably amount of steps to reach the target.
	 */
	public float getWayCost(int[] wayPoint, int targetFieldNumber) {

		int distX = Math.abs(wayPoint[0] % columns - targetFieldNumber
				% columns);
		int distY = Math.abs(wayPoint[0] / columns - targetFieldNumber
				/ columns);
		float costNode = (float) Math.sqrt(distX * distX + distY * distY);
		costNode = wayPoint[2] + costNode;
		return costNode;
	}
	
	/**
	 * 
	 * By the last way-element, which is the target itself, the whole path will be calculated.
	 * Therefore the parent of each way-point will be considered and the order of way-elements will
	 * be determined to reach the start-point.
	 * 
	 * @param lastWayPoint
	 * @return
	 */
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
	
	/**
	 * 
	 * The Index of the specify map will be calculated. This is only needed if all ways will
	 * be pre-calculated.
	 * 
	 * @param mapField			The actually point which is a map-element.
	 * @return index			Returns the index of the specify map.
	 */
	public int getMapIndex(int mapField) {

		for (int i = 0; i < mapCount; i++) {
			if (mapField == mapWayIndex[i])
				return i;
		}

		// Error
		return -1;
	}
	
	/**
	 * 
	 * The Index of the specify item will be calculated. This is only needed if all ways will
	 * be pre-calculated.
	 * 
	 * @param goodyField			The actually point which is a goody-element.
	 * @return index				Returns the index of the specify item.
	 */
	public int getGoodiesIndex(int goodyField) {

		for (int i = 0; i < numberOfGoodies; i++) {
			if (goodyField == goodiesIndex[i])
				return i;
		}

		// Error
		return -1;
	}
	
	/**
	 * 
	 * This function is only available if all ways are pre-calculated. The way from a start-point to
	 * a target will be returned.
	 * 
	 * @param startField			
	 * @param targetField
	 * @return waySequenze
	 */
	public int[] getWayToTarget(int startField, int targetField){
		int[] wayToTarget = mapInfo[getMapIndex(startField)][getGoodiesIndex(targetField)];
		return wayToTarget;
	}

	/**
	 * 
	 * This function is only available if all ways are pre-calculated. The way from a start-point with least 
	 * amount of steps will be returned.
	 * 
	 * @param startField			
	 * @return waySequenze
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
	 * 
	 * This function is only available if all ways are pre-calculated. The way from a start-point with most 
	 * amount of steps will be returned.
	 * 
	 * @param startField			
	 * @return waySequenze
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
	 * 
	 * Get the wall-models-information of the whole labyrinth.
	 * 
	 * @return walls				Returns all information about wall-model, rotation etc.
	 */
	public ArrayList<int[]> getwallInfo(){
		return walls;
	}
		
	/**
	 * 
	 *Get the start position of the whole labyrinth.
	 *
	 *@return startPosition 		The coordinates of the starting-point in the labyrinth.
	 */
	public Vector2i getStartPosition(){
		
		
		return new Vector2i(createdWays[0]%columns,createdWays[0]/columns);
	}
	
	/**#
	 * 
	 * Get the field-numbers where all positive goodies like barrel, stone and spring are placed.
	 * 
	 * @return goodiesIndex			The position where the goodies are placed will be returned.
	 */
	public List<Integer> getGoodiesPointsList(){
		List<Integer> goodiesList = new Vector<Integer>();
		
		for(int i=0;i<goodiesIndex.length;i++)
		{
			goodiesList.add(goodiesIndex[i]);
		}
		return goodiesList;
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
