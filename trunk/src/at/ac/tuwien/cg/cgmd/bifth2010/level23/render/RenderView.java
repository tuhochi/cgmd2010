package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Background;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.ObstacleManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Serializer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;

/**
 * The Class RenderView implements the renderer to render all needed objects.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class RenderView extends GLSurfaceView implements GLSurfaceView.Renderer {
	
	 /** FpsHandle Class */
	class FpsHandle implements Runnable{
		  
    	@Override
        public void run() {
            context.fpsChanged(timer.getFPS());
        }
    };
    
    class ScoreHandle implements Runnable{
		  
    	@Override
        public void run() {
            context.scoreChanged(score);
        }
    };
    
    /** The instance of the RenderView to pass it around. */
	public static RenderView instance;
    
	public static int gameState=0;
	
	public static final int INTRO=0;
	public static final int INGAME=1;
	public static final int GAMEOVER=2;
	
    /** Indicates if the ObstacleManager should be reset */
    private boolean isInitialized = false;
    
    /** Handles fps TextView */
	private FpsHandle fpsHandle;
	
	/** The Sound Manager */
	private SoundManager soundManager;
	
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
	public float balloonHeight;
		
	/** The Android context . */
	private LevelActivity context; 
	
	/** The main character. */
	private MainChar mainChar; 
	
	/** The background. */
	private Background background;
	
	/** The hud. */
	private Hud hud;
	
	/** The boolean that checks if the screen is pressed or released . */
	private boolean released = true; 
	
	/** The boolean that states if the level is game over */
	private boolean gameOver = false; 
	
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
	
	/** The obstacle manager */
	private ObstacleManager obstacleManager;
	
	/** the id from the audio player for start sound */
	private int startAudioId; 
	
	/** boolean indicating the first start */
	private boolean firstStart; 
	
	private CutScenes cutScenes;
	
	private int score;

	private int lastScore;
	
	private ScoreHandle scoreHandle;
	
	/**
	 * Instantiates a new render view.
	 *
	 * @param context the Android context
	 */
	public RenderView(Context context, AttributeSet attr)
	{
		super(context, attr);
		instance=this;
		
		this.context = (LevelActivity)context; 
		
		setRenderer(this); 
        setFocusable(true);
        requestFocus();
        
        timer = TimeUtil.instance;
        textureManager = TextureManager.instance;
        serializer = Serializer.getInstance();
        serializer.setContext(context);
        
        fpsHandle = new FpsHandle();
        scoreHandle = new ScoreHandle();
        
        soundManager = new SoundManager(context);
        obstacleManager = new ObstacleManager();
        cutScenes = new CutScenes();
        
        balloonHeight=0;   
        
        Display display = this.context.getWindowManager().getDefaultDisplay();
		aspectRatio = (float)display.getHeight()/(float)display.getWidth();
		topBounds = rightBounds*aspectRatio;
        
        hud = new Hud();
		mainChar = new MainChar();
		background = new Background();
		
		startAudioId = SoundManager.instance.requestPlayer(R.raw.l23_timerandstart, false);
		firstStart = true; 
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
		
		accTime += dt/1000;
		if(accTime > 0.5)
		{
			fpsChanged();
			accTime = 0;
		}

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		switch(gameState)
		{
			case INTRO:
				if(cutScenes.renderIntroScene(dt))
					gameState=INGAME;
				break;
			case INGAME:	
				if (!released)
					handleOnTouchMovement(lastMotionEvent);
				else
				{	
					if(!useSensor)
						mainCharMoveDir = MainChar.NO_MOVEMENT;
				}
				
				fetchKeyMoveData();
				
				balloonHeight += dt*Settings.BALLOON_SPEED;
				score = (int)(balloonHeight*Settings.SCOREHEIGHT_MODIFIER);
				if(lastScore != score)
				{
					scoreChanged();
					lastScore = score;
				}
				mainChar.update(dt,mainCharMoveDir);
				background.update(dt);
				background.render();
				obstacleManager.renderVisibleObstacles((int)balloonHeight);
				hud.render();
				mainChar.render();
				break;
			case GAMEOVER:		
				background.render();
				mainChar.renderGameOver(dt);
				obstacleManager.renderVisibleObstacles((int)balloonHeight);
				break;
		}		
		
		//Log.v("Balloon Height: ", String.valueOf(balloonHeight));
		if (firstStart) {
			if (soundManager.isPlaying(startAudioId))
					soundManager.pausePlayer(startAudioId);
			
			soundManager.startPlayer(startAudioId);
			firstStart = false; 
		}
		
	
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
		Display display = this.context.getWindowManager().getDefaultDisplay();
		aspectRatio = (float)display.getHeight()/(float)display.getWidth();
		topBounds = rightBounds*aspectRatio;
		
		Log.v("RenderView.java", "onSurfaceCreated");
		
		setupGL(gl);
		
		reset();
						
		int resID = context.getResources().getIdentifier("l23_balloon", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		mainChar.setTextureID(textureManager.getTextureId(context.getResources(), resID));
		resID = context.getResources().getIdentifier("l23_bg", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		background.setTextureID(textureManager.getTextureId(context.getResources(), resID));
		resID = context.getResources().getIdentifier("l23_intro", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		cutScenes.introTexId = textureManager.getTextureId(context.getResources(), resID);
		setGameOver(false); 
		
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
			break; 
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			lastKeyMovement = MainChar.MOVE_RIGHT;
			break;
		case KeyEvent.KEYCODE_S: 
			switchSensor();
			break;
		}
		
		return true; 
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
		
		if (isGameOver())
			return; 
		
		queueEvent(new Runnable(){
			public void run() 
			{	
				float x = evt.getRawX()*100.0f/screenWidth;
				float y = topBounds-evt.getRawY()*(100.0f*aspectRatio)/screenHeight;
				
				System.out.println("touchX: " + x );
				System.out.println("touchY: " + y );
				
				if(hud.testPressed(x,y) || useSensor)
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
	 * Gets the main char.
	 *
	 * @return the main char
	 */
	public MainChar getMainCharInstance() {
		return mainChar;
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
	
	/** 
	 * triggers fps textview in the activity thread
	 */
	public void fpsChanged() {	        
        LevelActivity.handler.post(fpsHandle);	
	}
	
	/** 
	 * triggers score textview in the activity thread
	 */
	public void scoreChanged() {	        
        LevelActivity.handler.post(scoreHandle);	
	}
	
	/**
	 * Sets the status of the level to game over or not
	 * @param gameOver boolean indicates if the level is game over
	 */
	public void setGameOver(boolean gameOver) {

		
		this.gameOver = gameOver; 
		
		if(gameOver)
		{
			soundManager.pauseAllAudio();
			gameState = GAMEOVER;
		}
		
		mainChar.setGameOver(gameOver);	
		background.setGameOver(gameOver); 
		obstacleManager.setGameOver(gameOver);
	}

	/**
	 * Checks if the level is game over
	 * @return true if game over, else otherwise
	 */
	public boolean isGameOver() {
		return gameOver; 
	}
	
	/**
	 * Reset the scene and managers
	 **/
	public void reset()
	{
		textureManager.reset();
		timer.reset();
		background.reset();
		mainChar.preprocess();
		hud.preprocess();
		cutScenes.preprocess();
		obstacleManager.preprocess();
		
		
		if(!isInitialized)
		{	
			hud.reset();
			mainChar.reset();
			obstacleManager.reset();
			timer.resetTimers();
			Settings.BALLOON_SPEED = Settings.BALLOON_STARTSPEED;
			gameState = INTRO;
			isInitialized = true;
		}
	}
	
	/**
	 * Writes to DataOutputStream 
	 * @param dos the stream to write to 
	 */
	public void writeToStream(DataOutputStream dos) {
		try {
			// local
			dos.writeInt(gameState);
			dos.writeBoolean(isInitialized);
			dos.writeBoolean(released);
			dos.writeInt(mainCharMoveDir);
			dos.writeFloat(balloonHeight);
			dos.writeBoolean(gameOver);
			
			// remote
			dos.writeFloat(timer.getAccFrameTimes());
			dos.writeBoolean(hud.isMoneyButtonActive());
			
			// propagate
			mainChar.writeToStream(dos); 
			background.writeToStream(dos); 
			obstacleManager.writeToStream(dos); 
			
		} catch (Exception e) {
			System.out.println("Error writing to stream in RenderView: "+e.getMessage());
		}
	}
	
	/**
	 * Writes to bundle
	 * @param bundle the Bundle to write to
	 */
	public void writeToBundle(Bundle bundle) 
	{
		obstacleManager.writeToBundle(bundle);	
		timer.writeToBundle(bundle);
	}
	
	/**
	 * Reads from bundle
	 * @param bundle the Bundle to write to
	 */
	public void readFromBundle(Bundle bundle) 
	{
		obstacleManager.readFromBundle(bundle);
		timer.readFromBundle(bundle);
	}
	
	/**
	 * Reads from stream
	 * @param dis the DataInputStream to read from
	 */
	public void readFromStream(DataInputStream dis) {
		try {
			gameState = dis.readInt();
			isInitialized = dis.readBoolean();
			released = dis.readBoolean(); 
			mainCharMoveDir = dis.readInt(); 
			balloonHeight = dis.readFloat(); 
			gameOver = dis.readBoolean();
			
			timer.setAccFrameTime(dis.readFloat()); 
			hud.setMoneyButtonActive(dis.readBoolean());
			mainChar.readFromStream(dis); 
			background.readFromStream(dis); 
			obstacleManager.readFromStream(dis); 
			
		} catch (Exception e) {
			System.out.println("Error reading from stream in RenderView.java: "+e.getMessage()); 
		}
	}
	
	/**
	 * @return the aspectRatio
	 */
	public float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * @return the rightBounds
	 */
	public float getRightBounds() {
		return rightBounds;
	}

	/**
	 * @return the topBounds
	 */
	public float getTopBounds() {
		return topBounds;
	}
	
}
