package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
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
	public GameScene(Bundle state, int width, int height) {
		setDimension(width, height);
		pipe = new PipeGame(state, width, height);
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	public void create() {
		pipe.create();
	}
	
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
	
	
	/**
     * Save the actual game state.
     * @param state Game state
     */
    public void onSaveState(Bundle state) {
    	pipe.onSaveState(state);
    }
    
    
    /**
     * Restore the actual game state.
     * @param state Game state
     */
    public void onRestoreState(Bundle state) {
    	pipe.onRestoreState(state);
    }
}
