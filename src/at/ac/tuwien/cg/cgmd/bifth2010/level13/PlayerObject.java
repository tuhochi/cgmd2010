package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import static android.opengl.GLES10.GL_UNSIGNED_SHORT;
import static android.opengl.GLES10.glDrawElements;
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
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		//bind texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texture.textures[0]);
		
		//define texture coordinates
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texture.textureBuffer);
		
		//point to vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//translate to center of screen
		gl.glTranslatef(this.position.x, this.position.y, 0.0f);
		
		//draw
		glDrawElements(GL10.GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, indexBuffer);

		//translate back to origin
		gl.glTranslatef(-this.position.x, -this.position.y, 0.0f);
		
		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
