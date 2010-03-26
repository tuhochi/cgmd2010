package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Handler;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLTextures;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Picker;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.OBJModel;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.OBJRenderable;

public class NormalModeWorld implements World {

	private static final String LOG_TAG = "World";
	
    private Vector2 mTouchPos = new Vector2();
    private Vector2 mNewTouchPos = null;
    private float mElapsedSeconds;
    private long mOldTime;
    private long mTime;
    private GLTextures mTextures;
    private LevelActivity mContext;
    private float mRotAngle = 0;
    @SuppressWarnings("unused")
	private Handler mHandler;

	// camera variables
	private Vector3 mEye = new Vector3(0f, 15f, 0f);
	private Vector3 mFocus = new Vector3(0f, 0f, 0f);
	private Vector3 mUp = new Vector3(1f, 0f, 0f);
	
	// player position information
	private Vector3 mPlayerPos = new Vector3(0f, 0f, 0f);
	private Vector3 mPlayerTarget = new Vector3(0f, 0f, 0f);
	
	// used to grab the current view and projection matrices
	private Picker mPicker = new Picker();
	
	//just as long as obj files don't work
	private Cube cube;	
    
    //the Level to render and collide against
	private Level mLevel;
	
	private OBJRenderable mObject;
	
	private boolean mPause = true;
	
	
    
	
    public NormalModeWorld(LevelActivity context, Handler handler)
    {
        Date date = new Date();
        mTime = date.getTime();
        mOldTime = mTime;
        mContext = context;
        mHandler = handler;
    }
    
    
	public synchronized void update()
	{
		if(mPause)
			return;
        mOldTime = mTime;
        
        Date date = new Date();
        mTime = date.getTime();
        mElapsedSeconds = (mTime - mOldTime) / 1000.0f;

        mRotAngle += mElapsedSeconds;
    	
        if (mNewTouchPos != null)
        {
        	mPlayerPos = mPicker.GetWorldPosition(mNewTouchPos);
        	Log.d(LOG_TAG, "Final World Coordinates:" + mPlayerPos.toString());
        	mNewTouchPos = null;
        }
        
        mLevel.update(mElapsedSeconds);
	}
	
	public synchronized void draw(GL10 gl)
	{

		MatrixTrackingGL trackGl = (MatrixTrackingGL)gl;
		trackGl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		trackGl.glMatrixMode(GL10.GL_MODELVIEW);
		trackGl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		trackGl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		trackGl.glEnable(GL10.GL_TEXTURE_2D);
		trackGl.glEnable(GL10.GL_CULL_FACE);
        
		trackGl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		//Set ModelView Matrix
		GLU.gluLookAt(trackGl, mEye.x, mEye.y, mEye.z, mFocus.x, mFocus.y, mFocus.z, mUp.x, mUp.y, mUp.z);
		
		//Drawing
		mTextures.setTexture(R.drawable.l17_crate);
		trackGl.glPushMatrix();
		trackGl.glTranslatef(mPlayerPos.x, mPlayerPos.y, mPlayerPos.z);			//Move z units into the screen
		cube.draw(trackGl, 1);					//Draw the Cube	
		trackGl.glPopMatrix();
		

		//mObject.draw(trackGl);
		mLevel.draw(trackGl);

        trackGl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public synchronized void init(GL10 gl)
	{
        mTextures = new GLTextures(gl, mContext);
        mTextures.add(R.drawable.l17_crate);
        mTextures.loadTextures();
        mLevel = new Level();
        
        mObject = new OBJRenderable(OBJModel.load("l17_test.obj", mContext));
		cube = new Cube();
		mPause = false;
	}


    public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
		//Set ModelView Matrix
		GLU.gluLookAt(gl, mEye.x, mEye.y, mEye.z, mFocus.x, mFocus.y, mFocus.z, mUp.x, mUp.y, mUp.z);
		
		// update Picker 
		mPicker.CameraChanged(gl);
		mPicker.SetReferencePosition(mPlayerPos);
    }
	
    public synchronized void fingerMove(Vector2 pos)
    {
    	mNewTouchPos = new Vector2(pos);
    }
    
    public synchronized void fingerDown(Vector2 pos)
    {
    	mNewTouchPos = new Vector2(pos);
    }   
    
    public synchronized void fingerUp(Vector2 pos)
    {
    	mNewTouchPos = new Vector2(pos);
    }
}
