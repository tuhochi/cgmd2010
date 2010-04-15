package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import static android.opengl.GLES10.GL_LINEAR;
import static android.opengl.GLES10.GL_LINEAR_MIPMAP_NEAREST;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.GL_TRUE;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glTexParameterf;
import static android.opengl.GLUtils.texImage2D;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;


/**
 * The Class TextureManager manages the textures to centralize texture loading.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class TextureManager {

	/** The texture id. */
	private int texId; 
	
	/** The texture map. */
	private HashMap<Integer, Integer> textureMap;
	
	/** The instance of TextureManager to pass around. */
	public static TextureManager instance = new TextureManager(); 
	
	/**
	 * Instantiates a new texture manager.
	 */
	public TextureManager() {
		textureMap = new HashMap<Integer, Integer>();
		instance = this; 
	}
	
	/**
	 * Resets the textureMap, so that textures are reloaded.
	 */
	public void reset()
	{
		textureMap.clear();
	}
	
	/**
	 * Loads a texture.
	 *
	 * @param res the resource
	 * @param resId the resource id
	 */
	public void loadTexture(Resources res, int resId) {
		// thx to lvl17
		
		// don t load again
		if (textureMap.containsKey(resId))
			return;
		
	
		Context context = LevelActivity.getContext();
		InputStream is = context.getResources().openRawResource(resId);
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory.decodeStream(is);
		}
		catch(Throwable t)
		{
			Log.e("TextureLoader::loadTexture", "Could not load Texture: " + res.toString());
			bitmap = null;
		}
		finally
		{
			//Always clear and close
			try
			{
				is.close();
				is = null;
			}
			catch (IOException e)
			{
			}
		}
		
		if(bitmap != null) {
			int[] textures = new int[1];
			glGenTextures(1, textures, 0);
			texId = textures[0];
			glBindTexture(GL_TEXTURE_2D, texId);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
			texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		
			//Clean up
			bitmap.recycle();
		}
		else
			texId = -1;
		
		textureMap.put(resId, texId);
	
}
	
	/**
	 * Gets the texture id.
	 *
	 * @param res the resource
	 * @param resId the resource id
	 * @return the texture id
	 */
	public int getTextureId(Resources res, int resId) {
		if (!textureMap.containsKey(resId))
			loadTexture(res, resId);
		
		return textureMap.get(resId);
	
	}
}
