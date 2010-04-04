package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import javax.microedition.khronos.opengles.GL10;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;

/**
 * Interface for a physical Object
 * 
 * A physical object is influenced by gravity, can move, draw itself and process input gestures on it
 * 
 * @author Matthias
 *
 */

public interface PhysicalObject {
	/** The Earth' Gravity */
	public static final float GRAVITY = 9.81f;
	
	/**
	 * Perform the movement of the physical object
	 * take into account gravity and possible input gestures on it
	 * 
	 * @param time the time since the last input gesture happened
	 */
	public void move(long time);
	/**
	 * process a input gesture on a physical object
	 * 
	 * @param gesture the input gesture (Swipe/Double Tap) to be processed
	 * @return true, if the processing has finished and therefore the time should be resetted
	 * 		   false otherwise
	 */
	public boolean processGesture(InputGesture gesture);
	/**
	 * Draw itself with OpenGL
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl);
	/**
	 * Set the position of the object
	 * 
	 * @param x the new x position
	 * @param y the new y position
	 */
	public void setPosition(float x, float y);
}
