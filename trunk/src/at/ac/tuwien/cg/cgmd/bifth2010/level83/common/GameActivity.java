package at.ac.tuwien.cg.cgmd.bifth2010.level83.common;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class GameActivity extends Activity implements GLSurfaceView.Renderer {
	private GLSurfaceView glsurfaceview;
	private float deltaTime;
	private long lastFrameTime;
	private GameListener gameListener;
	
	int width, height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		glsurfaceview = new GLSurfaceView(this);
		
		glsurfaceview.setRenderer(this);
		this.setContentView(glsurfaceview);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		glsurfaceview.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		glsurfaceview.onResume();
	}
	
	protected void setGameListener(GameListener gl){	
		gameListener = gl;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		long currentTime = System.nanoTime();
		deltaTime = (currentTime - lastFrameTime) / 1000000000f;
		lastFrameTime = currentTime;
		
		if(gameListener != null)
			gameListener.mainLoopIteration(this, gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		lastFrameTime = System.nanoTime();
		
		if(gameListener != null)
			gameListener.setup(this, gl);	
	}
	
	public int getViewportWidth(){
		return width;
	}
	
	public int getViewportHeight(){
		return height;
	}
}
