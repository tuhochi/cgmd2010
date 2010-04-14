/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * @author Reinbert
 *
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
	private Activity activity;
	private SessionState sessionState;		
	private GameManager gameMgr;
	
	
	private Cube cube;


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
		this.activity = activity;
		// The SessionState is a convenience class to set a result
		sessionState = new SessionState();
		setProgress(0);
		
			
	}
	
	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {			

		//Settings
//		gl.glDisable(GL10.GL_DITHER);				//Disable dithering ( NEW )
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0, 0, 0, 1); 		//Gray Background
		gl.glClearDepthf(1); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
		gameMgr = new GameManager(gl, activity);

		
		
	}

	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gameMgr.update();
		
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix

//		gl.glTranslatef(0.0f, 0.0f, -5f);
		
		
		gameMgr.renderEntities();

	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}
		
//		System.out.println(width + " | " + height);
//		System.out.println(getWidth() + " | " + getHeight());

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix
		
		getWidth();

		//Calculate The Aspect Ratio Of The Window
//		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
//		GLU.gluOrtho2D(gl, left, right, bottom, top)2D(gl, 0, width, 0, height);
		
		gl.glOrthof(0, width, 0, height, -10, 10);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix

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
		
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
		
			
		} else if(keyCode == KeyEvent.KEYCODE_BACK) {
			setProgress(5);
			activity.finish();
			
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
		//
		float x = event.getX();
        float y = event.getY();
        
        //If a touch is moved on the screen
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	//Calculate the change
        	float dx = x - oldX;
	        float dy = y - oldY;
                	                
        //A press on the screen
        } else if(event.getAction() == MotionEvent.ACTION_UP) {

        }
        
        //Remember the values
        oldX = x;
        oldY = y;
        
        //We handled the event
		return true;
	}
	
	/**
	 * @param p
	 */
	public void setProgress(int p) {
		
		// We set the progress the user has made (must be between 0-100)
		sessionState.setProgress(p);
		// We call the activity's setResult method 
		activity.setResult(Activity.RESULT_OK, sessionState.asIntent());
		
	}
}
