package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.Cube;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;

public class GameManager {

	static final int BUNNY_TEXTURE = R.drawable.l20_icon;
	static final int BUNNY_ENTITY = 0;
	
	Hashtable<Integer, Integer> textures;
	Hashtable<Integer, TextureEntity> entities;
	Context context;
	GL10 glHdl;
	
	private Cube cube;
	
	public GameManager(GL10 gl, Context context)
	{
		this.context = context;
		this.glHdl = gl;
		
		textures = new Hashtable<Integer, Integer>();
		entities = new Hashtable<Integer, TextureEntity>();
		
		loadTextures();
		createEntities();
		
		cube = new Cube();
		cube.loadGLTexture(gl, context);
	}
	

	public void update() {	
		

	}
	
	public void renderEntities()
	{
		// Render entities.
		Enumeration<Integer> keys = entities.keys();		

		while(keys.hasMoreElements()) {
			entities.get(keys.nextElement()).render(glHdl);
		}
		//cube.draw(glHdl, 0);
	}
	
	private void loadTextures() {
		int texId = loadTexture(BUNNY_TEXTURE);
		if (texId >= 0) {
			textures.put(BUNNY_TEXTURE, new Integer(texId));
		}		
	}
	
	private void createEntities() {
		TextureEntity bunny = new TextureEntity(100, 100);
		//bunny.setPos(0f, 0f);
		//bunny.setScale(1f, 1f);
		//bunny.setDepth(0f);
		bunny.setId(BUNNY_ENTITY);
		bunny.setTextureId(textures.get(BUNNY_TEXTURE));
					
		entities.put(new Integer(BUNNY_ENTITY), bunny);		
	}


	public int loadTexture(int bunnyTexture) {

		// Texture already loaded.
		if (textures.containsKey(bunnyTexture))
			return -1;

//		int resource = context.getResources().getIdentifier(bunnyTexture,
//				"drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		int id = R.drawable.l20_icon;
		InputStream is = context.getResources().openRawResource(bunnyTexture);
		Bitmap bitmap = null;

		try {
			// BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			// Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		// Generate there texture pointer
		int[] texture = new int[1];
		glHdl.glGenTextures(1, texture, 0);

		// Create Linear Filtered Texture and bind it to texture
		glHdl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		glHdl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		glHdl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR_MIPMAP_LINEAR);

		if (glHdl instanceof GL11) {
			glHdl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP,
					GL11.GL_TRUE);
		} else {
			Toast.makeText(context, "GL11 not supported", 2);
		}

		// Clean up
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();

		return texture[0];
	}
}
