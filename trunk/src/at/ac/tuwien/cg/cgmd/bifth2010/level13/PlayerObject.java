package at.ac.tuwien.cg.cgmd.bifth2010.level13;


import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class PlayerObject extends GameObject {

	//center of screen
	protected static final Vector2 center = new Vector2(((MyRenderer.screenWidth / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE, ((MyRenderer.screenHeight / GameObject.BLOCKSIZE / 2) * GameObject.BLOCKSIZE));
	
	/**
	 * constructor calls super with object's dimensions
	 */
	public PlayerObject() {
		//set dimension (must be equal to GameObject.BLOCKSIZE)
		super(GameObject.BLOCKSIZE, GameObject.BLOCKSIZE);
		
		//set position
		this.position.x = center.x;
		this.position.y = center.y;

	}
	
	/**
	 * @see GameObject#draw(GL10) 
	 */
	@Override
	public void draw(GL10 gl) {
		//enable client state
		super.draw(gl);
	}
}
