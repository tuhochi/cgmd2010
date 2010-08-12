package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;

/**
 * Class for general rendering purposes
 * @author Asperger, Radax
 */
class GLRenderer implements GLSurfaceView.Renderer {
	/** Game instance of the level*/
	private Game game;
	
	/**
	 * Constructor
	 * @param _game Game context of the level
	 */
	public GLRenderer(Game _game) {
		game = _game;
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		game.init(gl);
    }

    /* (non-Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	game.onSurfaceChanged(gl, width, height);
    }

    /* (non-Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
     */
    public void onDrawFrame(GL10 gl) {
    	game.update();
    	game.draw(gl);
    }
}