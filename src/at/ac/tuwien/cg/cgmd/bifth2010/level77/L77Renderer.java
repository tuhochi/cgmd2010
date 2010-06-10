package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
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
	private boolean	mTranslucentBackground;
	private Context	mContext;
	private Native	jni;
	private Rectangle timeBar;
	private Rectangle goldBar;
	private Rectangle barBorder;

	/**
	 * constructor for Level 77 Renderer
	 * 
	 * @param useTranslucentBackground
	 * @param aContext
	 */
	public L77Renderer(boolean useTranslucentBackground, Context aContext, Native native_jni)
	{
		mTranslucentBackground = useTranslucentBackground;
		mContext = aContext;
		jni = native_jni;
	}

	private float		mAngle	= 0.0f;
	private Cylinder	mCube;

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
		
		gl.glTranslatef(6.75f, 4.5f, 5.0f);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		//barBorder.draw(gl);
		
		gl.glTranslatef(-0.25f, 0, -0.5f);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);		
		timeBar.draw(gl);
		
		gl.glTranslatef(0.5f, 0.0f, 0.0f);
		
		timeBar.draw(gl);
		
//		gl.glTranslatef(0, 0, -3.0f);
//		gl.glRotatef(mAngle, 0, 1, 0);
//		gl.glRotatef(mAngle * 0.25f, 1, 0, 0);
//
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//
//		mCube.draw(gl);
//
//		gl.glRotatef(mAngle * 2.0f, 0, 1, 1);
//		gl.glTranslatef(0.5f, 0.5f, 0.5f);
//
//		mCube.draw(gl);
//
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
//
//		mAngle += 1.2f;

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

		timeBar = new Rectangle(0.5f, 9.0f);
		timeBar.setTexture(gl, mContext.getResources(), R.drawable.l77_time_bar);
		
		barBorder = new Rectangle(1.0f, 9.0f);
		

		
		
		mCube = new Cylinder(0.5f, 0.10f, (short) 10);
		mCube.setTexture(gl, mContext.getResources(), R.drawable.l00_coin);
		mCube.setColor(0.85f, 0.68f, 0.22f, 1.f);
	}

	public void finalize()
	{
		jni.deInit();
	}

}
