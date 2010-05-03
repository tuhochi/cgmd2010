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
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;


/**
 * This class is responsible for managing the whole game. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameManager implements Renderable, EventListener {
	
	// TODO: Not used yet
	 /** TestHandle Class */
	class TestThread implements Runnable{
		  
    	@Override
        public void run() {
    		moneyText.setText(Math.round(renderView.timer.getFPS()));
        }
    	
    	
		
    };
	
	// Textures.
	static final int[] TEXTURE_BUNNY = {R.drawable.l20_bunny1, R.drawable.l20_bunny2,
		R.drawable.l20_bunny3, R.drawable.l20_bunny4};	
	static final int TEXTURE_SHELF = R.drawable.l20_backg;
	static final int TEXTURE_CART = R.drawable.l20_shopping_cart;
	
	static final int[] TEXTURE_PRODUCTS = new int[]{
										R.drawable.l20_broccoli,
										R.drawable.l20_lollipop,
										R.drawable.l20_drink,
										R.drawable.l20_beer
										};
	
	public static Activity activity;
	public static RenderView renderView;

	/** The texture collection. (The ids increase themselves) */	 
	protected static Hashtable<Integer, Integer> textures;
		
	/** The animator collection */
	protected Hashtable<Integer, Animator> animators;

	/** The background Shelf of the game */		 
	protected Shelf shelf;
	
	/** The moving speed of the background and the products */ 
	public float scrollSpeed;
	
	protected int totalMoney;
	protected ShoppingCart shoppingCart;
	protected SpriteAnimationEntity bunny;
	
	public Handler handler;
	public TestThread testThread;
	
	/** The TextView to show the money count. */
	private TextView moneyText;
	/** The run time of the game in seconds. */
	private float gameTime;
	/** The TextView to show the time left. */
	private TextView timeText;

	/** If there's a touch on the screen */
	static protected boolean touchDown;
	static protected float touchX;
	static protected float touchY;
	
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
		
		// TODO: They have to be started in the other thread
//		handler = new Handler();
//		testThread = new TestThread();
		
		touchDown = false;
		touchX = 0;
		touchY = 0;
		
		scrollSpeed = 100f * 0.001f; // Pixel per second
		totalMoney = 100;
		gameTime = 60.f;
		createEntities(gl);
	}
	
	
	/**
	 * 
	 */
	public void createEntities(GL10 gl) {		
		// Create background shelf.
		shelf = new Shelf(renderView.getWidth(), renderView.getHeight());
		shelf.texture = getTexture(TEXTURE_SHELF, gl);			

		for (int i = 0; i < TEXTURE_PRODUCTS.length; i++) {
			getTexture(TEXTURE_PRODUCTS[i], gl);
		}	
		
		// Create shopping cart.
		shoppingCart = new ShoppingCart(125, 60, 2, 200, 110);
		shoppingCart.texture = getTexture(TEXTURE_CART, gl);
		
		// Create bunny.
		int[] bunnySequence = new int[TEXTURE_BUNNY.length];
		for (int i = 0; i < TEXTURE_BUNNY.length; i++)
		{
			bunnySequence[i] = getTexture(TEXTURE_BUNNY[i], gl);
		}
				
		bunny = new SpriteAnimationEntity(45, 40, 2, 64, 64);
		bunny.setFps(10);
		bunny.setAnimationSequence(bunnySequence);
				
		// Create text view for display of money count.
		moneyText = (TextView)activity.findViewById(R.id.l20_MoneyText);
		// Create text view for display of time left.		
		timeText = (TextView)activity.findViewById(R.id.l20_TimeText);
		
	}


	/**
	 * @param dt The delta time since the last frame.
	 */
	public void update(float dt) {
		
		// Difference in x since last frame 
		shelf.update(scrollSpeed * dt);
		bunny.update(dt);
		
		// Update all Animators
		Enumeration<Integer> keys = animators.keys();		
		while(keys.hasMoreElements()) {
			animators.get(keys.nextElement()).update(dt);
		}
		
//		handler.post(testThread);
		
		// TODO: Move gameTime to TimeUtil
		gameTime -= (dt/1000.f);
		// TODO Somehow this doesn't work here (and at many more places :( )
		//timeText.setText("Time:"+(int)gameTime);
		if(gameTime <= 0.f)
		{
			// TODO: Enable this again
//			gameOver();
		}						
	}

	
	private void gameOver() {
		SessionState s = new SessionState();
		s.setProgress(100-totalMoney); 
		activity.setResult(Activity.RESULT_OK, s.asIntent());
		activity.finish();
	}


	@Override
	public void render(GL10 gl) {
		
		shelf.render(gl);
		shoppingCart.render(gl);
		bunny.render(gl);

		int seconds = (int) (gameTime);		
		

	}
	
	
	
	// Interface methods
	//------------------------------------------------------------------------------------------------------


	/**
	 *  Reacts to moves and presses on the touchscreen.
	 *
	 * @param x The x coord in render space.
	 * @param y The y coord in render space.
	 * @param action The type of touch event. @see MotionEvent
	 */
	public void touchEvent(float x, float y, int action) {
		
		// Set these variables so that the touch can checked each frame
		touchDown = (action != MotionEvent.ACTION_UP);
		touchX = x;
		touchY = y;
		
		// And touch right now too, so we don't miss very short touches. (Useful on the emulator where fps are very low)
		shelf.touchEvent(x, y);
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
		}	
		break;
		
		case EventManager.PRODUCT_COLLECTED:
		{
			ProductEntity pe = (ProductEntity)eventData;
			totalMoney -= pe.price;			
			// This prevents displaying a negative money count.
//			if(totalMoney < 0) totalMoney = 0;
//			moneyText.setText("Money:" + totalMoney + "$");
//			if (totalMoney == 0) {
//				// TODO Enable this again
////				gameOver();
//			}
			
			
			// Move it to the basket.
			pe.animated = true;
			float[] pos = shoppingCart.getNextProductPosition();
			Animator a = new Animator(pe, pos[0], pos[1], 30);
			animators.put(a.id, a);
			shoppingCart.addProduct(pe);
		}
		break;
		
		default:
			break;
		}
		
	}
	
	
//	public void testTextView() {
//		moneyText.setText(Math.round(renderView.timer.getFPS()));
//	}


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
