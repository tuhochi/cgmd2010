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
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.VirtualFinger;

/**
 * The Surface View
 *
 */

public class GameScene extends GLSurfaceView implements Renderer {
	/**
	 * State of the Game
	 * @author Matthias
	 *
	 */
	public enum CurrentState {
		INTRO, RUNNING, EXTRO
	}
	
	/** the context of the scene */
	private LevelActivity activity = null;
	/** the flying rabbit */
	private PhysicalRabbit rabbit;
	/** the crosshairs that shoot on the rabbit */
	private Crosshairs crosshairs;
	/** the landscape moving with parallax-effect */
	private Landscape landscape;
	/** a coin sprite for drawing the OSD */
	private Sprite coin;
	/** the time display sprite */
	private Sprite timeDisplay;
	/** background during intro */
	private IntroBackground introBackground;
	/** virtual finger for demo */
	private VirtualFinger virtualFinger;

	/** thread for game logic */
	private GameThread gameThread;
	/** manages the available game time */
	private TimeManager timeManager;

	/** the inputgesture to process */
	private InputGesture input = null;
	/** the system's vibrator */
	private Vibrator vibrator = null;
	
	/** restored game state */
	private GameState gameState = null;
	/** current state of game */
	private CurrentState currentState = CurrentState.INTRO;
	
	/** The master object scaling value */
	private float masterScale = 1f;

	/**
	 * Creates the GameScene
	 * @param context The Context of the Scene
	 */
	public GameScene(LevelActivity context) {
		super(context);
		
		this.activity = context;
		
		// get the system's vibrator
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
		System.err.println("GameScene created");
		
		rabbit = null;
		landscape = null;
		introBackground = null;
		coin = null;
		timeDisplay = null;
		gameThread = null;
		timeManager = new TimeManager();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glScalef(masterScale, masterScale, 1);
		
		// Intro-Mode
		if (this.getCurrentState().equals(CurrentState.INTRO)) {
			introBackground.draw(gl);
			//rabbit.draw(gl);
			//virtualFinger.draw(gl);
		}
		
		// Running-Mode or Extro-Mode
		else if (this.getCurrentState().equals(CurrentState.RUNNING) || this.getCurrentState().equals(CurrentState.EXTRO)) {
			// first draw landscape and rabbit
			if (landscape != null) {
				landscape.draw(gl);
			}
			

			// then draw the crosshairs
			if (crosshairs != null && this.getCurrentState().equals(CurrentState.RUNNING)) {
				crosshairs.draw(gl);
			}
			
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

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 1);
		gl.glClearColorx((int)(255*.5), (int)(255*.7), 255, 255);
	}
	
	/**
	 * Initializes a texture
	 * @param gl
	 * @param texture
	 */
	private void configureTexture(GL10 gl, Texture texture) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureName());
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		int width = getWidth();
		int height = getHeight();
		
		/**
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
		
		/** 
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
		virtualFinger = new VirtualFinger(mainTexture, width, height);
		virtualFinger.setGesture(VirtualFinger.DemoGesture.SWIPE_RIGHT);
		
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
	 * restart the game thread
	 */
	private void restartGameThread() {
		stopGameThread();
		
		if (rabbit != null && landscape != null && crosshairs != null && timeManager != null && introBackground != null) {
			gameThread = new GameThread(this, rabbit, landscape, crosshairs, timeManager, introBackground);
			gameThread.start();
		}
	}
	
	/**
	 * stop the game thread
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
	
	@Override
	public void onPause() {
		super.onPause();
		stopGameThread();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		restartGameThread();
	}
	
	/**
	 * @param gesture the new input gesture
	 */
	public void addInputGesture(InputGesture gesture) {
		input = gesture;
	}
	
	/**
	 * @return the current input gesture
	 */
	public InputGesture getNextInputGesture() {
		/* get the next input gesture and remove it
		 * if it exists at all (set to null) */
		InputGesture result = input;
		input = null;
		return result;
	}
	
	/**
	 * @return the device's vibrator
	 */
	public Vibrator getVibrator() {
		return vibrator;
	}
	
	/**
	 * @return the rabbit the user controls
	 */
	public PhysicalRabbit getRabbit() {
		return rabbit;
	}

	/**
	 * @return the current score, between 0 and 100
	 */
	public int getScore() {
		return (CoinBucketSprite.FULL_COIN_COUNT - rabbit.getCoinCount())*10;
	}
	
	/**
	 * finish the level
	 */
	public void finishLevel() {
		activity.finishLevel(getScore());
	}

	/**
	 * permanently store the current instance state
	 * @param outState
	 */
	public void saveInstanceState(Bundle outState) {
		System.err.println("Saving instance state");
		
		gameState = new GameState();
		gameState.saveTimeManger(timeManager);
		gameState.saveCrosshairs(crosshairs);
		gameState.saveRabbit(rabbit);
		gameState.saveCurrentState(this);
		
		outState.putSerializable(GameState.KEY, gameState);
	}

	/**
	 * restore the saved instance state
	 * @param savedInstanceState
	 */
	public void restoreInstanceState(Bundle savedInstanceState) {
		System.err.println("Restoring instance state");
		
		Serializable restoredState = savedInstanceState.getSerializable(GameState.KEY);
		
		if (restoredState != null && restoredState instanceof GameState) {
			gameState = (GameState)restoredState;
		}		
	}

	/**
	 * @param currentState the new state of the game
	 */
	public synchronized void setCurrentState(CurrentState currentState) {
		this.currentState = currentState;
	}

	/**
	 * @return the state of the game
	 */
	public synchronized CurrentState getCurrentState() {
		return currentState;
	}
}
