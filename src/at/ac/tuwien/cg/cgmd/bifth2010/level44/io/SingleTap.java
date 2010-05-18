package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * Wrapper around the Input Gesture DoubleTap
 * This gesture performs a flap with both wings
 * 
 * @author Matthias
 *
 */
public class SingleTap implements InputGesture {
	/** the x-position of the tap */
	private float x;
	/** the y-position of the tap */
	private float y;
	
	public SingleTap(float x, float y) {
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

	@Override
	/**
	 * A SingleTap has no strength
	 */
	public float getStrength() {
		return 0;
	}

}