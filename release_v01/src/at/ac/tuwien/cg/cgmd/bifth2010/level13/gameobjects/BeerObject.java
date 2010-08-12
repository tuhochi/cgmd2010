package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;


/**
 * 
 * @author group13
 * 
 * class representing a beer object
 *
 */
public class BeerObject extends GameObject {

	/** id of beer */
	private String id;

	/**
	 * constructor calls super() with object's dimensions
	 * 
	 * @param x x-position of object
	 * @param y y-position of object
	 * @param id id of object
	 */
	public BeerObject(int x, int y, String id) {
		super();
		//set id
		this.id = id;
		
		//set position
		this.position.x = x * GameObject.BLOCKSIZE;
		this.position.y = y * GameObject.BLOCKSIZE;

	}

	
	/**
	 * check if player collides with this beer
	 * 
	 * @see GameObject#update()
	 */
	@Override
	public void update() {
		//check for player-collision
		if(CollisionHelper.checkPlayerObjectCollision((int)this.position.x, (int)this.position.y)) {
			GameControl.getInstance().encounterBeer(this);
		}
	}

	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		this.active = savedInstanceState.getBoolean("l13_beer" + id + "_active");
	}

	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		outState.putBoolean("l13_beer" + id + "_active", this.active);
	}
}
