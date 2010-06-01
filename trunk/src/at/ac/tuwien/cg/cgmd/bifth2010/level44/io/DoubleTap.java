package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * Wrapper around the Input Gesture DoubleTap
 * This gesture performs a flap with both wings
 * 
 * @author Matthias
 *
 */
public class DoubleTap implements InputGesture {
	/** the x-position of the tap */
	private float x;
	/** the y-position of the tap */
	private float y;
	
	/**
	 * creates a DoubleTap-Gesture
	 * @param x the x-position of the tap
	 * @param y the y-position of the tap
	 */
	public DoubleTap(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public float getEndX() {
		return x;
	}

	@Override
	public float getEndY() {
		return y;
	}

	@Override
	public float getStartX() {
		return x;
	}

	@Override
	public float getStartY() {
		return y;
	}

	/**
	 * A DoubleTap has the strength of a maximum Swipe
	 * Therefore the vertical movement of the rabbit will be maximal
	 */
	@Override
	public float getPower() {
		return Swipe.MAX_LENGTH;
	}

}
