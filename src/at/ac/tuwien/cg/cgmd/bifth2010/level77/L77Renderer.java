package at.ac.tuwien.cg.cgmd.bifth2010.level77;


import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Cylinder;


//one of the bloody things needed for getResources()
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * @author mike_vasiljevs
 * Instance of renderer for Level77
 */
public class L77Renderer implements Renderer 
{
    private boolean mTranslucentBackground;
    private Context mContext;
    private Native jni;

    /**
     * constructor for Level 77 Renderer
     * @param useTranslucentBackground
     * @param aContext
     */
    public L77Renderer(boolean useTranslucentBackground, Context aContext, Native native_jni) {
        mTranslucentBackground = useTranslucentBackground;
        mContext = aContext;
        jni = native_jni;
    }

	@Override
	public void onDrawFrame(GL10 gl) {
       jni.render();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		jni.resizeView(width, height);
		Log.i( "l77renderer", String.format("surface resized to %dx%d", width, height) );
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
 		Log.i("renderer", "initiliased");
        //init first part of jni stuff
        jni.init();
 		jni.init2();
	}
	
	public void finalize()
	{
		jni.deInit();
	}

}
