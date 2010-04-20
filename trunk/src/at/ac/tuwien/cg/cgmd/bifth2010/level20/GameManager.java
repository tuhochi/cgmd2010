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
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


/**
 * This class is responsible for managing the whole game. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameManager implements Renderable, EventListener {

	/** Log Identifier*/
	private static final String TAG = "BunnyShop";
	
	// Textures.
	static final int TEXTURE_BUNNY = R.drawable.l20_icon;
	static final int TEXTURE_SHELF = R.drawable.l20_backg;
	static final int TEXTURE_CART = R.drawable.l20_shopping_cart;
	static final int[] TEXTURE_PRODUCTS = new int[]{R.drawable.l20_broccoli,
										 R.drawable.l20_lollipop,
										 R.drawable.l20_drink };
	// Entities.
	// INFO: Das haut so nicht hin :-/ Kanns da erklären
//	static final int BUNNY_ENTITY = 0;
//	static final int CART_ENTITY = 1;
	
	public static Activity activity;
	public static RenderView renderView;

	// The texture collection. (The ids increase themselves)
	protected static Hashtable<Integer, Integer> textures;
		
	// The animator collection
	protected Hashtable<Integer, Animator> animators;

	// The background Shelf of the game	
	protected Shelf shelf;
	
	// The moving speed of the background and the products 
	// TODO: Better name
	public float scrollSpeed;
	
	protected int totalMoney;
	protected ShoppingCart shoppingCart;
	
	/** The TextView to show the money count */
	private TextView moneyText;

	
	/**
	 * @param gl
	 * @param context
	 */
	public GameManager(RenderView renderView, GL10 gl) {
		
		GameManager.activity = (Activity)renderView.getContext();
		GameManager.renderView = renderView;
		
		textures = new Hashtable<Integer, Integer>();
		animators = new Hashtable<Integer, Animator>();
		
		EventManager.getInstance().addListener(this);
		
		scrollSpeed = 100f * 0.001f; // Pixel per second
		totalMoney = 100;
		createEntities(gl);
	}
	
	
	/**
	 * 
	 */
	public void createEntities(GL10 gl) {		
		// Create background shelf.
		shelf = new Shelf(renderView.getWidth(), renderView.getHeight());
		shelf.texture = getTexture(TEXTURE_SHELF, gl);			

		getTexture(TEXTURE_BUNNY, gl);		

		for (int i = 0; i < TEXTURE_PRODUCTS.length; i++) {
			getTexture(TEXTURE_PRODUCTS[i], gl);
		}	
		
		// Create shopping cart.
		shoppingCart = new ShoppingCart(120, 60, 2, 200, 110);
		shoppingCart.texture = getTexture(TEXTURE_CART, gl);
		// INFO: Das gehts so nicht. Andersrum ja. 
//		shoppingCart.id = CART_ENTITY;
		
//		Activity activity = (Activity)renderView.getContext();
		// Create text view for display of money count.
		moneyText = new TextView(activity);
		
		moneyText.setBackgroundColor(0);
		moneyText.setTextColor(255);
		moneyText.setText("Money: 100$");
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		moneyText.setLayoutParams(params);
		moneyText.bringToFront();
		moneyText.setGravity(Gravity.CENTER);
				
	}


	
	/**
	 * @param dt The delta time since the last frame.
	 */
	public void update(float dt) {
		
		// Difference in x since last frame 
		shelf.update(scrollSpeed * dt);

		
		// Update all Animators
		Enumeration<Integer> keys = animators.keys();		
		while(keys.hasMoreElements()) {
			animators.get(keys.nextElement()).update(dt);
		}				

		
	}

	
	@Override
	public void render(GL10 gl) {
		
		shelf.render(gl);
		shoppingCart.render(gl);
		
//		// Render entities.		
//		Enumeration<Integer> keys = entities.keys();
//		while(keys.hasMoreElements()) {
//			entities.get(keys.nextElement()).render(gl);
//		}
	}
	
	
	
	// Interface methods
	//------------------------------------------------------------------------------------------------------


	/**
	 *  Reacts to moves and presses on the touchscreen.
	 */
	public void onTouch(float x, float y) {
		
		shelf.hitTest(x, y);
		
	}
	


	@Override
	public void handleEvent(int eventId, Object eventData) {
		
		switch (eventId) {
		case EventManager.ANIMATION_COMPLETE:
		{
			// Remove the Animator from the HashTable, effectively destroying it.
			// NOTE: Does this have to happen at a fixed point in time in the update loop?
			Animator a = (Animator)eventData;			
			animators.remove(a.id);
			Log.d(TAG, "Animator removed: "+ a.id +".");
		}	
		break;
		
		case EventManager.PRODUCT_COLLECTED:
		{
			// Hm, ich würd die Null Abfrage rausnehmen, damit es sich wenigstens gscheit aufhängt, wenns nicht passt :P
//			if (null != eventData) {
			ProductEntity pe = (ProductEntity)eventData;
			totalMoney -= pe.price;
			moneyText.setText("Money:" + totalMoney + "$");
			
			// Move it to the basket.
			float[] pos = shoppingCart.getNextProductPosition();
			Animator a = new Animator(pe, pos[0], pos[1], 30);
			animators.put(a.id, a);
			shoppingCart.addProduct(pe);
			Log.d(TAG, "Product collected: "+ pos[0] + "/" + pos[1] + ".");
//			}			
		}
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
	public static int getTexture(int resource, GL10 gl) {
		
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
	public static int getTexture(int resource) {
		return getTexture(resource, null);
	}

}
