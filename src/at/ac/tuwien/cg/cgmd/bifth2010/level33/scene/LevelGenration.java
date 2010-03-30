package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.ArrayList;
import java.util.Random;

public class LevelGenration {
	
	Random rg = new Random();
	int[] levelField;
	int[] createdWays;
	int columns=0;
	int rows=0;
	int wayOffset=0;
	double percentOfWay=0.0;
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
	}
	
	/**
	 * Creates a random Level-Matrix
	 * 
	 */
	int[] startCreation() {
		
		int levelSize=rows*columns;
		levelField = new int[levelSize];
		
		if(percentOfWay >0.50)
			percentOfWay=0.50;
		int numberOfWays =new Double(levelSize*percentOfWay).intValue();;
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
				levelField[allNeighbourIndex[newWayDirection]]=1;
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
			levelFieldIndex=createdWays[wayPointIndex];//TODO: BUG falls createdWays ==  [5, -1, -1, -1, -1, -1, -1, -1, -1, -1]
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
	 * Print the created labyrinth
	 */
	private void testCreation(){
		
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

}
