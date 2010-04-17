package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Bunny.MoveStatus;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * Class for the police
 * @author Asperger, Radax
 */
public class Police {
	private Game game;
	private int currentPosX, currentPosY;
	public float translateX, translateY;
	private Quad policeQuad;
	private boolean wasStuckLastFrame;

	private float waitingTime, maxWaitingTime;

	/**
	 * Moving possibilities of the police
	 * @author Asperger, Radax
	 */
	public enum MoveStatus {
		STANDING, MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN
	}
	public MoveStatus moveStatus;
	
	/**
	 * Constructor
	 * @param _game game context
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Police(Game _game, int x, int y) {
		game = _game;
		setPosition(x, y);

		moveStatus = MoveStatus.MOVE_LEFT;
		maxWaitingTime = 1.0f;
		waitingTime = 0;
		wasStuckLastFrame=false;
		
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
	 * Get the direction of the bunny 
	 * @return direction the bunny is currently heading to
	 */
	private MoveStatus getBunnyDir() {
		int bx = game.bunny.currentPosX;
		int by = game.bunny.currentPosY;
		
		if( bx!=currentPosX && by!=currentPosY ) return MoveStatus.STANDING;
		
		int x1, x2, y1, y2;
		x1 = currentPosX-1;
		x2 = currentPosX+1;
		y1 = currentPosY-1;
		y2 = currentPosY+1;
		while( game.map.cells[x1][currentPosY].isStreetForPolice ) x1--;
		while( game.map.cells[x2][currentPosY].isStreetForPolice ) x2++;
		while( game.map.cells[currentPosX][y1].isStreetForPolice ) y1--;
		while( game.map.cells[currentPosX][y2].isStreetForPolice ) y2++;
		
		if( bx <= x1 ) return MoveStatus.STANDING;
		else if( bx >= x2 ) return MoveStatus.STANDING;
		else if( by <= y1 ) return MoveStatus.STANDING;
		else if( by >= y2 ) return MoveStatus.STANDING;
		
		if( bx < currentPosX ) return MoveStatus.MOVE_LEFT;
		else if( bx > currentPosX ) return MoveStatus.MOVE_RIGHT;
		else if( by < currentPosY ) return MoveStatus.MOVE_DOWN;
		else /*if( by > currentPosY )*/ return MoveStatus.MOVE_UP;
	}
	
	/**
	 * Update the police 
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
		waitingTime += elapsedSeconds;
		if( waitingTime < maxWaitingTime ) return;
		waitingTime -= maxWaitingTime;
		
		if( game.bunny.currentPosX==currentPosX && game.bunny.currentPosY==currentPosY ) {
			game.policeCatchesBunny();
			return;
		}

		int mx=0, my=0; 
		MoveStatus newMoveDir = getBunnyDir();
		
		if( newMoveDir==MoveStatus.STANDING ) { // Hase nicht sichtbar
			ArrayList<MoveStatus> dirs = new ArrayList<MoveStatus>();
			if( game.map.cells[currentPosX][currentPosY].numStreetNeighboursPolice==1 || wasStuckLastFrame )
			{
				if( game.map.cells[currentPosX+1][currentPosY].isStreetForPolice )
					dirs.add(MoveStatus.MOVE_RIGHT);
				if( game.map.cells[currentPosX-1][currentPosY].isStreetForPolice )
					dirs.add(MoveStatus.MOVE_LEFT);
				if( game.map.cells[currentPosX][currentPosY+1].isStreetForPolice )
					dirs.add(MoveStatus.MOVE_UP);
				if( game.map.cells[currentPosX][currentPosY-1].isStreetForPolice )
					dirs.add(MoveStatus.MOVE_DOWN);
			}
			else {
				if( game.map.cells[currentPosX+1][currentPosY].isStreetForPolice && moveStatus!=MoveStatus.MOVE_LEFT )
					dirs.add(MoveStatus.MOVE_RIGHT);
				if( game.map.cells[currentPosX-1][currentPosY].isStreetForPolice && moveStatus!=MoveStatus.MOVE_RIGHT )
					dirs.add(MoveStatus.MOVE_LEFT);
				if( game.map.cells[currentPosX][currentPosY+1].isStreetForPolice && moveStatus!=MoveStatus.MOVE_DOWN )
					dirs.add(MoveStatus.MOVE_UP);
				if( game.map.cells[currentPosX][currentPosY-1].isStreetForPolice && moveStatus!=MoveStatus.MOVE_UP )
					dirs.add(MoveStatus.MOVE_DOWN);
			}

			if( dirs.size()==1 ) {
				newMoveDir = dirs.get(0);
			}
			else {
				Random r = new Random();
				newMoveDir = dirs.get(r.nextInt(dirs.size()));
			}
		}
		moveStatus = newMoveDir;

		if( newMoveDir!=MoveStatus.STANDING ) {
			if( newMoveDir==MoveStatus.MOVE_LEFT ) mx=-1;
			else if( newMoveDir==MoveStatus.MOVE_RIGHT ) mx=1;
			else if( newMoveDir==MoveStatus.MOVE_DOWN ) my=-1;
			else if( newMoveDir==MoveStatus.MOVE_UP ) my=1;
			
			if( !game.map.cells[currentPosX+mx][currentPosY+my].isPolicePresent ) {
				game.map.movePolice(currentPosX, currentPosY, currentPosX+mx, currentPosY+my);
				setPosition(currentPosX+mx, currentPosY+my);
				wasStuckLastFrame=false;
			}
			else {
				wasStuckLastFrame=true;
			}
		}
		
		if( game.bunny.currentPosX==currentPosX && game.bunny.currentPosY==currentPosY ) {
			game.policeCatchesBunny();
			return;
		}
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
		currentPosX = x;
		currentPosY = y;
		
		translateX = currentPosX*game.map.groundXDir.x + currentPosY*game.map.groundYDir.x;
		translateY = currentPosX*game.map.groundXDir.y + currentPosY*game.map.groundYDir.y;
	}
	
	/**
	 * Get the current position of the police
	 * @return the current position of the police
	 */
	public int[] getPosition(){
		int[] pos = {currentPosX, currentPosY};
		return pos; 
	}
	
	/**
	 * Get the waiting time of the police
	 * @return the waiting time of the police
	 */
	public float getWaitingTime(){
		return waitingTime;
	}
	
	/**
	 * Set the waiting time of the police after restoration
	 * @param wait waiting time of the police
	 */
	public void setWaitingTime(float wait){
		waitingTime = wait;
	}
	
	/**
	 * Method for restoring the movement
	 * @param move name of the movement
	 */
	public void setMovement(String move){
		
		if(move.equalsIgnoreCase("STANDING")){
			moveStatus = MoveStatus.STANDING;
		}
		else if(move.equalsIgnoreCase("MOVE_LEFT")){
			moveStatus = MoveStatus.MOVE_LEFT;
		}
		else if(move.equalsIgnoreCase("MOVE_RIGHT")){
			moveStatus = MoveStatus.MOVE_RIGHT;
		}
		else if(move.equalsIgnoreCase("MOVE_UP")){
			moveStatus = MoveStatus.MOVE_UP;
		}
		else if(move.equalsIgnoreCase("MOVE_DOWN")){
			moveStatus = MoveStatus.MOVE_DOWN;
		}
		else {
			moveStatus = MoveStatus.STANDING;
		}
	}
}

