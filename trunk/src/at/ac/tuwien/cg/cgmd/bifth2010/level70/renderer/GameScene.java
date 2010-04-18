package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.game.PipeGame;

/**
 * Game scene. Holds all game relevant data.
 */
public class GameScene {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	PipeGame pipe; //< Pipe game
	int width;     //< Width of the screen
	int height;    //< Height of the screen
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor / Dtor ----
	
	/**
	 * Create game scene.
	 */
	public GameScene() {
		
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Set screen dimension.
	 * @param width screen width
	 * @param height screen height
	 */
	public void setDimension(int width, int height) {
		this.width  = width;
		this.height = height;
	}
	
	
	/**
	 * Create game scene.
	 * Attention: OpenGL context must exist.
	 */
	public void create() {
		pipe = new PipeGame();
	}
	
	
	/**
	 * Update game scene
	 * @param dt The delta frame time.
	 */
	public void update(float dt) {
		
	}
	
	
	/**
	 * Draw game scene
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {
		pipe.draw(gl);
	}
	
	
	/**
	 * OnClick event
	 * @param event The event.
	 */
	public void onClick(MotionEvent event) {
		pipe.onClick(event);
	}
	
}
