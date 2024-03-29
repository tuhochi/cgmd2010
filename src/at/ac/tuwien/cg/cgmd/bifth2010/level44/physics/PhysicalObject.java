package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;

/**
 * Interface for a physical Object
 * 
 * A physical object is influenced by gravity, can move, draw itself and process
 * input gestures on it
 * 
 * @author Matthias Tretter
 * 
 */

public interface PhysicalObject {
	/** The Earth' Gravity */
	public static final float GRAVITY = 9.81f;

	/**
	 * Sets the velocity of the physical object
	 * 
	 * @param v
	 *            the new velocity
	 */
	public void setVelocity(float v);

	/**
	 * returns the velocity of the object
	 * 
	 * @return the velocity
	 */
	public float getVelocity();

	/**
	 * Perform the movement of the physical object take into account gravity and
	 * possible input gestures on it
	 */
	public void move();

	/**
	 * process a input gesture on a physical object
	 * 
	 * @param gesture
	 *            the input gesture (Swipe/Double Tap) to be processed
	 */
	public void processGesture(InputGesture gesture);

	/**
	 * clear all stored inputs
	 */
	public void clearInputQueue();

	/**
	 * reset all movement-variables
	 */
	public void resetMovement();

	/**
	 * Draw itself with OpenGL
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl);

	/**
	 * Set the position of the object
	 * 
	 * @param x
	 *            the new x position
	 * @param y
	 *            the new y position
	 */
	public void setPosition(float x, float y);

	/**
	 * @return the x-position of the object
	 */
	public float getX();

	/**
	 * @return the y-position of the object
	 */
	public float getY();
}
