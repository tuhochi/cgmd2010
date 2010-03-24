package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.os.Handler;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLTextures;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Picker;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

public class NormalModeWorld implements World {

	private static final String LOG_TAG = "World";
	
    private Vector2 mTouchPos = new Vector2();
    private Vector2 mNewTouchPos = null;
    private float mElapsedSeconds;
    private long mOldTime;
    private long mTime;
    private Matrix4x4 mRotation = new Matrix4x4();
    private GLTextures mTextures;
    private LevelActivity mContext;
    private float mRotAngle = 0;
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
    
    //private Renderable mObject;
    
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
        mOldTime = mTime;
        
        Date date = new Date();
        mTime = date.getTime();
        mElapsedSeconds = (mTime - mOldTime) / 1000.0f;

        mRotAngle += mElapsedSeconds;
    	
        mRotation = Matrix4x4.RotateZ(mRotAngle);
        
        if (mNewTouchPos != null)
        {
        	mPlayerPos = mPicker.GetWorldPosition(mNewTouchPos);
        	Log.d(LOG_TAG, "Final World Coordinates:" + mPlayerPos.toString());
        	mNewTouchPos = null;
        }
	}
	
	public synchronized void draw(GL10 gl)
	{
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_BLEND);	
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		//Set ModelView Matrix
		GLU.gluLookAt(gl, mEye.x, mEye.y, mEye.z, mFocus.x, mFocus.y, mFocus.z, mUp.x, mUp.y, mUp.z);
		
		//Drawing
		gl.glTranslatef(mPlayerPos.x, mPlayerPos.y, mPlayerPos.z);			//Move z units into the screen
		cube.draw(gl, 1);					//Draw the Cube	
        
        //gl.glScalef(0.02f,0.02f,0.02f);
        mTextures.setTexture(R.drawable.l17_crate); 
        gl.glMultMatrixf(mRotation.toFloatArray(), 0);
        //mObject.draw(gl);
        gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public synchronized void init(GL10 gl)
	{
        mTextures = new GLTextures(gl, mContext);
        mTextures.add(R.drawable.l17_crate);
        mTextures.loadTextures();
        
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.l17_crate);
        
        float imgAspect = (float)bmp.getWidth() / (float)bmp.getHeight();
        
        //mObject = new OBJRenderable(OBJModel.load("l17_test.obj", mContext));
		cube = new Cube();
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
