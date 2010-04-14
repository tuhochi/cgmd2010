package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class GameManager {

	static final int BUNNY_TEXTURE = R.drawable.l20_icon;
	static final int BUNNY_ENTITY = 0;
	
	Hashtable<Integer, Integer> textures;
	Hashtable<Integer, TextureEntity> entities;
	Context context;
	GL10 gl;
	
//	private Cube cube;
	private Triangle t;
	
	/**
	 * @param gl
	 * @param context
	 */
	public GameManager(GL10 gl, Context context)
	{
		this.context = context;
		this.gl = gl;
		
		textures = new Hashtable<Integer, Integer>();
		entities = new Hashtable<Integer, TextureEntity>();
		
		createEntities();
		
//		cube = new Cube();
//		cube.textures[0] = getTexture(R.drawable.l20_icon);
//		cube.textures[1] = getTexture(R.drawable.l00_coin);
//		cube.textures[2] = getTexture(R.drawable.l00_rabit_256);
		
		t = new Triangle();
	}
	

	/**
	 * 
	 */
	public void update() {	
		

	}
	
	public void renderEntities()
	{
		// INFO: Disabled for now
		// Render entities.
//		Enumeration<Integer> keys = entities.keys();		
//
//		while(keys.hasMoreElements()) {
//			entities.get(keys.nextElement()).render(gl);
//		}
//		cube.draw(gl, 0);
		t.draw(gl);
	}
	
// INFO: We don't need this anymore since a texture is automatically stored in a HashTable upon creation
//	private void loadTextures() {
//		int texId = getTexture(BUNNY_TEXTURE);
//		if (texId >= 0) {
//			textures.put(BUNNY_TEXTURE, new Integer(texId));
//		}		
//	}
	
	private void createEntities() {
		// INFO: Disabled for now
//		TextureEntity bunny = new TextureEntity(100, 100);
//		//bunny.setPos(0f, 0f);
//		//bunny.setScale(1f, 1f);
//		//bunny.setDepth(0f);
//		bunny.setId(BUNNY_ENTITY);
//		bunny.setTextureId(textures.get(BUNNY_TEXTURE));
//					
//		entities.put(new Integer(BUNNY_ENTITY), bunny);		
	}


	/**
	 * @param resource
	 * @return
	 */
	public int getTexture(final int resource) {
		
		// Try to find the texture
		Integer t = textures.get(resource);
		if (t != null) {
			return t;
		}		
		// Texture not yet created. Do it now
		

		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(resource);
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

		//Generate there texture pointer. (glGenTextures needs a pointer as input)
		int[] texture = new int[1];
		gl.glGenTextures(1, texture, 0);		

		//Create Linear Filtered Texture and bind it to texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
		
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);							
		} 
		else {
			Toast.makeText(context, "GL11 not supported", 2);
		}		
			
		//Clean up
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		textures.put(resource, texture[0]);
		return texture[0];	
	}
}
