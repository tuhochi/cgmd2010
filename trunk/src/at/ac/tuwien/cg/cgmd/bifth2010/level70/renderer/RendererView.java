package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * RendererView.
 * @author herrjohann
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
	 * Ctor.
	 * @param context
	 */
	public RendererView(Context context, int windowWidth, int windowHeight) 
	{
		super(context);
        setFocusable(true);
        
        scene = new GameScene();
        scene.create(windowWidth,windowHeight);
        
        renderTask = new RenderTask(scene);
        setRenderer(renderTask);
           
        updateTask = new UpdateTask(scene);
        Thread updTask = new Thread(updateTask);
        updTask.start();
    }
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Add key down event to UpdateTask input queue.
	 * @param The key code
	 * @param The key event
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LinkedList<KeyEvent> inputs = updateTask.getInputs();
		synchronized(inputs) {
			inputs.add(event);
		}
        return super.onKeyDown(keyCode, event);
    }
	
		
	/**
	 * Add key up event to UpdateTask input queue.
	 * @param The key code
	 * @param The key event
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LinkedList<KeyEvent> inputs = updateTask.getInputs();
		synchronized(inputs) {
			inputs.add(event);
		}
		return super.onKeyDown(keyCode, event);
    }

	
	public boolean  onTouchEvent(MotionEvent event) {
		Log.i("Input", "Touch event");
		return false;
	}
}
