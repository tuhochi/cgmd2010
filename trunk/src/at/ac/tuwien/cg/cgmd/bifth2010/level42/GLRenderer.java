package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.Cube;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class GLRenderer extends GLSurfaceView implements Renderer {

	private Context context;
	private Cube cube;
	private Camera cam;
	
	private float light_ambient[] = {0.9f,0.9f,0.9f,1.0f};
	private float light_diffuse[] = {0.9f,0.9f,0.9f,1.0f};
	private float light_position[] = {-3.0f,2.0f,2.0f,1.0f};
	
	public GLRenderer(Context context) {
		super(context);
		setFocusable(true);
		requestFocus();
		setRenderer(this);
		
		this.context = context;
		
		cube = new Cube();
		cam = new Camera(10.0f,-80.0f,80.0f,0.0f,0.0f,1.0f/60.0f,1.0f,1000.0f);
	}
	

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		initGLSettings(gl);
		
		//setup light
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light_position,0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, light_ambient,0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light_diffuse,0);

		cube.loadGLTexture(gl, this.context);
	}
	
	private void initGLSettings(GL10 gl){
		
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		cam.updatePosition(new Vector3(0.0f,0.0f,0.0f), 1.0f);
		cam.look(gl);
		cube.draw(gl,2);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light_diffuse,0);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		//thx to l17!!
		
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix

	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable(){
			public void run() {
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					cam.setLastPosition((int)event.getRawX(), (int)event.getRawY());
				else
					cam.setMousePosition((int)event.getRawX(), (int)event.getRawY());
			}});
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	
		if (keyCode == KeyEvent.KEYCODE_1)
			queueEvent(new Runnable() {
				public void run() {
					//cam.setDistance(instance.getCam().getDistance() - 10.0f);
				}
			});

		if (keyCode == KeyEvent.KEYCODE_2)
			queueEvent(new Runnable() {
				public void run() {
					//cam.setDistance(mRenderer.getCam().getDistance() + 10.0f);
				}
			});

		return false;
	}
}