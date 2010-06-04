package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;

/**
 * @author group13
 * 
 * class for beer blocks in drunk-bar
 *
 */
public class DrunkBarBlock extends GameObject {

	/**
	 * constructor calls super
	 * 
	 * @param objectWidth width of object
	 * @param objectHeight height of object
	 */
	public DrunkBarBlock(float objectWidth, float objectHeight) {
		super(objectWidth, objectHeight);
	}
	
	
	/**
	 * @see GameObject#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		//use absolute drawing
		this.position.add(GameObject.offset);
		super.draw(gl);
		this.position.sub(GameObject.offset);
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