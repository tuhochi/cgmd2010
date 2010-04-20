package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * RendererView.
 */
public class RendererView extends GLSurfaceView {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private GameScene  scene;      //< Game scene
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
        
        scene = new GameScene(state, width, height);
                
        renderTask = new RenderTask(scene);
        setRenderer(renderTask);

        updateTask = new UpdateTask(scene);
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
	 * Add key up event to UpdateTask input queue.
	 * @param keyCode The key code
	 * @param event The key event
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LinkedList<KeyEvent> inputs = updateTask.getInputKeys();
		synchronized(scene) {
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
		synchronized(inputs) {
			inputs.add(event);
		}
		return false;
	}
	
	
	/**
	 * On start event.
	 */
	public void onStart() {
        Log.i("RendererView", "onStart");
        updateTask.start();
    }


	/**
	 * On stop event.
	 */
    public void onStop() {
    	Log.i("RendererView", "onStop");
    	updateTask.setRunningFlag(false);
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
    	scene.onSaveState(outState);
    }
    
    
    /**
     * Restore the actual game state.
     * @param outState Game state
     */
    public void onRestoreState(Bundle outState) {
    	scene.onRestoreState(outState);
    }
}
