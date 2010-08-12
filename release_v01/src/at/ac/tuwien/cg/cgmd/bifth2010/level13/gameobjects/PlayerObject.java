package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

/**
 * 
 * @author group13
 * 
 * class representing the player
 *
 */
public class PlayerObject extends GameObject {

	/**
	 * constructor calls super and sets position
	 */
	public PlayerObject() {
		super();
		
		//set position to center of screen
		this.position.x = ((MyRenderer.getScreenWidth() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		this.position.y = ((MyRenderer.getScreenHeight() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
	}
	

	/**
	 * @see GameObject#draw(GL10) 
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

		//translate to correct position
		gl.glTranslatef(this.position.x, this.position.y, 0.0f);

		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	
	/**
	 * gets the current position of the player measured in tiles
	 * 
	 * @return position of player (measured in tiles)
	 */
	public Vector2 getCurrentTile() {
		int tileX = (int) ((position.x + GameObject.offset.x) / GameObject.BLOCKSIZE);
		int tileY = (int) ((position.y + GameObject.offset.y) / GameObject.BLOCKSIZE);
		return new Vector2(tileX, tileY);
	}
	
	
	/**
	 * gets the position in pixels of the player
	 * 
	 * @return position of player in pixels
	 */
	public Vector2 getRealPosition() {
		int x = (position.x + GameObject.offset.x);
		int y = (position.y + GameObject.offset.y);
		return new Vector2(x, y);
	}
	
	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		Vector2 currentTile = new Vector2();
		currentTile.x = savedInstanceState.getInt("l13_playerObject_currentTileX");
		currentTile.y = savedInstanceState.getInt("l13_playerObject_currentTileY");
		GameObject.setStartTile(currentTile);
	}
	
	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		Vector2 currentTile = getCurrentTile();
		outState.putInt("l13_playerObject_currentTileX", currentTile.x);
		outState.putInt("l13_playerObject_currentTileY", currentTile.y);
	}
}