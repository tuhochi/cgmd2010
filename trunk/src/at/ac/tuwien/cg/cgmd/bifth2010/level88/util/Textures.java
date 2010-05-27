package at.ac.tuwien.cg.cgmd.bifth2010.level88.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


/**
 * Textures class used to manage all textures needed by level 88
 * @author Asperger, Radax
 */
public class Textures {
	/**
	 * Map used to map resource Ids to texture object Ids
	 */
	private HashMap<Integer,Integer> resources2texIds;
	/**
	 * List of all textures needed by level 88
	 */
	private ArrayList<Integer> textureFiles;
	/**
	 * Array containing ids for the OpenGL texture objects
	 */
	private int[] textureIds;
	/**
	 *  OpenGL context of the android application
	 */
	private GL10 gl;
	/**
	 *  Context of the android application
	 */
	private Context context;
	/**
	 * Id of the last bound texture, used to avoid
	 * rebinding already bound textures.
	 */
	private int lastBound;

	
	/**
	 * Constructor
	 * @param _gl OpenGL context of android
	 * @param _context Android context
	 */
	public Textures(GL10 _gl, Context _context) {
		context = _context;
		gl = _gl;
		resources2texIds = new HashMap<Integer,Integer>();
		lastBound = -1;

		addTexture(R.drawable.l88_bunny);
		addTexture(R.drawable.l88_police);
		addTexture(R.drawable.l88_stash_red);
		addTexture(R.drawable.l88_stash_yellow);
		addTexture(R.drawable.l88_stash_orange);
		
		addTexture(R.drawable.l88_testtex);
		
		addTexture(R.drawable.l88_atlas);
	
		
		loadTextures();
	}
	
	
	/**
	 * Bind the texture resource
	 * @param resource resource to bind
	 */
	public void bind(int resource) {
		if( lastBound != resource ) {
			gl.glBindTexture(
					GL10.GL_TEXTURE_2D,
					resources2texIds.get(resource)
				); 
			lastBound = resource;
		}
	}
	
	
	/**
	 * Load the textures from the resource directory
	 */
	public void loadTextures() {
		textureIds = new int[textureFiles.size()];
		gl.glGenTextures(textureFiles.size(), textureIds, 0);
		
		for(int i=0; i<textureFiles.size(); i++) {
			resources2texIds.put(textureFiles.get(i), textureIds[i]);
			
			//Get the texture from the Android resource directory
			InputStream is = context.getResources().openRawResource(textureFiles.get(i));
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

			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[i]);
					
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
			
			//Create Nearest Filtered Texture
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

			//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
		
			//Clean up
			bitmap.recycle();
		}
	}
	
	
	/**
	 * Add a new texture
	 * @param resource texture to be added
	 */
	public void addTexture(int resource) {
		if( textureFiles == null ) {
			textureFiles = new ArrayList<Integer> ();
		}
		textureFiles.add(resource);
	}
}
