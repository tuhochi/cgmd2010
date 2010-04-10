package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.nio.IntBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture {
	int texture;
	float texelWidth;
	float texelHeight;
	
	
	static Context context;
	static GL10 gl;
	
	static int activeTexture;
	
	static Vector<Integer> loadedTex=new Vector<Integer>();
	
	public Texture () {
	}
	
	static void cleanUp() {
		Log.d("Texture", "cleanUp");
		loadedTex.clear();
	}
	
	public void finalize() {
		Log.d("Texture", "finalize");
		IntBuffer textures=IntBuffer.allocate(1);
		textures.put(texture);
		gl.glDeleteTextures(1, textures);
		
		loadedTex.remove(texture);
	}
	
	static public void setContext(Context _context) {
		context=_context;
	}
	
	static public void setGL(GL10 _gl) {
		gl=_gl;
	}
	
	boolean create(int resourceID) {
		texture=loadedTex.indexOf(new Integer(resourceID));
		if (texture==-1) {
			Bitmap image=BitmapFactory.decodeResource(context.getResources(), resourceID);
			
			if (image!=null) {
				
				int tile_height=image.getHeight();
				int tile_width=image.getWidth();
				
				texelWidth=1.0f/(float)tile_width;
				texelHeight=1.0f/(float)tile_height;
				
				IntBuffer textures=IntBuffer.allocate(1);
				
				gl.glGenTextures(1, textures);
				texture=textures.get(0);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
				gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
				gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
				
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image, 0);
				
				gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
				
				if (loadedTex.size()<texture+1) {
					loadedTex.setSize(texture+1);
				}
				loadedTex.set(texture,new Integer(resourceID));
				
				Log.d("Texture","success");
				return true;
			}
			
			Log.d("Texture","failed");
			return false;
		} else {
			Log.d("Texture","already loaded");
		}
		return true;
	}
	
	void bind(GL10 gl) {
		if (activeTexture!=texture) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			activeTexture=texture;
		}
	}
}
