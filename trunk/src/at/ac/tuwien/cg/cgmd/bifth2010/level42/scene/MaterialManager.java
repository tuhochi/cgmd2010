package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;
import static android.opengl.GLUtils.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class MaterialManager
{
	public static final MaterialManager instance = new MaterialManager();
	
	private final HashMap<String, Material> materials;
	private String boundMaterialName;
	private boolean textureEnabled;
	
	private MaterialManager()
	{
		materials = new HashMap<String, Material>();
		boundMaterialName = "";
		textureEnabled = true;
	}
	
	public void reset()
	{
		materials.clear();
		boundMaterialName = "";
		textureEnabled = true;
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
	
	public void bindMaterial(Material material)
	{
		if(!material.name.equals(boundMaterialName))
		{
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, material.ambient.asArray, 0);
	        glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material.diffuse.asArray, 0);
	        glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, material.specular.asArray, 0);
	        glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material.ks);
	        if(textureEnabled && material.texId == -1)
	        	glDisable(GL_TEXTURE_2D);
	        if(material.texId != -1)
	        {
	        	if(!textureEnabled)
	        		glEnable(GL_TEXTURE_2D);
	        	glBindTexture(GL_TEXTURE_2D, material.texId);
	        }
	        boundMaterialName = material.name;
		}
	}
	
	public void bindMaterial(String name)
	{
		if(materials.containsKey(name))
			bindMaterial(materials.get(name));
	}
	
	/*
	 * adds a material
	 * @return the name of the new material
	 */
	public Material addMaterial(String name, Color4 ambient, Color4 diffuse, Color4 specular, float ks, String textureFilename)
	{
		if(materials.containsKey(name))
			Log.w(LevelActivity.TAG, "Material '" + name + "' already exists, replacing...");
		Material m = new Material(name, ambient, diffuse, specular, ks, textureFilename);
		materials.put(name, m);
		return m;
	}
	
	/*
	 * @return creates the material if it doesn't exist yet and returns it
	 */
	public Material getMaterial(String name, Color4 ambient, Color4 diffuse, Color4 specular, float ks, String textureFilename)
	{
		if(materials.containsKey(name))
			return materials.get(name);
		else
		{
			Material m = new Material(name, ambient, diffuse, specular, ks, textureFilename);
			materials.put(name, m);
			return m;
		}
	}
	
	public class Material
	{
		private final String name;
		private final Color4 ambient;
		private final Color4 diffuse;
		private final Color4 specular;
		private final float ks;
		private final Bitmap tex;
		private int texId;
		private boolean initialized;
		
		private Material(String name, Color4 ambient, Color4 diffuse, Color4 specular, float ks, String textureFilename)
		{
			this.name = name;
			this.ambient = ambient;
			this.diffuse = diffuse;
			this.specular = specular;
			this.ks = ks;
			
			if(!textureFilename.equals(""))
				tex = loadTexture(textureFilename);
			else
				tex = null;
		}
		
		void init()
		{
			if(!initialized)
			{
				if(tex != null)
				{
					int[] textures = new int[1];
					glGenTextures(1, textures, 0);
					texId = textures[0];
					glBindTexture(GL_TEXTURE_2D, texId);
					glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
					glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);

					if(Config.GLES11)
					{
						glTexParameterf(GL_TEXTURE_2D, GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
						texImage2D(GL_TEXTURE_2D, 0, tex, 0);
					}
					else
					{
						buildMipmap(tex);
					}
				}
				else
					texId = -1;
				
				initialized = true;
			}
		}
		
		void deInit()
		{
			initialized = false;
		}
		
		public void persist(DataOutputStream dos)
		{
		}
		
		public void restore(DataInputStream dis)
		{
		}
		
		private Bitmap loadTexture(String filename)
		{
			Context context = LevelActivity.getInstance();
			InputStream is = context.getResources().openRawResource(context.getResources().getIdentifier(filename, "drawable", "at.ac.tuwien.cg.cgmd.bifth2010"));
			Bitmap bitmap = null;
			try
			{
				bitmap = BitmapFactory.decodeStream(is);
			}
			catch(Throwable t)
			{
				Log.e(LevelActivity.TAG, "Could not load Texture: " + filename);
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
