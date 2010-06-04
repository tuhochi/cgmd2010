package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import android.os.Bundle;

/**
 * 
 * @author group13
 * 
 * interface for objects which need to save persistent data
 *
 */
public interface IPersistence {
	/**
	 * saves persistent values into bundle
	 * 
	 * @param outState bundle in which values should be written
	 */
	public void save(Bundle outState);
	
	/**
	 * restores persistent values from bundle
	 * 
	 * @param savedInstanceState bundle from which values should be restored
	 */
	public void restore(Bundle savedInstanceState);
}


