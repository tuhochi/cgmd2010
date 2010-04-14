/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

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
	private int currentPosX, currentPosY;
	public float translateX, translateY;
	private Quad policeQuad;

	
	/**
	 * Constructor
	 * @param _game game context
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Police(Game _game, int x, int y) {
		game = _game;
		setPosition(x, y);

		
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
	 * Update the police 
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
    	// TODO
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
}
