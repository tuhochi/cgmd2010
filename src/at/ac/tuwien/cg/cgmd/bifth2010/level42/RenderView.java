package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalMovement;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.OrbitManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.SatelliteTransformation;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

// static imports
import static android.opengl.GLES10.*;
import static android.opengl.GLU.*;

public class RenderView extends GLSurfaceView implements Renderer
{
	private static final float LIGHT_AMBIENT[] = {0.5f,0.5f,0.5f,1.0f};
	private static final float LIGHT_DIFFUSE[] = {0.9f,0.9f,0.9f,1.0f};
	private static final float LIGHT_POSITION[] = {-100.0f,100.0f,0.0f,1.0f};
	private static final String EXTENSIONS[] = {"GL_OES_query_matrix"};
	
	private final Context context;
	private Scene scene;
	private final Camera cam;
	private final TimeManager timer = TimeManager.instance; 
	private final OrbitManager orbitManager = OrbitManager.instance;
	
	public RenderView(Context context)
	{
		super(context);
		setFocusable(true);
		requestFocus();
		setRenderer(this);
		
		this.context = context;
		
		cam = new Camera(20.0f,-80.0f,80.0f,0.0f,0.0f,1.0f/60.0f,1.0f,200.0f);
	}
	
	public RenderView(Context context, AttributeSet attr)
	{
		this(context);
	}
	
	private void initGLSettings()
	{
		glEnable(GL_CULL_FACE);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
	
		glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 
		glEnable(GL_DEPTH_TEST);
		glClearDepthf(1.0f);
		glEnable(GL_TEXTURE_2D);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// check for GLES11
		boolean gles11 = (gl instanceof GL11);
		Config.GLES11 = gles11;
		Log.i(LevelActivity.TAG, "OpenGL ES " + (gles11 ? "1.1" : "1.0") + " found!");
		
		// check for needed extensions if OpenGL ES 1.1 is not available
		if(!gles11)
		{
			String extensions = glGetString(GL_EXTENSIONS);
			boolean supported = true;
			for(int i=0; i<EXTENSIONS.length && supported; i++)
				supported &= extensions.contains(EXTENSIONS[i]);
			if(!supported)
			{
//				Log.e(LevelActivity.TAG, "Your phone does not support all required OpenGL extensions needed for playing this level.");
				/*
				 * TODO: Show some kind of warning and exit
				 */
			}
		}	
		
		initGLSettings();
		
		//setup light
		glEnable(GL_LIGHT0);
		glLightfv(GL_LIGHT0, GL_POSITION, LIGHT_POSITION,0);
		glLightfv(GL_LIGHT0, GL_AMBIENT, LIGHT_AMBIENT,0);
		glLightfv(GL_LIGHT0, GL_DIFFUSE, LIGHT_DIFFUSE,0);


		// client states
		
		/*
		 * Dummy Test Scene
		 */
		scene = SceneLoader.getInstance().readScene("l42_cube");
		
		Orbit orbit2 = new Orbit(scene.getSceneEntity(1),new Vector3(-3,3,0),new Vector3(3,-3,0),
								new Vector3(0,0,-5),5);
	
		SatelliteTransformation sat1 = new SatelliteTransformation(0, 2, 0, null);
		orbit2.setSatTrans(sat1);
		
		DirectionalMovement mov1 = new DirectionalMovement(	scene.getSceneEntity(0),
															new Vector3(-3,3,10),
															new Vector3(0,0,-1),0.1f);
		
		orbitManager.addOrbit(mov1);
		orbitManager.addOrbit(orbit2);
	
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
		
		timer.update();
		orbitManager.updateOrbits(timer.getDeltaTsec());
		cam.updatePosition(0.0f,0.0f,0.0f, 1.0f);
		cam.look(gl);
		
		scene.render();

		glLightfv(GL_LIGHT0, GL_POSITION, LIGHT_POSITION,0);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		
		//thx to l17!!
		
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		glViewport(0, 0, width, height); 	//Reset The Current Viewport
		glMatrixMode(GL_PROJECTION); 	//Select The Projection Matrix
		glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		glMatrixMode(GL_MODELVIEW); 	//Select The Modelview Matrix
		glLoadIdentity(); 					//Reset The Modelview Matrix

	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
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
	
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
			queueEvent(new Runnable() {
				public void run() {
					cam.setDistance(cam.getDistance() - 10.0f);
				}
			});
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			queueEvent(new Runnable() {
				public void run() {
					Orbit temp = (Orbit)orbitManager.getOrbit(1);
					temp.decA();
				}
			});

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			queueEvent(new Runnable() {
				public void run() {
					cam.setDistance(cam.getDistance() + 10.0f);
				}
			});

		return false;
	}
	
}