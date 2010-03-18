package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;


public class Renderer extends GLSurfaceView implements GLSurfaceView.Renderer {

	public static int screenWidth;
	public static int screenHeight;
	
	private ArrayList<SceneEntity> sceneEntities;
	private Context context; 
	private MainChar mainChar; 
	private boolean released = true; 
	private int stepWidth = 5; 
	private MotionEvent lastMotionEvent = null; 
	private float accTime;
	
	private boolean useSensor = false;
	
	private int mainCharMoveDir;
	
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
		TimeUtil.getInstance().update();
		
		accTime += TimeUtil.getInstance().getDt()/1000;
		if(accTime > 5)
		{
			System.out.println(TimeUtil.getInstance().getFPS());
			accTime = 0;
		}
		
		if(!useSensor)
		{
			if (!released)
				handleOnTouchMovement(lastMotionEvent);
			else
				mainCharMoveDir = MainChar.NO_MOVEMENT;
		}
		
		mainChar.update(TimeUtil.getInstance().getDt(),mainCharMoveDir);
				
		gl.glClearColor(1,0,0,0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//check if needed for all parts of the scene (hud?)
		//add textures etc.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		for(SceneEntity entity : sceneEntities)
			entity.render();

		
		
		//check if needed for all parts of the scene (hud?)
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//setup the Viewport with an Orthogonal View 1 unit = 1 pixel
		//0 0 is bottom left
		screenWidth = width;
		screenHeight = height;
		gl.glOrthof(0, width, 0, height, -10.0f, 1000);
		gl.glViewport(0, 0, width, height);		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		int resID = context.getResources().getIdentifier("l17_crate", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		mainChar.setTextureID(CommonFunctions.loadTexture(gl, context.getResources(), resID));
	}
		
	@Override
	public boolean onKeyDown(int key, KeyEvent evt) {
		switch(key) {
		case KeyEvent.KEYCODE_0: //left
//			mainChar.setMoveDirection(MainChar.MOVE_LEFT);
			break; 
		case KeyEvent.KEYCODE_1: //right
//			mainChar.setMoveDirection(MainChar.MOVE_RIGHT);
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

	@Override
	public boolean onKeyUp(int key, KeyEvent evt) {
		
		//do something when the key is released
		return true; 
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			released = false;
			lastMotionEvent = evt; 
//			handleOnTouchMovement(evt);
		
		}
		else if (evt.getAction() == MotionEvent.ACTION_UP) {
			released = true;
		}		
		
		return true; 
	}
	
	public void handleOnTouchMovement(final MotionEvent evt) {
		queueEvent(new Runnable(){
			public void run() {
				if (evt.getRawX() <  mainChar.getPosition().x) {
					mainCharMoveDir = MainChar.MOVE_LEFT;
				} else if (evt.getRawX() > (mainChar.getPosition().x + mainChar.getWidth() )) {
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
	

}
