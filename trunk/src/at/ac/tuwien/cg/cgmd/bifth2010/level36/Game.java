package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

public class Game extends GLSurfaceView implements Renderer {
	
	public Game(Context context) {
		super(context);
		System.out.println("GlActivity Konstrukt");
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		System.out.println("GlActivity onSurfaceChanged");
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		System.out.println("GlActivity onSurfaceCreated");

	}
}
