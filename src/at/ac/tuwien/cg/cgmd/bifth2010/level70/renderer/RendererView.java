package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame.TrainGame;

/**
 * RendererView.
 * 
 * @author herrjohann
 */
public class RendererView extends GLSurfaceView {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private TrainGame  game;   	   //< Train game
	private RenderTask renderTask; //< Renderer
	private UpdateTask updateTask; //< Renderer
	
	
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
	 * Add key down event to UpdateTask input queue.
	 * @param keyCode The key code
	 * @param event The key event
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LinkedList<KeyEvent> inputs = updateTask.getInputKeys();
		synchronized(inputs) {
			inputs.add(event);
		}
        return false;
    }
	

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
        Log.i("RendererView", "onStart");
        if (!updateTask.isAlive()) {
        	updateTask.start();
        }
    }

	
	/**
	 * On start event.
	 */
	public void onPause() {
        Log.i("RendererView", "onPause");
        super.onPause();
        updateTask.setPauseFlag(true);
    }
	
	
	/**
	 * On resume event.
	 */
	public void onResume() {
        Log.i("RendererView", "onResume");
        super.onResume();
        updateTask.setPauseFlag(false);
    }
	

	/**
	 * On stop event.
	 */
    public void onStop() {
    	Log.i("RendererView", "onStop");
    }
    
   
    /**
	 * On destroy event.
	 */
    public void onDestroy() {
		Log.i("RendererView", "onDestroy");
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
