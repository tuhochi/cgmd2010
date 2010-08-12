/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Manages spawning and handling of obstacles in the game.
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 *
 */
/**
 * @author Pilzinho
 *
 */
public class ObstacleManager {
	/** The obstacle entity to spawn. */
	private RenderEntity obstacle;
	/** The x-Position the obstacles are spawned. */
	private float spawnPos;
	/** The number of spawned obstacles up to now. */
	private int nSpawnedObstacles;
	/** The number of successfully avoided obstacles. */
	private int nAvoidedObstacles;
	/** The minimum time span in milliseconds between obstacle spawning. */
	private float timeInterval;
	/** The next time an obstacle is spawned. */
	private float nextSpawnTime;
	/** Accumulates the time since the last obstacle vanished. */
	private float updateTime;
	/** The position where the player crashes into the obstacle. */
	protected float crashPosition;
	/** Flag whether the player crashed into the obstacle. */
	protected boolean crashed;
	/** The duration of the crash scenario. */
	private int crashDuration;

	/** Constructor of ObstacleManager. */
	public ObstacleManager() {		
		nSpawnedObstacles = 0;
		nAvoidedObstacles = 0;
		timeInterval = 10000;
		nextSpawnTime = timeInterval;
		updateTime = 0;		
		crashPosition = 100;
		crashed = false;
		crashDuration = LevelActivity.instance.getResources().getInteger(R.integer.l20_crash_duration) * 1000;
	}
	
	/** Initializes the manager. 
	 * @param gl */
	public void init(GL10 gl) {		
		createObstacle(gl);
	}
	
	/** 
	 * Does the necessary updating of the ObstacleManager.  
	 * @param dt 	 The passed time since the last update in milliseconds.
	 * @param scroll The distance to move the obstacle along the x axis.
	 * */
	public void update(float dt, float scroll) {		
		if (!obstacle.visible || crashed) {
			updateTime += dt;
		}			
		
		// Try spawning obstacle only when player is at the left most position
//		if (!crashed && updateTime >= nextSpawnTime) {
//			spawnObstacle();	
//					
//		}			
		
		if (crashed && updateTime > crashDuration) {
			removeObstacle();
			crashed = false;
		}
		
		// Obstacle spawned.
		if (obstacle.visible && !crashed) {
			// Scroll.
			obstacle.x -= scroll;
			// Crashing with player.
			if (obstacle.x-obstacle.width*0.5f < crashPosition) {
				crashed = true;
				updateTime = 0;
				EventManager.getInstance().dispatchEvent(EventManager.OBSTACLE_CRASH, obstacle);				
			}
		}
		
		// Obstacle vanished.
		if (obstacle.x < 0) {
			removeObstacle();
		}
		
		// Need some sort of animation that makes more sense.
		// Just disvisible it?
		if(crashed) {
			//obstacle.visible = false;
		}
	}

	
	/**
	 * Checks if an obstacle was clicked.
	 * 
	 * @param x The x-position of the click.
	 * @param y The y-position of the click.
	 */
	public void touchEvent(float x, float y) {
		if (crashed || !obstacle.visible) return;
		if (obstacle.hitTest(x, y)) {
			nAvoidedObstacles++;
			obstacle.clickable = false;		
			removeObstacle();
			EventManager.getInstance().dispatchEvent(EventManager.OBSTACLE_AVOIDED, obstacle);
		}	
	}
	
	/**
	 * Removes the obstacle from the screen.
	 */
	public void removeObstacle() {
		obstacle.visible = false;
		obstacle.x = spawnPos;
		updateTime = 0;
		nextSpawnTime = timeInterval;
	}

	/**
	 * Spawns the obstacle at the right side of the screen.
	 */
	protected void spawnObstacle() {
		obstacle.visible = true;	
		obstacle.clickable = true;
		nSpawnedObstacles++;
	}

	
	/**
	 * Creates the obstacle object by loading resources and initializing values.
	 * 
	 * @param gl The OpenGL Context for loading resources.
	 */
	public void createObstacle(GL10 gl) {		
		float size = LevelActivity.instance.getResources().getInteger(R.integer.l20_obstacle_size);
		// Round the float value.
		size = (int) (size * GameManager.screenRatio);		
		spawnPos = LevelActivity.renderView.getWidth() + size;	
		float posY = 65 * GameManager.screenRatio;
		obstacle = new RenderEntity(spawnPos, posY, 1, size, size);		
		obstacle.visible = false;		
		obstacle.texture = LevelActivity.renderView.getTexture(R.drawable.l20_obstacle, gl);		
	}
	
	/**
	 * Renders the visible obstacles.
	 * @param gl	The OpenGL Context used for rendering.
	 */
	public void render(GL10 gl) {
		obstacle.render(gl);	
	}
	
	/**
	 * Sets the current position where the player would crash into the obstacle.
	 * @param cart The right most shopping cart used to determine the crash position. 
	 */
	public void setCrashPosition(ShoppingCart cart) {
		crashPosition = cart.x + cart.width*0.5f - LevelActivity.renderView.getWidth()*0.1f;
	}
}
