package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;


/**
 * 
 * @author group13
 * 
 * class for background (= level map)
 *
 */
public class BackgroundObject extends GameObject {

	/** size of level map */
	private static final Vector2 MAPSIZE = new Vector2(1024, 1024);
	
	/**
	 * constructor calls super() with object's dimensions
	 */
	public BackgroundObject() {
		super(MAPSIZE.x, MAPSIZE.y);
	}
	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		//do nothing
	}
	
	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		//do nothing
	}
}