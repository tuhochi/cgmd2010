package at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics;


import javax.microedition.khronos.opengles.GL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.GameThread;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.World;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;

/**
 * Implements a GlSurfaceView to draw to
 * @author MaMa
 *
 */
public class GLView extends GLSurfaceView {
	
	private JMRenderer mRenderer;//Game's Renderer 
	//private Vector2 mWindowSize;
	private GameThread mGame;
	private World mWorld;
	private boolean mFirstStart = true;
	
	/**
	 * Creates a new GlView
	 * @param context The Activity context
	 * @param size The size of the GlView
	 * @param world The game world 
	 */
    public GLView(Context context, Vector2 size, World world) 
    {
         super(context);                
                         
         mWorld = world;
          
         mRenderer = new JMRenderer(false, world);  
         this.setGLWrapper(new GLSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return new MatrixTrackingGL(gl);
            }});
         
         setRenderer(mRenderer);
         

         
         //mWindowSize = size;
         
         mGame = new GameThread(world);
         mGame.start();
   
    } 
    
    /**
     * Is called when the player moves the finger on the screen
     * @param x The new x position of the finger on the screen in screen coords
     * @param y The new y position of the finger on the screen in screen coords
     */
    public void fingerMove(float x, float y)
    {
    	Vector2 newTouchPos = new Vector2(x, y);
    	fingerMove(newTouchPos);
    }
	
	/**
	 * Is called when the player moves the finger on the screen
	 * @param pos The new position of the finger on the screen in screen coords
	 */
    public void fingerMove(Vector2 pos)
    {
    	mWorld.fingerMove(pos);
    }
    
    /**
     * Is called when the player touches the screen
     * @param x The x position of the finger in screen coords
     * @param y The y position of the finger in screen coords
     */
    public void fingerDown(float x, float y)
    {
    	Vector2 newTouchPos = new Vector2(x, y);
    	fingerDown(newTouchPos);
    }
	
	/**
	 * Is called when the player touches the screen
	 * @param pos The position the player touches the screen in screen coords
	 */
    public void fingerDown(Vector2 pos)
    {
    	mWorld.fingerDown(pos);
    }   
    
    /**
     * Is called when the player releases the screen
     * @param x The last x position of the finger on the screen in screen coords
     * @param y The last y position of the finger on the screen in screen coords
     */
    public void fingerUp(float x, float y)
    {
    	Vector2 newTouchPos = new Vector2(x, y);
    	fingerUp(newTouchPos);
    }
	
	/**
	 * Is called when the player releases the screen
	 * @param pos The last position the player touched the screen in screen coords
	 */
    public void fingerUp(Vector2 pos)
    
    {
    	mWorld.fingerUp(pos);
    }

	@Override
	public void onPause() {
		if(mWorld != null)
			mWorld.setPause(true);
		super.onPause();
	}

	@Override
	public void onResume() {
		if(mFirstStart)
			mFirstStart = false;
		else
		{
			if(mWorld != null)
				mWorld.setPause(false);
		}
		super.onResume();
	}
	
	/**
	 * Is called when the game starts
	 */
	public void onStart() {
		if(mGame != null && !mGame.isAlive() && mWorld != null)
			mGame.start();
	}


	/**
	 * Is called when the game stops 
	 */
	public void onStop() {
		if(mGame != null && mGame.isAlive())
		{
			// let thread run out, stop() is deprecated
			GameThread temp = mGame;
			mGame = null;
			temp.runOut();
		}
	}
    
}
