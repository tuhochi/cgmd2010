package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import javax.microedition.khronos.opengles.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Integer;
import android.opengl.GLUtils;

public class TextureManager {
	
	//private static TextureManager mInstance;// = new TextureManager();

	public static java.util.HashMap <Integer, Integer> textureMap; 
	private int[] textureFiles;
	private GL10 gl;
	private Context context;
	private int[] textures;
	private static TextureManager singletonObject;
	
	public void initializeGL(GL10 gl){		
		this.gl = gl;
	}
	
	public void initializeContext( Context context ){
		this.context = context;
		this.textureMap = new java.util.HashMap<Integer, Integer> ();
	}

	private TextureManager() {
	}
	
	public static synchronized TextureManager getSingletonObject() {
		if (singletonObject == null) {
			singletonObject = new TextureManager();
		}
		return singletonObject;
	}
	
	/**
	 * Add a resource to the textureFile array
	 * 
	 * @param resource
	 */
	public void add(int resource) {
		
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
	 */
	public void loadTextures() {
		
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
			
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
		}
		
	}
	/**
	 * sets active texture according to id
	 * @param id
	 */
	public void setTexture(int id) {
		
		try {
			int textureid = this.textureMap.get((Integer)id).intValue();
	    	gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[textureid]);
		
		}
		catch(Exception e) {
			return;
		}
	}
	

}

