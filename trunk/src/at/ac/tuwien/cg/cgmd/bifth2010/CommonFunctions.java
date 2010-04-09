package at.ac.tuwien.cg.cgmd.bifth2010;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;


public final class CommonFunctions {
	
	private static final String CLASS_TAG = CommonFunctions.class.getName();
	
	public static final int loadTexture(GL10 gl, Resources cResources, int iResource) {
		if(gl==null){
			Log.e(CLASS_TAG, "gl needed but not ready!");
			return -1;
		}
		
		
		int iTex = -1;
		Bitmap bm = null;
		try {
			int iError = gl.glGetError();
			if(iError>0) {
				Log.e(CLASS_TAG, "glError loading texture from ressource: "+GLU.gluErrorString(iError));
			}
			bm = BitmapFactory.decodeResource(cResources, iResource);
			iError = gl.glGetError();
			if(iError>0) {
				Log.e(CLASS_TAG, "glError loading texture from ressource: "+GLU.gluErrorString(iError));
			}
			iTex = loadTexture(gl,bm);
		} catch (OutOfMemoryError e) {
			Log.d(CLASS_TAG, "OutOfMemoryError", e);
			
		} finally {
			if(bm!=null){
				bm.recycle();
			}
		}
		return iTex;
	}
	
	public static final int loadTexture(GL10 gl, Bitmap bm) {
		
		if(gl==null){
			Log.e(CLASS_TAG, "gl needed but not ready!");
			return -1;
		}
		
		if((gl==null)||(bm==null))
			return -1;
		int[] tex = new int[1];
		
		gl.glGenTextures(1, tex, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex[0]);
		try{
			Bitmap.Config conf = bm.getConfig();
			if(conf==null) {
				Log.e(CLASS_TAG, "unknown bitmap configuration");
				gl.glDeleteTextures(1, tex, 0);
				return -1;
			} else {
	            int potWidth = (int) Math.pow(2,Math.ceil(Math.log((double)bm.getWidth())/Math.log(2.0))); 
	            int potHeight = (int) Math.pow(2,Math.ceil(Math.log((double)bm.getHeight())/Math.log(2.0)));
	            int iMax = Math.max(potWidth, potHeight);
	            iMax = Math.min(iMax,1024);
	            Bitmap bmScaled = Bitmap.createScaledBitmap(bm, iMax, iMax, false);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmScaled, 0);
				int iError = gl.glGetError();
				if(iError>0) {
					Log.e(CLASS_TAG, "glError loading texture from bitmap: "+GLU.gluErrorString(iError));
				} 
			}				
		}catch(Exception e){
			Log.e(CLASS_TAG, "couldn't load texture to graphics memory: "+e.getMessage());
		}
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		return tex[0];
	}

	public static int getIndexOfLevel(String level) {
		for(int i=0; i<Constants.LEVELIDS.length; i++) {
			if(Constants.LEVELIDS[i].equals(level))
			{
				return i;
			}
		}
		
		return -1;
	}
}
