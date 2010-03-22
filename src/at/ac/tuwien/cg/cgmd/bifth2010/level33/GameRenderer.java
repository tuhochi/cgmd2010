package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class GameRenderer implements GLSurfaceView.Renderer {

	public GameRenderer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		GameView.sceneGraph.render(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub

	}

}