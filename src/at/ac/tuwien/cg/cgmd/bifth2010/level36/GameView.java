package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class GameView extends GLSurfaceView implements Renderer {

	private FloatBuffer vBuffer;
	private FloatBuffer cBuffer;

	public GameView(Context context) {
		super(context);
		Log.v("View", "Konstruktor");
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(gl.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.v("View", "onSurfaceChanged");
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.v("View", "onSurfaceCreated");
	}
}
