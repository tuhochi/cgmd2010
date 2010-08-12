package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import java.io.Serializable;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Vibrator;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.CoinBucketSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.IntroBackground;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Landscape;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.TextureParts;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.TimeDisplay;

/**
 * The Game Scene
 * 
 * This class takes care of managing the OpenGL ES-related rendering and
 * creating the objects inside the level (rabbit, crosshairs, landscape).
 * 
 * It also partially manages the saving and restoring of the game state as
 * part of the Android life-cycle.
 *
 * @author Matthias Tretter
 * @author Thomas Perl
 */

public class GameScene extends GLSurfaceView implements Renderer {
	/** Valid states this game scene can be in */
	public enum CurrentState {
		INTRO,   /** Introductory instructions screen */
		RUNNING, /** Interactive gameplay */
		EXTRO,   /** Non-interactive "rabbit flies away" animation */
	}
	
	/** The surrounding Activity in which this scene is embedded */
	private LevelActivity activity = null;
	/** The rabbit head, including all surrounding assets (bucket, ...) */
	private PhysicalRabbit rabbit;
	/** The crosshairs that roams around the screen and shoots the rabbit */
	private Crosshairs crosshairs;
	/** Decorative scene surroundings (sky, clouds, meadow and mountains) */
	private Landscape landscape;
	/** Single coin used for drawing the upper left on-screen display */
	private Sprite coin;
	/** Time "cake" used in the upper right corner of the screen */
	private Sprite timeDisplay;
	/** Instructions screen (background + moving arrows) */
	private IntroBackground introBackground;

	/** Thread for the game logic and animations */
	private GameThread gameThread;
	/** Interruptable countdown timer */
	private TimeManager timeManager;

	/** The most recent input gesture received */
	private InputGesture input = null;
	/** A reference to the phone's vibration motor */
	private Vibrator vibrator = null;
	
	/** Entity object for a game state to be restored */
	private GameState gameState = null;
	/** The current mode this game scene is in at the moment */
	private CurrentState currentState = CurrentState.INTRO;
	
	/** The visual scaling factor that will be applied to the whole scene */
	private float masterScale = 1f;

	/**
	 * Create a new game scene
	 * 
	 * @param context The LevelActivity this scene is displayed in
	 */
	public GameScene(LevelActivity context) {
		super(context);
		
		this.activity = context;
		
		// get the system's vibrator
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
		
		rabbit = null;
		landscape = null;
		introBackground = null;
		coin = null;
		timeDisplay = null;
		gameThread = null;
		timeManager = new TimeManager();
	}

	/**
	 * Draw the current scene onto a GL10 context
	 * 
	 * The content will be scaled by the masterScale factor.
	 * 
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glScalef(masterScale, masterScale, 1);
		
		// Intro-Mode
		if (this.getCurrentState().equals(CurrentState.INTRO)) {
			introBackground.draw(gl);
		}
		
		// Running-Mode or Extro-Mode
		else if (this.getCurrentState().equals(CurrentState.RUNNING) || this.getCurrentState().equals(CurrentState.EXTRO)) {
			/** Game elements: */
			
			// first draw landscape and rabbit
			if (landscape != null) {
				landscape.draw(gl);
			}
			
			// then draw the crosshairs
			if (crosshairs != null && this.getCurrentState().equals(CurrentState.RUNNING)) {
				crosshairs.draw(gl);
			}
			
			/** OSD Elements: */
			
			// draw the coins that are left to loose
			if (rabbit != null && coin != null) {
				int coins = rabbit.getCoinCount();
				
				for (int i=0; i<coins; i++) {
					coin.setPosition(20+i*coin.getWidth()/2, 20);
					coin.draw(gl);				
				}
			}
			
			// draw the time left
			if (timeDisplay != null) {
				timeDisplay.draw(gl);
			}
		}
	}

	/**
	 * Set up the OpenGL ES projection matrix and clear color
	 * 
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 1);
		gl.glClearColorx((int)(255*.5), (int)(255*.7), 255, 255);
	}
	
	/**
	 * Initializes a texture with default parameters
	 * 
	 * This will set the parameters for the textures to known-good
	 * default values, as used by our game.
	 * 
	 * @param gl An OpenGL ES 1.1 context
	 * @param texture The Texture object to be configured
	 */
	private void configureTexture(GL10 gl, Texture texture) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureName());
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	}

	/**
	 * (Re-)Initialize the game elements and (re-)start the game thread
	 * 
	 * This function takes care of setting up and loading textures
	 * and in-game objects and will optionally restore a previously
	 * saved game state into the created objects. 
	 * 
	 * @param gl An OpenGL ES 1.1 context
	 * @param config Unused
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		int width = getWidth();
		int height = getHeight();
		
		/*
		 * Workaround: In some cases (e.g. when locking the screen),
		 * width and height are exchanged until the system realizes
		 * that we want landscape mode. We simply detect this here,
		 * and do the initialization with the values switched, because
		 * the system will switch to landscape mode later on.
		 **/
		if (width < height) {
			width = getHeight();
			height = getWidth();
		}
		
		/* 
		 * Magnify the game field for Nexus One and other phones.
		 * 
		 * We do not yet have HDPI-resolution textures, because they
		 * do not fit into the memory of the Nexus One (Memory allocation
		 * error when trying to load the 2048x2048 texture). Instead,
		 * we simply upscale the game field (and the "lo-res" textures)
		 * to the screen size.
		 **/
		if (width == 800) {
			masterScale = 1.3f;
		}
		
		/** Scale the "virtual" screen size accordingly */
		width /= masterScale;
		height /= masterScale;

		Texture mainTexture = new Texture(gl, getContext(), R.drawable.l44_texture);
		configureTexture(gl, mainTexture);
		
		rabbit = new PhysicalRabbit(new RabbitSprite(mainTexture), new Sprite(TextureParts.makeCoin(mainTexture)), width, height);
		rabbit.setPosition(width/2, height/2);
		
		crosshairs = new Crosshairs(this, mainTexture, width, height);
		crosshairs.setPosition(30, height/2);
		crosshairs.setRabbit(rabbit);
		
		landscape = new Landscape(mainTexture, width, height);
		landscape.setRabbit((PhysicalRabbit)rabbit);
		
		coin = new Sprite(TextureParts.makeCoin(mainTexture));
		
		timeDisplay = new TimeDisplay(activity, mainTexture, timeManager);
		timeDisplay.setPosition(width-timeDisplay.getWidth()-10, 10);
		
		introBackground = new IntroBackground(mainTexture, width, height);
		
		if (gameState != null) {
			/* consume restored values and remove gameState */
			gameState.restoreTimeManger(timeManager);
			gameState.restoreCrosshairs(crosshairs);
			gameState.restoreRabbit(rabbit);
			gameState.restoreCurrentState(this);
			
			gameState = null;
		}
		
		restartGameThread();
	}
	
	/**
	 * (Re-)Start the game thread associated with this scene
	 * 
	 * An existing game thread will be stopped if possible. The
	 * time manager will be stopped and then re-started after
	 * the new thread has been created.
	 * 
	 * A new thread will not be created if child objects have
	 * not been created by onSurfaceCreated yet, but this method
	 * is usually called at the end of onSurfaceCreated, so no
	 * problem there :)
	 */
	private void restartGameThread() {
		stopGameThread();
		
		if (rabbit != null && landscape != null && crosshairs != null && timeManager != null && introBackground != null) {
			gameThread = new GameThread(this, rabbit, landscape, crosshairs, timeManager, introBackground);
			gameThread.start();
		}
	}
	
	/**
	 * Stop the game thread (if running) and pause the time manager
	 * 
	 * If no game thread is running, no game thread will be stopped.
	 * If no time manager is set up, it will (obviously) not be paused.
	 */
	public void stopGameThread() {
		if (gameThread != null) {
			gameThread.doQuit();
			gameThread = null;
		}	
		
		if (timeManager != null) {
			timeManager.onPause();
		}
	}
	
	/**
	 * Called by the LevelActivity when the activity is paused
	 * 
	 * This will stop the running game thread.
	 */
	@Override
	public void onPause() {
		super.onPause();
		stopGameThread();
	}
	
	/**
	 * Called by the LevelActivity when the activity is resumed
	 * 
	 * This will re-start the game thread.
	 */
	@Override
	public void onResume() {
		super.onResume();
		restartGameThread();
	}
	
	/**
	 * Add a new input gesture as received by the input framework
	 * 
	 * @param gesture The most recent input gesture received
	 */
	public void addInputGesture(InputGesture gesture) {
		input = gesture;
	}
	
	/**
	 * Get the next input gesture that is to be processed
	 * 
	 * @return The latest InputGesture received by this scene
	 */
	public InputGesture getNextInputGesture() {
		/* get the next input gesture and remove it
		 * if it exists at all (set to null) */
		InputGesture result = input;
		input = null;
		return result;
	}
	
	/**
	 * Get the vibrator object associated with this scene
	 * 
	 * @return A reference to the device's vibrator
	 */
	public Vibrator getVibrator() {
		return vibrator;
	}
	
	/**
	 * Get the physical rabbit head object
	 * 
	 * @return The rabbit that is controlled by the user
	 */
	public PhysicalRabbit getRabbit() {
		return rabbit;
	}

	/**
	 * Get the current score for the framework
	 * 
	 * @return The current score - a value between 0 and 100
	 */
	public int getScore() {
		return (CoinBucketSprite.FULL_COIN_COUNT - rabbit.getCoinCount())*10;
	}
	
	/**
	 * Finish the level and return the current score
	 * 
	 * The current score will be returned to the calling
	 * activity as defined by the CGMD Framework.
	 */
	public void finishLevel() {
		activity.finishLevel(getScore());
	}

	/**
	 * Serialize (and store) the current game state
	 * 
	 * This will be called in cases where the Android system
	 * MIGHT kill our application while it's in the background.
	 * 
	 * It's not guranteed that our application will be killed,
	 * so we store the data in a member variable for the case
	 * where our activity isn't destroyed, but re-initialized.
	 * 
	 * @param outState A bundle into which to store the state
	 */
	public void saveInstanceState(Bundle outState) {
		gameState = new GameState();
		gameState.saveTimeManger(timeManager);
		gameState.saveCrosshairs(crosshairs);
		gameState.saveRabbit(rabbit);
		gameState.saveCurrentState(this);
		
		outState.putSerializable(GameState.KEY, gameState);
	}

	/**
	 * Restore a (previously saved) game state
	 * 
	 * This will be called by the Android system if our app was
	 * killed in the background and it needs restoring. The saved
	 * game state will simply be saved as a member variable of this
	 * object, ready to be merged into the current state in the
	 * function onSurfaceCreated.
	 * 
	 * This will not be called if we were in the background, but
	 * were not killed by the framework.
	 * 
	 * @param savedInstanceState A bundle containing our stored state
	 */
	public void restoreInstanceState(Bundle savedInstanceState) {
		Serializable restoredState = savedInstanceState.getSerializable(GameState.KEY);
		
		if (restoredState != null && restoredState instanceof GameState) {
			gameState = (GameState)restoredState;
		}		
	}

	/**
	 * Set a new state for this scene
	 * 
	 * @param currentState The new state of this scene
	 */
	public synchronized void setCurrentState(CurrentState currentState) {
		this.currentState = currentState;
	}

	/**
	 * Get the current state of this scene
	 * 
	 * @return The current state of this scene
	 */
	public synchronized CurrentState getCurrentState() {
		return currentState;
	}
}
