package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * Interface for all supported InputGestures
 * 
 * @author Matthias
 *
 */
public interface InputGesture {
	public float getStartX();
	public float getStartY();
	
	public float getEndX();
	public float getEndY();
	
	public float getStrength();
	
	/** enum to determine in which half of the screen the gesture happened */
	public enum ScreenHalf { LEFT, RIGHT }
}
