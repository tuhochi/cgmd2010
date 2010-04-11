package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

/**
 * 
 * @author arthur/ sebastian (group 13)
 *
 */
public class MyRenderer extends GLSurfaceView implements Renderer {
	
	
	//the current map in form of an array
	
	public static int map[][] = {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 0, 0, 0, 0, 2, 0, 0, 0, 1, 0, 1, 1, 1, 1, 2, 1 },
		{ 1, 1, 0, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 2, 0, 4, 0, 0, 0, 1, 1 },
		{ 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 3, 2, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
		{ 1, 1, 0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 2, 0, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
};
	
	
	//dimensions of screen
	public static int screenWidth;
	public static int screenHeight;
	
	//general movement speed (must be a divisor of GameObject.BLOCKSIZE -> see todo in collisonhandler)
	public static final float SPEED = 17f;
	
	//movement vector
	public static Vector2 movement = new Vector2(0, 0);

	//attached context
	private Context context;
	
	//all game objects
	private List<GameObject> gameObjects;
	
	//counter for fps
	private FPSCounter counter;
	private float accTime = 0;
	

	/**
	 * constructor
	 * @param context
	 */
	public MyRenderer(Context context) {
		super(context);
		//init members
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
		
		//update offset
		GameObject.updateOffset(movement);
		
		//draw all game objects
		for(GameObject gameObject : gameObjects) {
			if (gameObject.isActive){
			gameObject.update();
			gameObject.draw(gl);
			}
		}
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
		gl.glOrthof(0.0f, width, 0.0f, height, -1.0f, 1.0f);
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
			float deltaX = Math.abs(event.getX() - MyRenderer.screenWidth / 2.0f);
			float deltaY = Math.abs(event.getY() - MyRenderer.screenHeight / 2.0f);
			
			if(deltaX >= deltaY) {
				if(event.getX() < MyRenderer.screenWidth / 2.0f) {
					//move left
					movement.x = -MyRenderer.SPEED;
					movement.y = 0.0f;
				}
				else if(event.getX() > MyRenderer.screenWidth / 2.0f) {
					//move right
					movement.x = MyRenderer.SPEED;
					movement.y = 0.0f;
				}
			}
			else {
				//event starts at top left
				if(event.getY() < MyRenderer.screenHeight / 2.0f) {
					//move up
					movement.x = 0.0f;
					movement.y = MyRenderer.SPEED;
				}
				else if(event.getY() > MyRenderer.screenHeight / 2.0f) {
					//move up
					movement.x = 0.0f;
					movement.y = -MyRenderer.SPEED;
				}
			}
		}
		return true;
	}
}
