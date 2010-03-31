package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLES10Ext;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalMovement;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.OrbitManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.SatelliteTransformation;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

// static imports
import static android.opengl.GLES10.*;

public class RenderView extends GLSurfaceView implements Renderer
{
	private static final float LIGHT_AMBIENT[] = {0.5f,0.5f,0.5f,1.0f};
	private static final float LIGHT_DIFFUSE[] = {0.9f,0.9f,0.9f,1.0f};
	private static final float LIGHT_POSITION[] = {-100.0f,100.0f,0.0f,1.0f};
	private static final String EXTENSIONS[] = {"GL_OES_query_matrix"};
	
	private final Context context;
	private OGLManager oglManager = OGLManager.instance;
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
		
		//setup light
		glEnable(GL_LIGHT0);
		glLightfv(GL_LIGHT0, GL_POSITION, LIGHT_POSITION,0);
		glLightfv(GL_LIGHT0, GL_AMBIENT, LIGHT_AMBIENT,0);
		glLightfv(GL_LIGHT0, GL_DIFFUSE, LIGHT_DIFFUSE,0);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// check for GLES11
		boolean gles11 = (gl instanceof GL11);
		Config.GLES11 = gles11;
		Log.i(LevelActivity.TAG, "OpenGL ES " + (gles11 ? "1.1" : "1.0") + " found!");
		
		String extensions = glGetString(GL_EXTENSIONS);
		Log.i(LevelActivity.TAG, "Supported Extensions: " + extensions);
		
		// check for needed extensions if OpenGL ES 1.1 is not available
		if(!gles11)
		{
			boolean supported = true;
			for(int i=0; i<EXTENSIONS.length && supported; i++)
				supported &= extensions.contains(EXTENSIONS[i]);
			if(!supported)
			{
				Log.e(LevelActivity.TAG, "Your phone does not support all required OpenGL extensions needed for playing this level.");
				/*
				 * TODO: Show some kind of warning and exit
				 */
			}
		}
		
		initGLSettings();
		
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
	
	private void update()
	{
		timer.update();
		orbitManager.updateOrbits(timer.getDeltaTsec());
		cam.updatePosition(0.0f,0.0f,0.0f, 1.0f);
	}
	
	private void pre_render(GL10 gl)
	{
		glLoadIdentity();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLightfv(GL_LIGHT0, GL_POSITION, LIGHT_POSITION,0);
		
		cam.look(gl);
		
		float[] modelview = new float[16];
		
		if(Config.GLES11)
		{
			GLES11.glGetFloatv(GLES11.GL_PROJECTION_MATRIX, modelview, 0);
		}
		else
		{
			int[] mantissa = new int[16];
			int[] exponent = new int[16];
			
			GLES10Ext.glQueryMatrixxOES(mantissa, 0, exponent, 0);
			for(int i=0; i<16; i++)
				modelview[i] = ((float)mantissa[i]) * ((float)Math.pow(2, exponent[i]));
		}
		oglManager.getModelview().set(modelview);
	}
	
	private void render(GL10 gl)
	{
		scene.render();
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		update(); // call must be moved to logic thread
		
		/*
		 * Wait for logic thread to finish the current frame
		 */
		
		// copies transformation matrizes
		scene.update();
		
		// sets light and camera
		pre_render(gl);
		
		/*
		 * Collect input data and schedule logic thread for another frame,
		 * processing the input data
		 */
		
		render(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		// Prevent a Divide By Zero by making height equal one
		if(height == 0)
			height = 1;

		// Reset the current viewport
		glViewport(0, 0, width, height);
		float[] viewport = oglManager.getViewport();
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = width;
		viewport[3] = height;
		
		// Select and reset the projection matrix
		glMatrixMode(GL_PROJECTION); 	
		glLoadIdentity();

		// Fill the projection Matrix
		float fovy = 45.0f;
		float aspect = (float)width / (float)height;
		float zNear = 0.1f;
		float zFar = Float.MAX_VALUE;
		
		float top = (float)(Math.tan(fovy*0.0087266463f) * zNear); // top = tan((fovy/2)*(PI/180))*zNear
		float bottom = -top;
		float left = aspect * bottom;
		float right = aspect * top;
		
		Matrix44 projection = oglManager.getProjection();
		
		glFrustumInfinite(left, right, bottom, top, zNear, zFar, projection);

		glLoadMatrixf(projection.getArray16(), 0);
		
		// Select the modelview matrix again
		glMatrixMode(GL_MODELVIEW);

	}
	
	private void glFrustumInfinite(float left, float right, float bottom, float top, float zNear, float zFar, Matrix44 result)
	{
		float x, y, a, b, c, d;
		x = (2.0f * zNear) / (right - left);
		y = (2.0f * zNear) / (top - bottom);
		a = (right + left) / (right - left);
		b = (top + bottom) / (top - bottom);

		if (zFar == Float.MAX_VALUE)
		{
			c = -0.999f;
			d = -1.999f * zNear;
		}
		else
		{
			c = -(zFar + zNear) / (zFar - zNear);
			d = -(2.0f * zFar * zNear) / (zFar - zNear);
		}

		result.set(
				x, 0, 0, 0,
				0, y, 0, 0,
				a, b, c, -1,
				0, 0, d, 0);
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