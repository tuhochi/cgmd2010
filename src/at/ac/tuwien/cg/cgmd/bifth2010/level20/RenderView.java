/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;

/**
 * The standard RenderView enabling OpenGLES 10.
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class RenderView extends GLSurfaceView implements Renderer {

		
	/*
	 * These variables store the previous X and Y
	 * values as well as a fix touch scale factor.
	 * These are necessary for the rotation transformation
	 * added to this lesson, based on the screen touches. ( NEW )
	 */
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.2f;		//Proved to be good for normal rotation ( NEW )

	
	/** The Activity Context */
//	private Activity activity;
//	private SessionState sessionState;		
	private GameManager gameManager;
	public TimeUtil timer;
	
	
//	private Cube cube;


	/**
	 * @param context
	 */
	/**
	 * @param activity
	 */
	public RenderView(Activity activity) {
		super(activity);
		
		//Set this as Renderer
		this.setRenderer(this);
		//Request focus, otherwise buttons won't react
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		
		// Store Activity (= Context)
//		this.activity = activity;
		// The SessionState is a convenience class to set a result
//		sessionState = new SessionState();
//		setProgress(0);

		timer = TimeUtil.getInstance();
		
		
	}
	
	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {			

		//Settings
		gl.glClearColor(0.3f, 0.3f, 0.3f, 1); 				// Gray Background
		gl.glClearDepthf(1);
		
//		gl.glDisable(GL10.GL_DITHER);				//Disable dithering ( NEW )
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		
		gl.glEnable(GL10.GL_DEPTH_TEST);				
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
		
		// Create this here, so we can pass "gl" to the GameManager.
		gameManager = new GameManager(this, gl);
	}

	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		// Advance in time
		timer.update();		
		float dt = timer.getDt();
		
		// Update the GameManager first
		gameManager.update(dt);
		
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
		
		// And then draw it
		gameManager.render(gl);

	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if(height == 0) {
			height = 1;
		}
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		// Use an Ortho projection
		gl.glOrthof(0, width, 0, height, -10, 1);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	/* ***** Listener Events ( NEW ) ***** */	
	/**
	 * Override the key listener to receive keyUp events.
	 * 
	 * Check for the DPad presses left, right, up, down and middle.
	 * Change the rotation speed according to the presses
	 * or change the texture filter used through the middle press.
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			gameManager.scrollSpeed += 20f;
		
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			gameManager.scrollSpeed -= 20f;
			
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			
			
		} else if(keyCode == KeyEvent.KEYCODE_BACK) {
			
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {

		}

		//We handled the event
		return true;
	}

	
	/**
	 * Override the touch screen listener.
	 * 
	 * React to moves and presses on the touchscreen.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// Be aware that the Y axis is opposite to the render Y axis. 
		float x = event.getX();
        float y = getHeight() - event.getY();
        
        gameManager.onTouch(x, y);
        
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	
        	gameManager.onTouch(x, y);
        }
        

//        Toast.makeText(getContext(), "Touch Event", 2);
        
        
//        //If a touch is moved on the screen
//        if(event.getAction() == MotionEvent.ACTION_MOVE) {
//        	//Calculate the change
//        	float dx = x - oldX;
//	        float dy = y - oldY;
//                	                
//        //A press on the screen
//        } else if(event.getAction() == MotionEvent.ACTION_UP) {
//
//        }
        
        //Remember the values
        oldX = x;
        oldY = y;
        
        //We handled the event
		return true;
	}
	
	/**
	 * @param p
	 */
//	public void setProgress(int p) {
//		
//		// We set the progress the user has made (must be between 0-100)
//		sessionState.setProgress(p);
//		// We call the activity's setResult method 
//		activity.setResult(Activity.RESULT_OK, sessionState.asIntent());
//		
//	}
}
