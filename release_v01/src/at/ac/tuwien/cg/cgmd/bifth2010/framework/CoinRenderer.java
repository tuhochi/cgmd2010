package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class CoinRenderer implements GLSurfaceView.Renderer {

	private long mLastTime = 0;
	private int mFrameCounter = 0; 
	
	int mWidth=0;
	int mHeight=0;
	
	private Resources mResources = null;
	
	public ConcurrentLinkedQueue<Bitmap> mBitmaps = new ConcurrentLinkedQueue<Bitmap>();
	
	public CoinRenderer(Resources resources){
		mResources = resources;
	
	}
	

	float mAngle=0;
	Cylinder mCoin = null;
	
	//private float mBannerHeight = 0;
	private long mLastFrame = 0;

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//camera transform
		gl.glTranslatef(0, 0, -1.5f);

		long now = System.currentTimeMillis();
		if(mLastFrame==0) {
			mLastFrame = now; 
		}
		long elapsedTime = mLastFrame  - now;
		mLastFrame =now;
		

		gl.glPushMatrix();
		gl.glTranslatef(-1.2f, 0, 0);
		gl.glRotatef(mAngle, 0, 1, 0);
		mCoin.draw(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		//gl.glTranslatef(0, 1, 0);
		gl.glRotatef(mAngle, 0, 1, 0);
		mCoin.draw(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslatef(1.2f, 0, 0);
		gl.glRotatef(mAngle, 0, 1, 0);
		mCoin.draw(gl);
		gl.glPopMatrix();

		//gl.glColor4f(1,1,1,1);


		mAngle += 5;

		mFrameCounter++;

		long elapsed = now-mLastTime; 
		if(elapsed > 3000){
			float millisPerFrame = (float)elapsed / (float)mFrameCounter;
			float fps = 1000.f / millisPerFrame;
			//Log.d(CLASS_TAG, "fps: "+fps);
			mFrameCounter=0;
			mLastTime = System.currentTimeMillis();
		}
		
		Bitmap screen = SavePixels(0, 0, getWidth(), getHeight(), gl);
		mBitmaps.add(screen);		
		
		
	}
	
	int getWidth(){
		return mWidth;
	}
	
	int getHeight(){
		return mHeight;
	}
	
	protected Bitmap SavePixels(int x, int y, int w, int h, GL10 gl)
	 
    {  

		
         int b[]=new int[w*h];

         int bt[]=new int[w*h];

         IntBuffer ib=IntBuffer.wrap(b);

         ib.position(0);

         gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

         for(int i=0; i<h; i++)

         {

              for(int j=0; j<w; j++)

              {

                   //correction of R and B

                   int pix=b[i*w+j];

                   int pb=(pix>>16)&0xff;

                   int pr=(pix<<16)&0x00ff0000;

                   int pix1=(pix&0xff00ff00) | pr | pb;

                   //correction of rows

                   bt[(h-i-1)*w+j]=pix1;

              }

         }                  

         
         Bitmap sb=Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);

         return sb;

    }

	
	 public int[] getConfigSpec() {
	                // We want a depth buffer and an alpha buffer
	                int[] configSpec = {
	                        EGL10.EGL_RED_SIZE,      8,
	                        EGL10.EGL_GREEN_SIZE,    8,
	                        EGL10.EGL_BLUE_SIZE,     8,
	                        EGL10.EGL_ALPHA_SIZE,    8,
	                        EGL10.EGL_DEPTH_SIZE,   16,
	                        EGL10.EGL_NONE
	                };
	                return configSpec;
	    }



	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mWidth=width;
		mHeight=height;
		//the glEnable(GL10.GL_TEXTURE_2D) call here fixes an android bug (textures are not correctly displayed after resume)
		//do not remove it!
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glClearColor(0,0,0,0);

		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-2, 2, -2, 2, 1, 10);
//		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		mCoin = new Cylinder(0.5f, 0.10f, (short) 10);
		mCoin.setTexture(gl, mResources, R.drawable.l00_coin);
		mCoin.setColor(0.85f, 0.68f, 0.22f, 1.f);



		//gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		gl.glClearColor(0,0,0,0);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);



	}


}
