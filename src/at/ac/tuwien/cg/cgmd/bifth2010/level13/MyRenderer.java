package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.SoundManager.SoundFX;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BackgroundObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BeerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.CopObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.DrunkBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.JailBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.MistressObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.PlayerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.StatusBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.TextureSingletons;

/**
 * 
 * @author arthur/ sebastian (group 13)
 *
 */
public class MyRenderer extends GLSurfaceView implements Renderer {
	
	
	//the current map in form of an array
	
	//Control delay timer
	public static int controlDelay = 0;
	
	public static int map[][] = {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 2, 2, 0, 2, 0, 2, 0, 2, 0, 2, 2, 1, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 0, 0, 0, 0, 2, 0, 2, 0, 1, 2, 1, 1, 1, 1, 2, 1 },
		{ 1, 1, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 2, 1, 1, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 2, 1, 1, 1, 1, 2, 0, 2, 0, 2, 0, 2, 0, 0, 2, 0, 0, 2, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 2, 0, 2, 0, 2, 0, 2, 0, 4, 0, 0, 0, 1, 1 },
		{ 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 2, 0, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 3, 2, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1 },
		{ 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 2, 0, 2, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 2, 2, 0, 2, 4, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 2, 0, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
};
	
	
	//dimensions of screen
	public static int screenWidth;
	public static int screenHeight;
	
	//general movement speed (must be a divisor of GameObject.BLOCKSIZE -> see todo in collisonhandler)
	public static final float SPEED = 17f;
	
	

	//attached context
	private Context context;
	
	//all game objects
	private List<GameObject> gameObjects;
	
	//counter for fps
	private FPSCounter counter;
	private float accTime = 0;
	public DrunkBar drunkStatusBar;
	public JailBar jailStatusBar;
	SoundManager sound;
	
	//public PlayerObject player;
	/**
	 * constructor
	 * @param context
	 */
	public MyRenderer(Context context) {
		super(context);
		//init members
		SoundManager.initSoundManager(context);
		this.context = context;
		this.gameObjects = new ArrayList<GameObject>();
		this.counter = FPSCounter.getInstance();
		//set renderer for view
		this.setRenderer(this);
		//make it focusable (for touch events)
		this.setFocusable(true);
		
	}

	/**
	 * @see Renderer#onDrawFrame(GL10)
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
		
		//clear color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		GameControl.update();
		
		//draw all game objects
		for(GameObject gameObject : gameObjects) {
			if (gameObject.isActive){
			gameObject.update();
			gameObject.draw(gl);
			
			}
		}
		
			drunkStatusBar.draw(gl);
			GameControl.updateDrunkStatus(drunkStatusBar);
			GameControl.updateJailStatus(jailStatusBar);
			jailStatusBar.draw(gl);
	}

	/**
	 * @see Renderer#onSurfaceChanged(GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//set new screen dimensions
		MyRenderer.screenWidth = width;
		MyRenderer.screenHeight = height;
		
		//set parallel projection
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glOrthof(0, width, 0, height, -1.0f, 1.0f);
		
		gl.glViewport(0, 0, width, height);
		
		//init all textures
		TextureSingletons.initTextures(gl, context);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		//create all game objects
		gameObjects.add(new BackgroundObject());
		for(int i = 0; i < MyRenderer.map.length; i++) {
			for(int j = 0; j < MyRenderer.map[i].length; j++) {
				if(MyRenderer.map[i][j] == 2) {
					gameObjects.add(new BeerObject(j, Math.abs(i - map.length + 1)));
				}
				else if (MyRenderer.map[i][j] == 3){
					gameObjects.add(new CopObject(j, Math.abs(i - map.length+1)));
				}
				else if (MyRenderer.map[i][j] == 4){
					gameObjects.add(new MistressObject(j, Math.abs(i - map.length+1)));
				}
			}
		}
		drunkStatusBar = new DrunkBar(200, 50);
		jailStatusBar = new JailBar(200, 50);
	    jailStatusBar.position.y = 50;

		
		//player = new PlayerObject();
		gameObjects.add(new PlayerObject());
	}

	/**
	 * @see Renderer#onSurfaceCreated(GL10, EGLConfig)
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
		gl.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
	}
	

	
	
	/**
	 * @see GLSurfaceView#onTouchEvent(MotionEvent)
	 */

	public boolean onTouchEvent(MotionEvent event) {
		//only process event if touch is finished
		if(event.getAction() == MotionEvent.ACTION_UP) {
			//calculate difference of touch-position and screen-center
			GameControl.movePlayer(event.getX(), event.getY());
			
			
			
			
		}
		return true;
	}
}
