package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;

/**
 * 
 * @author group13
 * 
 * status bar for drunken beer
 *
 */
public class BeerStatusBar extends StatusBar {

	/**
	 * default constructor calls super, sets size and scale
	 */
	public BeerStatusBar() {
		super(GameObject.BLOCKSIZE, GameObject.BLOCKSIZE);
		this.setTexture(TextureSingletons.getTexture(BeerObject.class.getSimpleName()));
		this.scaleFactor = 1.0f;
	}

	/**
	 * @see super{@link #draw(GL10)}
	 */
	@Override
	public void draw(GL10 gl) {
		//draw beers
		for(int i = 0; i < GameControl.getInstance().getConsumedBeer(); i++) {
			this.position.x = i * GameObject.BLOCKSIZE;
			super.draw(gl);
		}
		
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
