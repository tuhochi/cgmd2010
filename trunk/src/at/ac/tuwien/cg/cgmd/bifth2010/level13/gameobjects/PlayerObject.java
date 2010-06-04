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
		//use absolute drawing
		this.position.add(GameObject.offset);
		super.draw(gl);
		this.position.sub(GameObject.offset);
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
		float x = (position.x + GameObject.offset.x);
		float y = (position.y + GameObject.offset.y);
		return new Vector2(x, y);
	}
	
	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		Vector2 currentTile = new Vector2();
		currentTile.x = savedInstanceState.getFloat("l13_playerObject_currentTileX");
		currentTile.y = savedInstanceState.getFloat("l13_playerObject_currentTileY");
		GameObject.setStartTile(currentTile);
	}
	
	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		Vector2 currentTile = getCurrentTile();
		outState.putFloat("l13_playerObject_currentTileX", currentTile.x);
		outState.putFloat("l13_playerObject_currentTileY", currentTile.y);
	}
}