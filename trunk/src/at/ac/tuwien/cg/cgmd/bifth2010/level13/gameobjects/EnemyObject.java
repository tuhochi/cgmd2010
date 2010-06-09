package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

/**
 * 
 * @author group13
 * 
 * class for enemy objects (cops and prostitutes)
 *
 */
public abstract class EnemyObject extends GameObject {

	/** id of object */
	protected String id;

	/** movement1 of object */
	protected Vector2 movement1;

	/** movement2 of object */
	protected Vector2 movement2;

	/** movement speed of enemies */
	private static final int SPEED = 2;

	/** possible movement-directions of object */
	private static final Vector2[] directionVectorArray = {
		new Vector2(0,1 * SPEED), 
		new Vector2(0,-1 * SPEED),
		new Vector2(1 * SPEED, 0),
		new Vector2(-1 * SPEED, 0)
	};

	/** random number generator */
	protected Random random;


	/**
	 * constructor creates a new enemy object
	 * 
	 * @param x x-position of object
	 * @param y y-position of object
	 * @param id id of object
	 */
	public EnemyObject(int x, int y, String id) {
		super();
		//set members
		this.id = id;
		movement1 = new Vector2(0,0);
		movement2 = new Vector2(0, 0);
		random = new Random(id.hashCode());
		setRandomDirection();

		//set position
		this.position.x = x * GameObject.BLOCKSIZE;
		this.position.y = y * GameObject.BLOCKSIZE;
	}


	/**
	 * set new random movement-direction
	 */
	protected void setRandomDirection(){

		//set random move-direction1
		int randomDir = random.nextInt(4);
		this.movement1 = directionVectorArray[randomDir];

		//set movement2 to random opposite of movement1
		randomDir = random.nextInt(2);
		if(randomDir == 0) {
			this.movement2.x = this.movement1.y;
			this.movement2.y = this.movement1.x;
		}
		else {
			this.movement2.x = -this.movement1.y;
			this.movement2.y = -this.movement1.x;
		}
	}
}
