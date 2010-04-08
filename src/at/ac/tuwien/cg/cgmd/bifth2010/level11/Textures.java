package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.*;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Integer;
import android.opengl.GLUtils;
import android.util.Log;

public class Textures {
	private static final String LOG_TAG = Textures.class.getSimpleName();
	
	public static java.util.HashMap <Integer, Integer> textureMap; 
	public static Textures tex;
	private int[] textureFiles;
	private GL10 gl;
	private Context context;
	private int[] textures;
	
	public Textures(GL10 gl,Context context) {
		//Log.i(LOG_TAG, "Textures()");
		
		this.tex = this;
		
		this.gl = gl;
		this.context = context;
		this.textureMap = new java.util.HashMap<Integer, Integer> ();
	}
	
	/**
	 * Add a resource to the textureFile array
	 * 
	 * @param resource
	 */
	public void add(int resource) {
		//Log.i(LOG_TAG, "add()");
		
		if(textureFiles==null) {
			textureFiles = new int[1];
			textureFiles[0]=resource;
			
		} else {
			int[] newarray = new int[textureFiles.length+1];
			
			for(int i=0;i<textureFiles.length;i++) {
				newarray[i]=textureFiles[i];
			}
			
			newarray[textureFiles.length]=resource;
			textureFiles = newarray;
		}
	}
	
	/**
	 * Load all textures and put them into the textureMap
	 * 
	 * 
	 * 
	 */
	public void loadTextures() {
		//Log.i(LOG_TAG, "loadTextures()");
		
		// generate one texture pointer...
		int[] temp_texture = new int[textureFiles.length]; 
		gl.glGenTextures(textureFiles.length, temp_texture, 0);
		textures = temp_texture;
		
		for(int i=0;i<textureFiles.length;i++) {
			// get the texture from the Android resource directory
			InputStream is = context.getResources().openRawResource(textureFiles[i]);
			Bitmap bitmap = null;
			try {
				// BitmapFactory is an Android graphics utility for images
				bitmap = BitmapFactory.decodeStream(is);
				
	
			} finally {
				// always clear and close, that's what they said at DS LU
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			
			

			this.textureMap.put(new Integer(textureFiles[i]),new Integer(i));
			
			//...and bind it to our array
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
			
			//Create Nearest Filtered Texture
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

			//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
			//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
		}
		
	}
	
	public void setTexture(int id) {
		//Log.i(LOG_TAG, "setTexture()");
		
		try {
			int textureid = this.textureMap.get((Integer)id).intValue();
	    	gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[textureid]);
		
		}
		catch(Exception e) {
			return;
		}
	}



}

