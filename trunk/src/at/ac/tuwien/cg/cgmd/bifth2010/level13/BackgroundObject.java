package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class BackgroundObject extends GameObject {


	/**
	 * constructor calls super() with object's dimensions
	 */
	public BackgroundObject() {
		//object is 714*714 pixels
		super(714, 714);
	}
	
	/**
	 * @see GameObject#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		
		//Reset modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//test for collision with solid tiles
		if(CollisionHandler.checkBackgroundCollision(MyRenderer.map)) {
			//reset offset
			GameObject.offset.sub(GameControl.movement);
			//stop movement
			GameControl.movement.x = 0;
			GameControl.movement.y = 0;
		}
		//enable client state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//define texture coordinates
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texture.textureBuffer);

		//point to vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//bind texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texture.textures[0]);

		//translate whole background (instead of moving player)
		gl.glTranslatef(-GameObject.offset.x, -GameObject.offset.y, 0f);
		
		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		
		
		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
