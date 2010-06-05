package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;


/**
 * 
 * @author group13
 * 
 * class representing the drunk-bar
 *
 */
public class DrunkBar extends StatusBar{

	/**
	 * constructor inits members
	 * 
	 * @param objectWidth width of drunkbar
	 * @param objectHeight height of drunkbar
	 */
	public DrunkBar(float objectWidth, float objectHeight) {
		super(objectWidth, objectHeight);
	}


	/**
	 * @see StatusBar#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		super.draw(gl);
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