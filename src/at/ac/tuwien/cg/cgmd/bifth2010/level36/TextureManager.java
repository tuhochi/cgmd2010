	package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import static android.opengl.GLES10.GL_LINEAR;
import static android.opengl.GLES10.GL_LINEAR_MIPMAP_NEAREST;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.GL_TRUE;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glTexParameterf;
import static android.opengl.GLUtils.texImage2D;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.opengl.GLUtils;
import android.util.Log;


public class TextureManager {

	private static TextureManager instance;
	private Context context;
	
	public TextureManager(Context context) {
		instance = this; 
		this.context = context;
	}
	
	public int loadTexture(GL10 gl, int resId) {
		InputStream is = this.context.getResources().openRawResource(resId);
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory.decodeStream(is);
		}
		catch(Throwable t)
		{
			Log.e("TextureLoader::loadTexture", "Could not load Texture.");
			bitmap = null;
		}
		finally
		{
			//Always clear and close
			try
			{
				is.close();
				is = null;
			}
			catch (IOException e)
			{
			}
		}
		
		//bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
		bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, false);
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		System.err.println("Loaded texture: " + width + "x" + height);

		int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        int textureName = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

        //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
		
//		if(bitmap != null) {
//			int[] textures = new int[1];
//			glGenTextures(1, textures, 0);
//			texId = textures[0];
//			glBindTexture(GL_TEXTURE_2D, texId);
//			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
//			glTexParameterf(GL_TEXTURE_2D, GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
//			texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
//		
//			//Clean up
//			bitmap.recycle();
//		}
//		else
//			texId = -1;
		
		return textureName;
	}
	
}