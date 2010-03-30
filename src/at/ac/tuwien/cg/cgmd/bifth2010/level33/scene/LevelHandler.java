package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.Random;

import android.provider.UserDictionary.Words;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;

public class LevelHandler {

	int world[];
	Vector2i worldDim;
	Vector2f gameCharacterPosition; // actual Position
	Vector2f gameCharacterTargetPosition; // target Position
	float gameCharacterSpeed = 30;

	byte surface;
	byte wall;
	Random rand = new Random();

	public LevelHandler() {
		generateLevel();
		gameCharacterTargetPosition = new Vector2f(gameCharacterPosition);
	}

	/**
	 * this method generate the Level
	 */
	private void generateLevel() {

		
		// HARDCODED WORLD
		worldDim = new Vector2i(5, 7);
		world = new int[worldDim.area()];
		int x = 0;
		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;
		world[x++] = 0;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 0;
		world[x++] = 0;		world[x++] = 1;		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;
		world[x++] = 0;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 0;
		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;		world[x++] = 1;		world[x++] = 0;
		world[x++] = 0;		world[x++] = 1;		world[x++] = 1;		world[x++] = 1;		world[x++] = 0;
		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;		world[x++] = 0;
		gameCharacterPosition = new Vector2f(1, 1);
		// HARDCODED WORLD END
		
		
		
	LevelGenration levelGenration = new LevelGenration(5, 0, 0.4,2 ,6 , 4,3 ,3 );
		
//		worldDim = new Vector2i(5, 5);
	int[] theworld = new int[worldDim.area()];
//	 theworld = levelGenration.startCreation();// TODO BUG
//		
		System.out.println(theworld.length);
//		
//		gameCharacterPosition = new Vector2f(1,1);
		
		
		
		
		
		
		
		
		
		// setup surface
		surface = SceneGraph.GEOMETRY_WAY;

		// setup wall
		wall = SceneGraph.GEOMETRY_WALL;
			
		
	}

	public void updateLogic() {

		
		// START DEMO: random walk
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

		// now update Character Position

		// if Target Position is the same end
		if (gameCharacterPosition.equals(gameCharacterTargetPosition))
			return;

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

		gameCharacterPosition.add(step.divide(1/SceneGraph.deltaTime));

		// if the step size is larger then to the target
		if(Math.abs(diff.x+diff.y)<Math.abs(step.x+step.y))
			gameCharacterPosition.set(gameCharacterTargetPosition);
	}

	
	public boolean isDirectWayPossilbe(Vector2i to){
		
		// test if "to" is in the world
		if(to.x<0||to.x>worldDim.x|| to.y<0||to.y>worldDim.y)
			return false;
		
		// if to is no wall!
		if(world[to.y*worldDim.x+to.x]==this.wall)
			return false;
		
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
					if(world[y*worldDim.x+i]==wall)
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
					if(world[i*worldDim.x+x]==wall)
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
	public void gameCharacterStepTo(boolean horizontal, int length) {
//
//		// is a strait way possible ? no wall in front of
////		 if(
////		 ){return}
//
//		// in x direction
//		if (horizontal)
//			gameCharacterTargetPosition.set(this.gameCharacterPosition.x
//					+ length, this.gameCharacterPosition.y);
//		// in y driection
//		else
//			gameCharacterTargetPosition.set(this.gameCharacterPosition.x,
//					this.gameCharacterPosition.y + length);

	}

}
