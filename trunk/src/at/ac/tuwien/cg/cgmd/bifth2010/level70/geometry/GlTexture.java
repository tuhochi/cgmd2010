package at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry;

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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;

/**
 * OpenGL Texture Wrapper
 */
public class GlTexture {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private int[] texId;   //< Texture handle
	private int   width;   //< Width of the texture
	private int   height;  //< Height of the texture
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create OpenGL Texture.
	 * @param resid Resource-ID
	 */
	public GlTexture(int resid) {
		
		create(resid);
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Bind texture.
	 */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texId[0]);
	}
	
	
	/**
	 * Return texture width.
	 * @return width of the texture
	 */
	public int getWidth() {
		return width;
	}
	
	
	/**
	 * Return texture height
	 * @return height of the texture
	 */
	public int getHeight() {
		return height;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----
	
	/**
	 * Create texture
	 * @param resid Resource-ID
	 */
	private void create(int resid) {
		try {
			Context     ctx   = LevelActivity.getInstance();
			InputStream is    = ctx.getResources().openRawResource(resid);
			Bitmap      bmp   = BitmapFactory.decodeStream(is);
			is.close();
			
			texId = new int[1];
			glGenTextures(1, texId, 0);
			glBindTexture(GL_TEXTURE_2D, texId[0]);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
			texImage2D(GL_TEXTURE_2D, 0, bmp, 0);
			
			width  = bmp.getWidth();
			height = bmp.getHeight();
			bmp.recycle();
		}
		catch(IOException e) {
			Log.e("GlTexture", e.getMessage());
		}
	}
}
