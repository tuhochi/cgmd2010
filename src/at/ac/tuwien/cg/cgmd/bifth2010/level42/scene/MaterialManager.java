package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;
import static android.opengl.GLUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class MaterialManager
{
	private static final MaterialManager instance = new MaterialManager();
	
	private HashMap<String, Material> materials;
	private String boundMaterialName;
	
	private MaterialManager()
	{
		materials = new HashMap<String, Material>();
		boundMaterialName = "";
	}
	
	public static MaterialManager getInstance()
	{
		return instance;
	}
	
	public boolean hasMaterial(String name)
	{
		return materials.containsKey(name);
	}
	
	/*
	 * @return the material for the given name or null if it doesn't exist
	 */
	public Material getMaterial(String name)
	{
		return materials.get(name);	
	}
	
	public void bindMaterial(String name)
	{
		if(materials.containsKey(name) && !boundMaterialName.equals(name))
		{
			Material m = materials.get(name);
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, m.ambient.asArray, 0);
	        glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, m.diffuse.asArray, 0);
	        glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, m.specular.asArray, 0);
	        glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, m.ks);
	        glBindTexture(GL_TEXTURE_2D, m.texId);
	        boundMaterialName = name;
		}
	}
	
	/*
	 * adds a material
	 * @return the name of the new material
	 */
	public String addMaterial(String name, Color4 ambient, Color4 diffuse, Color4 specular, float ks, int textureResourceID)
	{
		if(materials.containsKey(name))
			Log.w(LevelActivity.TAG, "Material '" + name + "' already exists, replacing...");
		materials.put(name, new Material(name, ambient, diffuse, specular, ks, textureResourceID));
		return name;
	}
	
	/*
	 * @return creates the material if it doesn't exist yet and returns it
	 */
	public Material getMaterial(String name, Color4 ambient, Color4 diffuse, Color4 specular, float ks, int textureResourceID)
	{
		if(materials.containsKey(name))
			return materials.get(name);
		else
		{
			Material m = new Material(name, ambient, diffuse, specular, ks, textureResourceID);
			materials.put(name, m);
			return m;
		}
	}
	
	public class Material
	{
		public final String name;
		public final Color4 ambient;
		public final Color4 diffuse;
		public final Color4 specular;
		public final float ks;
		public final int texId;
		
		private Material(String name, Color4 ambient, Color4 diffuse, Color4 specular, float ks, int textureResourceID)
		{
			this.name = name;
			this.ambient = ambient;
			this.diffuse = diffuse;
			this.specular = specular;
			this.ks = ks;
			
			Bitmap tex = loadTexture(textureResourceID);
			if(tex != null)
			{
				int[] textures = new int[1];
				glGenTextures(1, textures, 0);
				texId = textures[0];
				glBindTexture(GL_TEXTURE_2D, texId);
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
				
				if(Config.GLES11)
				{
					glTexParameterf(GL_TEXTURE_2D, GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
					texImage2D(GL_TEXTURE_2D, 0, tex, 0);
				}
				else
				{
					buildMipmap(tex);
				}		
				
				//Clean up
				tex.recycle();
			}
			else
				texId = 0;
		}
		
		private Bitmap loadTexture(int resourceID)
		{
			InputStream is = LevelActivity.getInstance().getResources().openRawResource(resourceID);
			Bitmap bitmap = null;
			try
			{
				bitmap = BitmapFactory.decodeStream(is);
			}
			catch(Throwable t)
			{
				Log.e(LevelActivity.TAG, "Could not load Texture: " + resourceID);
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
			return bitmap;
		}
		
		/*
		 * Our own MipMap generation implementation.
		 * Scale the original bitmap down, always by factor two,
		 * and set it as new mipmap level.
		 * 
		 * Thanks to Mike Miller (with minor changes by l17)!
		 * 
		 * @param gl - The GL Context
		 * @param bitmap - The bitmap to mipmap
		 */
		private void buildMipmap(Bitmap bitmap)
		{
			int level = 0;
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();

			while(height >= 1 || width >= 1)
			{
				//First of all, generate the texture from our bitmap and set it to the according level
				texImage2D(GL_TEXTURE_2D, level, bitmap, 0);
				
				if(height == 1 || width == 1)
					break;

				//Increase the mipmap level
				level++;

				//Downscale
				height /= 2;
				width /= 2;
				Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
				
				//Clean up
				bitmap.recycle();
				bitmap = bitmap2;
			}
		}
	}
}
