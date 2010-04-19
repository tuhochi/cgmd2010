package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL;
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
public class GameManager implements Renderable, EventListener {

	static final int BUNNY_TEXTURE = R.drawable.l20_icon;
	static final int BUNNY_ENTITY = 0;
	
	public Activity activity;
	public RenderView renderView;
	// NOTE: We can't store gl, because it surely becomes invalid. 
//	public GL10 gl;
	
	// The texture collection. (The ids increase themselves)
	protected Hashtable<Integer, Integer> textures;
	
	// The entity collection
	protected Hashtable<Integer, ProductEntity> entities;
	
	// The animator collection
	protected Hashtable<Integer, Animator> animators;

	// The background Shelf of the game	
	protected Shelf shelf;
	
	// The moving speed of the background and the products 
	// TODO: Better name
	public float scrollSpeed;
	
	// The global distance traveled so far
	// TODO: Better name
	public float distMovedX; 
	
	// The Y position where products move over the screen.
	// TODO: Better name
	public float[] productSpawnY;
	
	// This value counts the number of columns, where products were spawned.  
	// TODO: Better name
	public int productSpawnColumns;

	

	
	/**
	 * @param gl
	 * @param context
	 */
	public GameManager(RenderView renderView, GL10 gl) {
		
		this.activity = (Activity)renderView.getContext();
		this.renderView = renderView;
//		this.gl = gl;
		
		textures = new Hashtable<Integer, Integer>();
		entities = new Hashtable<Integer, ProductEntity>();
		animators = new Hashtable<Integer, Animator>();
		
		EventManager.getInstance().addListener(this);
		
		scrollSpeed = 100f * 0.001f; // Pixel per second
		distMovedX = 0;
		
		
		float spawnY = renderView.getHeight() / 5f;		
		productSpawnY = new float[]{spawnY, spawnY * 2, spawnY * 3, spawnY * 4};
		
		productSpawnColumns = 0;
		
		createEntities(gl);
	}
	
	
	/**
	 * 
	 */
	public void createEntities(GL10 gl) {
		
		shelf = new Shelf(renderView.getWidth(), renderView.getHeight());
		shelf.texture = getTexture(R.drawable.l17_crate, gl);
				
		int start = R.drawable.l88_stash_blue;
		int range = R.drawable.l88_stash_yellow - R.drawable.l88_stash_blue + 1;
		
		// The product icons are optimized for a screen resolution of 800 x 480. Calculate the scale factor the items if the resolution is different. 
		float productSize = 100 * renderView.getHeight() / 480f;
		
		for (int i = 0; i < 10; i++) {
			
			int spawnSlot = (int)(Math.random() * 3) + 1;			
			ProductEntity pe = new ProductEntity(	(float)Math.random() * (renderView.getWidth() - 100) + 50, productSpawnY[spawnSlot], 1, productSize);
			
			pe.texture = getTexture(start + (i % range), gl);
			pe.angle = (float)Math.random() * 360;			
			
			// Put it into the HashTable. The id gets assigned internally. 
			entities.put(pe.id, pe);
		}
		
		
//		bunny.setId(BUNNY_ENTITY);
//		bunny.setTextureId(textures.get(BUNNY_TEXTURE));
	}


	
	/**
	 * @param dt The delta time since the last frame.
	 */
	public void update(float dt) {
		
		// Difference in x since last frame 
		float dx = scrollSpeed * dt;
		distMovedX += dx;
		
		// Calculation of background movement. (The height is equivalent to 1 in Texture Space)
		shelf.scrollX = distMovedX / renderView.getHeight();
		
		
		
		// Update all Animators
		Enumeration<Integer> keys = animators.keys();		
		while(keys.hasMoreElements()) {
			animators.get(keys.nextElement()).update(dt);
		}
		
		
		
		
		// Now move all products on the shelf in the same speed
		keys = entities.keys();
		ProductEntity pe = null;
		
		while(keys.hasMoreElements()) {
			pe = entities.get(keys.nextElement());
			
			if (!pe.clickable)
				continue;
			
			pe.x -= dx;
			
			// If they are out of the screen remove them
			if (pe.x < -pe.width) {
				
				entities.remove(pe.id);
				
//				pe.x = renderView.getWidth() + (float)Math.random() * 300 + 50;
//				
//				// Spawn in the upper 3 slots
//				int spawnSlot = (int)(Math.random() * 3) + 1;
//				pe.y = productSpawnY[spawnSlot];
			}
		}
		
		// At last, add some new products every few pixels
		// Everytime there's a new column, spawn a new set of products. Needs to be an integer division		
		if ((int)distMovedX / 150 > productSpawnColumns) {
			productSpawnColumns++;
			
			// This varies due to changes in scroll speed
			float d = distMovedX % 150;
			
			// Spawn outside the screen and subtract the amount of d already passed 
			float x = renderView.getWidth() + 75 - d;
			float y = productSpawnY[(int)(Math.random() * 3) + 1];
			
			// The product icons are optimized for a screen resolution of 800 x 480. Calculate the scale factor the items if the resolution is different. 
			float productSize = 100 * renderView.getHeight() / 480f;
			
			int start = R.drawable.l88_stash_blue;
			int range = R.drawable.l88_stash_yellow - R.drawable.l88_stash_blue + 1;
			
			pe = new ProductEntity(x, y, 1, productSize);
			
			pe.texture = getTexture(start + (int)(Math.random() * range));
			pe.angle = (float)Math.random() * 360;			
			
			entities.put(pe.id, pe);
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
	
	
	
	// Interface methods
	//------------------------------------------------------------------------------------------------------


	/**
	 *  Reacts to moves and presses on the touchscreen.
	 */
	public void onTouch(float x, float y) {
		
		
		Enumeration<Integer> keys = entities.keys();
		ProductEntity pe = null;
		
		while(keys.hasMoreElements()) {
			
			pe = entities.get(keys.nextElement());
			
			if (pe.visible && pe.clickable && pe.hitTest(x, y)) {
				
				pe.clickable = false;
				// Move it to the basket
				Animator a = new Animator(pe, 100, 100, 30);
				animators.put(a.id, a);
				
				
//				
//				pe.x = renderView.getWidth() + (float)Math.random() * 500 + 50;
////				pe.y = (float)Math.random() * (renderView.getHeight() - 100) + 50;
//				
//				// Spawn in the upper 3 slots
//				int spawnSlot = (int)(Math.random() * 3) + 1;
//				pe.y = productSpawnY[spawnSlot];
//				
//				// Change speed depending on the product color. Not now :P
////				scrollSpeed -= 0.001; // 20 pixel per second
//				
//				// Create a new product
//				int start = R.drawable.l88_stash_blue;
//				int range = R.drawable.l88_stash_yellow - R.drawable.l88_stash_blue + 1;				
//				int texChoose = (int)(Math.random() * range) + start;
//				
//				pe.texture = getTexture(texChoose); // HACK HACK!! If the texture isn't available yet, the texture id will be 0!!
//				
//				
				
				
				break;
			}
		}
		
	}
	
	
	


	@Override
	public void handleEvent(int eventId, Object eventData) {
		
		switch (eventId) {
		case EventManager.ANIMATION_COMPLETE:
			
			// Remove the Animator from the HashTable, effectively destroying it.
			// NOTE: Does this have to happen at a fixed point in time in the update loop?
			Animator a = (Animator)eventData;			
			animators.remove(a.id);
			
			break;

		default:
			break;
		}
		
	}


	/** 
	 * This method returns the texture id of the given resource file if already available or creates it on the fly. 
	 * Note: You have to pass a valid GL everytime you use this method. 
	 * 
	 * @param resource The resource identifier delivered by "at.ac.tuwien.cg.cgmd.bifth2010.R".
	 * @param gl Pass a valid GL here, or the texture id will always be 0. 
	 * @returns The texture id of the file.
	 */
	public int getTexture(int resource, GL10 gl) {
		
		// Try to find the texture
		Integer t = textures.get(resource);
		if (t != null) {
			return t;
		}
		
		// Texture not yet created. Try it now.		
		// If using the fast version, abort here and return zero. 
		if (gl == null)
			return 0;

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
	
	/** 
	 * This method returns the texture id of the resource file if already available. Otherwise returns 0.
	 * Use "int getTexture(int resource, GL10 gl)" to create a save texture handle on the fly. 
	 * 
	 * @param resource The resource identifier delivered by "at.ac.tuwien.cg.cgmd.bifth2010.R".
	 * @returns The texture id of the file.
	 */
	public int getTexture(int resource) {
		return getTexture(resource, null);
	}
}
