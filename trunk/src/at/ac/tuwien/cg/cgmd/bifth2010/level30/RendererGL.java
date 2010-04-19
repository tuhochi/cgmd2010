package at.ac.tuwien.cg.cgmd.bifth2010.level30;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;

/*
 * Implements the renderer
 */
public class RendererGL implements GLSurfaceView.Renderer {
	
	GameWorld gameWorld;
	
	/* Constructor
	 * @param world The main gameworld. 
	 */
	public RendererGL(GameWorld world) {
		gameWorld = world;
	}

	/* Draw the frame
	 * @param gl OpenGL object. 
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		gameWorld.Draw(gl);
	}

	/* Change drawing surface
	 * @param gl OpenGL object.
	 * @param width Surface Width.
	 * @param height Surface Height.
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		gameWorld.onSurfaceChanged(gl, width, height);
	}

	/* Create renderer
	 * @param gl OpenGL object.
	 * @param config Renderer configuration.
	 	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		gl.glDisable(GL10.GL_DITHER);	        
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
				GL10.GL_NICEST);

		gl.glClearColor(1.0f,1.0f,1.0f,1.0f);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);

		gameWorld.Init(gl);
	}
}
