package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author group13
 *
 * it is a status bar !
 * use set scaleFactor to set the scale ;-).
 *
 */

public abstract class StatusBar extends GameObject{

	/** scale factor of status-bar */
	protected float scaleFactor = 0.0f;


	/**
	 * constructor only calls super
	 * 
	 * @param objectWidth width of status-bar
	 * @param objectHeight height of status-bar
	 */
	public StatusBar(float objectWidth,float objectHeight){
		super(objectWidth, objectHeight);
	}

	
	/**
	 * set new scale factor
	 * 
	 * @param scaleFactor new scale factor
	 */
	public void updateScale(float scaleFactor){
		this.scaleFactor = scaleFactor;
	}


	/**
	 * same as draw in {@link GameObject#draw(GL10)} but additionally applies scale factor
	 */
	@Override
	public void draw(GL10 gl) {
		//reset modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		//enable client state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//bind texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texture.textures[0]);

		//define texture coordinates
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texture.textureBuffer);

		//point to vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		//scale
		gl.glScalef(scaleFactor, 1, 1);
		
		//translate to correct position
		gl.glTranslatef(this.position.x, this.position.y, 0.0f);

		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}