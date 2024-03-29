package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.opengl.GLU;
import android.os.Handler;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.Sound;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Textures;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;


/**
 * Game class for the main logic of the level
 * @author Asperger, Radax
 */
public class Game {
	/**
	 * Tag used for logging
	 */
	private static final String TAG = "Game"; 
	/**
	 * Time of the current and the previous frame
	 */
	private long newTime, oldTime;
	/**
	 * Elapsed seconds since last frame
	 */
	private float elapsedSeconds;
	/**
	 * Width and height of the screen
	 */
	public int screenWidth, screenHeight;
	/**
	 * Context of the android application
	 */
	public LevelActivity context;
	/**
	 * Textures of the level
	 */
	public Textures textures;
	/**
	 * Handler class
	 */
	private Handler handler;
	/**
	 * Camera position of the game
	 */
	private Vector2 cameraPos;
	/**
	 * Scaling of the world, used to 'zoom' into the scene
	 */
	private float worldScale;
	/**
	 * Map instance of the level
	 */
	public Map map;
	/**
	 * Bunny instance of the level
	 */
	public Bunny bunny;
	/**
	 * Array containing all instances of the police
	 */
	public ArrayList<Police> police;
	/**
	 * Array containing all instances of the stashes
	 */
	public ArrayList<Stash> stashes;
	
	/**
	 * A single quad, used to render bunny/police/stashes
	 */
	public Quad objectQuad;
	/**
	 * Array containing all instances of the stashes with size 1
	 * Used for rendering to avoid texture changes
	 */
	public ArrayList<Stash> stashes_size1;
	/**
	 * Array containing all instances of the stashes with size 2
	 * Used for rendering to avoid texture changes
	 */
	public ArrayList<Stash> stashes_size2;
	/**
	 * Array containing all instances of the stashes with size 3
	 * Used for rendering to avoid texture changes
	 */
	public ArrayList<Stash> stashes_size3;
	
	/**
	 * Set true if there was a new touch on the touch screen
	 */
	public boolean newTouch;
	/**
	 * Position of the touch on the touch screen
	 */
	public Vector2 touchPosition;
	/**
	 * Gold left that can be hidden 
	 */
	public int gold;
	/**
	 * Sound instance for sound effects
	 */
	public Sound player = null;
	/**
	 * Set true if music/sound is enabled
	 */
	public boolean musicOn = true;

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
		stashes_size1 = new ArrayList<Stash> ();
		stashes_size2 = new ArrayList<Stash> ();
		stashes_size3 = new ArrayList<Stash> ();
			
		bunny = new Bunny(this);
		map = new Map(this);
		map.load();

		newTouch = false;
		touchPosition = new Vector2();

		Date date = new Date();
        newTime = date.getTime();
        oldTime = newTime;
        elapsedSeconds = 0;
        
        player = Sound.getInstance(context);
        //player.play(Sounds.BACKGROUND);
        
        musicOn = context.mMusicOn;

		Vector2 groundYDir = new Vector2(-0.81f, -0.59f);
		Vector2 groundXDir = new Vector2(1.16f, -0.46f);
        Vector2 xDir = new Vector2(1.41f, 0);
        Vector2 yDir = new Vector2(0, -1.41f);
        Vector2 quadBase = new Vector2();
        quadBase.add(groundYDir);
        quadBase.add(groundXDir);
        quadBase.mult(-1.0f);
        quadBase.add(xDir);
        quadBase.add(yDir);
        quadBase.mult(-0.5f);
        quadBase.add(new Vector2(0, -0.28f));        
        objectQuad = new Quad(quadBase, xDir, yDir);
        
        
        
        Log.d(TAG, "Game() - end");
	}
	
	/**
	 * Method for the actions if the bunny has been caught by the police
	 */
	public void policeCatchesBunny() {
		if(context.mMusicOn){
			MediaPlayer soundPlayer = MediaPlayer.create(context, R.raw.l88_police);
			try{
				if(soundPlayer!=null){
					soundPlayer.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
							
						}
					});
					soundPlayer.start();
				}
			} catch(IllegalStateException e){
				//Ignore
			}
		}
		
		/**
		 * Handler class for a thread to end the level
		 * @author Asperger, Radax
		 */
		class R implements Runnable{
        	@Override
            public void run() {
        		context.endLevel();
            }
        };
        Runnable r = new R();
        handler.post(r);		
	}
	
	
	/**
	 * Method to loose the gold and probably end the level if there has been no more gold left
	 * @param lostGold
	 */
	public void looseGold(int lostGold) {
		gold -= lostGold;
		
		//TODO Variante 2, falls es mit dem anderen Probs am Handy gibt!
		/*if(context.mMusicOn){
			player.play(Sounds.GOLD);
		}*/
		
		if(context.mMusicOn){
			MediaPlayer soundPlayer = MediaPlayer.create(context, R.raw.l00_gold01);
			try{
				if(soundPlayer!=null){
					soundPlayer.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
							
						}
					});
					soundPlayer.start();
				}
			} catch(IllegalStateException e){
				//Ignore
			}
		}
		
		if( gold <= 0 ) {
			gold = 0;
			
			if(context.mMusicOn){
				MediaPlayer soundPlayer = MediaPlayer.create(context, R.raw.l88_win);
				try{
					if(soundPlayer!=null){
						soundPlayer.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								mp.release();
								
							}
						});
						soundPlayer.start();
					}
				} catch(IllegalStateException e){
					//Ignore
				}
			}
			
			/**
			 * Handler class for a thread to end the level
			 * @author Asperger, Radax
			 */
			class R implements Runnable{
	        	@Override
	            public void run() {
	        		//player.destroy();
	        		context.endLevel();
	            }
	        };
	        Runnable r = new R();
	        handler.post(r);
		}
		else {
			/**
			 * Handler class for a thread to update the texts on the screen
			 * @author Asperger, Radax
			 */
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
		Stash stash = new Stash(this, x, y, size); 
		stashes.add(stash);
		
		if( size==1 )
		{
			stashes_size1.add(stash);
		}
		else if( size==2 )
		{
			stashes_size2.add(stash);
		}
		else
		{
			stashes_size3.add(stash);
		}
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
	public boolean hasNewInput() {
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
		gl.glShadeModel(GL10.GL_FLAT); 			//Enable Smooth Shading
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		textures = new Textures(gl, context);
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
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
        float screenWidthScale;
        float screenHeightScale;
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
	 * Update the game and all its components
	 */
	public void update() {
		if( bunny==null ) return;

		Date date = new Date();
        oldTime = newTime;
		newTime = date.getTime();
        elapsedSeconds = (newTime - oldTime) / 1000.0f;

        bunny.update(elapsedSeconds);
        if( bunny.moveStatus != Bunny.STANDING ) { // standing bunny = pause
	        for(int i=0; i<stashes.size(); i++) {
	        	stashes.get(i).update(elapsedSeconds);
	        }
	        for(int i=0; i<police.size(); i++) {
	        	police.get(i).update(elapsedSeconds);
	        }
	        map.update(elapsedSeconds);
        }

        cameraPos.x = bunny.translateX + map.groundXDir.x/2.0f + map.groundYDir.x/2.0f;
        cameraPos.y = bunny.translateY + map.groundXDir.y/2.0f + map.groundYDir.y/2.0f;
	}

	/**
	 * Set the game to pause (bunny not moving)
	 * @param gl OpenGL context of android
	 */
	public void pause() {
		if( bunny!=null ) {
			bunny.moveStatus = Bunny.STANDING;
		}
		player.pause();
	}
	
	/**
	 * Draw the whole screen
	 * @param gl OpenGL context of android
	 */
	public void draw(GL10 gl) {
		if( bunny==null ) return;
		
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        gl.glLoadIdentity();
        gl.glScalef(worldScale, worldScale, worldScale);
        gl.glTranslatef(-cameraPos.x, -cameraPos.y, -1f);
        
        map.draw(gl);

        objectQuad.vbos.set(gl);

		textures.bind(R.drawable.l88_stash_yellow);
        for(int i=0; i<stashes_size1.size(); i++) {
        	stashes_size1.get(i).draw(gl);
        }
		textures.bind(R.drawable.l88_stash_orange);
        for(int i=0; i<stashes_size2.size(); i++) {
        	stashes_size2.get(i).draw(gl);
        }
		textures.bind(R.drawable.l88_stash_red);
        for(int i=0; i<stashes_size3.size(); i++) {
        	stashes_size3.get(i).draw(gl);
        }

        textures.bind(R.drawable.l88_police);
        for(int i=0; i<police.size(); i++) {
        	police.get(i).draw(gl);
        }
        
		textures.bind(R.drawable.l88_bunny);
        bunny.draw(gl);
	}	
}
