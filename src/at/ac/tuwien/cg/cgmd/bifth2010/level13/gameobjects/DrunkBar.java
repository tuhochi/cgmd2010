package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;


/**
 * 
 * @author group13
 * 
 * class representing the drunk-bar
 *
 */
public class DrunkBar extends StatusBar{

	/** beer blocks used */
	private DrunkBarBlock[] beerBlocks;

	/**
	 * constructor inits members
	 * 
	 * @param objectWidth width of drunkbar
	 * @param objectHeight height of drunkbar
	 */
	public DrunkBar(float objectWidth, float objectHeight) {
		super(objectWidth, objectHeight);
		//create beer blocks
		beerBlocks = new DrunkBarBlock[GameControl.MAX_DRUNK_LEVEL];
		for(int i = 0; i < beerBlocks.length; i++){
			beerBlocks[i] = new DrunkBarBlock(objectWidth/GameControl.MAX_DRUNK_LEVEL,objectHeight);
			beerBlocks[i].position.x = i*objectWidth/GameControl.MAX_DRUNK_LEVEL;
		}
	}


	/**
	 * @see StatusBar#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		super.draw(gl);
		//also draw beer blocks
		for (int i = 0; i < GameControl.getInstance().getConsumedBeer();i++){
			beerBlocks[i].draw(gl);
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