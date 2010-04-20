package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * Class for the police
 * @author Asperger, Radax
 */
public class Police {
	private Game game;
	public int currentPosX, currentPosY, prevPosX, prevPosY;
	public float transition, transitionTime;
	public boolean stopTransition;
	public float translateX, translateY;
	private Quad policeQuad;
	private boolean wasStuckLastFrame;

	/**
	 * Moving possibilities of the police
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
	 * @param _game game context
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Police(Game _game, int x, int y) {
		game = _game;

		prevPosX = x;
		prevPosY = y;
		currentPosX = x;
		currentPosY = y;
		
		moveStatus = MOVE_LEFT;
		transitionTime = 0.75f;
		transition = 0;
		stopTransition=false;
		wasStuckLastFrame=false;

		updateTranslation();
		
		float norm;
        Vector2 groundYDir = new Vector2(-229, -169);
        norm = 1.0f / groundYDir.length();
        groundYDir.mult(norm);
        Vector2 groundXDir = new Vector2(329, -131);
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
        
        policeQuad = new Quad(quadBase, xDir, yDir);
	}
	
	
	/**
	 * Search for the direction where the police would have to
	 * move to catch the bunny. Returns STANDING if bunny not
	 * visible.
	 * @return direction the direction of the bunny
	 */
	private int getBunnyDir() {
		int bx = game.bunny.currentPosX;
		int by = game.bunny.currentPosY;
		
		/*
		 * Bunny must be in the same row or the same column
		 */
		if( bx!=currentPosX && by!=currentPosY ) return STANDING;
		
		/*
		 * Find the min/max x/y coordinates the police can see
		 */
		int x1, x2, y1, y2;
		x1 = currentPosX-1;
		x2 = currentPosX+1;
		y1 = currentPosY-1;
		y2 = currentPosY+1;
		while( game.map.cells[x1][currentPosY].isStreetForPolice ) x1--;
		while( game.map.cells[x2][currentPosY].isStreetForPolice ) x2++;
		while( game.map.cells[currentPosX][y1].isStreetForPolice ) y1--;
		while( game.map.cells[currentPosX][y2].isStreetForPolice ) y2++;
		
		/*
		 * If bunny is outside the min/max coordinates it can't be seen
		 */
		if( bx <= x1 ) return STANDING;
		else if( bx >= x2 ) return STANDING;
		else if( by <= y1 ) return STANDING;
		else if( by >= y2 ) return STANDING;
		
		/*
		 * With the previous tests we have figured out that the bunny must be visible
		 * Test where it is and return the direction.
		 */
		if( bx < currentPosX ) return MOVE_LEFT;
		else if( bx > currentPosX ) return MOVE_RIGHT;
		else if( by < currentPosY ) return MOVE_DOWN;
		else if( by > currentPosY ) return MOVE_UP;
		return STANDING;
	}
	
	/**
	 * Update the police (search for bunny and move the police)
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
		transition += elapsedSeconds;
		if( transition >= transitionTime )
		{
			transition -= transitionTime;
			stopTransition=true;
			
			/*
			 * If the bunny and the police are at the same place: bunny caught!
			 */
			if( game.bunny.currentPosX==currentPosX && game.bunny.currentPosY==currentPosY ) {
				//game.policeCatchesBunny();
				//return;
			}

			int mx=0, my=0;
			/*
			 * Search if bunny is visible
			 */
			int newMoveDir = getBunnyDir();
			
			/*
			 * If bunny is not visible
			 */
			if( newMoveDir==STANDING ) {
				ArrayList<Integer> dirs = new ArrayList<Integer>();
				/*
				 * If the current position the police is standing at is a blind end (or the police was stuck
				 * couldn't move in the last frame), find all possible ways the police could move to 
				 */
				if( game.map.cells[currentPosX][currentPosY].numStreetNeighboursPolice==1 || wasStuckLastFrame )
				{
					if( game.map.cells[currentPosX+1][currentPosY].isStreetForPolice )
						dirs.add(MOVE_RIGHT);
					if( game.map.cells[currentPosX-1][currentPosY].isStreetForPolice )
						dirs.add(MOVE_LEFT);
					if( game.map.cells[currentPosX][currentPosY+1].isStreetForPolice )
						dirs.add(MOVE_UP);
					if( game.map.cells[currentPosX][currentPosY-1].isStreetForPolice )
						dirs.add(MOVE_DOWN);
				}
				/*
				 * If the police isn't at a blind end, search for all possible move directions, except the one
				 * the police came from
				 */
				else {
					if( game.map.cells[currentPosX+1][currentPosY].isStreetForPolice && moveStatus!=MOVE_LEFT )
						dirs.add(MOVE_RIGHT);
					if( game.map.cells[currentPosX-1][currentPosY].isStreetForPolice && moveStatus!=MOVE_RIGHT )
						dirs.add(MOVE_LEFT);
					if( game.map.cells[currentPosX][currentPosY+1].isStreetForPolice && moveStatus!=MOVE_DOWN )
						dirs.add(MOVE_UP);
					if( game.map.cells[currentPosX][currentPosY-1].isStreetForPolice && moveStatus!=MOVE_UP )
						dirs.add(MOVE_DOWN);
				}
	
				/*
				 * Select a random direction from the collected directions.
				 * All of this directions are valid (so the next field is a street for the police).
				 */
				if( dirs.size()==1 ) {
					newMoveDir = dirs.get(0);
				}
				else {
					Random r = new Random();
					newMoveDir = dirs.get(r.nextInt(dirs.size()));
				}
			}
			moveStatus = newMoveDir;
	
			/*
			 * If the police isn't standing
			 */
			if( newMoveDir!=STANDING ) {
				/*
				 * Calculate shift relative to the current position
				 */
				if( newMoveDir==MOVE_LEFT ) mx=-1;
				else if( newMoveDir==MOVE_RIGHT ) mx=1;
				else if( newMoveDir==MOVE_DOWN ) my=-1;
				else if( newMoveDir==MOVE_UP ) my=1;
				
	
				/*
				 * If the police can move forward (so if there is no other police standing at
				 * the target position), move the police forward.
				 */
				if( !game.map.cells[currentPosX+mx][currentPosY+my].isPolicePresent ) {
					game.map.movePolice(currentPosX, currentPosY, currentPosX+mx, currentPosY+my);
					setPosition(currentPosX+mx, currentPosY+my);
					stopTransition=false;
					wasStuckLastFrame=false;
				}
				/*
				 * If a police is standing at the target position: don't move this police but
				 * mark it as stuck
				 */
				else {
					wasStuckLastFrame=true;
				}
			}
			
			/*
			 * If the bunny and the police are at the same place: bunny caught!
			 * This has to be tested again. The first test checks for the case that the bunny moved onto
			 * the position of the police, while this case checks for the case the police moved onto
			 * the position of the bunny.
			 */
			if( game.bunny.currentPosX==currentPosX && game.bunny.currentPosY==currentPosY ) {
				//game.policeCatchesBunny();
				//return;
			}
		}
		updateTranslation();
	}
	
	/**
	 * Update translation vector
	 */
	private void updateTranslation() {
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
	 * Draw the police
	 * @param gl OpenGL context of android
	 */
	public void draw(GL10 gl) {
		policeQuad.vbos.set(gl);

		gl.glPushMatrix();
		gl.glTranslatef(translateX, translateY, 0);
		
		game.textures.bind(R.drawable.l88_police);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		
		gl.glPopMatrix();
	}
	
	/**
	 * Set the position of the police
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

