/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * The standard RenderView enabling OpenGLES 10.
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class RenderView extends GLSurfaceView implements Renderer {


	protected GameManager gameManager;
	public TimeUtil timer;

	protected float gameTime;
	

	/**
	 * @param context The render context
	 * @param attr The attribute set
	 */
	public RenderView(Context context, AttributeSet attr)
	{
		super(context, attr);
		
		setRenderer(this); 
        setFocusable(true);
        requestFocus();
		setFocusableInTouchMode(true);
		
		gameManager = null;
		timer = TimeUtil.instance;
		gameTime = 60.f;
	}
	
	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {			

		//Settings
		gl.glClearColor(0.3f, 0.3f, 0.3f, 1); 		// Gray Background
		gl.glClearDepthf(1);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping		
		gl.glShadeModel(GL10.GL_FLAT);//SMOOTH); 			//Enable Smooth Shading
		
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DITHER);				//Disable dithering
		
		//gl.glEnable(GL10.GL_DEPTH_TEST);				
		//gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Really fast perspective calculations
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);        
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		// Rendering via arrays
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		// Create this here, so we can pass "gl" to the GameManager.
//		if (gameManager == null) {
			gameManager = new GameManager(this, gl);
//		}
	}

	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView#onPause()
	 */
	@Override
	public void onPause() {
		
		super.onPause();
	}



	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();		
		timer.reset();
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
		gl.glOrthof(0, width, 0, height, -10, 100);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}


	/* (non-Javadoc)
	 * @see android.view.View#onKeyUp(int, android.view.KeyEvent)
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
	
	

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// Be aware that the event Y axis is mirrored to the render Y axis. 
		float x = event.getX();
        float y = getHeight() - event.getY();        
        int action = event.getAction();
        
        // Handle only these 3 events
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
        	gameManager.touchEvent(x, y, action);
        }
        
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
