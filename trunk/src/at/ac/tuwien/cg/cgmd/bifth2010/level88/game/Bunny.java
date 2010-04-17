package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * Class representing the bunny
 * @author Asperger, Radax
 */
public class Bunny {
	private Game game;
	public int currentPosX, currentPosY;
	public float translateX, translateY;
	private Quad bunnyQuad;
	private Vector2 groundYDir, groundXDir;
	public float waitingTime, maxWaitingTime;

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
		
		maxWaitingTime = 0.75f;
		waitingTime = 0;

		float norm;
        groundYDir = new Vector2(-229, -169);
        norm = 1.0f / groundYDir.length();
        groundYDir.mult(norm);
        groundXDir = new Vector2(329, -131);
        groundXDir.mult(norm);

        Vector2 xDir = new Vector2(400, 0);
        xDir.mult(norm);
        Vector2 yDir = new Vector2(0, -400);
        yDir.mult(norm);

        Vector2 quadBase = new Vector2();
        quadBase.add(groundYDir);
        quadBase.add(groundXDir);
        quadBase.mult(-1.0f);
        quadBase.add(xDir);
        quadBase.add(yDir);
        quadBase.mult(-0.5f);
        quadBase.add(new Vector2(0, -80*norm));        

        bunnyQuad = new Quad(quadBase, xDir, yDir);
	}
	
	
	/**
	 * Update the bunny:
	 * - check if the user wants to change the movement direction of the bunny
	 * - auto-turn at corners
	 * - move bunny forward
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
		waitingTime += elapsedSeconds;
		if( waitingTime < maxWaitingTime ) return;
		waitingTime -= maxWaitingTime;
		
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
           	float angleX = groundXDir.getAngle();
           	float angleY = groundYDir.getAngle();
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
			 * Calculate shift relativ to the current position
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
        	if( !game.map.cells[x+nx][y+ny].isStreetForBunny && game.map.cells[x][y].numStreetNeighboursBunny==2 ) {
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
   				 * Calculate shift relativ to the current position. It has to be done again since the move direction changed
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
    		}
        }
	}
	
	
	/**
	 * Draw the bunny
	 * @param gl OpenGL context of android
	 */
	public void draw(GL10 gl) {
		bunnyQuad.vbos.set(gl);

		gl.glPushMatrix();
		gl.glTranslatef(translateX, translateY, 0);
		
		game.textures.bind(R.drawable.l88_bunny);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		
		gl.glPopMatrix();
	}
	
	
	/**
	 * Set the position of the bunny
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setPosition(int x, int y) {
		currentPosX = x;
		currentPosY = y;
		
		translateX = currentPosX*game.map.groundXDir.x + currentPosY*game.map.groundYDir.x;
		translateY = currentPosX*game.map.groundXDir.y + currentPosY*game.map.groundYDir.y;
	}
}
