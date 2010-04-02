package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import static android.opengl.GLES10.*;

import java.util.LinkedList;

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
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.DirectionalMotion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Moveable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.SatelliteTransformation;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Synchronizer;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

public class RenderView extends GLSurfaceView implements Renderer
{
	private static final float LIGHT_AMBIENT[] = {0.5f,0.5f,0.5f,1.0f};
	private static final float LIGHT_DIFFUSE[] = {0.9f,0.9f,0.9f,1.0f};
	private static final float LIGHT_POSITION[] = {-100.0f,100.0f,0.0f,1.0f};
	private static final String EXTENSIONS[] = {};
	
	private final LevelActivity context;
	private OGLManager oglManager = OGLManager.instance;
	private Scene scene;
	private final Camera cam;
	private final TimeManager timer = TimeManager.instance; 
	private final MotionManager motionManager = MotionManager.instance;
	private CollisionManager collManager;
	
	// thread stuff
	private final Synchronizer synchronizer;
	private final LinkedList<MotionEvent> motionEvents;
	private final LinkedList<KeyEvent> keyEvents;
	
	public RenderView(Context context)
	{
		super(context);
		setFocusable(true);
		requestFocus();
		setRenderer(this);
		
		this.context = (LevelActivity)context;
		
		cam = new Camera(20.0f,-80.0f,80.0f,0.0f,0.0f,1.0f/60.0f,1.0f,200.0f);
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(RenderView.this.context.running)
					update();
			}
		}).start();
		synchronizer = new Synchronizer();
		motionEvents = new LinkedList<MotionEvent>();
		keyEvents = new LinkedList<KeyEvent>();
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
		collManager = new CollisionManager(scene);
		
		Orbit orbit2 = new Orbit(new Vector3(-3,3,0),new Vector3(3,-3,0),
								new Vector3(0,0,-5),5);
	
		SatelliteTransformation sat1 = new SatelliteTransformation(0, 2, 0, null);
		orbit2.setSatTrans(sat1);
		
		DirectionalMotion mov1 = new DirectionalMotion(	scene.getSceneEntity(0),
															new Vector3(-3,3,10),
															new Vector3(0,0,-1),0.1f);
		
		motionManager.addMotion(mov1,(Moveable)scene.getSceneEntity(0));
		motionManager.addMotion(orbit2,(Moveable)scene.getSceneEntity(1));
	}
	
	private void update()
	{
		synchronizer.waitForPreRender();
		timer.update();
		
		/*
		 * process all events
		 */
		synchronized(motionEvents)
		{
			MotionEvent event;
			while((event = motionEvents.poll()) != null)
			{
				int rawX = (int)event.getRawX();
				int rawY = (int)event.getRawY();
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					cam.setLastPosition(rawX, rawY);
					Vector3 unprojectedPoint = oglManager.unProject(rawX, rawY);
					Vector3 rayDirection = Vector3.subtract(unprojectedPoint,cam.eyePosition).normalize();
				
					SceneEntity entity = collManager.intersectRay(cam.eyePosition, rayDirection);
					if(entity!=null){
						motionManager.addMotion(new Orbit(	entity.getBoundingSphereWorld().center,
															new Vector3(),
															new Vector3(0,0,-1),5),
												(Moveable)entity);
					}
	
			        Log.d(LevelActivity.TAG,"unprojectedPoint=" + unprojectedPoint + ", eye=" + cam.eyePosition + ", ray=" + rayDirection);
				}					
				else
					cam.setMousePosition(rawX, rawY);
			}
		}
		
		synchronized(keyEvents)
		{
			KeyEvent event;
			while((event = keyEvents.poll()) != null)
			{
				int keyCode = event.getKeyCode();
				
				switch(keyCode)
				{
				case KeyEvent.KEYCODE_DPAD_UP:
					cam.setDistance(cam.getDistance() - 10.0f);
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					Orbit temp = (Orbit)motionManager.getMotion(1);
					temp.decA();
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					cam.setDistance(cam.getDistance() + 10.0f);
					break;
				}
			}
		}
		
		motionManager.updateMotion(timer.getDeltaTsec());
		cam.updatePosition(0.0f,0.0f,0.0f, 1.0f);
		synchronizer.logicDone();
	}
	
	private void pre_render()
	{
//		glLoadIdentity(); // not needed, because cam.look() sets the modelview matrix completely new
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLightfv(GL_LIGHT0, GL_POSITION, LIGHT_POSITION,0);
		
		cam.look();
	}
	
	private void render()
	{
		scene.render();
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		synchronizer.waitForLogic();
		// copies transformation matrizes
		scene.update();
		// sets light and camera
		pre_render();
		synchronizer.preRenderDone();
		
		render();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		// Prevent a Divide By Zero by making height equal one
		if(height == 0)
			height = 1;

		// Reset the current viewport
		glViewport(0, 0, width, height);
		int[] viewport = oglManager.getViewport();
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
		//float zFar = Float.MAX_VALUE;
		float zFar = 1000;
		
		float top = (float)(Math.tan(fovy*0.0087266463f) * zNear); // top = tan((fovy/2)*(PI/180))*zNear
		float bottom = -top;
		float left = aspect * bottom;
		float right = aspect * top;
		
		Matrix44 projection = oglManager.getProjection();
		
		oglManager.glFrustumInfinite(left, right, bottom, top, zNear, zFar, projection);

		glLoadMatrixf(projection.getArray16(), 0);
		
		// Select the modelview matrix again
		glMatrixMode(GL_MODELVIEW);
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		synchronized(motionEvents)
		{
			motionEvents.addLast(event);
		}
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		synchronized(keyEvents)
		{
			keyEvents.addLast(event);
		}
		return false;
	}


}