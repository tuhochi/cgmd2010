package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import static android.opengl.GLES10.*;

import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.CollisionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.CustomGestureDetector;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Synchronizer;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.CustomGestureDetector.CustomOnGestureListener;

public class RenderView extends GLSurfaceView implements Renderer
{
	private static final float LIGHT_AMBIENT[] = {0.6f,0.6f,0.6f,1.0f};
	private static final float LIGHT_DIFFUSE[] = {1.0f,1.0f,1.0f,1.0f};
	private static final float LIGHT_POSITION[] = {-100.0f,100.0f,100.0f,1.0f};
	
	private final LevelActivity context;
	public final Scene scene;
	public final Camera cam;
	
	// Managers
	private final TimeManager timer = TimeManager.instance; 
	private final MotionManager motionManager = MotionManager.instance;
	private final OGLManager oglManager = OGLManager.instance;
	private final MaterialManager materialManager = MaterialManager.instance;
	private CollisionManager collManager;
	
	// thread stuff
	public final Synchronizer synchronizer;
	
	// main thread stuff
	private final Handler guiThreadHandler;
	private final Runnable fpsUpdateRunnable;
	private final Runnable scoreUpdateRunnable;
	private final Runnable remainingGameTimeRunnable;
	
	// event stuff
	private final LinkedList<MotionEvent> motionEvents;
	private final LinkedList<KeyEvent> keyEvents;
	private final CustomGestureDetector gestureDetector;
	
	// temp vars
	private final Vector3 selectionDirection;
	
	public RenderView(Context context, AttributeSet attr)
	{
		super(context, attr);
		setFocusable(true);
		requestFocus();
		setRenderer(this);
		
		this.context = (LevelActivity)context;
		
		cam = new Camera(40.0f,-80.0f,80.0f,0.0f,0.0f,1.0f/60.0f,1.0f,200.0f);
		
		synchronizer = new Synchronizer();
		motionEvents = new LinkedList<MotionEvent>();
		keyEvents = new LinkedList<KeyEvent>();
		
		gestureDetector = new CustomGestureDetector(new GestureListener());
		
		//init temp vars
		selectionDirection = new Vector3();
		
		scene = SceneLoader.getInstance().readScene("l42_orbit");
		motionManager.generateRandomOrbit(scene,1,15,0,(float)Math.PI/4,0,(float)Math.PI/4,15,20);
		collManager = new CollisionManager(scene);
		
		guiThreadHandler = this.context.handler;
		fpsUpdateRunnable = this.context.fpsUpdateRunnable;
		scoreUpdateRunnable = this.context.scoreUpdateRunnable;
		remainingGameTimeRunnable = this.context.remainingGameTimeRunnable;
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
		Log.v(LevelActivity.TAG,"onSurfaceCreated(" + gl + ", " + config + ")");
		
		// reset everything
		oglManager.reset();
		materialManager.reset();
		timer.reset(false);
		scene.deInit();
		
		// check for GLES11
		boolean gles11 = (gl instanceof GL11);
		Config.GLES11 = gles11;
		Log.i(LevelActivity.TAG, "OpenGL ES " + (gles11 ? "1.1" : "1.0") + " found!");
		
		initGLSettings();
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(RenderView.this.synchronizer.running)
					update();
			}
		}, "Logic Thread").start();
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
				gestureDetector.onTouchEvent(event);
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
				case KeyEvent.KEYCODE_DPAD_DOWN:
					cam.setDistance(cam.getDistance() + 10.0f);
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					Orbit test = (Orbit) motionManager.getMotion(1).getMotion();
					test.morphAxisScale(0.4f, 0.5f,100);
					break;	
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					Orbit test1 = (Orbit) motionManager.getMotion(1).getMotion();
					test1.morphAxisScale(1.6f,1.5f,100);
				break;
				}
			}
		}
		
		motionManager.updateMotion(timer.getDeltaTsec());
		collManager.doCollisionDetection();
		cam.updatePosition(0.0f,0.0f,0.0f, 1.0f);
		
		/*
		 * Update FPS
		 */
		if(timer.haveFPSchanged())
			guiThreadHandler.post(fpsUpdateRunnable);
		
		/*
		 * Update Time
		 */
		if(timer.hasRemainingGameTimeChanged())
			guiThreadHandler.post(remainingGameTimeRunnable);
		
		/*
		 * Update Score
		 */
		boolean scoreChanged = false;
		if(scoreChanged)
			guiThreadHandler.post(scoreUpdateRunnable);
		
		synchronizer.logicDone(); 
	}
	
	private class GestureListener implements CustomOnGestureListener
	{
		@Override
		public boolean onTouchUp(MotionEvent e, long duration)
		{
			Log.v(LevelActivity.TAG, "onLongerPress(" + e + ", " + duration + ")");

			Vector3 unprojectedPoint = oglManager.unProject((int)e.getRawX(), (int)e.getRawY());
			Vector3 rayDirection = Vector3.subtract(unprojectedPoint,cam.eyePosition).normalize();

			SceneEntity entity = collManager.intersectRay(cam.eyePosition, rayDirection);

			//entity selected
			if(entity!=null && !entity.getName().equals(Config.PLANET_NAME))
			{
				selectionDirection.copy(cam.viewPosition);
				selectionDirection.subtract(cam.eyePosition);

				//force strength
				int difference = ((int)duration)/10;
				Log.d(LevelActivity.TAG,"" + difference);
				selectionDirection.normalize().multiply(difference);

				if(entity.getMotion()==null)
				{
					motionManager.addMotion(
							new Orbit(entity.getBoundingSphereWorld().center,
									new Vector3(),
									selectionDirection,
									5,
									entity.getBasicOrientation()),
							entity);
				}
				else
				{
					Orbit orbit = (Orbit)entity.getMotion();
					orbit.morph(selectionDirection);
					motionManager.changeSatelliteTransformation(entity, orbit.getCurrDirectionVec(), selectionDirection,Config.SATELLITE_SPEEDROTA_RATIO);
					Log.d(LevelActivity.TAG,"selectionDirection=" + selectionDirection);
				}
			}
			
			Log.d(LevelActivity.TAG,"unprojectedPoint=" + unprojectedPoint + ", eye=" + cam.eyePosition + ", ray=" + rayDirection);

			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			Log.v(LevelActivity.TAG, "onScroll(" + e1 + ", " + e2 + ", " + distanceX + ", " + distanceY + ")");
			cam.setMouseDiff(distanceX, distanceY);
			return true;
		}
		
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
		Log.v(LevelActivity.TAG,"onSurfaceChanged(" + gl + ", " + width + ", " + height + ")");
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