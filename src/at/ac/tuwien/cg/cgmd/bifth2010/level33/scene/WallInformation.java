package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.ArrayList;
import java.util.Random;

/**
 * All information which are needed to choose the right wall-element on each position
 * will be calculated. For each point all it's neighbours will be calculated and with
 * this information the right wall-model will be calculated. Also the rotation will
 * be calculated and if some additional short wall-elements are needed to close same
 * holes at corners.
 * 
 * 
 *DESCRIPTION
 * 
 * Array: worldField[5]
 * 
 * ***** WALL *****
 * 
 * [WALL-MODEL]					-	normalWall, wall with Corners, wall with edges
 * [ROTATION OF WALL-MODEL]		-	0,90,180,270  0 = right or right up
 * [CONNECTION-WALL-MODEL]		-	wall-elements on the corners
 * [ROTATION OF CONNECTION-WALL]-	0,90,180,270  0 = right or right up
 * [TEXTURE]					-	Random Texture-Index between 0 - 3
 * 
 * 
 * ***** WAY *****
 * 
 * [WAY-MODEL]					-	onlyWay, Stone, Trash, Barrel,...
 * [ALPHA-VALUE]				-	initialization with 100%
 * [Parent NEIGHBOUR]			-	the way-point before this one
 * [WAY-COST]					-	how many steps are gone
 * [TEXTURE]					-	Random Texture-Index between 0 - 3
 */
public class WallInformation {
	
	
	private Random rg = new Random();
	
	private int[] levelField = null;
	private int columns = 0;
	private int rows = 0;
	private ArrayList<int[]> worldField;
	
	public WallInformation(int[] levelField, int columns){
		this.levelField = levelField;
		this.columns=columns;
		this.rows=columns;
		this.worldField = new ArrayList<int[]>(levelField.length);
	}
	
	/**
	 * Each points in the level will be considered and if there is a wall-element
	 * the neighbours will be calculated to find the right wall-model.
	 * If the considered level-point is way-element, it will we be initialized to
	 * standard-way-element.
	 */
	public void calculatesWallInformation(){
		
		for(int i=0;i<levelField.length;i++)
		{
			int[] field = new int[5];
			if(levelField[i]==0)
			{
				int[] neighbours = calculateAllNeighbours(i);
				
				wallModelCalculation(neighbours, field);
				
				//Texture
				field[4]=wallModelTexture();
			}
			else
			{
				field[0]=levelField[i];
				//Alpha-Wert oder Zeit
				field[1]=0;
				//Parent-Init
				field[2]=-1;
				//WayCost-Init
				field[3]=0;
			}
			
			//Test
			//System.out.println("Element: "+i+" - "+field[0]+"  "+field[1]+"  "+field[2]+"  "+field[3]);
			
			//Add to WorldArray
			worldField.add(i, field);
		}
	}
	
	/**
	 * 
	 * For a wall-element the neighbours will be considered to calculate the 
	 * right wall-model. Then the rotation of this model will be calculated.
	 * 
	 * @param neighbours		Contains all neighbours about a specify point.
	 * @param field				Contains the wall-model, rotation, additional walls and texture
	 * 
	 * field[4]					[wall-model,rotation,additional walls,rotation,texture]
	 */
	private void wallModelCalculation(int[] neighbours,int[] field){
		
		//Check the main-neighbours
		boolean isOnlyWallAround = true;
		int numberOfSpecialEdges=0;
		for(int i=0;i<neighbours.length;i++)
		{
			if(neighbours[i]!=0)
				isOnlyWallAround=false;
			
			if(i%2==0 && neighbours[i]!=0)
			{
				//SpecialEdge found
				numberOfSpecialEdges++;
			}
		}
		
		if(isOnlyWallAround)
		{
			field[0]=SceneGraph.EDGE_NONE_SPECIAL_WALL;
			field[1]=0;
			field[2]=0;
			field[3]=0;
			return;
		}else
		if(numberOfSpecialEdges==1)
		{
			field[0]=SceneGraph.EDGE_ONE_SPECIAL_WALL;
		}
		else
		if(numberOfSpecialEdges==2 && ((neighbours[0]==0 && neighbours[4]==0) ||
									   (neighbours[2]==0 && neighbours[6]==0)))
		{
			field[0]=SceneGraph.EDGE_COUNTERPART_SPECIAL_WALL;
			numberOfSpecialEdges=5;
		}
		else
		if(numberOfSpecialEdges==2)
			field[0]=SceneGraph.EDGE_TWO_SPECIAL_WALL;
		else
		if(numberOfSpecialEdges==3)
			field[0]=SceneGraph.EDGE_THREE_SPECIAL_WALL;
		else
		if(numberOfSpecialEdges==4)
			field[0]=SceneGraph.EDGE_FOUR_SPECIAL_WALL;
		
		//Model-Rotation
		if(numberOfSpecialEdges!=0)
		{
			//Model-Rotation
			int modelRotation = wallModelRotation(numberOfSpecialEdges,neighbours);
			field[1]=modelRotation;
		}
		
		//Special-Corner
		wallCornersCalculation(neighbours, field);
		
		//SubModel
		wallSubModelCalculation(neighbours,field);
		
		
		
		
	}
	
	/**
	 * 
	 *  For a wall-element the neighbours will be considered to calculate to add corners
	 *  to the right wall-model if needed. Then the rotation will be the same as the wall-model.
	 *  If no wall-model was found, there will be only a corner. So the rotation will be calculated again.
	 * 
	 * @param neighbours		Contains all neighbours about a specify point.
	 * @param field				Contains the wall-model, rotation, additional walls and texture
	 * 
	 * field[4]					[wall-model,rotation,additional walls,rotation,texture]
	 */
	private void wallCornersCalculation(int[] neighbours,int[] field){
		
		int[] tempNeighbours = neighbours.clone();
		
		int numberOfSpecialCorners=0;
		int next = 0;
		int onlyOneCorner=0;
		for(int i=1;i<neighbours.length;i=i+2)
		{
			next=i+1;
			
			if(next>7)
				next=0;
			
			if(neighbours[i]!=0 && neighbours[i-1]==0 && neighbours[next]==0)
			{
				numberOfSpecialCorners++;
				onlyOneCorner=i;
				tempNeighbours[i]=-1;
			}
		}
		
		if( field[0] != 0 &&(numberOfSpecialCorners==0 || field[0]==SceneGraph.EDGE_THREE_SPECIAL_WALL ||
							 field[0]==SceneGraph.EDGE_FOUR_SPECIAL_WALL))
			return;
		
		//There is already a wall
		if(field[0]==SceneGraph.EDGE_ONE_SPECIAL_WALL)
		{
			if(numberOfSpecialCorners==2)
			{
				field[0]=SceneGraph.SPECIAL_ONE_EDGE_TWO_CORNER_WALL;
				return;
			}
			else
			{
				if(field[1]==0 && onlyOneCorner==7 ||
				   field[1]==90 && onlyOneCorner==5 ||
				   field[1]==180 && onlyOneCorner==3 ||
				   field[1]==270 && onlyOneCorner==1)
				{
					field[0]=SceneGraph.SPECIAL_ONE_EDGE_ONE_RIGHT_CORNER_WALL;
					return;
				}
				else
				{
					field[0]=SceneGraph.SPECIAL_ONE_EDGE_ONE_LEFT_CORNER_WALL;
					return;
				}		
			}	
		}
		else
		if(field[0]==SceneGraph.EDGE_TWO_SPECIAL_WALL)
		{
			field[0]=SceneGraph.SPECIAL_TWO_EDGE_ONE_CORNER_WALL;
			return;
		}
		
		
		if(numberOfSpecialCorners==1)
			field[0]=SceneGraph.CORNER_ONE_SPECIAL;
		else
		if(numberOfSpecialCorners==2)
		{
			//Diagonal-Corner
			if((tempNeighbours[1]==-1 && tempNeighbours[5]==-1) ||
			   (tempNeighbours[7]==-1 && tempNeighbours[3]==-1))
			{
				numberOfSpecialCorners=5;
				field[0]= SceneGraph.CORNER_COUNTERPART_SPECIAL;
			}
			else
				field[0]=SceneGraph.CORNER_TWO_SPECIAL;
		}
		else
		if(numberOfSpecialCorners==3)
			field[0]=SceneGraph.CORNER_THREE_SPECIAL;
		else
		if(numberOfSpecialCorners==4)
			field[0]=SceneGraph.CORNER_FOUR_SPECIAL;
		else
			System.err.println("Error-CornerCalculation.");
		
		//Rotation
		int rotation = cornerModelRotation(numberOfSpecialCorners, tempNeighbours);
		field[1]=rotation;
	}
	
	/**
	 * 
	 * The rotation of wall-element, which consists of some corners will be calculated. Therefore the
	 * neighbours will be considered.
	 * 
	 *  90	x	0
	 * 	x	SP	x
	 * 	180	x	270
	 * 
	 * @param numberOfCorners	The numbers of corners for one wall-element
	 * @param neighbours		Contains all neighbours about a specify point.
	 * @return rotation			The rotation will be returned.
	 */
	private int cornerModelRotation(int numberOfCorners,int[] neighbours){
		
		/*
		 * 	90	x	0
		 * 	x	SP	x
		 * 	180	x	270
		 */
		int rotation = 0;
		
		//1 corner
		if(numberOfCorners==1)
		{
			if(neighbours[1]==-1)
				return 0;
			else if(neighbours[7]==-1)
				return 90;
			else if(neighbours[5]==-1)
				return 180;
			else if(neighbours[3]==-1)
				return 270;
		}
		
		//2 edges
		else if(numberOfCorners==2)
		{
			if(neighbours[1]==-1 && neighbours[3]==-1)
				return 0;
			else if(neighbours[7]==-1 && neighbours[1]==-1)
				return 90;
			else if(neighbours[5]==-1 && neighbours[7]==-1)
				return 180;
			else if(neighbours[3]==-1 && neighbours[5]==-1)
				return 270;
		}
		
		//2 edges
		else if(numberOfCorners==5)
		{
			if(neighbours[1]==-1 && neighbours[5]==-1)
				return 0;
			else
				return 90;
		}
		
		//3 edges
		else if(numberOfCorners==3)
		{
			if(neighbours[1]==-1 && neighbours[3]==-1 && neighbours[5]==-1)
				return 0;
			else if(neighbours[7]==-1 && neighbours[1]==-1 && neighbours[3]==-1)
				return 90;
			else if(neighbours[5]==-1 && neighbours[7]==-1 && neighbours[1]==-1)
				return 180;
			else if(neighbours[3]==-1 && neighbours[5]==-1 && neighbours[7]==-1)
				return 270;
		}
		
		//4 edges
		else if(numberOfCorners==4)
		{
			return 0;
		}
		
		return rotation;
	}
	
	/**
	 * 
	 * All neighbours will be considered to check if additional walls are necessary.
	 * If additional walls are needed to close holes, the number of these will be
	 * calculated.
	 * 
	 * @param field				Contains the wall-model, rotation, additional walls and texture
	 * @param neighbours		Contains all neighbours about a specify point.
	 */
	private void wallSubModelCalculation(int[] neighbours,int[] field){
		
		int[] tempNeighbours = neighbours.clone();
		
		int numberOfSubModels=0;
		int next = 0;
		for(int i=1;i<neighbours.length;i=i+2)
		{
			next=i+1;
			
			if(next>7)
				next=0;
			
			if(neighbours[i]==0 && neighbours[i-1]!=0 && neighbours[next]!=0)
			{
				numberOfSubModels++;
				tempNeighbours[i]=-1;
			}
		}		
		
		if(numberOfSubModels==0)
		{
			field[2]=SceneGraph.CONNECTION_NONE_WALL;
			return;
		}	
		else
		if(numberOfSubModels==1)
			field[2]=SceneGraph.CONNECTION_ONE_WALL;
		else
		if(numberOfSubModels==2)
		{
			//Diagonal-Corner
			if((tempNeighbours[1]==-1 && tempNeighbours[5]==-1) ||
			   (tempNeighbours[7]==-1 && tempNeighbours[3]==-1))
			{
				numberOfSubModels=5;
				field[2]= SceneGraph.CONNECTION_COUNTERPART_WALL;
			}
			else
				field[2]=SceneGraph.CONNECTION_TWO_WALL;
		}
		else
		if(numberOfSubModels==3)
			field[2]=SceneGraph.CONNECTION_THREE_WALL;
		else
		if(numberOfSubModels==4)
			field[2]=SceneGraph.CONNECTION_FOUR_WALL;
		else
			System.err.println("Error-ConnectionWallCalculation.");
		
		//Rotation
		int rotation = cornerModelRotation(numberOfSubModels, tempNeighbours);
		field[3]=rotation;
		
	}

	/**
	 * 
	 * All neighbours will be considered to check how the specify wall-element
	 * has to rotate. 
	 * 
	 * 	x	90	x
	 * 	180	SP	0
	 * 	x  270	x
	 * 
	 * @param numberOfEdges		The number of additional walls.
	 * @param neighbours		Contains all neighbours about a specify point.
	 * @return	rotation		The rotation will be returned.		
	 */
	private int wallModelRotation(int numberOfEdges,int[] neighbours){
		
		/*
		 * 	x	90	x
		 * 	180	SP	0
		 * 	x	270	x
		 */
		int rotation = 0;
		
		//1 edge
		if(numberOfEdges==1)
		{
			if(neighbours[2]==1)
				return 0;
			else if(neighbours[0]==1)
				return 90;
			else if(neighbours[6]==1)
				return 180;
			else if(neighbours[4]==1)
				return 270;
		}
		
		//2 edges
		else if(numberOfEdges==2)
		{
			if(neighbours[2]==1 && neighbours[4]==1)
				return 0;
			else if(neighbours[0]==1 && neighbours[2]==1)
				return 90;
			else if(neighbours[6]==1 && neighbours[0]==1)
				return 180;
			else if(neighbours[4]==1 && neighbours[6]==1)
				return 270;
		}
		
		//2 edges
		else if(numberOfEdges==5)
		{
			if(neighbours[2]==1 && neighbours[6]==1)
				return 0;
			else
				return 90;
		}
		
		//3 edges
		else if(numberOfEdges==3)
		{
			if(neighbours[2]==1 && neighbours[4]==1 && neighbours[6]==1)
				return 0;
			else if(neighbours[0]==1 && neighbours[2]==1 && neighbours[4]==1)
				return 90;
			else if(neighbours[6]==1 && neighbours[0]==1 && neighbours[2]==1)
				return 180;
			else if(neighbours[4]==1 && neighbours[6]==1 && neighbours[0]==1)
				return 270;
		}
		
		//4 edges
		else if(numberOfEdges==4)
		{
			return 0;
		}
		
		return rotation;
	}
	
	/**
	 * 
	 * A random-number will be generated which represent the index of a texture-array.
	 * 
	 * @return textureIndex		Returns the texture-index.
	 */
	private int wallModelTexture(){
		
		int textureIndex =rg.nextInt(4);
		return textureIndex;
		
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
		int[] mainNeighbours = calculateMainNeighbours(seedPoint);
		
		allNeighbours[0]=levelField[mainNeighbours[0]];
		allNeighbours[2]=levelField[mainNeighbours[1]];
		allNeighbours[4]=levelField[mainNeighbours[2]];
		allNeighbours[6]=levelField[mainNeighbours[3]];
		
		//1
		if(mainNeighbours[1]-columns<0)
			allNeighbours[1]=levelField[mainNeighbours[1]+(rows-1)*columns];
		else
			allNeighbours[1]=levelField[mainNeighbours[1]-columns];
		
		//3
		if(mainNeighbours[1]+columns>columns*rows-1)
			allNeighbours[3]=levelField[mainNeighbours[1]-(rows-1)*columns];
		else
			allNeighbours[3]=levelField[mainNeighbours[1]+columns];

		
		//5
		if(mainNeighbours[3]+columns>columns*rows-1)
			allNeighbours[5]=levelField[mainNeighbours[3]-(rows-1)*columns];
		else
			allNeighbours[5]=levelField[mainNeighbours[3]+columns];
		
		//7
		if(mainNeighbours[3]-columns<0)
			allNeighbours[7]=levelField[mainNeighbours[3]+(rows-1)*columns];
		else
			allNeighbours[7]=levelField[mainNeighbours[3]-columns];				
		
		return allNeighbours;
	}
	
	/**
	 * 
	 * only the  neighbours-indexes of 4-connectivity will be calculated an the value 
	 * of each neighbour will be determined. 
	 * 
	 * 		0	
	 *	3	SP	1
	 * 		2	
	 * 
	 * 
	 * @param seedPoint			The point in the middle of the neighbours
	 * @return neighbours		Only main-neighbours-values will be returned.
	 */
	public int[] calculateMainNeighbours(int seedPoint){
		
		int[] mainNeighbours = new int[4];
		
		//0 up
		if(seedPoint-columns<0)
			mainNeighbours[0]=seedPoint+(rows-1)*columns;
		else
			mainNeighbours[0]=seedPoint-columns;
		
		//1 right
		if((seedPoint+1)%columns==0)
			mainNeighbours[1]=seedPoint-(columns-1);
		else
			mainNeighbours[1]=seedPoint+1;
		
		//2 down
		if(seedPoint+columns>columns*rows-1)
			mainNeighbours[2]=seedPoint-(rows-1)*columns;
		else
			mainNeighbours[2]=seedPoint+columns;
		
		//3 left
		if((seedPoint-1)%columns==columns-1 ||seedPoint==0)
			mainNeighbours[3]=seedPoint+(columns-1);
		else
			mainNeighbours[3]=seedPoint-1;
		
		return mainNeighbours;
	}
	
	public ArrayList<int[]> getNewWorldArray(){
		
		/*
		 * *************************************
		 * 
		 * DESCRIPTION
		 * 
		 * Array: worldField[5]
		 * 
		 * ***** WALL *****
		 * 
		 * [WALL-MODEL]					-	normalWall, wall with Corners, wall with edges
		 * [ROTATION OF WALL-MODEL]		-	0,90,180,270  0 = right or right up
		 * [CONNECTION-WALL-MODEL]		-	wall-elements on the corners
		 * [ROTATION OF CONNECTION-WALL]-	0,90,180,270  0 = right or right up
		 * [TEXTURE]					-	Random Texture-Index between 0 - 3
		 * 
		 * 
		 * ***** WAY *****
		 * 
		 * [WAY-MODEL]					-	onlyWay, Stone, Trash, Barrel,...
		 * [ALPHA-VALUE]				-	initialization with 100%
		 * [Parent NEIGHBOUR]			-	the waypoint before this one
		 * [WAY-COST]					-	how many steps are gone
		 * [TEXTURE]					-	Random Texture-Index between 0 - 3
		 */
		return worldField;
	}

}
