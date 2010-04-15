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
    private static native void nativeDone();
    private static native void nativeRender();
    private static native void nativeResize(int w, int h);
    //debug
    private static native int nativeNumDrawn();
    private static native int nativePushBitmap(int[] pixels, int w, int h);

    private boolean mTranslucentBackground;
    private Context mContext;

    public L77Renderer(boolean useTranslucentBackground, Context aContext) {
        mTranslucentBackground = useTranslucentBackground;
        mContext = aContext;
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
 		
 		loadBitmapTest();
 		Log.i("renderer", "initiliased");
	}
	
	public void loadBitmapTest()
	{
        InputStream is = mContext.getResources().openRawResource(R.drawable.l00_icon);

        Bitmap bitmap;
		try {
		    bitmap = BitmapFactory.decodeStream(is);
		} finally {
		    try {
		        is.close();
		    } catch(IOException e) {
		    }
		}
		
		//...or Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imgname);
		//1st method: GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		//2nd method
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		int[] pixels = new int[w * h]; 
		bitmap.getPixels(pixels, 0, w, 0, 0, w, h); 
		
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				if (i > 5 && i < (w - 5) &&
						j > 5 && j < (h - 5))
				{
					pixels[j*w+i] = 0xff00ff00;
				}
				else
				{
					pixels[j*w+i] = 0x00770077;
				}
			}
		}
		
		nativePushBitmap(pixels, w, h);
		
	}
	
	public void finalize()
	{
		nativeDone();
	}

}
