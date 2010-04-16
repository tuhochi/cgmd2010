package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Handler;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Textures;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;


/**
 * Game class for the main logic of the level
 * @author Asperger, Radax
 */
public class Game {
	public static final String TAG = "Game"; 
	
	private long newTime, oldTime;
	private float elapsedSeconds;
	public int screenWidth, screenHeight;
	public float screenWidthScale, screenHeightScale;
	public LevelActivity context;
	public Textures textures;
	private Handler handler;
	private Vector2 cameraPos;
	private float worldScale;
	public Map map;
	public Bunny bunny;
	public ArrayList<Police> police;
	public ArrayList<Stash> stashes;
	
	public boolean newTouch;
	public Vector2 touchPosition;
	
	public int gold;

	
	/**
	 * Constructor
	 * @param _context Context of the Android application
	 */
	public Game(LevelActivity _context, Handler _handler) {
		Log.d(TAG, "Game()");

		context = _context;
		handler = _handler;
		cameraPos = new Vector2();
		worldScale = 0.2f;
		gold = 100;
		
		police = new ArrayList<Police> ();
		stashes = new ArrayList<Stash> ();
			
		newTouch = false;
		touchPosition = new Vector2();

		Date date = new Date();
        newTime = date.getTime();
        oldTime = newTime;
        elapsedSeconds = 0;
	}
	
	public void policeCatchesBunny() {
		class R implements Runnable{
        	@Override
            public void run() {
        		context.endLevel();
            }
        };
        Runnable r = new R();
        handler.post(r);		
	}
	
	public void looseGold(int lostGold) {
		gold -= lostGold;
		if( gold <= 0 ) {
			gold = 0;

			class R implements Runnable{
	        	@Override
	            public void run() {
	        		context.endLevel();
	            }
	        };
	        Runnable r = new R();
	        handler.post(r);
		}
		else {
			class R implements Runnable{
	        	@Override
	            public void run() {
	        		context.updateTexts();
	            }
	        };
	        Runnable r = new R();
	        handler.post(r);			
		}
	}
	
	/**
	 * Add a new stash
	 * @param x x-position of the stash on the map
	 * @param y y-position of the stash on the map
	 * @param size size of the stash 
	 */
	public void addStash(int x, int y, int size) {
		stashes.add(new Stash(this, x, y, size));
	}
	
	
	/**
	 * Add a new police character
	 * @param x x-position of the police start point on the map
	 * @param y y-position of the police start point on the map
	 */
	public void addPolice(int x, int y) {
		police.add(new Police(this, x, y));
	}

	
	/**
	 * Get information if there is a new input
	 * @return information if there is a new input 
	 */
	public synchronized boolean hasNewInput() {
		boolean old = newTouch;
		newTouch = false;
		return old;
	}

	/**
	 * Method for the touch events
	 * @param pos position of the finger
	 */
	public void touchEvent(Vector2 pos) {
		touchPosition = new Vector2(pos);
		newTouch = true;
	}
	
	/**
	 * Initialize the OpenGL context
	 * @param gl OpenGLcontext of android
	 */
	public synchronized void init(GL10 gl) {
		Log.d(TAG, "init()");
    	gl.glClearColor(1, 1, 1, 0);

    	gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		textures = new Textures(gl, context);
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		
		bunny = new Bunny(this);
		map = new Map(this);
		map.load();
	}
	
	
	/**
	 * Recalculation of the surface
	 * @param gl OpenGL context of android
	 * @param width width of the screen
	 * @param height height of the screen
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		screenWidth = width;
		screenHeight = height;
		gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        if( width > height ) {
        	screenWidthScale = (float)width/(float)height;
        	screenHeightScale = 1;
        }
        else {
        	screenHeightScale = (float)height/(float)width;
        	screenWidthScale = 1;
        }
        GLU.gluOrtho2D(gl, -screenWidthScale, screenWidthScale, screenHeightScale, -screenHeightScale);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
	}
	
	
	/**
	 * Update function for the screen
	 */
	public synchronized void update() {
		if( bunny==null ) return;

		Date date = new Date();
        oldTime = newTime;
		newTime = date.getTime();
        elapsedSeconds = (newTime - oldTime) / 1000.0f;

        bunny.update(elapsedSeconds);
        for(int i=0; i<stashes.size(); i++) {
        	stashes.get(i).update(elapsedSeconds);
        }
        for(int i=0; i<police.size(); i++) {
        	police.get(i).update(elapsedSeconds);
        }
        map.update(elapsedSeconds);

        cameraPos.x = bunny.translateX + map.groundXDir.x/2.0f + map.groundYDir.x/2.0f;
        cameraPos.y = bunny.translateY + map.groundXDir.y/2.0f + map.groundYDir.y/2.0f;
	}

	
	
	/**
	 * Draw the whole screen
	 * @param gl OpenGL context of android
	 */
	public synchronized void draw(GL10 gl) {
		if( bunny==null ) return;
		
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        gl.glLoadIdentity();
        gl.glScalef(worldScale, worldScale, worldScale);
        gl.glTranslatef(-cameraPos.x, -cameraPos.y, -1f);
        
        map.draw(gl);

        for(int i=0; i<stashes.size(); i++) {
        	stashes.get(i).draw(gl);
        }
        
        for(int i=0; i<police.size(); i++) {
        	police.get(i).draw(gl);
        }
        
        bunny.draw(gl);
	}
}
