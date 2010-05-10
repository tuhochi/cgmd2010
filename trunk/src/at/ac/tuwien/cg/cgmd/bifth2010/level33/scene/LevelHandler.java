package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;

public class LevelHandler {

	int world[];
	Vector2i worldDim;
	Vector2f gameCharacterPosition; // actual Position
	Vector2f gameCharacterTargetPosition; // target Position
	boolean characterMoves = false; // if Character is moving in this moment
	float gameCharacterSpeed = 30;
	ArrayList<int[]> worldEntry;
	List<Integer> goodiesIndex;
	
	List<int[]> springChangeList = new Vector<int[]>();
	public static MapCalculationThread mapCalculationThread = null;
	boolean isFirstMap = true;
	int mapIsActiveTimer = 0;
	boolean mapIsActive = false;
	int[][] mapResult;
	boolean isMapThreadStarted = false;
	
	StopTimer t;

	Random rand = new Random();
	public boolean demomode = false;

	public LevelHandler() {
		generateLevel();
		gameCharacterTargetPosition = new Vector2f(gameCharacterPosition);
	}

	/**
	 * this method generate the Level
	 */
	private void generateLevel() {

		
		// HARDCODED WORLD
		
		LevelGenration levelGenration = new LevelGenration(44);
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
	gameCharacterPosition = new Vector2f(levelGenration.getStartPosition().x,levelGenration.getStartPosition().y);
		
	}

	public void updateLogic() {

		
		// START DEMO: random walk
		if(demomode)
		if (gameCharacterPosition.equals(gameCharacterTargetPosition)){
			
			boolean ok=false;
			while(!ok)
			{
				Vector2i to = new Vector2i(rand.nextInt(worldDim.x),rand.nextInt(worldDim.y));
				if(isDirectWayPossilbe(to))
				{

				gameCharacterTargetPosition.set(to.x,to.y);
				ok=true;
				}
			}
			
		}
		// END DEMO: random walk

		// update level
		// change spring to an other item
		updateSpringChanges();
		
		//check if it's necessary to rebuild the way-elements after shown way to a goody
		if(mapIsActive && mapIsActiveTimer<SceneGraph.timeInSeconds)
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
	 * The world will be updated
	 */
	private void updateLevelAfterStep() {
		
		int worldIndex = getWorldId(Math.round((gameCharacterPosition.x)), Math.round((gameCharacterPosition.y)));
		
//		int rows = new Float(gameCharacterPosition.y).intValue();
//		int columns = new Float(gameCharacterPosition.x).intValue();
//		Log.d("worldIndex1", String.valueOf(worldIndex));
//		worldIndex = worldDim.y*rows+columns;
//		Log.d("worldIndex2", String.valueOf(worldIndex));
//		
		int value = worldEntry.get(worldIndex)[0];
		
		if(value!=1)
		{
			if(LevelActivity.IS_MUSIC_ON)
			{
				if(value==SceneGraph.GEOMETRY_STONE)
				{
					//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_STONE);
					LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.STONE);
					
					//Update GoodiesList
					updateGoodiesList(worldIndex);
					
					//add to progress
					LevelActivity.progressHandler.collectStone();
				}
				else if(value==SceneGraph.GEOMETRY_BARREL)
				{
					//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_BARREL);
					LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.BARREL);
				
					//Update GoodiesList
					updateGoodiesList(worldIndex);
					
					//add to progress
					LevelActivity.progressHandler.collectBarrel();
				}
				else if(value==SceneGraph.GEOMETRY_TRASH)
				{
					//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_TRASH);
					//LevelActivity.vibrator.vibrate(120L);
					LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.TRASH);
				
					//add to progress
					LevelActivity.progressHandler.collectTrash();
				}
				else if(value==SceneGraph.GEOMETRY_SPRING)
				{
					//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_SPRING);
					LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.SPRING);
					
					//add to progress
					LevelActivity.progressHandler.collectSpring();
					
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
				}
				else if(value==SceneGraph.GEOMETRY_MAP)
				{
					//LevelActivity.soundHandler.playActivitySound(SoundHandler.ACTIVITY_MUSIC_MAP);
					LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.MAP);
				
//					if(isFirstMap)
//						isFirstMap = false;
//					else
//					{
						//start thread to calculate way to goody
						t = new StopTimer();
						

						mapCalculationThread = new MapCalculationThread(this, worldDim.x);
						mapCalculationThread.setStartProperties(worldIndex, goodiesIndex, worldEntry);
						mapCalculationThread.start();
						isMapThreadStarted = true;
//					}

				}
			}
			worldEntry.get(worldIndex)[0]=1;
			
			
			
			//AlphaBLENDING
			//TODO
		}
		//LevelActivity.soundHandler.releaseActivityAudioPlayer();
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
		t.logTime("Map-Thread fertig nach: ");
		
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
		float additionalTime = (float) (mapResult.length*0.8);
		mapIsActiveTimer= Math.round(SceneGraph.timeInSeconds+additionalTime);
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
			   actuallyPointValue==SceneGraph.ARROW_DOWN)
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
			desiredPoint.add(new Vector2i(length, 0));
		else
			desiredPoint.add(new Vector2i(0, length));

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
			float cx=((lastTouch.x-0.5f)*2);
			float cy=((lastTouch.y-0.5f)*2);
			
			// horizontal
			if(Math.abs(cx)>Math.abs(cy))
			{
				if(cx>0)
					steerCharacterTo(true, 1);
				else
					steerCharacterTo(true, -1);
				
			}
			// vertical
			else
			{
				if(cy>0)
					steerCharacterTo(false, 1);
				else
					steerCharacterTo(false, -1);
			}

			
			
		}

		
	}

}