package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * Wrapper around the Input Gesture DoubleTap
 * 
 * This gesture performs a flap with both wings
 * 
 * @author Matthias Tretter
 * 
 */
public class DoubleTap implements InputGesture {
	/** the x-position of the tap */
	private float x;
	/** the y-position of the tap */
	private float y;

	/**
	 * creates a DoubleTap-Gesture
	 * 
	 * @param x
	 *            the x-position of the tap
	 * @param y
	 *            the y-position of the tap
	 */
	public DoubleTap(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the x coordinate of the end touch point
	 * 
	 * @return The end X coordinate
	 */
	@Override
	public float getEndX() {
		return x;
	}
	
	/**
	 * Return the y coordinate of the end touch point
	 * 
	 * @return The end Y coordinate
	 */
	@Override
	public float getEndY() {
		return y;
	}

	/**
	 * Return the x coordinate of the start touch point
	 * 
	 * @return The start X coordinate
	 */
	@Override
	public float getStartX() {
		return x;
	}

	/**
	 * Return the y coordinate of the start touch point
	 * 
	 * @return The start Y coordinate
	 */
	@Override
	public float getStartY() {
		return y;
	}

	/**
	 * A DoubleTap has the strength of a maximum Swipe
	 * 
	 * The vertical movement of the rabbit will
	 * therefore be maximal.
	 * 
	 * @return The power value of this gesture
	 */
	@Override
	public float getPower() {
		return Swipe.MAX_LENGTH * .8f;
	}

}
