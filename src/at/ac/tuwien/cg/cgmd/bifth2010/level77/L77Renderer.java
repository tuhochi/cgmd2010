package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Cylinder;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Rectangle;

//one of the bloody things needed for getResources()
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author mike_vasiljevs Instance of renderer for Level77
 */
public class L77Renderer implements Renderer
{
	private Context	mContext;
	private Native	jni;
	private Rectangle timeBar;
	private CoinBar coinBar;
	private long startTime;
	
	private static String TAG = "L77Renderer";
	private Callback<Void> timeUp;

	/**
	 * constructor for Level 77 Renderer
	 * 
	 * @param useTranslucentBackground
	 * @param aContext
	 * @param timeUp 
	 */
	public L77Renderer(boolean useTranslucentBackground, Context aContext, Native native_jni, Callback<Void> timeUp)
	{
		mContext = aContext;
		jni = native_jni;
		this.timeUp = timeUp;
	}


	private float percent = 1.0f;
	
	/**
	 * TODO Javadoc
	 */
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// Render in native code

		gl.glEnable(GL10.GL_TEXTURE_2D);
		jni.render();

		gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		
		gl.glTranslatef(6.5f, 0.0f, 5.0f);
		coinBar.draw(percent);
			
		gl.glTranslatef(0.5f, 4.5f, 0.0f);
		long currentTime = System.currentTimeMillis();
		float timePercent = 1.0f - ((float) (currentTime - startTime)) / 1000f / 120f; 
//		if (timePercent <= 0)
//			timeUp.onSucces(null);
		
		gl.glScalef(1.0f, timePercent, 1.0f);
		gl.glColor4f(1, 1, 1, 1);
		timeBar.draw(gl);
		
		gl.glFlush();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		jni.resizeView(width, height);
		Log.i("l77renderer", String.format("surface resized to %dx%d", width, height));
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Log.i("renderer", "initiliased");
		// init first part of jni stuff
		jni.init();
		jni.init2();

		timeBar = new Rectangle(0.25f, 9.0f);
		timeBar.setTexture(gl, mContext.getResources(), R.drawable.l77_time_bar);	
		coinBar = new CoinBar(0.5f, 9.0f, 0.1f, -42.0f, mContext, gl);

	}

	public void finalize()
	{
		jni.deInit();
	}

	private float dragStartX = 0, dragStartY = 0, lastDragX = 0, lastDragY = 0;
	
	public void onTouchEvent(MotionEvent event)
	{
		//
		float x = event.getX(), y = event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			dragStartX = lastDragX = x;
			dragStartY = lastDragY = y;
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			float distanceX = x - lastDragX;
			float distanceY = y - lastDragY;
			lastDragX = x;
			lastDragY = y;
			
			
			percent = percent - distanceX / 100.0f;
			Log.d(TAG, "percent: " + percent);

		}
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

}
