package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.game.TrainGame;

/**
 * RenderTask to display the game.
 * 
 * @author Christoph Winklhofer
 */
public class RenderTask implements Renderer {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
    /** Instance to the train game */
	TrainGame game;
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create render task.
	 * @param game The game scene.
	 */
	public RenderTask(TrainGame game) {
		this.game = game;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		game.createOpenGl(gl);
	}
	
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		game.setScreen(gl, width, height);
	}
	
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		synchronized(game) {
			try {
				// Wait for the update task
				game.wait();
			}
			catch(InterruptedException e) {
				
			}
			game.draw(gl);
		}
	}	
}
