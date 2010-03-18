package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public class Texture {
	int textureName;
	int width;
	int height;
	
	public Texture(GL10 gl, Context context, int resource) {
		InputStream is = context.getResources().openRawResource(resource);
		
        Bitmap bitmap;
		try {
		    bitmap = BitmapFactory.decodeStream(is);
		} finally {
		    try {
		        is.close();
		    } catch(IOException e) {
		    }
		}

		//bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
		bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, false);
		
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		System.err.println("Loaded texture: " + width + "x" + height);

		int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        textureName = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

        //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
	}
	
	public int getTextureName() {
		return textureName;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}

}
