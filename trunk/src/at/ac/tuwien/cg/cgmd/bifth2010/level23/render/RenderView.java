package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;


import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Background;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.ObstacleManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Serializer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;


/**
 * The Class RenderView implements the renderer to render all needed objects.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class RenderView extends GLSurfaceView implements GLSurfaceView.Renderer {
	
	/** The screen width. */
	private float screenWidth;
	
	/** The screen height. */
	private float screenHeight;
	
	/** The aspect ratio. */
	private float aspectRatio;
	
	/** The right bounds. */
	private float rightBounds=100.0f;
	
	/** The top bounds. */
	private float topBounds=100.0f;
	
	/** The balloon height. */
	private float balloonHeight=0;
	
	/** The instance of the RenderView to pass it around. */
	private static RenderView instance;
	
	/** The Android context . */
	private Context context; 
	
	/** The main character. */
	private MainChar mainChar; 
	
	/** The background. */
	private Background background;
	
	/** The hud. */
	private Hud hud;
	
	/** The boolean that checks if the screen is pressed or released . */
	private boolean released = true; 
	
	/** The last motion event. */
	private MotionEvent lastMotionEvent = null; 
	
	/** The accumulation time. */
	private float accTime;
	
	/** The orientation listener. */
	private OrientationListener orientationListener; 
	
	/** The boolean indicating if orientation sensor is used or not. */
	private boolean useSensor = false;
	
	/** The main characters movement direction. */
	private int mainCharMoveDir;
	
	/** The last key movement. */
	private int lastKeyMovement;
	
	/** The timer. */
	private TimeUtil timer;
	
	/** The texture manager. */
	private TextureManager textureManager; 
	
	/** The serializer. */
	private Serializer serializer; 
	
	/**
	 * Instantiates a new render view.
	 *
	 * @param context the Android context
	 */
	public RenderView(Context context)
	{
		
		super(context);
		timer = TimeUtil.getInstance();
		this.context = context; 
		setRenderer(this); 
		// so that the key events can fire
        setFocusable(true);
        requestFocus();
        instance=this;
        textureManager = TextureManager.getInstance();
        serializer = Serializer.getInstance();
        serializer.setContext(context); 
	}
	
	/**
	 * Gets the single instance of RenderView. Implements the singleton pattern
	 *
	 * @return singleton of RenderView
	 */
	public static RenderView getInstance()
	{
		return instance;
	}
	
	/**
	 * Persist scene entities to disk.
	 */
	public void persistSceneEntities() {
		Serializer.getInstance().serializeObjects(mainChar, background);
	}
	
	/**
	 * Restore scene entities from disk.
	 */
	public void restoreSceneEntities() {
		HashMap<Integer, SceneEntity> map = Serializer.getInstance().getSerializedObjects();
		mainChar = (MainChar)map.get(Serializer.SERIALIZED_MAINCHAR);
		background = (Background)map.get(Serializer.SERIALIZED_BACKGROUND);
	}
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	/**
	 * method which loops while the level is running, to do all work needed
	 */
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		timer.update();
		
		float dt = timer.getDt();
				
		balloonHeight += dt*Settings.BALLOON_SPEED;
		accTime += dt/1000;
		if(accTime > 5)
		{
			System.out.println(timer.getFPS());
			accTime = 0;
		}
		
		if(!useSensor)
		{
			if (!released)
				handleOnTouchMovement(lastMotionEvent);
			else
				mainCharMoveDir = MainChar.NO_MOVEMENT;
		}
		
		fetchKeyMoveData();
		
		mainChar.update(dt,mainCharMoveDir);
		background.update(dt);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		background.render();
		mainChar.render();
		hud.render();
		
		ObstacleManager.getInstance().renderVisibleObstacles((int)balloonHeight);
		
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	/**
	 * called when the surface has changed (landscape to portrait, hw keyboard on etc)
	 * Here, the screen parameters are set, viewport and projection are set
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.v("RenderView.java", "onSurfaceChanged");
		//setup the Viewport with an Orthogonal View 1 unit = 1 pixel
		//0 0 is bottom left
		
		screenWidth = width;
		screenHeight = height;
		aspectRatio = screenHeight/screenWidth;
		topBounds = rightBounds*aspectRatio;
		
		gl.glViewport(0, 0, width, height);	
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, rightBounds, 0.0f, topBounds, 0.0f, 1.0f);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	/**
	 * called when the surface has been created, on startup 
	 * Here, display is set, mainchar, obstacles, background and hut are created and the textures are loaded 
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{	
		Log.v("RenderView.java", "onSurfaceCreated");
		setupGL(gl);
		
		Display display = LevelActivity.getInstance().getWindowManager().getDefaultDisplay();
		aspectRatio = (float)display.getHeight()/(float)display.getWidth();
		topBounds = rightBounds*aspectRatio;
		
		mainChar = new MainChar(25.0f,45.0f,new Vector2(0,0));
		
		ObstacleManager.getInstance().generateObstacles();
		
		background = new Background();
		
		hud = new Hud();
				
		int resID = context.getResources().getIdentifier("l23_balloon", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		mainChar.setTextureID(textureManager.getTextureId(context.getResources(), resID));
		resID = context.getResources().getIdentifier("l23_bg", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		background.setTextureID(textureManager.getTextureId(context.getResources(), resID));
	}
		
	/* (non-Javadoc)
	 * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
	 */
	/**
	 * handles the event when a key is pressed
	 * @param key the key which is pressed
	 * @param evt the delivered KeyEvent
	 */
	
	@Override
	public boolean onKeyDown(int key, KeyEvent evt) {
		
		switch(key) {
		
		case KeyEvent.KEYCODE_DPAD_LEFT:
			lastKeyMovement = MainChar.MOVE_LEFT;
//			Log.i("moveDir: ", String.valueOf(mainCharMoveDir));
			break; 
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			lastKeyMovement = MainChar.MOVE_RIGHT;
//			Log.i("moveDir: ", String.valueOf(mainCharMoveDir));
			break;
		/*case KeyEvent.KEYCODE_2: //down
			mainChar.moveUpDown(-stepWidth);
			break;
		case KeyEvent.KEYCODE_3: //up
			mainChar.moveUpDown(stepWidth);
			break;*/
		case KeyEvent.KEYCODE_S: 
			switchSensor();
			break;
		}
		
		return true; 
	}

	/**
	 * enables/disables the usage of the orientation sensor. Also registers or unregisters the listener for this sensor 
	 */
	public void switchSensor()
	{
		useSensor = !useSensor; 
		if (useSensor)
			OrientationManager.registerListener(orientationListener);
		else
			OrientationManager.unregisterListener(orientationListener);
	}
	
	/**
	 * Handles the event when a key is released
	 * @param key the key affected
	 * @param evt the delivered KeyEvent 
	 */
	@Override
	public boolean onKeyUp(int key, KeyEvent evt) {
		
//		mainCharMoveDir = MainChar.NO_MOVEMENT;
		return true; 
	}
	
	
	/**
	 * Handles the motion event, when they screen is touched 
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			released = false;
			lastMotionEvent = evt; 
		}
		else if (evt.getAction() == MotionEvent.ACTION_UP) {
			released = true;
		}		
		
		//hack to keep framerate stable (event spamming)
		try {Thread.sleep(1);} catch (InterruptedException e){}
		
		return true; 
	}
	
	/**
	 * Handle the movement when screen is touched.
	 *
	 * @param evt the event delivered by touching the screen 
	 */
	public void handleOnTouchMovement(final MotionEvent evt) {
		queueEvent(new Runnable(){
			public void run() 
			{	
				float x = evt.getRawX()*100.0f/screenWidth;
				float y = topBounds-evt.getRawY()*100.0f/screenHeight;
				
				if(hud.testPressed(x,y))
					return;
				
				float charX = mainChar.getPosition().x;
				if (x <  charX) {
					mainCharMoveDir = MainChar.MOVE_LEFT;
				} else if (x > (charX + mainChar.getWidth() )) {
					mainCharMoveDir = MainChar.MOVE_RIGHT;
				}
				else
				{
					mainCharMoveDir = MainChar.NO_MOVEMENT;
				}
			}
		});
	}
	
	/**
	 * Handle the roll movement using orientation sensor.
	 *
	 * @param moveDir the direction the mobile has been rolled
	 */
	public void handleRollMovement(int moveDir) 
	{
		if(useSensor)
			mainCharMoveDir = moveDir;
	}
	
	/**
	 * Sets the orientation listener.
	 *
	 * @param listener the OrientationListener to set
	 */
	public void setOrientationListener(OrientationListener listener) {
		orientationListener = listener; 
	}
	
	/**
	 * Gets the main char.
	 *
	 * @return the main char
	 */
	public MainChar getMainChar() {
		return mainChar;
	}

	/**
	 * Sets the main char.
	 *
	 * @param mainChar the mainChar to set
	 */
	public void setMainChar(MainChar mainChar) {
		this.mainChar = mainChar;
	}

	/**
	 * Fetches key move data.
	 */
	public void fetchKeyMoveData()
	{
		if(lastKeyMovement!=0)
		{
			mainCharMoveDir = lastKeyMovement;
			lastKeyMovement = 0;
		}
	}
	
	/**
	 * Gets the screen width.
	 *
	 * @return the screen width
	 */
	public float getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Sets the screen width.
	 *
	 * @param screenWidth the screen width to set
	 */
	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Gets the screen height.
	 *
	 * @return the screen height
	 */
	public float getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Sets the screen height.
	 *
	 * @param screenHeight the screen height to set
	 */
	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * Gets the aspect ratio.
	 *
	 * @return the aspect ratio
	 */
	public float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * Sets the aspect ratio.
	 *
	 * @param aspectRatio the aspect ratio to set
	 */
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/**
	 * Gets the right bounds.
	 *
	 * @return the right bounds
	 */
	public float getRightBounds() {
		return rightBounds;
	}

	/**
	 * Sets the right bounds.
	 *
	 * @param rightBounds the right bounds to set
	 */
	public void setRightBounds(float rightBounds) {
		this.rightBounds = rightBounds;
	}

	/**
	 * Gets the top bounds.
	 *
	 * @return the top bounds
	 */
	public float getTopBounds() {
		return topBounds;
	}

	/**
	 * Sets the top bounds.
	 *
	 * @param topBounds the top bounds to set
	 */
	public void setTopBounds(float topBounds) {
		this.topBounds = topBounds;
	}
		
	/**
	 * Checks if orientation sensor is used.
	 *
	 * @return true if the orientation sensor is in use, false otherwise
	 */
	public boolean isUseSensor() {
		return useSensor;
	}

	/**
	 * Sets if the sensor shall be used.
	 *
	 * @param useSensor true if the orientation sensor shall be used, false otherwise
	 */
	public void setUseSensor(boolean useSensor) {
		this.useSensor = useSensor;
	}

	/**
	 * Sets up the GL stuff.
	 *
	 * @param gl Reference to GL10 object
	 */
	private void setupGL(GL10 gl)
	{
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glShadeModel(GL10.GL_FLAT);
	    gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        
        gl.glClearColor(0, 0, 0, 0);
        
        Settings.GLES11Supported = (gl instanceof GL11);
	}
}
