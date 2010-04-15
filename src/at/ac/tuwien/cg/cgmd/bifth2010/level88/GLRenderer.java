package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;

class GLRenderer implements GLSurfaceView.Renderer {
	private Game game;
	
	public GLRenderer(Game _game) {
		game = _game;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		game.init(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	game.onSurfaceChanged(gl, width, height);
    }

    public void onDrawFrame(GL10 gl) {
    	game.update();
    	game.draw(gl);
    }
}