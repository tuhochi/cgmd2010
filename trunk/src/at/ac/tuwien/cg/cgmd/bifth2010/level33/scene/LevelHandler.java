package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;

/**
 * This class handles all the logic-steps. After the level has been created every loop the time-based changes,
 * like "show the shortest path to an item" or if you picked up a spring and it changes to an other item, will
 * be updated.
 * The second path contains to update the character's position. Therefore the target position will be considered
 * and after checking if steering to this position is possible the movement will be calculated.
 * After each step the actually level-field will be considered. If there is an item, it will be picked up. For each
 * a sound will be played and the individual changes will be done.
 *
 */
public class LevelHandler {
	
	

	int world[];
	
	/** The level's size as a vector*/
	public static Vector2i worldDim;
	Vector2f gameCharacterPosition; // actual Position
	Vector2f gameCharacterTargetPosition; // target Position
	
	/** In which direction the character looks*/
	public static int characterRotaion=0;
	
	boolean characterMoves = false; // if Character is moving in this moment
	float gameCharacterSpeed = 30;
	
	/** The whole level as ArrayList. Contains all informations about walls and items*/
	public static ArrayList<int[]> worldEntry;
	
	/** Contains all field-numbers where the good items can be find in the level*/
	public static List<Integer> goodiesIndex;
	
	/** The character's field*/
	public static int gameCharacterField;
	
	List<int[]> springChangeList = new Vector<int[]>();
	
	/** The collected items, important for animation*/
	public static List<float[]> collectedItemList = new Vector<float[]>();
	
	/** The thread which calculates the shortest path to an item*/
	public static MapCalculationThread mapCalculationThread = null;
	
	/** True if the first map was collected */
	public static boolean isFirstMap = false;
	
	/** Time in seconds how long the shortest path will be shown */
	public static int mapIsActiveTimer = 0;
	
	/** True if the shortest way to an item will be shown*/
	public static boolean mapIsActive = false;
	int[][] mapResult;
	
	/** True if the thread which calculates the shortest way has been started*/
	public static boolean isMapThreadStarted = false;
	
	/** Number of items which are available*/
	public static int numberOfGoodGoodies=0;
	
	/** Number of collected maps*/
	public static int collectedMap=-1;
	String collectedMapText="";
	
	
	StopTimer t;

	Random rand = new Random();
	public boolean demomode = false;

	public LevelHandler() {
		generateLevel();
		gameCharacterTargetPosition = new Vector2f(gameCharacterPosition);
	}

	/**
	 * this method generate the Level and all level-parameter will be initialized
	 */
	private void generateLevel() {

		//reset all variables
		mapCalculationThread = null;
		isFirstMap = false;
		mapIsActiveTimer = 0;
		mapIsActive = false;
		isMapThreadStarted = false;
		numberOfGoodGoodies=0;
		collectedMap=-1;
		collectedMapText="";
		
		
		
		// HARDCODED WORLD
		
		//LevelGenration levelGenration = new LevelGenration(44);
		LevelGenration levelGenration = new LevelGenration(25, 3, 0.6, 5, 22, 8, 8, 6);
		worldDim = new Vector2i(levelGenration.rows,levelGenration.columns);
		
//		world = new int[worldDim.area()];
//		int x = 0;
//		world[x++] = 0;		world[x++] = 0;		world[x++] = 1;		world[x++] = 0;		world[x++] = 0;
//		world[x++] = 0;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 0;
//		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;
//		world[x++] = 0;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 0;
//		world[x++] = 0;		world[x++] = 0;		world[x++] = 1;		world[x++] = 0;		world[x++] = 0;
//		
//		gameCharacterPosition = new Vector2f(2, 1);
//		// HARDCODED WORLD END

		
		
	world = levelGenration.startCreation();
	worldEntry=levelGenration.getwallInfo();
	goodiesIndex = levelGenration.getGoodiesPointsList();
	numberOfGoodGoodies = goodiesIndex.size();
	gameCharacterPosition = new Vector2f(levelGenration.getStartPosition().x,levelGenration.getStartPosition().y);
	
		
	}
	
	/**
	 * This method describes the main-update-logic. First all time-based-changes like a collected spring
	 * will be updated.
	 * The second path contains to update the character's position. Therefore the target position will be considered
	 * and after checking if steering to this position is possible the movement will be calculated.
	 * After each step the actually level-field will be considered.
	 */
	public void updateLogic() {

		
//		// START DEMO: random walk
//		if(demomode)
//		if (gameCharacterPosition.equals(gameCharacterTargetPosition)){
//			
//			boolean ok=false;
//			while(!ok)
//			{
//				Vector2i to = new Vector2i(rand.nextInt(worldDim.x),rand.nextInt(worldDim.y));
//				if(isDirectWayPossilbe(to))
//				{
//
//				gameCharacterTargetPosition.set(to.x,to.y);
//				ok=true;
//				}
//			}
//			
//		}
		// END DEMO: random walk

		// update level
		// change spring to an other item
		updateSpringChanges();
		
		//check if it's necessary to rebuild the way-elements after shown way to a goody
		if(mapIsActive && mapIsActiveTimer<=0)
			reUpdateMapChanges();
		
		//check if map-trhead is ready or not
		if(isMapThreadStarted && MapCalculationThread.isThreadReady)
		{
			updateMapChanges();
			isMapThreadStarted=false;
		}
			

		// now update Character Position

		// if Target Position is the same end
		if (gameCharacterPosition.equals(gameCharacterTargetPosition)){
			
			//if(characterMoves)
				//updateLevelAfterStep();
			
			characterMoves=false;	
			return;
		}		
		
		// else it is moving
		characterMoves=true;

		// now change Position in small steps

		Vector2f diff = new Vector2f(gameCharacterTargetPosition);
		diff.subtract(gameCharacterPosition);

		Vector2f step = new Vector2f(0, 0);

		if (diff.x > 0)
			step.x = 1;
		else if (diff.x < 0)
			step.x = -1;

		if (diff.y > 0)
			step.y = 1;
		else if (diff.y < 0)
			step.y = -1;

//		Log.d("deltaTime",String.valueOf(SceneGraph.deltaTime));
//		Log.i("dif",String.valueOf(diff.x+" "));
		
		if(SceneGraph.deltaTime<0.2f)
			gameCharacterPosition.add(step.divide(0.2f/SceneGraph.deltaTime));
		else
			gameCharacterPosition.add(step);
		
		

		// if the step size is larger then to the target
		if(Math.abs(diff.x+diff.y)<Math.abs(step.x+step.y)){
			gameCharacterPosition.set(gameCharacterTargetPosition);
			characterMoves=false;
		}
		
		// modulo into real world
		Vector2f real= getRealWorldCoordinate(gameCharacterPosition);
		
		if(!real.equals(gameCharacterPosition)){
			gameCharacterPosition=real;
			gameCharacterTargetPosition.set(getRealWorldCoordinate(gameCharacterTargetPosition));
		}
		
		//update Level
		//if(gameCharacterPosition.x%1==0 && gameCharacterPosition.y%1==0)
			updateLevelAfterStep();
	}


	/**
	 * The world will be updated after a step. If there is an item on the field, it will collected. The individual 
	 * changes will be done, like playing sound, update the amount of gold, update the user interface-information
	 * and update the list of collected items for animation.
	 * 
	 */
	private void updateLevelAfterStep() {
		
		int worldIndex = getWorldId(Math.round((gameCharacterPosition.x)), Math.round((gameCharacterPosition.y)));
		gameCharacterField = worldIndex;
//		int rows = new Float(gameCharacterPosition.y).intValue();
//		int columns = new Float(gameCharacterPosition.x).intValue();
//		Log.d("worldIndex1", String.valueOf(worldIndex));
//		worldIndex = worldDim.y*rows+columns;
//		Log.d("worldIndex2", String.valueOf(worldIndex));
//		
		int value = worldEntry.get(worldIndex)[0];
		
		if(value!=1)
		{
			if(value==SceneGraph.GEOMETRY_STONE)
			{
				//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_STONE);
				LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.STONE);
				
				//Update GoodiesList
				updateGoodiesList(worldIndex);
				
				//add to progress
				LevelActivity.progressHandler.collectStone();
				
				LevelGenration.numberOfStone--;
				numberOfGoodGoodies--;
				
				//add to translateList
				float[] addCollectedItem = new float[]{worldIndex,SceneGraph.GEOMETRY_STONE,worldEntry.get(worldIndex)[1]};
				collectedItemList.add(addCollectedItem);
				
			}
			else if(value==SceneGraph.GEOMETRY_BARREL)
			{
				//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_BARREL);
				LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.BARREL);
			
				//Update GoodiesList
				updateGoodiesList(worldIndex);
				
				//add to progress
				LevelActivity.progressHandler.collectBarrel();
				
				LevelGenration.numberOfBarrel--;
				numberOfGoodGoodies--;
				
				//add to translateList
				float[] addCollectedItem = new float[]{worldIndex,SceneGraph.GEOMETRY_BARREL,worldEntry.get(worldIndex)[1]};
				collectedItemList.add(addCollectedItem);
			}
			else if(value==SceneGraph.GEOMETRY_TRASH)
			{
				//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_TRASH);
				//LevelActivity.vibrator.vibrate(120L);
				LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.TRASH);
			
				//add to progress
				LevelActivity.progressHandler.collectTrash();
				
				LevelGenration.numberOfTrashes--;
				
				//add to translateList
				float[] addCollectedItem = new float[]{worldIndex,SceneGraph.GEOMETRY_TRASH,worldEntry.get(worldIndex)[1]};
				collectedItemList.add(addCollectedItem);
			}
			else if(value==SceneGraph.GEOMETRY_SPRING)
			{
				//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_SPRING);
				LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.SPRING);
				
				//add to progress
				LevelActivity.progressHandler.collectSpring();
				
				LevelGenration.numberOfSpring--;
				numberOfGoodGoodies--;
				
				//Update GoodiesList
				updateGoodiesList(worldIndex);
				
				//Add to springList
				int newGoody = rand.nextInt(3);
				if(newGoody==0)
					newGoody=SceneGraph.GEOMETRY_STONE;
				else if(newGoody==1)
					newGoody=SceneGraph.GEOMETRY_BARREL;
				else if(newGoody==2)
					newGoody=SceneGraph.GEOMETRY_TRASH;
				int[] addSpring = new int[]{worldIndex,(int)SceneGraph.timeInSeconds+3,newGoody};
				springChangeList.add(addSpring);
				
				//add to translateList
				float[] addCollectedItem = new float[]{worldIndex,SceneGraph.GEOMETRY_SPRING,worldEntry.get(worldIndex)[1]};
				collectedItemList.add(addCollectedItem);
			}
			else if(value==SceneGraph.GEOMETRY_MAP)
			{
				//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_MAP);
				LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.MAP);
				
				//add to translateList
				float[] addCollectedItem = new float[]{worldIndex,SceneGraph.GEOMETRY_MAP,worldEntry.get(worldIndex)[1]};
				collectedItemList.add(addCollectedItem);
				
				LevelGenration.numberOfMaps--;
				if(!isFirstMap)
					isFirstMap = true;
				else
				{
					collectedMap++;
					
					if(collectedMap<10)
						collectedMapText=" x 0"+collectedMap;
					else
						collectedMapText=" x "+collectedMap;
					
					if(collectedMap==1)
					{
						//SceneGraph.ibPathButton.setVisibility(ImageButton.VISIBLE);
						//SceneGraph.tvPathCount.setVisibility(TextView.VISIBLE);
						SceneGraph.activity.runOnUiThread(new Runnable() {public void run() {
							SceneGraph.ibPathButton.setVisibility(ImageButton.VISIBLE);
							SceneGraph.tvPathCount.setVisibility(TextView.VISIBLE);	
							SceneGraph.tvPathCount.setText(collectedMapText);
						}});
					}
					else
					{
						SceneGraph.activity.runOnUiThread(new Runnable() {public void run() {
							SceneGraph.tvPathCount.setText(collectedMapText);
						}});
					}
				}

			}
			worldEntry.get(worldIndex)[0]=1;
		}
	}
	
	/**
	 * The goodiesList contains all items which are available in the labyrinth. If an item 
	 * will be collected it will be removed from this list.
	 * @param fieldIndex
	 */
	private void updateGoodiesList(int fieldIndex){
		
		for(int i=0;i<goodiesIndex.size();i++)
		{
			if(goodiesIndex.get(i)==fieldIndex)
			{
				goodiesIndex.remove(i);
				return;
			}
		}	
	}
	
	/**
	 * In a list all collected springs will be stored until the springs changed to
	 * new items. For each spring the way-element and the time when the item should be
	 * visible will be considered. If the time is over the spring changes to the new
	 * item and the spring will be removed from the list. 
	 */
	private void updateSpringChanges(){
		
		if(springChangeList==null)
			return;
		for(int i=0; i<springChangeList.size();i++)
		{
			if(springChangeList.get(i)[1]<=SceneGraph.timeInSeconds)
			{
				int[] newGoody = springChangeList.get(i);
				worldEntry.get(newGoody[0])[0]=newGoody[2];
				if(newGoody[2]==SceneGraph.GEOMETRY_BARREL)
				{
					LevelGenration.numberOfBarrel++;
					goodiesIndex.add(newGoody[0]);
					numberOfGoodGoodies++;
				}
				else if(newGoody[2]==SceneGraph.GEOMETRY_STONE)
				{
					LevelGenration.numberOfStone++;
					goodiesIndex.add(newGoody[0]);
					numberOfGoodGoodies++;
				}
				else if(newGoody[2]==SceneGraph.GEOMETRY_TRASH)
				{
					LevelGenration.numberOfTrashes++;
				}
				springChangeList.remove(i);
			}
		}
	}
	
	/**
	 * The way to an item will be calculated by the thread. If the thread is
	 * ready the information about the way which should be displayed will be transfered.
	 * The thread terminates. Then the arrows which show the way will be added to the world.
	 */
	private void updateMapChanges(){
		//t.logTime("Map-Thread fertig nach: ");
		
		if(mapIsActive)
			reUpdateMapChanges();
		mapResult = mapCalculationThread.getMapResult();
		int[] pointChanges = new int[2];
		for(int i=0;i<mapResult.length;i++)
		{
			pointChanges = mapResult[i];
			if(worldEntry.get(pointChanges[0])[0]==SceneGraph.GEOMETRY_WAY)
			{
				worldEntry.get(pointChanges[0])[0]=pointChanges[1];
			}
		}
		
		//SetTime
		float additionalTime = (float) (mapResult.length*0.7);
		mapIsActiveTimer= Math.round(additionalTime);
		mapIsActive = true;
		
	}
	
	/**
	 * If it's time to reset the shortest way to a item, the arrows will be replaced
	 * with normal-way-elements. If you follow the arrows you will pick up them and so
	 * these way-elements wont be reseted.
	 */
	private void reUpdateMapChanges(){
		
		
		int[] pointChanges = new int[2];
		for(int i=0;i<mapResult.length;i++)
		{
			pointChanges = mapResult[i];
			int actuallyPointValue = worldEntry.get(pointChanges[0])[0];
			if(actuallyPointValue==SceneGraph.ARROW_LEFT  || 
			   actuallyPointValue==SceneGraph.ARROW_UP    ||
			   actuallyPointValue==SceneGraph.ARROW_RIGHT ||
			   actuallyPointValue==SceneGraph.ARROW_DOWN  ||
			   actuallyPointValue==SceneGraph.ARROW_BOTTOM_TO_LEFT ||
			   actuallyPointValue==SceneGraph.ARROW_BOTTOM_TO_RIGHT ||
			   actuallyPointValue==SceneGraph.ARROW_LEFT_TO_BOTTOM ||
			   actuallyPointValue==SceneGraph.ARROW_LEFT_TO_TOP ||
			   actuallyPointValue==SceneGraph.ARROW_RIGHT_TO_BOTTOM ||
			   actuallyPointValue==SceneGraph.ARROW_RIGHT_TO_TOP ||
			   actuallyPointValue==SceneGraph.ARROW_TOP_TO_LEFT ||
			   actuallyPointValue==SceneGraph.ARROW_TOP_TO_RIGHT)
			{
				worldEntry.get(pointChanges[0])[0]=SceneGraph.GEOMETRY_WAY;
			}
		}
		mapIsActive = false;
			
	}


	/**
	 * 
	 * @param pos of the desired Entry
	 * @return the Entry
	 */
	public int[] getWorldEntry(Vector2i pos){		
		return worldEntry.get(getWorldId(pos));
	}
	/**
	 * look to getWorldEntry(Vector2i pos)
	 */
	public int[] getWorldEntry(int x, int y){
		return getWorldEntry(new Vector2i(x,y));
	}
	
	public int getWorldId(int x, int y){
		return getWorldId(new Vector2i(x,y));
	}
	
	private int getWorldId(Vector2i pos) {
		Vector2i real= getRealWorldCoordinate(pos);
		return	real.y*worldDim.x+real.x;
	}

	private Vector2i getRealWorldCoordinate(Vector2i pos) {
		Vector2f real= getRealWorldCoordinate(new Vector2f(pos.x, pos.y));
		return new Vector2i((int)real.x,(int)real.y);
	}
	
	public Vector2i getWorldCoordinate(int fieldNumber){
		Vector2i position = new Vector2i(fieldNumber%worldDim.x,fieldNumber/worldDim.x);
		return position;
	}
	
	/**
	 * 
	 * Checks if the field is in the frustum.
	 * 
	 * @param fieldNumber		the field-number which should be checked
	 * @param frustumMin		the min-border of the frustum
	 * @param frustumMax		the max-border of the frustum
	 * @return 					true if field is in frustum?
	 */
	public boolean isFieldInFrustum(int fieldNumber,Vector2i frustumMin, Vector2i frustumMax){
		
		Vector2i position = getWorldCoordinate(fieldNumber);
		
		if(frustumMin.x<frustumMax.x && frustumMin.y<frustumMax.y)
		{
			if((position.x>frustumMax.x || position.x<frustumMin.x) ||
			   (position.y>frustumMax.y || position.y<frustumMin.y))
					return false;
		}
		else if(frustumMin.x>frustumMax.x && frustumMin.y<frustumMax.y)
		{
			if((position.x>frustumMax.x && position.x<frustumMin.x) ||
			   (position.y>frustumMax.y || position.y<frustumMin.y))
					return false;
		}
		else if(frustumMin.x<frustumMax.x && frustumMin.y>frustumMax.y)
		{
			if((position.x>frustumMax.x || position.x<frustumMin.x) ||
			   (position.y>frustumMax.y && position.y<frustumMin.y))
					return false;
		}
		else if(frustumMin.x>frustumMax.x && frustumMin.y>frustumMax.y)
		{
			if((position.x>frustumMax.x && position.x<frustumMin.x) &&
			   (position.y>frustumMax.y && position.y<frustumMin.y))
					return false;
		}
	
		return true;
	}
	
	/**
	 * 
	 * @param to desired Point
	 * @return Point in Real World Coordinate
	 */
	public Vector2f getRealWorldCoordinate(Vector2f to){
		// toReal is the to Vector in the Real-World Coordinate System
		Vector2f toReal = new Vector2f(to);
		while ((int)toReal.x < 0 || (int)toReal.x > (int)worldDim.x - 1 || (int)toReal.y < 0
				|| toReal.y > worldDim.y - 1) {
			if (toReal.x < 0)
				toReal.x += worldDim.x;
			if (toReal.y < 0)
				toReal.y += worldDim.y;
			if (toReal.x > worldDim.x - 1)
				toReal.x -= worldDim.x;
			if (toReal.y > worldDim.y - 1)
				toReal.y -= worldDim.y;
		}
		return toReal;
		
	}
	
	
	/**
	 * control if a direct way, horizontal or vertical is possible
	 * @param to desired point
	 * @return true if possible
	 */
	public boolean isDirectWayPossilbe(Vector2i to){

		Vector2i toReal= getRealWorldCoordinate(to);
		
		
		Log.d("to entry:",""+world[toReal.y*worldDim.x+toReal.x]);
		Log.d("to entry wordEntry:",""+worldEntry.get(toReal.y*worldDim.x+toReal.x)[0]);

		// if toReal is no wall!
		if(worldEntry.get(toReal.y*worldDim.x+toReal.x)[0]<=SceneGraph.EDGE_NONE_SPECIAL_WALL)
			return false;

		
//		// test if "to" is in the mirror world
//		if(to.x<0||to.x>worldDim.x-1|| to.y<0||to.y>worldDim.y-1){
//			return false;
//		}
			
		//test if horizontal or vertical way is possible
		Vector2i diff = new Vector2i(to);
		diff.subtract(Math.round(gameCharacterPosition.x),Math.round(gameCharacterPosition.y));
				
		// if the way to "to" is only horizontal or vertical
		if(diff.x*diff.y==0){
			
			// horizontal
			if(Math.abs(diff.x)>0){
				int x =Math.round(gameCharacterPosition.x);
				int y =Math.round(gameCharacterPosition.y);
				int step = -1;
				if(diff.x>0)
					step=1;
				for(int i= x; i!=to.x;i=i+step)
					if(getWorldEntry(i,y)[0]<=SceneGraph.EDGE_NONE_SPECIAL_WALL)
						return false;
				return true;
					
			}
			// vertical
			else{
				int x =Math.round(gameCharacterPosition.x);
				int y =Math.round(gameCharacterPosition.y);
				int step = -1;
				if(diff.y>0)
					step=1;
				for(int i= y; i!=to.y;i=i+step)
					if(getWorldEntry(x,i)[0]<=SceneGraph.EDGE_NONE_SPECIAL_WALL)
						return false;
				return true;
			}
			
		}
		// if the way is diagonal
		else{
			// TODO: implement diagonal check method
		}
		
		return false;
	}
	/**
	 * this method steer the Caracter to the desired position
	 * @param horizontal, if true go horizontal (x-axis), else vertical (y-axis)
	 * @param length how long (length of the path) should he go
	 * @return if the way is possible
	 */
	public boolean steerCharacterTo(boolean horizontal, int length) {
		
		if(characterMoves)
			return false;
		
		Vector2i desiredPoint = new Vector2i(Math
				.round(gameCharacterPosition.x), Math
				.round(gameCharacterPosition.y));
		if (horizontal)
		{
			desiredPoint.add(new Vector2i(length, 0));
			if(length<0)
				characterRotaion=90;
			else
				characterRotaion=-90;
		}
		else
		{
			desiredPoint.add(new Vector2i(0, length));
			if(length<0)
				characterRotaion=0;
			else
				characterRotaion=180;
		}

		if (isDirectWayPossilbe(desiredPoint)) {
			gameCharacterTargetPosition.set(desiredPoint.x, desiredPoint.y);
			return true;
		}
		return false;
	}
	
	public void steerTouchEvent(Vector2f lastTouch) {
		
		// calculate Position 
		int x = (int) Math.round(lastTouch.x*SceneGraph.touchDim.x -SceneGraph.touchDim.x/2);
		int y = (int) Math.round(lastTouch.y*SceneGraph.touchDim.y -SceneGraph.touchDim.y/2);
		
		Vector2i to = new Vector2i(Math.round(x+gameCharacterPosition.x), Math.round(y+gameCharacterPosition.y));
		
		Log.w("lastouch= ",lastTouch.toString());
		Log.w("to=", to.toString());
		
//		if(1==1)
//		return;
		
		//if(false)
		if(isDirectWayPossilbe(to))
		{
			Log.d("direct","yes");
			Vector2i pos = new Vector2i(Math.round(gameCharacterPosition.x),Math.round(gameCharacterPosition.y));
			
			
			// if not horizontal
			if(pos.x==to.x)
				steerCharacterTo(false, to.y-pos.y);
			else
				steerCharacterTo(true, to.x-pos.x);
			
		}
		
		/// else split Screen in to 4 areas and go in this direction 
		///  \ /
		///   X
		///  / \
		else
		{
			float dx=((lastTouch.x-0.5f)*2);
			float dy=((lastTouch.y-0.5f)*2);
			
			Log.d("dx/dy",dx+" "+dy);
			
			boolean hasSteered = false;
			
			// horizontal
			if(Math.abs(dx)>Math.abs(dy))
			{
				if(dx>0)
					hasSteered = steerCharacterTo(true, 1);
				else
					hasSteered = steerCharacterTo(true, -1);
				
				if(!hasSteered&& !demomode)
				{
					if(dy>0)
						steerCharacterTo(false, 1);
					else
						steerCharacterTo(false, -1);
				}
				
			}
			// vertical
			else
			{
				if(dy>0)
					hasSteered = steerCharacterTo(false, 1);
				else
					hasSteered = steerCharacterTo(false, -1);
				
				if(!hasSteered&& !demomode)
				{
					if(dx>0)
						steerCharacterTo(true, 1);
					else
						steerCharacterTo(true, -1);
				}
			}

			
			
		}

		
	}

}