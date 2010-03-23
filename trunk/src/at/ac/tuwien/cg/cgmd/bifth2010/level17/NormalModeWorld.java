package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLTextures;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

public class NormalModeWorld implements World {

    private Vector2 mTouchPos = new Vector2();
    private Vector2 mNewTouchPos = null;
    private float mElapsedSeconds;
    private long mOldTime;
    private long mTime;
    private Matrix4x4 mRotation = new Matrix4x4();
    private GLTextures mTextures;
    private LevelActivity mContext;
    private Renderable mQuad;
    private float mRotAngle = 0;
    private Handler mHandler;
    
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
	}
	
	public synchronized void draw(GL10 gl)
	{
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_BLEND);	
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0,0,-3.0f);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        mTextures.setTexture(R.drawable.l17_crate); 
        gl.glMultMatrixf(mRotation.toFloatArray(), 0);
        mQuad.draw(gl);
        gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public synchronized void init(GL10 gl)
	{
        mTextures = new GLTextures(gl, mContext);
        mTextures.add(R.drawable.l17_crate);
        mTextures.loadTextures();
        
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.l17_crate);
        
        float imgAspect = (float)bmp.getWidth() / (float)bmp.getHeight();
        mQuad = new Quad(imgAspect * 2.0f, 2.0f);
	}


    public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         float ratio = (float) width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
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
