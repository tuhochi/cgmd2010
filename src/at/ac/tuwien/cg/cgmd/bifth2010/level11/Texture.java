package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.*;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Integer;
import android.opengl.GLUtils;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Texture {
	private java.util.HashMap <Integer, Integer> textureMap; 
	private int[] textureFiles;
	private GL10 gl;
	private Context context;
	private int[] textures;
	
	public Texture(GL10 gl,Context context)
	{
		this.gl = gl;
		this.context = context;
		this.textureMap = new java.util.HashMap<Integer, Integer> ();
	}
	
	public void loadTextures()
	{
		for(int i=0;i<textureFiles.length;i++)
		{
			// get the texture from the Android resource directory
			InputStream is = context.getResources().openRawResource(textureFiles[i]);
			Bitmap bitmap = null;
			try {
				// BitmapFactory is an Android graphics utility for images
				bitmap = BitmapFactory.decodeStream(is);
				
				this.textureMap.put(new Integer(textureFiles[i]),new Integer(i));
	
			} finally {
				// always clear and close, that's what they said at DS LU
				try {
					is.close();
					is = null;
				} catch (IOException e) {
				}
			}
			
			
			// generate one texture pointer...
			gl.glGenTextures(1, textures, 0);
			//...and bind it to our array
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			
			//Create Nearest Filtered Texture
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

			//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
		}
		
		
	}
	
	public void setTexture(int id)
	{
		try
		{
			int textureid = this.textureMap.get(new Integer(id)).intValue();
	    	gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[textureid]);
		
		}
		catch(Exception e)
		{
			return;
		}
	}

	public void add(int resource)
	{
		if(textureFiles==null)
		{
			textureFiles = new int[1];
			textureFiles[0]=resource;
		}
		else
		{
			int[] newarray = new int[textureFiles.length+1];
			for(int i=0;i<textureFiles.length;i++)
			{
				newarray[i]=textureFiles[i];
			}
			newarray[textureFiles.length]=resource;
			textureFiles = newarray;
		}
	}

}

