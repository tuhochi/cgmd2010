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
 * Texture class for level 88
 * @author Asperger, Radax
 */
public class Textures {
	private HashMap<Integer,Integer> resources2texIds;
	private ArrayList<Integer> textureFiles;
	private int[] textureIds;
	private GL10 gl; 
	private Context context; 
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
		
		addTexture(R.drawable.l88_street_turn);
		addTexture(R.drawable.l88_street_junction);
		addTexture(R.drawable.l88_street_straight);
		addTexture(R.drawable.l88_street_tjunction);
		addTexture(R.drawable.l88_street_none);
		addTexture(R.drawable.l88_street_end);
		
		addTexture(R.drawable.l88_house_jail);
		addTexture(R.drawable.l88_house_block1);
		addTexture(R.drawable.l88_house_block2);
		addTexture(R.drawable.l88_house_block3);
		addTexture(R.drawable.l88_house_block4);
		addTexture(R.drawable.l88_house_block5);
		
		addTexture(R.drawable.l88_bunny);
		addTexture(R.drawable.l88_police);
		addTexture(R.drawable.l88_stash_red);
		addTexture(R.drawable.l88_stash_green);
		addTexture(R.drawable.l88_stash_blue);
		addTexture(R.drawable.l88_stash_yellow);
		addTexture(R.drawable.l88_stash_orange);
		addTexture(R.drawable.l88_stash_magenta);
		addTexture(R.drawable.l88_stash_cyan);
		addTexture(R.drawable.l88_stash_dark);
		addTexture(R.drawable.l88_stash_light);
		
		addTexture(R.drawable.l88_testtex);
		addTexture(R.drawable.l88_greenstar);
		addTexture(R.drawable.l88_redstar);
		addTexture(R.drawable.l88_yellowstar);
		
		
		
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
			
			//Create Nearest Filtered Texture
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

			//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
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
