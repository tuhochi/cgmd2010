package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class BeerObject extends GameObject {
	
	//beer map
	public static int map[][] = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 1, 1, 0, 0, 0, 0, 2, 0, 0, 0, 1, 0, 1, 1, 1, 1, 2, 1 },
			{ 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 1, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 1 },
			{ 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 },
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
			{ 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};
	
	//if object is visible
	private boolean visible;

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
		
		//beer is visible until player drinks it
		this.visible = true;
	}

	/**
	 * @see GameObject#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		//check for player-collision
		if(CollisionHandler.checkBeerCollision((int)this.position.x, (int)this.position.y)) {
			this.visible = false;
		}
		
		//don't draw invisible beer
		if(!visible) {
			return;
		}
		
		//update position with offset
		this.position.diff(GameObject.offset);
		
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
