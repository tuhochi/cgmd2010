package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.PlayerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.TextureSingletons;

/**
 * 
 * @author group13
 * 
 * opengl renderer for all game objects
 *
 */
public class MyRenderer extends GLSurfaceView implements Renderer, IPersistence {

	/** step size for fixed time step algorithm */
	private static final long STEP = 50;

	/** rotation amount per fixed time step */
	private static final float ROTATIONINC = 4.5f;
	
	/** limit for discarding track ball events */
	private static final float TRACKBALLSENSITIVITY = 0.5f;

	/** width of screen */
	private static int screenWidth;

	/** height of screen */
	private static int screenHeight;

	/** last time of fixed time step algorithm */
	private long lastTime;

	/** rotation for whole game used for rendering */
	private float rotation;

	/** number of times player was rendered (used in invincible state for blinking) */
	private int renderedPlayer;

	/** zoomfactor for drunk state */
	private float zoomFactor;

	/** zoom for drunk state */
	private float zoom;

	/** attached context */
	private Context context;

	/** singleton object of fps-counter */
	private FPSCounter counter;

	/** accumulated time for fps-counter */
	private float accTime;

	/** singleton object of game control */
	private GameControl gameControl;


	/**
	 * @see IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		this.rotation = savedInstanceState.getFloat("l13_myRenderer_rotation");
	}


	/**
	 * @see IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		outState.putFloat("l13_myRenderer_rotation", this.rotation);
	}


	/**
	 * constructor initializes all members
	 * 
	 * @param context attached context
	 * @param attr the attributeset
	 */
	public MyRenderer(Context context, AttributeSet attr) {
		super(context, attr);

		//init members
		this.lastTime = System.currentTimeMillis();
		this.rotation = 0;
		this.renderedPlayer = 0;
		this.zoomFactor = 1.0f;
		this.zoom = 0.0f;
		this.accTime = 0;
		this.context = context; 
		this.counter = FPSCounter.getInstance();
		this.gameControl = GameControl.getInstance();

		//set renderer for view
		this.setRenderer(this);

		//make it focusable (for touch events)
		this.setFocusable(true);
	}



	/**
	 * called for drawing a frame
	 * 
	 * @see super#onDrawFrame(GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		//calculate and update fps
		counter.update();
		float dt = counter.getDt();
		accTime += dt/1000;
		if(accTime > 5)
		{
			accTime = 0;
		}
		long now = System.currentTimeMillis();

		//fixed time step algorithm
		while(lastTime + STEP <= now) {
			//update positions, states, etc.
			gameControl.update();

			//increase rotation if in rat-arsed state
			if(gameControl.isRatArsedState()) {
				rotation += ROTATIONINC;
			}
			else {
				//reset rotation
				rotation = 0;
			}

			//zoom in/out if in drunk state
			if(gameControl.isDrunkState()){
				if (zoomFactor > 1.1f){
					zoom = -0.01f;
				}

				if (zoomFactor <= 1.0f){
					zoom = 0.01f;
				}

				zoomFactor+=zoom;
			}
			else {
				zoomFactor = 1.0f;
			}

			lastTime += STEP;
		}

		//set up orthogonal projection
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, screenWidth*zoomFactor, 0, screenHeight*zoomFactor, -1.0f, 1.0f);

		//rotate whole scene
		gl.glTranslatef(gameControl.getPlayer().getRealPosition().x - GameObject.offset.x, gameControl.getPlayer().getRealPosition().y - GameObject.offset.y, 0);
		gl.glRotatef(rotation, 0, 0, 1);
		gl.glTranslatef((-1)*(gameControl.getPlayer().getRealPosition().x - GameObject.offset.x), (-1)*(gameControl.getPlayer().getRealPosition().y - GameObject.offset.y), 0);

		gl.glViewport(0, 0, screenWidth, screenHeight);

		//clear color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//draw all game objects (except for player object)
		for(GameObject gameObject : gameControl.getGameObjects()) {
			if(gameObject instanceof PlayerObject) {
				continue;
			}
			//only if object is active
			if(gameObject.isActive()){
				gameObject.draw(gl);
			}
		}

		//draw player
		if(gameControl.isInvincibleState()) {
			//simulate blinking effect of player in invincible state
			if(renderedPlayer == 8) {
				gameControl.getPlayer().draw(gl);
				renderedPlayer = 0;
			}
			else {
				renderedPlayer++;
			}
		}
		else {
			gameControl.getPlayer().draw(gl);
		}


		//set up camera for status-bars
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, screenWidth, 0, screenHeight, -1.0f, 1.0f);
		gl.glViewport(0, 0, screenWidth, screenHeight);
		//draw status-bars
		gameControl.getDrunkStatusBar().draw(gl);
		gameControl.getJailStatusBar().draw(gl);
		if(!gameControl.isJailState() && !gameControl.isRatArsedState()) {
			gameControl.getBeerStatusBar().draw(gl);
		}
	}

	/**
	 * called if screen changes
	 * 
	 * @param gl gl
	 * @param width screen-width
	 * @param height screen-height
	 * 
	 * @see super#onSurfaceChanged(GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//set new screen dimensions
		screenWidth = width;
		screenHeight = height;

		//set parallel projection
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width*zoomFactor, 0, height*zoomFactor, -1.0f, 1.0f);
		gl.glViewport(0, 0, width, height);
	}


	/**
	 * called when created
	 * 
	 * @see super{@link #onSurfaceCreated(GL10, EGLConfig)}
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//enable texture mapping
		gl.glEnable(GL10.GL_TEXTURE_2D);

		//enable blend capability
		gl.glEnable(GL10.GL_BLEND);

		//specify blend function
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		//set shading model
		gl.glShadeModel(GL10.GL_SMOOTH);

		//set background color
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

		//set display size
		screenHeight = this.getHeight();
		screenWidth = this.getWidth();

		//init all textures
		TextureSingletons.reset();
		TextureSingletons.initTextures(gl, context);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		//create all game objects
		gameControl.createGameObjects();

		//start background music
		SoundManager.init(context);
		SoundManager.getInstance().startMusic();
	}


	/**
	 * called when user touches screen
	 * 
	 * @see super#onTouchEvent(MotionEvent)
	 */

	public boolean onTouchEvent(MotionEvent event) {
		//only process event if touch is finished
		if(event.getAction() == MotionEvent.ACTION_UP) {
			//move player
			gameControl.movePlayer(event.getX(), event.getY());
		}
		return true;
	}


	/**
	 * handles key movement
	 * 
	 * @see super{@link #onKeyUp(int, KeyEvent)}
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode) {
		//move up
		case KeyEvent.KEYCODE_DPAD_UP:
			gameControl.movePlayer(screenWidth / 2.0f, 0);
			break;
			//move down
		case KeyEvent.KEYCODE_DPAD_DOWN:
			gameControl.movePlayer(screenWidth / 2.0f, screenHeight);
			break;
			//move left
		case KeyEvent.KEYCODE_DPAD_LEFT:
			gameControl.movePlayer(0, screenHeight / 2.0f);
			break;
			//move right
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			gameControl.movePlayer(screenWidth, screenHeight / 2.0f);
			break;
			//stop movement
		case KeyEvent.KEYCODE_DPAD_CENTER:
			gameControl.stopMovement();
		default:
			return false;
		}
		return true;
	}

	/**
	 * handles trackball movement
	 * 
	 * @see super{@link #onTrackballEvent(MotionEvent)}
	 */
	public boolean onTrackballEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			//get trackball direction
			float x = event.getX();
			float y = event.getY();

			//calculate dominating direction
			if(Math.abs(x) > Math.abs(y)) {
				//discard event if trackball was not enough moved
				if(Math.abs(x) > TRACKBALLSENSITIVITY) {
					if(x > 0) {
						//move right
						gameControl.movePlayer(screenWidth, screenHeight / 2.0f);
					}
					else {
						//move left
						gameControl.movePlayer(0, screenHeight / 2.0f);
					}
				}
			}
			else {
				//discard event if trackball was not enough moved
				if(Math.abs(y) > TRACKBALLSENSITIVITY) {
					if(y > 0) {
						//move up
						gameControl.movePlayer(screenWidth / 2.0f, screenHeight);
					}
					else {
						//move down
						gameControl.movePlayer(screenWidth / 2.0f, 0);
					}
				}
			}
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN) {
			//stop movement
			gameControl.stopMovement();
			return true;
		}
		return false;
	}


	/**
	 * getter for screen-width
	 * 
	 * @return screen-width
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}


	/**
	 * getter for screen-height
	 * 
	 * @return screen-height
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}
}
