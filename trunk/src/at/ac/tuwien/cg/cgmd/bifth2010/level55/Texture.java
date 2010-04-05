package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Texture {
	int texture;
	static Context context;
	static GL10 gl;
	
	static int activeTexture;
	
	static Vector<Integer> loadedTex=new Vector<Integer>();
	
	public Texture () {
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
				int tile_size=tile_height*tile_width;
				
				int[] pixels = new int[tile_width];
				
				short[] rgba_4444 = new short[tile_size];
				
				int i=0;
				for (int y = 0; y < tile_height; y++) {
					image.getPixels(pixels, 0, tile_width, 0, y, tile_width,1);
		            for (int x = 0; x < tile_width; x++) {
		                int argb = pixels[x];
		                
		                // 0xFFFFFFFF
		                // A                             R                             G                           B
		                // 31 30 29 28 27 26 25 24       23 22 21 20 19 18 17 16       15 14 13 12 11 10 9 8       7 6 5 4 3 2 1 0
		                
		                int r = 0xf & (argb >> 20);
		                int g = 0xf & (argb >> 12);
		                int b = 0xf & (argb >> 4);
		                int a = 0xf & (argb >> 28);
		                int rgba = (r << 12) | (g << 8) | (b << 4) | a;
		                rgba_4444[i] = (short) rgba;
		                ++i;
		            }
		        }
	
				ShortBuffer texBuffer=ShortBuffer.wrap(rgba_4444, 0, tile_size);
				
				IntBuffer textures=IntBuffer.allocate(1);
				
				gl.glGenTextures(1, textures);
				texture=textures.get(0);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
				gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
				gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		
				gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, tile_width, tile_height,
					0, GL10.GL_RGBA, GL10.GL_UNSIGNED_SHORT_4_4_4_4, texBuffer);
				
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
