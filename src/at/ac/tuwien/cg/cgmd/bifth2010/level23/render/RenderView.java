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


public class RenderView extends GLSurfaceView implements GLSurfaceView.Renderer {
	
	private float screenWidth;
	private float screenHeight;
	private float aspectRatio;
	
	private float rightBounds=100.0f;
	private float topBounds=100.0f;
	
	private float balloonHeight=0;
	private static RenderView instance;
	
	private Context context; 
	private MainChar mainChar; 
	private Background background;
	private Hud hud;
	private boolean released = true; 
	private MotionEvent lastMotionEvent = null; 
	private float accTime;
	private OrientationListener orientationListener; 
	
	private boolean useSensor = false;
	
	private int mainCharMoveDir;
	private int lastKeyMovement;
	private TimeUtil timer;
	private TextureManager textureManager; 
	private Serializer serializer; 
	
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
	
	public static RenderView getInstance()
	{
		return instance;
	}
	
	public void persistSceneEntities() {
		Serializer.getInstance().serializeObjects(mainChar, background);
	}
	
	public void restoreSceneEntities() {
		HashMap<Integer, SceneEntity> map = Serializer.getInstance().getSerializedObjects();
		mainChar = (MainChar)map.get(Serializer.SERIALIZED_MAINCHAR);
		background = (Background)map.get(Serializer.SERIALIZED_BACKGROUND);
	}
	
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

	public void switchSensor()
	{
		useSensor = !useSensor; 
		if (useSensor)
			OrientationManager.registerListener(orientationListener);
		else
			OrientationManager.unregisterListener(orientationListener);
	}
	
	@Override
	public boolean onKeyUp(int key, KeyEvent evt) {
		
//		mainCharMoveDir = MainChar.NO_MOVEMENT;
		return true; 
	}
	
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
	
	public void handleRollMovement(int moveDir) 
	{
		if(useSensor)
			mainCharMoveDir = moveDir;
	}
	
	public void setOrientationListener(OrientationListener listener) {
		orientationListener = listener; 
	}
	
	/**
	 * @return the mainChar
	 */
	public MainChar getMainChar() {
		return mainChar;
	}

	/**
	 * @param mainChar the mainChar to set
	 */
	public void setMainChar(MainChar mainChar) {
		this.mainChar = mainChar;
	}

	public void fetchKeyMoveData()
	{
		if(lastKeyMovement!=0)
		{
			mainCharMoveDir = lastKeyMovement;
			lastKeyMovement = 0;
		}
	}
	
	/**
	 * @return the screenWidth
	 */
	public float getScreenWidth() {
		return screenWidth;
	}

	/**
	 * @param screenWidth the screenWidth to set
	 */
	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * @return the screenHeight
	 */
	public float getScreenHeight() {
		return screenHeight;
	}

	/**
	 * @param screenHeight the screenHeight to set
	 */
	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * @return the aspectRatio
	 */
	public float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * @param aspectRatio the aspectRatio to set
	 */
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/**
	 * @return the rightBounds
	 */
	public float getRightBounds() {
		return rightBounds;
	}

	/**
	 * @param rightBounds the rightBounds to set
	 */
	public void setRightBounds(float rightBounds) {
		this.rightBounds = rightBounds;
	}

	/**
	 * @return the topBounds
	 */
	public float getTopBounds() {
		return topBounds;
	}

	/**
	 * @param topBounds the topBounds to set
	 */
	public void setTopBounds(float topBounds) {
		this.topBounds = topBounds;
	}
		
	/**
	 * @return the useSensor
	 */
	public boolean isUseSensor() {
		return useSensor;
	}

	/**
	 * @param useSensor the useSensor to set
	 */
	public void setUseSensor(boolean useSensor) {
		this.useSensor = useSensor;
	}

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
