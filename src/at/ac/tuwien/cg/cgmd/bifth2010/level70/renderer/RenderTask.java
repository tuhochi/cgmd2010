package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/**
 * RenderTask.
 */
public class RenderTask implements Renderer {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	GameScene scene; //< Game scene
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create render task.
	 * @param The game scene.
	 */
	public RenderTask(GameScene scene) {
		this.scene = scene;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * OpenGL initialization.
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		Log.i("RenderTask", "onSurfaceCreated");
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		scene.create();
		
	}
	
	
	/**
	 * Surface changed.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		Log.i("RenderTask", "onSurfaceChanged");
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	
	/**
	 * Draw.
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		synchronized(scene) {
			try {
				// Wait for the update task
				scene.wait();
			}
			catch(InterruptedException e) {
				
			}
			scene.draw(gl);
		}
	}	
}
