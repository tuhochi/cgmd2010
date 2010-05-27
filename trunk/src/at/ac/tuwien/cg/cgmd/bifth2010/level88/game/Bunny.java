package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * Class representing the bunny
 * @author Asperger, Radax
 */
public class Bunny {
	/**
	 * Game instance of the level
	 */
	private Game game;
	/**
	 * Current and previous position of the bunny.
	 * This position are logical coordinates on the map.
	 */
	public int currentPosX, currentPosY, prevPosX, prevPosY;
	/**
	 * Current transition of the bunny (transition between two
	 * field of the map).
	 */
	public float transition;
	/**
	 * Time it takes the bunny to move from one field of the
	 * map to another.
	 */
	public float transitionTime;
	/**
	 * True if the bunny just finished transition between two cells.
	 * Used to avoid 'jumping' of the bunny when bunny reaches a
	 * t-junction or a dead end.
	 */
	public boolean stopTransition;
	/**
	 * Position of the bunny. This position is
	 * used to translate the bunny for rendering (so
	 * it is displayed at the right position).
	 */
	public float translateX, translateY;
	/**
	 * Remaining time of the speed power up
	 */
	private float powerUp_remainingTime_speed;
	
	/**
	 * Moving possibilities of the bunny
	 * @author Asperger, Radax
	 */
	public static final int STANDING = 0;
	public static final int MOVE_LEFT = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int MOVE_UP = 3;
	public static final int MOVE_DOWN = 4;
	public int moveStatus;
	
	/**
	 * Constructor
	 * @param _game Game context
	 */
	public Bunny(Game _game) {
		game = _game;
		currentPosX = 0;
		currentPosY = 0;
		translateX = 0;
		translateY = 0;
		moveStatus = STANDING;
		powerUp_remainingTime_speed = 0;
		
		transitionTime = 0.5f;
		transition = 0;
		stopTransition = false;
	}
	
	
	/**
	 * Update the bunny:
	 * - check if the user wants to change the movement direction of the bunny
	 * - auto-turn at corners
	 * - move bunny forward
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
		if( powerUp_remainingTime_speed > 0 )
		{
			transition += elapsedSeconds * 1.5f;
			powerUp_remainingTime_speed -= elapsedSeconds;
		}
		else
		{
			transition += elapsedSeconds;
		}
		while( transition >= transitionTime )
		{
			transition -= transitionTime;
			stopTransition = true;

			/*
			 * If there is a new user input
			 */
	        if( game.hasNewInput() ) {
	        	/*
	        	 * Calculate a vector that points to the touch position (relativ to the center
	        	 * of the screen).
	        	 */
	           	Vector2 pos = new Vector2(game.touchPosition);
	           	pos.mult(2);
	           	pos.add(new Vector2(-1,-1));
	           	pos.x *= game.screenWidth;
	           	pos.y *= game.screenHeight;
	
	           	/*
	           	 * Calculate angle of the touch vector, the base vectors. And calculate the
	           	 * distance of the touch to the center of the screen. 
	           	 */
	           	float angle = pos.getAngle();
	           	float angleX = game.map.groundXDir.getAngle();
	           	float angleY = game.map.groundYDir.getAngle();
	           	float distance  = pos.length();
	           	
	           	/*
	           	 * Check where the user has touched the screen and what he wanted to do
	           	 */
	           	if( distance < 50 ) {
	           		moveStatus = STANDING;
	           	}
	           	else if( Math.abs(angle-angleX) < 30 ) {
	           		moveStatus = MOVE_RIGHT;
	           	}
	           	else if( Math.abs(180+angle-angleX) < 30 ) {
	           		moveStatus = MOVE_LEFT;
	           	}
	           	else if( Math.abs(angle-angleY) < 30 ) {
	           		moveStatus = MOVE_UP;
	           	}
	           	else if( Math.abs(180+angle-angleY) < 30 ) {
	           		moveStatus = MOVE_DOWN;
	           	}
	        }
	        
	        /*
	         * If the bunny isn't standing
	         */
	        if( moveStatus != STANDING ) {
	        	int x=currentPosX, y=currentPosY, nx=0, ny=0;
	        	
	        	/*
				 * Calculate shift relative to the current position
				 */
	        	if( moveStatus == MOVE_LEFT )		nx--;
	        	else if( moveStatus == MOVE_RIGHT )	nx++;
	        	else if( moveStatus == MOVE_DOWN )	ny--;
	        	else if( moveStatus == MOVE_UP )	ny++;
	        	
	        	/*
	        	 * If the bunny can't move forward (no street there) and there are just two possible move directions
	        	 * available (one where we came from): search for the available direction
	        	 * This will make the bunny turn at corners.
	        	 */
	        	if( !game.map.cells[x+nx][y+ny].isStreetForBunny && game.map.cells[x][y].numStreetNeighboursBunny==2) {
	        		ArrayList<Integer> dirs = new ArrayList<Integer>();
	   				if( game.map.cells[currentPosX+1][currentPosY].isStreetForBunny && moveStatus!=MOVE_LEFT )
	   					dirs.add(MOVE_RIGHT);
	   				if( game.map.cells[currentPosX-1][currentPosY].isStreetForBunny && moveStatus!=MOVE_RIGHT )
	   					dirs.add(MOVE_LEFT);
	   				if( game.map.cells[currentPosX][currentPosY+1].isStreetForBunny && moveStatus!=MOVE_DOWN )
	   					dirs.add(MOVE_UP);
	   				if( game.map.cells[currentPosX][currentPosY-1].isStreetForBunny && moveStatus!=MOVE_UP )
	   					dirs.add(MOVE_DOWN);
	
	   				moveStatus = dirs.get(0);
	   				/*
	   				 * Calculate shift relative to the current position. It has to be done again since the move direction changed
	   				 */
	   				nx=ny=0;
	   	        	if( moveStatus == MOVE_LEFT )		nx--;
	   	        	else if( moveStatus == MOVE_RIGHT )	nx++;
	   	        	else if( moveStatus == MOVE_DOWN )	ny--;
	   	        	else if( moveStatus == MOVE_UP )	ny++;
	        	}
	
	        	/*
	        	 * If the bunny can move forward: move it
	        	 * The bunny might not move when he reaches a t-junction
	        	 */
	        	if( game.map.cells[x+nx][y+ny].isStreetForBunny ) {
	    			setPosition(x+nx, y+ny);
	    			stopTransition = false;
	    			
	    			if( game.map.cells[x+nx][y+ny].type=='g' )
	    			{
	    				powerUp_remainingTime_speed = 10.0f;
	    			}
	    		}
	        }
		}
		
		float t=1;
		if( !stopTransition ) t = transition/transitionTime;
		translateX =
			t * (currentPosX*game.map.groundXDir.x + currentPosY*game.map.groundYDir.x) + 
			(1-t) * (prevPosX*game.map.groundXDir.x + prevPosY*game.map.groundYDir.x);
		translateY =
			t * (currentPosX*game.map.groundXDir.y + currentPosY*game.map.groundYDir.y) + 
			(1-t) * (prevPosX*game.map.groundXDir.y + prevPosY*game.map.groundYDir.y);		
	}
	
	
	/**
	 * Draw the bunny
	 * @param gl OpenGL context of android
	 */
	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(translateX, translateY, 0);

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		
		gl.glPopMatrix();
	}
	
	
	/**
	 * Init the position of the bunny
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void initPosition(int x, int y) {
		prevPosX = x;
		prevPosY = y;
		currentPosX = x;
		currentPosY = y;
	}
	
	/**
	 * Set the position of the bunny
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setPosition(int x, int y) {
		prevPosX = currentPosX;
		prevPosY = currentPosY;
		currentPosX = x;
		currentPosY = y;
	}
}
