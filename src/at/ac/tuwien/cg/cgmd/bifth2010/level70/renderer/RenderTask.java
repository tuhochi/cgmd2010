package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GlTexture;

/**
 * RenderTask.
 */
public class RenderTask implements Renderer {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	GameScene scene; //< Game scene
	GlTexture tex;
	
	
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
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		Log.i("RenderTask", "scene create");
		scene.create();
		Log.i("RenderTask", "after scene create");
		synchronized(scene) {
			scene.notify();
		}
		Log.i("RenderTask", "notify");
	}
	
	
	/**
	 * Surface changed.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
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
				scene.wait();
			}
			catch(InterruptedException e) {
				
			}
			scene.draw(gl);
		}
		
		Log.i("Renderer", "onDrawFrame");
	}	
}
