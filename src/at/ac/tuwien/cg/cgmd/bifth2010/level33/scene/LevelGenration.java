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
	
	//WallInfomation
	int numberOfWallTextures=4;
	ArrayList<int[]> wallField = new ArrayList<int[]>(4);
	
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
		
		//only Way
		for(int i=1; i<numberOfWays;i++)
		{
			creatWay(i);
		}
		
		//goodies
		for(int i=0; i<numberOfMaps;i++)
		{
			creatGoodiesWayPoints("Map");
		}
		
		for(int i=0; i<numberOfStone;i++)
		{
			creatGoodiesWayPoints("Stone");
		}
		
		for(int i=0; i<numberOfTrashes;i++)
		{
			creatGoodiesWayPoints("Trash");
		}
		
		for(int i=0; i<numberOfSpring;i++)
		{
			creatGoodiesWayPoints("Spring");
		}
		
		for(int i=0; i<numberOfBarrel;i++)
		{
			creatGoodiesWayPoints("Barrel");
		}
		
		//calculates wall-information
		wallGeneration();
		
		return levelField;
	
	}
	
	/**
	 * Creates random ways 
	 * 
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
	private void creatGoodiesWayPoints(String goodieMode)
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
		}else
		if(goodieMode.equals("Map"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_MAP;
		}else
		if(goodieMode.equals("Trash"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_TRASH;
		}else
		if(goodieMode.equals("Spring"))
		{
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_SPRING;
		}else
		if(goodieMode.equals("Barrel")){
			levelField[levelFieldIndex]=SceneGraph.GEOMETRY_BARREL;
		}
		createdWays[wayPointIndex]=-1;
	}
	
	/**
	 * The wall-ArrayList will be calculated, which defines how many corners a 
	 * wall-element has and it`s rotation
	 */
	public void wallGeneration(){
		
		//Init Walls
		int[] allNeighbourIndex;
		for(int i=0; i<levelField.length; i++)
		{
			int[] wallProperties = new int[4];
			
			if(levelField[i]==0)
			{
				//Wall
				wallProperties[0]=0;
				
				allNeighbourIndex = checkAllNeighbours(i);
				int[] specialCornerAndRotation=wallModelCalculation(i,allNeighbourIndex);
				wallProperties[1]=specialCornerAndRotation[0];
				wallProperties[2]=specialCornerAndRotation[1];
				
				wallProperties[3]=rg.nextInt(numberOfWallTextures);
				
				wallField.add(i, wallProperties);
			}
			else
			{
				//something else
				wallProperties[0]=-1;
				wallProperties[1]=-1;
				wallProperties[2]=-1;
				wallProperties[3]=-1;
				wallField.add(i, wallProperties);
			}
		}
		
	}
	
	/**
	 * Calculates the number of special-corners of a particular wall-element
	 */
	public int[] wallModelCalculation(int seedPoint, int[] allNeighbourIndex){
		
		/*
		 * wallProperties: [wall/noWall wallModel wallModelRatation wallModelTexture]
		 * wallModels:
		 * 				0	-	no special corner
		 * 				1	-	1 special corner
		 * 				2	- 	2 special corner
		 * 			   (5) 	-	2 special corner (diagonal special corner)
		 * 				3	-	3 special corner
		 * 				4	-	4 special corner
		 */
		
		int specialCorner =0;
		
		//Load all Neighbour-Values
		int[] tempLevelField = new int[8];
		for(int i=0; i<tempLevelField.length; i++)
		{
			tempLevelField[i]=levelField[allNeighbourIndex[i]];
		}
		
		//no special corner
		if(tempLevelField[1]==0 && tempLevelField[3]==0 && tempLevelField[5]==0 &&
		   tempLevelField[7]==0)
			specialCorner=0;
		else
		{
			int next=0;
			//some special corners
			for(int i=1; i<tempLevelField.length; i=i+2)
			{
				if(tempLevelField[i]!=0 )
				{
					if(i==7)
						next=0;
					else
						next=i+1;
					
					if(tempLevelField[i-1]!=0 && tempLevelField[next]!=0)
					{
						//Found special Corner
						specialCorner++;
						tempLevelField[i]=-1;
					}
				}
			}
			
			//Sonderfall bei 2 special corner abfragen
			if(specialCorner==2 && 
			((tempLevelField[1]==-1 && tempLevelField[5]==-1) || (tempLevelField[3]==-1 && tempLevelField[7]==-1)))
			{
				specialCorner=5;
			}
		}
		
		
		int rotation = wallModelRotation(tempLevelField,specialCorner);
		int[]specialCornerAndRotation = new int[2];
		specialCornerAndRotation[0]=specialCorner;
		specialCornerAndRotation[1]=rotation;
		return specialCornerAndRotation;
	}
	
	/**
	 * Calculates the rotation of a particular wall-model
	 */
	public int wallModelRotation(int[] tempLevelField, int specialCorner){
		
		/*
		 * 	180	x	270
		 * 	x	SP	x
		 * 	90	x	0
		 */
		
		int rotation=0;
		
		//no corners
		if(specialCorner==0)
			return rotation;
		
		//1 corner
		else if(specialCorner==1)
		{
			if(tempLevelField[1]==-1)
				return 270;
			else if(tempLevelField[3]==-1)
				return 0;
			else if(tempLevelField[5]==-1)
				return 90;
			else if(tempLevelField[7]==-1)
				return 180;
		}
		
		//2 corners
		else if(specialCorner==2)
		{
			if(tempLevelField[1]==-1 && tempLevelField[3]==-1)
				return 270;
			else if(tempLevelField[3]==-1 && tempLevelField[5]==-1)
				return 0;
			else if(tempLevelField[5]==-1 && tempLevelField[7]==-1)
				return 90;
			else if(tempLevelField[7]==-1 && tempLevelField[1]==-1)
				return 180;
		}
		
		//2 diagonal corners
		else if(specialCorner==5)
		{
			if(tempLevelField[3]==-1 && tempLevelField[7]==-1)
				return 0;
			else
				return 90;
		}
		
		//3 corners
		else if(specialCorner==3)
		{
			if(tempLevelField[1]==-1 && tempLevelField[3]==-1 && tempLevelField[5]==-1)
				return 270;
			else if(tempLevelField[3]==-1 && tempLevelField[5]==-1 && tempLevelField[7]==-1)
				return 0;
			else if(tempLevelField[5]==-1 && tempLevelField[7]==-1 && tempLevelField[1]==-1)
				return 90;
			else if(tempLevelField[7]==-1 && tempLevelField[1]==-1 && tempLevelField[3]==-1)
				return 180;
		}
		
		//4 corners
		else if(specialCorner==4)
		{
			return 0;
		}
		
		return rotation;
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
	 *Get the start position of the whole labyrinth
	 */
	public Vector2i getStartPosition(){
		
		
		return new Vector2i(createdWays[0]%columns,createdWays[0]/columns);
	}
	
	/**
	 * Get the wall-information about the whole level
	 */
	public ArrayList<int[]> getWallField(){
		
		/*
		 * wallProperties: [wall/noWall wallModel wallModelRatation wallModelTexture]
		 * 
		 * wall/noWall: 
		 * 				0	-	wall
		 * 			   -1	-	noWall (all other parameters are -1 too)
		 * 
		 * wallModels:
		 * 				0	-	no special corner
		 * 				1	-	1 special corner
		 * 				2	- 	2 special corner
		 * 			   (5) 	-	2 special corner (diagonal special corner)
		 * 				3	-	3 special corner
		 * 				4	-	4 special corner
		 * 
		 * rotation:
		 * 				0,90,180,270 degree
		 * 
		 * texture:
		 * 				0,1,2,3,4 (amount of textures can be changed, see constructor)
		 */
		return wallField;
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
		
		if(!all)
			return;
		System.out.println("------------------------");
		int[] test = new int[4];
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				test = wallField.get(10*i+j);
				System.out.print(" "+test[0]);
				//System.out.print(" "+(10*i+j));
			}
			System.out.print("\n");
		}
	}

}
