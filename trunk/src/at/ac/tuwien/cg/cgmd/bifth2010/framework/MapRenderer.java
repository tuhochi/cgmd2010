package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class MapRenderer implements GLSurfaceView.Renderer{
	
	private static final String CLASS_TAG = MapRenderer.class.getName();
	
	private Resources mResources;
	private Rectangle mMap;
	private Circle mIconLevel01; 
	private float mAngle;
	private long mLastTime;
	private long mElapsedTime = 0;
	private long mFilteredElapsedTime = 0;
	
	public MapRenderer(Resources resources) {
		mMap = new Rectangle(2,3);	
		mIconLevel01 = new Circle(1.f, 100);
		mResources=resources;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {

		long currentTime = System.nanoTime();
		mElapsedTime = currentTime-mLastTime;
		mFilteredElapsedTime = (mFilteredElapsedTime/2) + (mElapsedTime/2); 
		mLastTime = currentTime;
		

		//clear the color and the depth buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		//set up transformations
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -3); 
		gl.glRotatef(mAngle, 0, 0, 1);

		//draw the map
		mMap.draw(gl);
		mIconLevel01.draw(gl);
		
		//do animation (this is not timing based animation)
		mAngle += 1.f; 
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//set up view port
		gl.glViewport(0, 0, width, height);
		//set projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		//set modelview matrix to identity
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//set clear color and depth
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepthf(1.0f);
		//enable depth test
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		setTextures(gl);
		mLastTime = System.nanoTime();
	}
	
	public void setTextures(GL10 gl){
		mIconLevel01.setTexture(gl, mResources, R.drawable.l00_map_512);
		mMap.setTexture(gl, mResources, R.drawable.l00_map_512);
	}
	
	public float getFps()
	{
		return 1000000000.f / (float)mElapsedTime;
	}
	
	public float getFilteredFps()
	{
		return 1000000000.f / (float)mFilteredElapsedTime;
	}

   

}
