package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;


/**
 * 
 * This thread calculates the shortest path to a random target. The thread will be started when a map was picked up.
 * Then a random item will be chosen. Therefore the frustum will be considered because the target should be outer
 * the frustum if it is possible.
 * Then the path to this item will be calculated. All way-possibilities will be considered an the path which has the
 * lowest way-cost which we are looking for.
 *
 */
public class MapCalculationThread extends Thread {
	
	private int startPoint = 0;
	private List<Integer> goodiesIndex;
	private List<Integer> possibleMapTargets = new Vector<Integer>();
	private Random rand = new Random();
	private ArrayList<int[]> walls;
	private int columns = 0;
	private int rows = 0;
	private int[][] arrowOrder;
	private int[][] newArrowOrder;
	private LevelHandler level = null;
	
	public static boolean isThreadReady = false;
	private boolean done = true;
	
	public MapCalculationThread(LevelHandler level, int columnsRows){
		this.level = level;
		this.columns = columnsRows;
		this.rows = columnsRows;
		done = false;
		
	}
	
	/**
	 * 
	 * The actually level-properties will be set to find a target which is available.
	 * 
	 * @param startPoint				start-point of the way
	 * @param goodiesIndex				possible targets
	 * @param walls						the labyrinth itself
	 */
	public void setStartProperties(int startPoint,List<Integer> goodiesIndex,ArrayList<int[]> walls){
		this.startPoint = startPoint;
		this.goodiesIndex = goodiesIndex;
		this.walls = walls;
	}
	
	/**
	 * The thread which calculates the shortest path to a random target will be started. If the calculations
	 * are done, a boolean variable will be set. So the update-logic knows that there will be some changes.
	 */
	public void run(){
				
		int targetField = findMapTarget();
		int[] waySequenze = calculateShortestPath(startPoint, targetField);
		arrowOrder = new int[waySequenze.length][2];
		for(int i=0; i<waySequenze.length;i++)
		{
			if(waySequenze[i]!=targetField)
			{
				arrowOrder[i][0]=waySequenze[i];
				
				int[] neighbours = checkWayPossibility(waySequenze[i]);
				
				if(waySequenze[i+1]==neighbours[1])
					arrowOrder[i][1]= SceneGraph.ARROW_UP;
				else if(waySequenze[i+1]==neighbours[2])
					arrowOrder[i][1]= SceneGraph.ARROW_RIGHT;
				else if(waySequenze[i+1]==neighbours[3])
					arrowOrder[i][1]= SceneGraph.ARROW_DOWN;
				else if(waySequenze[i+1]==neighbours[4])
					arrowOrder[i][1]= SceneGraph.ARROW_LEFT;
			}
		}
		
		//add corner-arrows
		addCornerArrows();
		
		isThreadReady=true;
		
		while(!done){}
		
	}
	
	/**
	 * The array which contains all arrows in a row will be updated. This is necessary to find corners and
	 * these arrows will be updated to corner-arrows. For each way-element of the shortest path the parent-way-element
	 * will be considered. If the direction of the actually arrow is not equal to the parent-arrow, then there have to
	 * be a corner-arrow.
	 */
	private void addCornerArrows(){
		
		newArrowOrder = new int[arrowOrder.length-1][2];
		for(int i=0;i<arrowOrder.length && i+1<arrowOrder.length;i++)
		{
			if(i==0)
			{
				int[] mainNeighbours = checkWayPossibility(arrowOrder[i][0]);
				for(int j=1;j<mainNeighbours.length;j++)
				{
					if(mainNeighbours[j]==startPoint)
					{
						int[] startPointNeighbours = calculateAllNeighbours(mainNeighbours[j]);
						newArrowOrder[i][0]=arrowOrder[i][0];
						if(startPointNeighbours[1]==arrowOrder[i+1][0])
						{
							if(startPointNeighbours[0]==arrowOrder[i][0])
								newArrowOrder[i][1]=SceneGraph.ARROW_BOTTOM_TO_RIGHT;
							else
								newArrowOrder[i][1]=SceneGraph.ARROW_LEFT_TO_TOP;
						}	
						else if(startPointNeighbours[3]==arrowOrder[i+1][0])
						{
							if(startPointNeighbours[2]==arrowOrder[i][0])
								newArrowOrder[i][1]=SceneGraph.ARROW_LEFT_TO_BOTTOM;
							else
								newArrowOrder[i][1]=SceneGraph.ARROW_TOP_TO_RIGHT;
						}	
						else if(startPointNeighbours[5]==arrowOrder[i+1][0])
						{
							if(startPointNeighbours[4]==arrowOrder[i][0])
								newArrowOrder[i][1]=SceneGraph.ARROW_TOP_TO_LEFT;
							else
								newArrowOrder[i][1]=SceneGraph.ARROW_RIGHT_TO_BOTTOM;
						}
							
						else if(startPointNeighbours[7]==arrowOrder[i+1][0])
						{
							if(startPointNeighbours[6]==arrowOrder[i][0])
								newArrowOrder[i][1]=SceneGraph.ARROW_RIGHT_TO_TOP;
							else
								newArrowOrder[i][1]=SceneGraph.ARROW_BOTTOM_TO_LEFT;
						}	
						else
							newArrowOrder[i][1]=arrowOrder[i][1];
					}
				}
			}
			else
			{
				newArrowOrder[i][0]=arrowOrder[i][0];
				
				if(arrowOrder[i-1][1]==arrowOrder[i][1])
					newArrowOrder[i][1]=arrowOrder[i][1];
				else
				{
					if(arrowOrder[i][1]==SceneGraph.ARROW_LEFT)
					{
						if(arrowOrder[i-1][1]==SceneGraph.ARROW_DOWN)
							newArrowOrder[i][1]=SceneGraph.ARROW_TOP_TO_LEFT;
						else if(arrowOrder[i-1][1]==SceneGraph.ARROW_UP)
							newArrowOrder[i][1]=SceneGraph.ARROW_BOTTOM_TO_LEFT;
					}
					else if(arrowOrder[i][1]==SceneGraph.ARROW_RIGHT)
					{
						if(arrowOrder[i-1][1]==SceneGraph.ARROW_DOWN)
							newArrowOrder[i][1]=SceneGraph.ARROW_TOP_TO_RIGHT;
						else if(arrowOrder[i-1][1]==SceneGraph.ARROW_UP)
							newArrowOrder[i][1]=SceneGraph.ARROW_BOTTOM_TO_RIGHT;
					}
					else if(arrowOrder[i][1]==SceneGraph.ARROW_UP)
					{
						if(arrowOrder[i-1][1]==SceneGraph.ARROW_LEFT)
							newArrowOrder[i][1]=SceneGraph.ARROW_RIGHT_TO_TOP;
						else if(arrowOrder[i-1][1]==SceneGraph.ARROW_RIGHT)
							newArrowOrder[i][1]=SceneGraph.ARROW_LEFT_TO_TOP;
					}
					else if(arrowOrder[i][1]==SceneGraph.ARROW_DOWN)
					{
						if(arrowOrder[i-1][1]==SceneGraph.ARROW_LEFT)
							newArrowOrder[i][1]=SceneGraph.ARROW_RIGHT_TO_BOTTOM;
						else if(arrowOrder[i-1][1]==SceneGraph.ARROW_RIGHT)
							newArrowOrder[i][1]=SceneGraph.ARROW_LEFT_TO_BOTTOM;
					}
				}	
			}
		}
	}
	
	
	/**
	 * 
	 * Returns the result of the thread. Before the result will be returned the boolean-variable will be set false.
	 * After this the thread will be terminated.
	 * 
	 * @return	arrowOrder[][]			way-sequence and which arrows will be used
	 */
	public int[][] getMapResult(){
		done=true;
		isThreadReady=false;
		return newArrowOrder;
		//return arrowOrder;
	}
	
	/**
	 * Terminates the actually thread.
	 */
	public void terminateThread(){
		done=true;
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
	private int[] calculateShortestPath(int startPoint,int targetPoint) {

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
	private int findBestNode(ArrayList<int[]> possibleList,
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
	private float getWayCost(int[] wayPoint, int targetFieldNumber) {

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
	 * @return wayIndexSequence			contains the order of way-elements
	 */
	private int[] calculateShortesWayIndex(int[] lastWayPoint) {

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
				
		return wayNeighbourPreferences;
	}
	
	/**
	 * 
	 * All neighbours-indexes will be calculated an the value of each neighbour will be
	 * determined. 
	 * 
	 * 	7	0	1
	 *	6	SP	2
	 * 	5	4	3
	 * 
	 * 
	 * @param seedPoint			The point in the middle of the neighbours
	 * @return neighbours		All neighbours-values will be returned.
	 */
	public int[] calculateAllNeighbours(int seedPoint){
		
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
			allNeighbours[1]=mainNeighbours[2]+(rows-1)*columns;
		else
			allNeighbours[1]=mainNeighbours[2]-columns;
		
		//3
		if(mainNeighbours[2]+columns>columns*rows-1)
			allNeighbours[3]=mainNeighbours[2]-(rows-1)*columns;
		else
			allNeighbours[3]=mainNeighbours[2]+columns;

		
		//5
		if(mainNeighbours[4]+columns>columns*rows-1)
			allNeighbours[5]=mainNeighbours[4]-(rows-1)*columns;
		else
			allNeighbours[5]=mainNeighbours[4]+columns;
		
		//7
		if(mainNeighbours[4]-columns<0)
			allNeighbours[7]=mainNeighbours[4]+(rows-1)*columns;
		else
			allNeighbours[7]=mainNeighbours[4]-columns;				
		
		return allNeighbours;
	}
	
	/**
	 * 
	 * All available goodies will be considered to find a target. Also the frustum will be considered to
	 * find an item which is not in the frustum. If no such item exist a random item will be chosen.
	 * 
	 * @return target			returns the target which will be the destination
	 */
	private int findMapTarget() {
		
		boolean areGoodiesOuterFrustum = false;
		
		int positionX = startPoint%columns;
		int positionY = startPoint/columns;
		int frustumMinX=Math.round(positionX-SceneGraph.frustumDim.x);
		int frustumMinY=Math.round(positionY-SceneGraph.frustumDim.y);
		int frustumMaxX=frustumMinX+SceneGraph.frustumDim.x*2+1;
		int frustumMaxY=frustumMinY+SceneGraph.frustumDim.y*2+1;
		
		while (frustumMinX < 0 || frustumMinX > columns - 1 || frustumMinY < 0
				|| frustumMinY > columns - 1) {
			if (frustumMinX < 0)
				frustumMinX += columns;
			if (frustumMinY < 0)
				frustumMinY += columns;
			if (frustumMinX > columns - 1)
				frustumMinX -= columns;
			if (frustumMinY > columns - 1)
				frustumMinY -= columns;
		}
		
		while (frustumMaxX < 0 || frustumMaxX > columns - 1 || frustumMaxY < 0
				|| frustumMaxY > columns - 1) {
			if (frustumMaxX < 0)
				frustumMaxX += columns;
			if (frustumMaxY < 0)
				frustumMaxY += columns;
			if (frustumMaxX > columns - 1)
				frustumMaxX -= columns;
			if (frustumMaxY > columns - 1)
				frustumMaxY -= columns;
		}
		
		int goodyPosition = 0;
		int goodyPositionX = 0;
		int goodyPositionY = 0;
		
		for(int i=0;i<goodiesIndex.size();i++)
		{
			//check if it is outer the frustum

			goodyPosition = goodiesIndex.get(i);
			goodyPositionX = goodyPosition%columns;
			goodyPositionY = goodyPosition/columns;
			
			if(frustumMinX<frustumMaxX && frustumMinY<frustumMaxY)
			{
				if((goodyPositionX>frustumMaxX || goodyPositionX<frustumMinX) ||
				   (goodyPositionY>frustumMaxY || goodyPositionY<frustumMinY))
				{
					possibleMapTargets.add(goodyPosition);
					areGoodiesOuterFrustum = true;
				}
			}
			else if(frustumMinX>frustumMaxX && frustumMinY<frustumMaxY)
			{
				if((goodyPositionX>frustumMaxX && goodyPositionX<frustumMinX) ||
				   (goodyPositionY>frustumMaxY || goodyPositionY<frustumMinY))
				{
					possibleMapTargets.add(goodyPosition);
					areGoodiesOuterFrustum = true;
				}
			}
			else if(frustumMinX<frustumMaxX && frustumMinY>frustumMaxY)
			{
				if((goodyPositionX>frustumMaxX || goodyPositionX<frustumMinX) ||
				   (goodyPositionY>frustumMaxY && goodyPositionY<frustumMinY))
				{
					possibleMapTargets.add(goodyPosition);
					areGoodiesOuterFrustum = true;
				}
			}
			else if(frustumMinX>frustumMaxX && frustumMinY>frustumMaxY)
			{
				if((goodyPositionX>frustumMaxX && goodyPositionX<frustumMinX) &&
				   (goodyPositionY>frustumMaxY && goodyPositionY<frustumMinY))
				{
					possibleMapTargets.add(goodyPosition);
					areGoodiesOuterFrustum = true;
					
					//performance test
					//return goodyPosition;
				}
			}
			
			
		}
		
		if(!areGoodiesOuterFrustum)
		{
			return goodiesIndex.get(rand.nextInt(goodiesIndex.size()));
		}
		
		int target = possibleMapTargets.get(rand.nextInt(possibleMapTargets.size()));
		possibleMapTargets.clear();
		
		return target;
	}
}
