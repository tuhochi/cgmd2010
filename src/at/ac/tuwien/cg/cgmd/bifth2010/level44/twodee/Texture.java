package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * High-level abstraction of a texture
 * 
 * This class provides the necessary abstraction for
 * simply using drawable resources as textures in an
 * OpenGL context.
 * 
 * @author Thomas Perl
 */

public class Texture {
	/** the name of the opengl-texture */
	int textureName;
	/** the width of the texture */
	int width;
	/** the height of the texture */
	int height;

	/**
	 * Allocate and load a new texture from a given drawable
	 * 
	 * @param gl The OpenGL ES context into which to load the texture
	 * @param context The context used for getting the resource
	 * @param resource The ID of the drawable texture resource
	 */
	public Texture(GL10 gl, Context context, int resource) {
		InputStream is = context.getResources().openRawResource(resource);

		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}

		// bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
		bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, false);

		width = bitmap.getWidth();
		height = bitmap.getHeight();
		System.err.println("Loaded texture: " + width + "x" + height);

		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		textureName = textures[0];

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

		// gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP,
		// GL11.GL_TRUE);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();
	}

	/**
	 * @return The ID (OpenGL "texture name") used for binding
	 */
	public int getTextureName() {
		return textureName;
	}

	/**
	 * @return the width of the texture in pixels
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height of the texture in pixels
	 */
	public float getHeight() {
		return height;
	}

}
