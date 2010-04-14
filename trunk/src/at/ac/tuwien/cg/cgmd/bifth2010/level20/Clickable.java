package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public interface Clickable {
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	boolean hitTest(float x, float y);
}
