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
		
		//enable client state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		//bind texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texture.textures[0]);
		
		//define texture coordinates
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texture.textureBuffer);
		
		//point to vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//translate to correct position
		gl.glTranslatef(this.position.x, this.position.y, 0.0f);
		
		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		
		//translate back
		gl.glTranslatef(-this.position.x, -this.position.y, 0.0f);
		
		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		//reset position
		this.position.add(GameObject.offset);		
	}
}
