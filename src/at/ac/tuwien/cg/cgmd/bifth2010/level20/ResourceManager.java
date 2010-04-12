/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * @author Reinbert
 *
 */
public class ResourceManager {
	
	protected static GL10 gl;
	protected static Context context;
	protected static Hashtable<Integer, Integer> textures;

	/**
	 * @param gl
	 * @param context
	 */
	public static void init(GL10 gl, Context context) {
		ResourceManager.gl = gl;
		ResourceManager.context = context;
		
		textures = new Hashtable<Integer, Integer>();
	}
	
	/**
	 * @param resource
	 * @return
	 */
	public static int loadTexture(final int resource) {
		
		// Try to find the texture
		Integer t = textures.get(resource);
		if (t != null) {
			return t;
		}		
		// Texture not yet created. Do it now
		

		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(resource);
		Bitmap bitmap = null;
		
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate there texture pointer
		int[] texture = new int[1];
		gl.glGenTextures(1, texture, 0);		

		//Create Linear Filtered Texture and bind it to texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR); //GL_LINEAR_MIPMAP_LINEAR
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
		//
		} else {
			Toast.makeText(context, "GL11 not supported", 2);
		}		
		
		//Clean up
		bitmap.recycle();
		
		textures.put(resource, texture[0]);
		return texture[0];
	}
}
