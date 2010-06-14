package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * Interface for all supported InputGestures
 * 
 * @author Matthias Tretter
 * 
 */
public interface InputGesture {
	/**
	 * @return the starting x-position of the gesture
	 */
	public float getStartX();

	/**
	 * @return the starting y-position of the gesture
	 */
	public float getStartY();

	/**
	 * @return the ending x-position of the gesture
	 */
	public float getEndX();

	/**
	 * @return the ending y-position of the gesture
	 */
	public float getEndY();

	/**
	 * @return the computed power of the gesture
	 */
	public float getPower();

	/** enum to determine in which part of the screen the gesture happened */
	public enum Position {
		LEFT, MIDDLE, RIGHT
	}
}
