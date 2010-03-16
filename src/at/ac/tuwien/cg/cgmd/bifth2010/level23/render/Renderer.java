package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity;
import android.util.Log;

public class Renderer extends GLSurfaceView implements GLSurfaceView.Renderer {

	private ArrayList<SceneEntity> sceneEntities;
	private Context context; 
	private MainChar mainChar; 
	private boolean released = true; 
	private boolean lastDirection; 
	private int stepWidth = 5; 
	private MotionEvent lastMotionEvent = null; 
	public Renderer(Context context)
	{
		
		super(context);
		this.context = context; 
		setRenderer(this); 
		mainChar = new MainChar(50,50,new Vector2(100,0));
		sceneEntities = new ArrayList<SceneEntity>();
		sceneEntities.add(mainChar);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClearColor(1,0,0,0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//check if needed for all parts of the scene (hud?)
		//add textures etc.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		for(SceneEntity entity : sceneEntities)
			entity.render();

		if (!released)
			handleOnTouchMovement(lastMotionEvent);
		
		//check if needed for all parts of the scene (hud?)
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//setup the Viewport with an Orthogonal View 1 unit = 1 pixel
		//0 0 is bottom left
		gl.glOrthof(0, width, 0, height, 0.01f, 10);
		gl.glViewport(0, 0, width, height);		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		
	}
	
	
	public boolean onKeyDown(int key, KeyEvent evt) {
		switch(key) {
		case KeyEvent.KEYCODE_0: //left
			mainChar.moveLeftRight(-stepWidth);
			break; 
		case KeyEvent.KEYCODE_1: //right
			mainChar.moveLeftRight(stepWidth);
			break;
		case KeyEvent.KEYCODE_2: //down
			mainChar.moveUpDown(-stepWidth);
			break;
		case KeyEvent.KEYCODE_3: //up
			mainChar.moveUpDown(stepWidth);
			break;
		}
		return true; 
	}

	public boolean onKeyUp(int key, KeyEvent evt) {
		
		//do something when the key is released
		return true; 
	}
	
	public boolean onTouchEvent(final MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			released = false;
			lastMotionEvent = evt; 
			handleOnTouchMovement(evt);
		
		}
		else if (evt.getAction() == MotionEvent.ACTION_UP) {
			released = true;
		}
		
		
		
		return true; 
	}
	

	public boolean isInboundsAfterStep(boolean toRight) {
		if (toRight) {
	    	if (mainChar.getPosition().x + mainChar.getWidth() + stepWidth <= 400) { //application width instead of fixed value
	    		return true; 
	    	}
		}
		
		else {
			if (mainChar.getPosition().x - stepWidth > 0)
				return true; 
		}
		
		return false; 
	}	
	
	public void handleOnTouchMovement(final MotionEvent evt) {
		queueEvent(new Runnable(){
			public void run() {
				if (evt.getRawX() <  mainChar.getPosition().x) {
					mainChar.moveLeftRight(-stepWidth);
				} else if (evt.getRawX() > (mainChar.getPosition().x + mainChar.getWidth() )) {
					mainChar.moveLeftRight(stepWidth);
				}
			}
		});
	}

}
