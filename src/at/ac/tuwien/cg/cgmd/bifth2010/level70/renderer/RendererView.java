package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.game.TrainGame;

/**
 * RendererView - Manages the user input and displays the game.
 * 
 * @author Christoph Winklhofer
 */
public class RendererView extends GLSurfaceView {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
    /** Instance to the train game */
	private TrainGame  game;
	
	/** RenderTask to display the game */
	private RenderTask renderTask;
	
	/** UpdateTask to update the game states */
	private UpdateTask updateTask;
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor / Dtor ----
	
	/**
	 * Create renderer view.
	 * @param context The android context.
	 * @param state The saved game state.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public RendererView(Context context, Bundle state, int width, int height) {
		super(context);
        setFocusable(true);
                
        // Enable alpha channel 
        setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        getHolder().setFormat(PixelFormat.RGBA_8888); 
        
        game = new TrainGame(state, width, height);
                
        renderTask = new RenderTask(game);
        setRenderer(renderTask);
        
        updateTask = new UpdateTask(game);
    }
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Add touch event to UpdateTask input queue.
	 * @param event The event.
	 */
	public boolean onTouchEvent(MotionEvent event) {
		LinkedList<MotionEvent> inputs = updateTask.getInputMotions();
		synchronized(game) {
			inputs.add(event);
		}
		return super.onTouchEvent(event);
	}
	
	
	/**
	 * On start event.
	 */
	public void onStart() {
        if (!updateTask.isAlive()) {
        	updateTask.start();
        }
    }

	
	/**
	 * On start event.
	 */
	public void onPause() {
        super.onPause();
        updateTask.setPauseFlag(true);
    }
	
	
	/**
	 * On resume event.
	 */
	public void onResume() {
        super.onResume();
        updateTask.setPauseFlag(false);
    }
	

	/**
	 * On stop event.
	 */
    public void onStop() {
    	
    }
    
   
    /**
	 * On destroy event.
	 */
    public void onDestroy() {
		updateTask.setRunningFlag(false);
	}
    
    
    /**
     * Save the actual game state.
     * @param outState Game state
     */
    public void onSaveState(Bundle outState) {
    	game.onSaveState(outState);
    }
    
    
    /**
     * Restore the actual game state.
     * @param outState Game state
     */
    public void onRestoreState(Bundle outState) {
    	game.onRestoreState(outState);
    }
}
