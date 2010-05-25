package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;


/**
 * 
 * @author arthur/sebastian (group 13)
 *
 */
public class BeerObject extends GameObject {


	/**
	 * constructor calls super() with object's dimensions
	 * @param x x-position (*GameObject.BLOCKSIZE)
	 * @param y y-position (*GameObject.BLOCKSIZE)
	 */
	public BeerObject(int x, int y) {
		super(GameObject.BLOCKSIZE, GameObject.BLOCKSIZE);
		
		//set position
		this.position.x = x * GameObject.BLOCKSIZE;
		this.position.y = y * GameObject.BLOCKSIZE;
		
	}

	/**
	 * @see GameObject#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		//check for player-collision
		if(CollisionHelper.checkPlayerObjectCollision((int)this.position.x, (int)this.position.y)) {
			this.isActive = false;
			gameControl.consumeBeer();
		}
		
		//update position with offset
		this.position.sub(GameObject.offset);
		
		super.draw(gl);
		
		//reset position
		this.position.add(GameObject.offset);		
	}
}
