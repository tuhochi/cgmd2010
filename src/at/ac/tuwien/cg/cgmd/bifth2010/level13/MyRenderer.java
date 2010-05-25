package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BackgroundObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BeerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.CopObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.DrunkBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.JailBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.MistressObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.PlayerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.TextureSingletons;

/**
 * 
 * @author arthur/ sebastian (group 13)
 *
 */
public class MyRenderer extends GLSurfaceView implements Renderer {
	
	
	//the current map in form of an array
	
	//Control delay timer
	//public static int controlDelay = 0;
	public static int map[][] = {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 0, 2, 0, 0, 0, 0, 2, 2, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, 
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 2, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 0, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, 
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, 
		{ 1, 2, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 2, 1, 1, 1, 1 }, 
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1 }, 
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, 
		{ 1, 2, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 2, 1 },
		{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 2, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 2, 0, 0, 1, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 2, 0, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
		{ 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
		{ 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1 },
		{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 2, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
		{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
		{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 2, 0, 0, 1, 1, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};
	
	
	//dimensions of screen
	public static int screenWidth;
	public static int screenHeight;
	
	//general movement speed (must be a divisor of GameObject.BLOCKSIZE -> see todo in collisonhandler)
	public static final float SPEED = 8f;
	
	private float zoomFactor = 1.0f;
	private float zoom = 0.0f;
	//attached context
	private Context context;
	
	//all game objects
	private static List<GameObject> gameObjects = new ArrayList<GameObject>();
	
	//counter for fps
	private FPSCounter counter;
	private float accTime = 0;
	public DrunkBar drunkStatusBar;
	public JailBar jailStatusBar;
	SoundManager sound;
	private GameControl gameControl;
	
	public PlayerObject player;
	private float rotate = 0f;
	
	public static void reset() {
		gameObjects = new ArrayList<GameObject>();
	}
	/**
	 * constructor
	 * @param context
	 */
	public MyRenderer(Context context, AttributeSet attr) {
		super(context, attr);
		//init members
		SoundManager.initSoundManager(context);
		this.context = context; 
		this.counter = FPSCounter.getInstance();
		this.gameControl = GameControl.getInstance();
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
		int width = MyRenderer.screenWidth;
		int height = MyRenderer.screenHeight;
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		
		if(gameControl.isDrunkState()){
			if (zoomFactor > 1.1f){
				zoom = -0.01f;
			}

			if (zoomFactor <= 1.0f){
				zoom = 0.01f;
			}

			zoomFactor+=zoom;
			gl.glOrthof(0, width*zoomFactor, 0, height*zoomFactor, -1.0f, 1.0f);

			gl.glTranslatef(player.getRealPosition().x - GameObject.offset.x, player.getRealPosition().y - GameObject.offset.y, 0);
			gl.glRotatef(rotate, 0, 0, 1);
			gl.glTranslatef((-1)*(player.getRealPosition().x - GameObject.offset.x), (-1)*(player.getRealPosition().y - GameObject.offset.y), 0);
			rotate += 3;
		}
		else {
			gl.glOrthof(0, width, 0, height, -1.0f, 1.0f);
		}
		 	
		gl.glViewport(0, 0, width, height);
		
		
		
		
		
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
		
		gameControl.update();
		
		//draw all game objects
		for(GameObject gameObject : gameObjects) {
			if (gameObject.isActive){
			gameObject.update();
			gameObject.draw(gl);
			
			}
		}
		
			drunkStatusBar.draw(gl);
			gameControl.updateDrunkStatus(drunkStatusBar);
			gameControl.updateJailStatus(jailStatusBar);
			jailStatusBar.draw(gl);
			
			
			//update game time
			GameTimer.getInstance().update();
			
	}

	/**
	 * @see Renderer#onSurfaceChanged(GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d("df", "called onsurfacechanged");
		//set new screen dimensions
		MyRenderer.screenWidth = width;
		MyRenderer.screenHeight = height;
		
		//set parallel projection
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		
		
		gl.glOrthof(0, width*zoomFactor, 0, height*zoomFactor, -1.0f, 1.0f);
		
		gl.glViewport(0, 0, width, height);
		/*
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
		gameObjects.add(new PlayerObject());*/
	}

	/**
	 * @see Renderer#onSurfaceCreated(GL10, EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d("df", "called onsurfacecreated");
		
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
		
		//set display size
		MyRenderer.screenHeight = this.getHeight();
		MyRenderer.screenWidth = this.getWidth();
		
		//init all textures
		TextureSingletons.reset();
		TextureSingletons.initTextures(gl, context);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		//create all game objects
		if(gameObjects.isEmpty()) {
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
			player = new PlayerObject();
			gameObjects.add(player);
			GameObject.setStartTile(new Vector2(3, 1));
		}
		else {
			for(GameObject object : gameObjects) {
				if(object instanceof PlayerObject) {
					PlayerObject player = (PlayerObject)object;
					Vector2 currentTile = player.getCurrentTile();
					player.updatePosition();
					GameObject.setStartTile(currentTile);
				}
			}
			
		}
		
		drunkStatusBar = new DrunkBar(200, 50);
		jailStatusBar = new JailBar(200, 50);
	    jailStatusBar.position.y = 50;
	
	}
	

	
	
	/**
	 * @see GLSurfaceView#onTouchEvent(MotionEvent)
	 */

	public boolean onTouchEvent(MotionEvent event) {
		//only process event if touch is finished
		if(event.getAction() == MotionEvent.ACTION_UP) {
			//calculate difference of touch-position and screen-center
			gameControl.movePlayer(event.getX(), event.getY());
			
			
			
			
		}
		return true;
	}
	/**
	 * handles key movement
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				gameControl.movePlayer(MyRenderer.screenWidth / 2.0f, 0);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				gameControl.movePlayer(MyRenderer.screenWidth / 2.0f, MyRenderer.screenHeight);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				gameControl.movePlayer(0, MyRenderer.screenHeight / 2.0f);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				gameControl.movePlayer(MyRenderer.screenWidth, MyRenderer.screenHeight / 2.0f);
				break;
			default:
				break;
		}
		return true;
	}
	
	/**
	 * handles trackball movement
	 */
	public boolean onTrackballEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			float x = event.getX();
			float y = event.getY();
			if(Math.abs(x) > Math.abs(y)) {
			//	if(Math.abs(x) > 0.6) {
					if(x > 0) {
						gameControl.movePlayer(MyRenderer.screenWidth, MyRenderer.screenHeight / 2.0f);
					}
					else {
						gameControl.movePlayer(0, MyRenderer.screenHeight / 2.0f);
					}
				//}
			}
			else {
				//if(Math.abs(y) > 0.6) {
					if(y > 0) {
						gameControl.movePlayer(MyRenderer.screenWidth / 2.0f, MyRenderer.screenHeight);
					}
					else {
						gameControl.movePlayer(MyRenderer.screenWidth / 2.0f, 0);
					}
				//}
			}
		
		}
		return true;
	}
	
	
}
