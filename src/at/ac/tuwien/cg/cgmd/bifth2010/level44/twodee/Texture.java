package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * Class that represents the one big texture used
 * 
 * @author thp
 */

public class Texture {
	/** the name of the opengl-texture */
	int textureName;
	/** the width of the texture */
	int width;
	/** the height of the texture */
	int height;

	/**
	 * Creates the texture
	 * @param gl OpenGL
	 * @param context the context used for getting the resource
	 * @param resource the resource-id of the texture
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
	 * @return the texture-name
	 */
	public int getTextureName() {
		return textureName;
	}

	/**
	 * @return the width of the texture
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height of the texture
	 */
	public float getHeight() {
		return height;
	}

}
