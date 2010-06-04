package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import android.os.Bundle;

/**
 * 
 * @author group13
 * 
 * class representing the jail-bar
 *
 */
public class JailBar extends StatusBar{

	/**
	 * constructor calls super
	 * 
	 * @param objectWidth width of jail-bar
	 * @param objectHeight height of jail-bar
	 */
	public JailBar(float objectWidth, float objectHeight) {
		super(objectWidth, objectHeight);
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