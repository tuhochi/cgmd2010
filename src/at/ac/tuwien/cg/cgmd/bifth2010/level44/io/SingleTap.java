package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * Wrapper around the Input Gesture SingleTap
 * 
 * @author Matthias
 */
public class SingleTap implements InputGesture {
	/** the x-position of the tap */
	private float x;
	/** the y-position of the tap */
	private float y;

	/**
	 * creates a Single-Tap
	 * 
	 * @param x
	 *            x-pos
	 * @param y
	 *            y-pos
	 */
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
	public float getPower() {
		return 0;
	}

}
