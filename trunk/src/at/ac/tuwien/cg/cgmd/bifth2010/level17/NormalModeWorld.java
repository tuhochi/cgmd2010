package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Bundle;
import android.os.Handler;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Picker;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.OBJRenderable;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Quad;

/**
 * The implementation of the World we are using
 * @author MaMa
 *
 */
public class NormalModeWorld implements World, PlayerStateListener {

	//private static final String LOG_TAG = "World";
	
	private Vector2 mTouchPos = null;
    private Vector2 mNewTouchPos = null;
    private float mElapsedSeconds;
    private long mOldTime;
    private long mTime;
    private LevelActivity mContext;
    private float mRotAngle = 0;
	private Handler mHandler;

	// camera variables
	private Vector3 mLookDirection = new Vector3(0f, -30f, 0f);
	private Vector3 mUp = new Vector3(1f, 0f, 0f);
	
	// used to grab the current view and projection matrices
	private Picker mPicker = new Picker();
    
    //the Level to render and collide against
	private Level mLevel;
	
	// test
	@SuppressWarnings("unused")
	private OBJRenderable mObject;
	
	private boolean mPause = true;

	private Quad mBackground;
	
	private Bundle mSavedInstance;
	
	
    
	/**
	 * Creates a new World
	 * @param context The context for acess to the UI
	 * @param handler A handler for callbacks
	 */
    public NormalModeWorld(LevelActivity context, Handler handler, Bundle savedInstance)
    {
        Date date = new Date();
        mTime = date.getTime();
        mOldTime = mTime;
        mContext = context;
        mHandler = handler;
        mSavedInstance = savedInstance;
    }
    
    /**
     * Update the world
     */
	public synchronized void update()
	{
		if(mPause)
			return;
        mOldTime = mTime;
        
        Date date = new Date();
        mTime = date.getTime();
        mElapsedSeconds = (mTime - mOldTime) / 1000.0f;
        
        fpsChanged(1f/mElapsedSeconds);

        mRotAngle += mElapsedSeconds;
    	Vector3 moveDelta = new Vector3();
        if (mNewTouchPos != null)
        {
        	Vector3 currentWorldTouchPos = mPicker.GetWorldPosition(mNewTouchPos);
        	if(mTouchPos != null)
        	{
            	Vector3 lastWorldTouchPos = mPicker.GetWorldPosition(mTouchPos);
        		moveDelta = Vector3.diff(lastWorldTouchPos, currentWorldTouchPos);
        		moveDelta.y = 0;
        	}
        	mTouchPos = mNewTouchPos;
        	//Log.d(LOG_TAG, "Final World Coordinates:" + currentWorldTouchPos.toString());
        	mNewTouchPos = null;
        }
        
        mLevel.update(mElapsedSeconds, moveDelta);
	}
	
	/**
	 * Render the world
	 * @param gl The OpenGL Context
	 */
	public synchronized void draw(GL10 gl)
	{

		MatrixTrackingGL trackGl = (MatrixTrackingGL)gl;
		trackGl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		float[] color = {1.0f,1.0f,1.0f,1.0f};

		trackGl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		trackGl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		trackGl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		trackGl.glEnable(GL10.GL_TEXTURE_2D);
		trackGl.glEnable(GL10.GL_CULL_FACE);	
		gl.glFrontFace(GL10.GL_CCW);

		//DRAW THE BACKGORUND
    	gl.glPushMatrix();
		trackGl.glMatrixMode(GL10.GL_MODELVIEW);
		trackGl.glLoadIdentity();					
		gl.glDepthMask(false);
		Vector3 viewPos = new Vector3(0,10.0f,0);
		Vector3 targetPos = Vector3.add(viewPos, mLookDirection);
		GLU.gluLookAt(trackGl, viewPos.x, viewPos.y, viewPos.z, targetPos.x, targetPos.y, targetPos.z, mUp.x, mUp.y, mUp.z);
    	GLManager.getInstance().getTextures().setTexture(R.drawable.l17_bg);
    	mBackground.draw(gl);
    	gl.glPopMatrix();
    	gl.glDepthMask(true);
    	
		trackGl.glFogfv(GL10.GL_FOG_COLOR, color, 0);
		trackGl.glFogf(GL10.GL_FOG_START, 70.0f);
		trackGl.glFogf(GL10.GL_FOG_END, 130.0f);
		trackGl.glFogf(GL10.GL_FOG_DENSITY, 0.5f);
		trackGl.glFogx(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
		trackGl.glEnable(GL10.GL_FOG);
    	
		SetViewMatrix(trackGl);

		mLevel.draw(trackGl);

        trackGl.glDisable(GL10.GL_TEXTURE_2D);
        trackGl.glDisable(GL10.GL_FOG);
	}
	
	/**
	 * Set the view matrix
	 * @param trackGl The OpenGL context
	 */
	private void SetViewMatrix(MatrixTrackingGL trackGl)
	{
		trackGl.glMatrixMode(GL10.GL_MODELVIEW);
		trackGl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		//Set ModelView Matrix
		Vector3 playerPos = mLevel.getPlayer().getPosition();
		Vector3 targetPos = Vector3.add(playerPos, mLookDirection);
		GLU.gluLookAt(trackGl, playerPos.x, playerPos.y, playerPos.z, targetPos.x, targetPos.y, targetPos.z, mUp.x, mUp.y, mUp.z);
		
		// update Picker 
		mPicker.CameraChanged(trackGl);
		mPicker.SetReferencePosition(targetPos);
	}
	
	/**
	 * Initialize the scene
	 * @param gl The OpenGL context
	 */
	public synchronized void init(GL10 gl)
	{
		MatrixTrackingGL trackGl = (MatrixTrackingGL)gl;
        GLManager.getInstance().init(trackGl, mContext);
        mLevel = new Level(this, mSavedInstance);
        mLevel.getPlayer().addPlayerStateListener(this);
        
        GLManager.getInstance().getTextures().loadTextures();
		mPause = false;

		mBackground = new Quad(20f,20f);
		
	}


    public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 200.0f);

		SetViewMatrix((MatrixTrackingGL)gl);
    }
	
    public synchronized void fingerMove(Vector2 pos)
    {
    	mNewTouchPos = new Vector2(pos);
    }
    
    public synchronized void fingerDown(Vector2 pos)
    {
    	mTouchPos = new Vector2(pos);
    	mNewTouchPos = mTouchPos;
    }   
    
    public synchronized void fingerUp(Vector2 pos)
    {
    	mTouchPos = new Vector2(pos);
    	mNewTouchPos = mTouchPos;
    }

    private void fpsChanged(float fps)
    {
		class FPSRunnable implements Runnable{
        	private float mFps;
        	public FPSRunnable(float fps)
        	{
        		mFps = fps;
        	}
        	@Override
            public void run() {
                mContext.updateFPS(mFps);
            }
        };
        
        Runnable hprunnable = new FPSRunnable(fps);
        mHandler.post(hprunnable);
    }

	@Override
	public synchronized void playerHPChanged(float hp) {
		
		class HPRunnable implements Runnable{
        	private float mHp;
        	public HPRunnable(float hp)
        	{
        		mHp = hp;
        	}
        	@Override
            public void run() {
                mContext.playerHPChanged(mHp);
            }
        };
        
        Runnable hprunnable = new HPRunnable(hp);
        mHandler.post(hprunnable);
	}
	
	@Override
	public void playerMoneyChanged(int money) {
		
		class MoneyRunnable implements Runnable{
        	private int mMoney;
        	public MoneyRunnable(int money)
        	{
        		mMoney = money;
        	}
        	@Override
            public void run() {
                mContext.playerMoneyChanged(mMoney);
            }
        };
        
        Runnable moneyrunnable = new MoneyRunnable(money);
        mHandler.post(moneyrunnable);
		
	}


	@Override
	public synchronized void setPause(boolean pause) {
		mPause = pause;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		mLevel.onSaveInstanceState(outState);
	}
}
