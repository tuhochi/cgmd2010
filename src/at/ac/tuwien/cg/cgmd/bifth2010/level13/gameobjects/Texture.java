package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * 
 * @author group13
 * 
 * class representing a texture
 * responsible for loading textures
 *
 */
public abstract class Texture {
	/** texture buffer */
	protected FloatBuffer textureBuffer;

	/** texture */
	protected float[] texture;

	/** textures */
	protected int[] textures;

	/** texture id */
	protected int[] textureId;

	/**
	 * constructor initializes common texture attributes
	 */
	public Texture() {
		//define texture coordinates
		texture = new float[8];
		texture[0] = 0.0f;
		texture[1] = 1.0f;

		texture[2] = 1.0f;
		texture[3] = 1.0f;

		texture[4] = 1.0f;
		texture[5] = 0.0f;

		texture[6] = 0.0f;
		texture[7] = 0.0f;

		//set up texture buffer
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		this.textureId = new int[1];
		this.textures = new int[1];
	}

	/**
	 * loads a texture
	 * @param gl gl
	 * @param context context
	 */
	protected void loadGLTexture(GL10 gl, Context context) {
		//set number of textures that will be generated
		gl.glGenTextures(textureId.length, textures, 0);

		//load texture from resources
		InputStream is = context.getResources().openRawResource(textureId[0]);

		Bitmap bmp = null;

		try {
			bmp = BitmapFactory.decodeStream(is);
		}
		finally {
			try {
				is.close();
				is = null;	
			}
			catch(IOException e) {
				//do nothing
			}
		}

		//bind and set texture parameters
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

		bmp.recycle();
	}
}