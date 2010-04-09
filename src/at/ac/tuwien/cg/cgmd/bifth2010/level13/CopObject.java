package at.ac.tuwien.cg.cgmd.bifth2010.level13;


import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author sebastian (group 13)
 *
 */
public class CopObject extends GameObject {


	/**
	 * constructor calls super with object's dimensions
	 */

	public float speed = 5.0f;

	public Vector2[] directionVectorArray = new Vector2[4];
	Random random;
	
	
	public CopObject(int x, int y) {
		//set dimension (must be equal to GameObject.BLOCKSIZE)
		super(GameObject.BLOCKSIZE, GameObject.BLOCKSIZE);
		moveVec = new Vector2(0,0);
		
		directionVectorArray[0] = new Vector2(0,1);
		directionVectorArray[1] = new Vector2(0,-1);
		directionVectorArray[2] = new Vector2(1,0);
		directionVectorArray[3] = new Vector2(-1,0);
		
		for (int i = 0; i < 4;i++)
			directionVectorArray[i].mult(speed);
		
		random = new Random();
		
		setRandomDirection();
		
		//set position
		this.position.x = x * GameObject.BLOCKSIZE;
		this.position.y = y * GameObject.BLOCKSIZE;
	}
	
	
	private void setRandomDirection(){
		
		int randomDir = random.nextInt(4);
		this.moveVec = directionVectorArray[randomDir];

	}
	
	
	//Sets the initial position of the cop object
	public void setPos(int x, int y){
		this.position.x = x;
		this.position.y = y;
	}
	
	
	@Override
	public void update(){
		this.position.add(moveVec);
		}
	
	/**
	 * @see GameObject#draw(GL10) 
	 */
	@Override
	public void draw(GL10 gl) {
		if (CollisionHandler.checkBackgroundCollision(MyRenderer.map, (GameObject)this)){
			setRandomDirection();
		}
		
		//update position with offset
		this.position.sub(GameObject.offset);
		
		super.draw(gl);
		
		//reset position
		this.position.add(GameObject.offset);		
	}
}
