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


public class L77Renderer implements Renderer {

    
	// c++ inteface/adaptors
	private static native void nativeInit();
	private static native void nativeInitClasses();    
    private static native void nativeDone();
    private static native void nativeRender();
    private static native void nativeResize(int w, int h);
    //debug
    private static native int nativeNumDrawn();

    private boolean mTranslucentBackground;
    private Context mContext;
    private Images mImages;

    public L77Renderer(boolean useTranslucentBackground, Context aContext) {
        mTranslucentBackground = useTranslucentBackground;
        mContext = aContext;
        mImages = new Images(mContext);
    }

	@Override
	public void onDrawFrame(GL10 gl) {
       nativeRender();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		nativeResize(width, height);
		Log.i("renderer", "surface changed/resized");
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
 		nativeInit();
 		
 		mImages.loadImages();
 		
 		nativeInitClasses();
 		Log.i("renderer", "initiliased");
	}
	
	public void finalize()
	{
		nativeDone();
	}

}
