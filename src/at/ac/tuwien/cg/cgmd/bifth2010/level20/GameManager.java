package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


/**
 * This class is responsible for managing the whole game. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameManager implements Renderable {

	static final int BUNNY_TEXTURE = R.drawable.l20_icon;
	static final int BUNNY_ENTITY = 0;
	
	protected Hashtable<Integer, Integer> textures;
	protected Hashtable<Integer, ProductEntity> entities;

	public Activity activity;
	public RenderView renderView;
	public GL10 gl;
	
	protected Shelf shelf;
	public float scrollSpeed;
	
//	
//	protected ProductEntity pe;
	
	/**
	 * @param gl
	 * @param context
	 */
	public GameManager(RenderView renderView, GL10 gl) {
		
		this.activity = (Activity)renderView.getContext();
		this.renderView = renderView;
		this.gl = gl;
		
		textures = new Hashtable<Integer, Integer>();
		entities = new Hashtable<Integer, ProductEntity>();
		
		scrollSpeed = 100f; // Pixel per second
		createEntities();
	}
	
	
	/**
	 * 
	 */
	public void createEntities() {
		
		shelf = new Shelf(480, 320);
		shelf.texture = getTexture(R.drawable.l17_crate);
		
		ProductEntity re = new ProductEntity(100, 100, 1, 100, 100);
		re.texture = getTexture(R.drawable.l20_icon);
		re.setAngle(0);
		entities.put(0, re);
		
		re = new ProductEntity(150, 150, 2, 100, 100);
		re.texture = getTexture(R.drawable.l00_coin);
		re.setAngle(0);
		entities.put(1, re);
		
		re = new ProductEntity(300, 150, 3, 150, 100);
		re.texture = getTexture(R.drawable.l17_crate);
		re.setAngle(0);
		entities.put(2, re);
		
//		pe = new ProductEntity(100, 100, 1, 100, 100);
//		pe.texture = getTexture(R.drawable.l20_icon);
//		pe.setAngle(45);
//		entities.put(3, pe);
		
//		bunny.setId(BUNNY_ENTITY);
//		bunny.setTextureId(textures.get(BUNNY_TEXTURE));
	}


	
	/**
	 * @param dt The delta time since the last frame.
	 */
	public void update(float dt) {
		
		// Move scrollSpeed pixel every second.
		float speed = scrollSpeed * dt;
		float shelfSpeed = speed / renderView.getWidth();
		
		shelf.scrollBy(shelfSpeed);
		
		Enumeration<Integer> keys = entities.keys();
		ProductEntity pe = null;
		
		while(keys.hasMoreElements()) {
			pe = entities.get(keys.nextElement());
			
			pe.setX(pe.x() - speed);
			
			if (pe.x() < -100) {
				
				pe.setX(renderView.getWidth() + 100);
				pe.setY((float)Math.random() * renderView.getHeight());
				pe.visible = true;
			}
		}
	}

	
	@Override
	public void render(GL10 gl) {
		
		shelf.render(gl);
		
		// Render entities.		
		Enumeration<Integer> keys = entities.keys();
		while(keys.hasMoreElements()) {
			entities.get(keys.nextElement()).render(gl);
		}
	}


	/**
	 *  Reacts to moves and presses on the touchscreen.
	 */
	public void onTouch(float x, float y) {
		
		
		Enumeration<Integer> keys = entities.keys();
		ProductEntity pe2 = null;
		
		while(keys.hasMoreElements()) {
			
			pe2 = entities.get(keys.nextElement());
			
			if (pe2.visible && pe2.hitTest(x, y)) {
				pe2.visible = false;
			}
		}
		
	}
	
	
	

	/**
	 * @param resource The resource identifier delivered by "at.ac.tuwien.cg.cgmd.bifth2010.R".
	 * @returns The texture id of the file. 
	 */
	public int getTexture(int resource) {
		
		// Try to find the texture
		Integer t = textures.get(resource);
		if (t != null) {
			return t;
		}		
		// Texture not yet created. Do it now
		

		//Get the texture from the Android resource directory
		InputStream is = activity.getResources().openRawResource(resource);
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
			Toast.makeText(activity, "GL11 not supported", 2);
		}		
			
		//Clean up
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		textures.put(resource, texture[0]);
		return texture[0];	
	}
}
