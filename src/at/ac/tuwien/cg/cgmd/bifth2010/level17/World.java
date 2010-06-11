package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;

/**
 * An interface for a game world
 * @author MaMa
 *
 */
public interface World {
	/**
	 * Is called to update the world
	 */
	public void update();
	
	/**
	 * Is called every frame to render the world
	 * @param gl The openGL context
	 */
	public void draw(GL10 gl);
	
	/**
	 * The surface to render to has changed
	 * @param gl The OpenGL context
	 * @param width The new width
	 * @param height The new height
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height);
	
	/**
	 * Initialize the world and load all assets
	 * @param gl The OpenGL context
	 */
	public void init(GL10 gl);
	
	/**
	 * Is called when the player touches the screen
	 * @param pos The position the player touches the screen in screen coords
	 */
	public void fingerDown(Vector2 pos);	
	
	/**
	 * Is called when the player moves the finger on the screen
	 * @param pos The new position of the finger on the screen in screen coords
	 */
	public void fingerMove(Vector2 pos);
	
	/**
	 * Is called when the player releases the screen
	 * @param pos The last position the player touched the screen in screen coords
	 */
	public void fingerUp(Vector2 pos);

	/**
	 * Pauses or resumes the game 
	 * @param pause The pause state that should be set
	 */
	public void setPause(boolean pause);

	/**
	 * Pauses or resumes the game 
	 * @param pause The pause state that should be set
	 */
	public boolean getPause();
	
	/**
	 * Save the actual gamestate
	 * @param outState The bundle to save to
	 */
	public void onSaveInstanceState(Bundle outState);
}
