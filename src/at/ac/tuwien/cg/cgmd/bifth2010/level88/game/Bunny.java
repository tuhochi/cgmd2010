package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;
import javax.microedition.khronos.opengles.GL10;

/**
 * Class representing the bunny
 * @author Asperger, Radax
 */
public class Bunny {
	private Game game;
	private int currentPosX, currentPosY;
	public float translateX, translateY;
	private Quad bunnyQuad;
	private Vector2 groundYDir, groundXDir;
	private float waitingTime, maxWaitingTime;

	/**
	 * Moving possibilities of the bunny
	 * @author Asperger, Radax
	 */
	public enum MoveStatus {
		STANDING, MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN
	}
	private MoveStatus moveStatus;
	
	
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
		moveStatus = MoveStatus.STANDING;
		
		maxWaitingTime = 1.0f;
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
	 * Method to update the position of the bunny
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
		waitingTime += elapsedSeconds;
		if( waitingTime < maxWaitingTime ) return;
		waitingTime -= maxWaitingTime;
		
        if( game.hasNewInput() ) {
           	Vector2 pos = new Vector2(game.touchPosition);
           	pos.mult(2);
           	pos.add(new Vector2(-1,-1));
           	pos.x *= game.screenWidth;
           	pos.y *= game.screenHeight;

           	float angle = pos.getAngle();
           	float angleX = groundXDir.getAngle();
           	float angleY = groundYDir.getAngle();
           	float distance  = pos.length();
           	
           	if( distance < 50 ) {
           		moveStatus = MoveStatus.STANDING;
           		Log.d("Bunny-Update", "STANDING");
           	}
           	else if( Math.abs(angle-angleX) < 30 ) {
           		moveStatus = MoveStatus.MOVE_RIGHT;
           		Log.d("Bunny-Update", "MOVE_RIGHT");
           	}
           	else if( Math.abs(180+angle-angleX) < 30 ) {
           		moveStatus = MoveStatus.MOVE_LEFT;
           		Log.d("Bunny-Update", "MOVE_LEFT");
           	}
           	else if( Math.abs(angle-angleY) < 30 ) {
           		moveStatus = MoveStatus.MOVE_UP;
           		Log.d("Bunny-Update", "MOVE_UP");
           	}
           	else if( Math.abs(180+angle-angleY) < 30 ) {
           		moveStatus = MoveStatus.MOVE_DOWN;
           		Log.d("Bunny-Update", "MOVE_DOWN");
           	}
        }
        
        if( moveStatus != MoveStatus.STANDING ) {
        	int x=currentPosX, y=currentPosY, nx=0, ny=0;
        	if( moveStatus == MoveStatus.MOVE_LEFT ) {
        		nx--;
        	}
        	else if( moveStatus == MoveStatus.MOVE_RIGHT ) {
        		nx++;
        	}
        	else if( moveStatus == MoveStatus.MOVE_DOWN ) {
        		ny--;
        	}
        	else if( moveStatus == MoveStatus.MOVE_UP ) {
        		ny++;
        	}
        	if( game.map.cells[x+nx][y+ny].isStreetForBunny ) {
    			setPosition(x+nx, y+ny);
    		}
        	else {
        		moveStatus = MoveStatus.STANDING; // TODO: es wäre besser wenn der Hase automatisch um ecken biegt (falls vorhanden)
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
