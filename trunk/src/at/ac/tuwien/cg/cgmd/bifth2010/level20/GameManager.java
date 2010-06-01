package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level20.SoundManager.SOUNDS;


/**
 * This class is responsible for managing the whole game. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameManager implements EventListener, OnTouchListener, OnKeyListener {

	protected LevelActivity activity;
	protected RenderView renderView;
	
	/** @see at.ac.tuwien.cg.cgmd.bifth2010.level20.TimeUtil */
	protected TimeUtil time;

	/** The background Shelf of the game */		 
	protected Shelf shelf;
	
	/** Manages products and interactions */		 
	protected ProductManager productManager;
	
	/** Manager audio resources and playback. */
	SoundManager soundManager;
	
	/** Handles obstacles in the game. */
	ObstacleManager obstacleManager;
	
	/** The animator collection */
	protected Hashtable<Integer, Animator> animators;
	
	/** The moving speed of the background and the products */ 
	protected float scrollSpeed;
	
	/** The amount of all money spent on products */
	protected float totalMoney;
	
	/** The time left in the game */
	protected float remainingTime;	
	
	/** Our collection of shopping carts */
	protected ShoppingCart[] shoppingCarts;
	
	/** The amount of shopping carts */
	protected int nShoppingCarts;
	
		
	protected SpriteAnimationEntity bunny;
	
	

	/** If there's a touch on the screen */
	protected boolean touchDown;
	protected float touchX;
	protected float touchY;
	
	/** If the Game has been run before. If this is not the first time, textures need to be rebuilt instead */
	protected boolean firstRun;
	
	/**
	 * @param gl
	 * @param context
	 */
	public GameManager() {
		
		firstRun = true;
		
		activity = LevelActivity.instance;
		renderView = LevelActivity.renderView;
		
		time = TimeUtil.instance;
		
		productManager = new ProductManager();	
		
		soundManager = new SoundManager((Context)LevelActivity.instance);
		soundManager.playSound(SOUNDS.RUN);
		
		obstacleManager = new ObstacleManager();
		
		animators = new Hashtable<Integer, Animator>();
		shoppingCarts = new ShoppingCart[3];
		nShoppingCarts = 0;
		
		EventManager.getInstance().addListener(this);		
		
		// Pixel per second
		scrollSpeed = activity.getResources().getInteger(R.integer.l20_scroll_speed) * 0.001f;
		
		// Time in milliseconds
		remainingTime = activity.getResources().getInteger(R.integer.l20_level_time) * 1000;
		totalMoney = 100;

		touchDown = false;
		touchX = 0;
		touchY = 0;
	}
	
	
	/**
	 * 
	 */
	public void createEntities(GL10 gl) {
		
		// Clear all textures. If this method is called, existing textures are invalid
		renderView.textures.clear();
		
		// Create background shelf.
		shelf = new Shelf(renderView.getWidth(), renderView.getHeight());
		shelf.texture = renderView.getTexture(RenderView.TEXTURE_SHELF, gl);		

		// Preload product textures
		for (int i = 0; i < ProductInfo.length; i++) {
			renderView.getTexture(ProductInfo.texture(i), gl);
		}
		
		obstacleManager.createObstacle(gl);		
		
		// This is the default for a screen height of 480px. 
		float shoppingCartSize = activity.getResources().getInteger(R.integer.l20_shopping_cart_default_size);
		shoppingCartSize *= renderView.getHeight() / 480.0f;
			
		// Create shopping cart.		
		shoppingCarts[0] = new ShoppingCart(200, 100, 2, shoppingCartSize, shoppingCartSize);
		shoppingCarts[0].texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
		shoppingCarts[0].clickable = false;
		nShoppingCarts = 1;
		
		// Create bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++) {
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
		bunny = new SpriteAnimationEntity(45, 40, 2, 64, 64);
		bunny.setFps(10);
		bunny.setAnimationSequence(bunnySequence);
		
		
		firstRun = false;
	}
	
	/**
	 * 
	 */
	public void reCreateEntities(GL10 gl) {
		
		// Clear all textures. If this method is called, existing textures are invalid
		renderView.textures.clear();
		
		// Create background shelf
		shelf.texture = renderView.getTexture(RenderView.TEXTURE_SHELF, gl);			

		// Preload textures
		for (int i = 0; i < ProductInfo.length; i++) {
			renderView.getTexture(ProductInfo.texture(i), gl);
		}	
		
		// Assign each product the same texture again
		Enumeration<Integer> keys = productManager.products.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = productManager.products.get(keys.nextElement());
			pe.texture = renderView.getTexture(ProductInfo.texture(pe.type), gl);
			
		}
		
		// ReCreate shopping cart texture.
		shoppingCarts[0].texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
		
		// ReCreate bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++) {
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
		bunny.setAnimationSequence(bunnySequence);
		
	}


	/**
	 * Handles all updates for this frame
	 */
	public void update() {
		
		// Advance in time
		time.update();
		
		float dt = time.getDt();
		
		// Difference in movement since last frame 
		productManager.update(scrollSpeed * dt);
		bunny.update(dt);
		
		// Update all Animators
		Enumeration<Integer> keys = animators.keys();		
		while(keys.hasMoreElements()) {
			animators.get(keys.nextElement()).update(dt);
		}
		
		remainingTime -= dt;
		
		// End the game if the time's up
		if (remainingTime <= 0) {			
			gameOver();
		}
	}

	
	/**
	 * If called, the activity finishes and returns the result.
	 */
	private void gameOver() {
		SessionState s = new SessionState();
		s.setProgress(100 - (int)totalMoney); 
		activity.setResult(Activity.RESULT_OK, s.asIntent());
		
		boolean finish = activity.getResources().getBoolean(R.bool.l20_endgame);
		if (finish) {
			activity.finish();
		}
	}

	
	
	
	// Interface methods
	//------------------------------------------------------------------------------------------------------

	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		// Set these variables so that the touch can be checked each frame.
		// Be aware that the event Y axis is mirrored to the render Y axis.
		touchX = event.getX();
		touchY = renderView.getHeight() - event.getY();		
		touchDown = (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN);		
        
		// Forward the event if there is a touchDown, so we don't miss very short touches. (Useful on the emulator where fps are very low)
        if (touchDown) {        	
    		productManager.touchEvent(touchX, touchY);
    		obstacleManager.touchEvent(touchX, touchY);
        }
		
		// Sleep the thread, so that it doesn't fire too many events
		try {
			Thread.sleep(35);
		} catch (InterruptedException e) {
			Log.e(GameManager.class.getName(), "Interrupt Exception", e);
		}
		
		// We handled the event, return true.
		return true;
	}
	

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		Log.d(GameManager.class.getName(), String.valueOf(keyCode));
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_SEARCH:
			
			scrollSpeed += 20f;
			return true;
			
		case KeyEvent.KEYCODE_MENU:
			
			scrollSpeed -= 20f;
			return true;
		
//		case KeyEvent.KEYCODE_BACK:
//			
//			renderView.running = false;
//			renderView.startButton.setText(R.string.l20_button_resume_text);
//			renderView.descriptionText.setVisibility(GLSurfaceView.VISIBLE);
//			renderView.startButton.setVisibility(GLSurfaceView.VISIBLE);
//			renderView.startButton.setClickable(true);
//			return true;
			
		default:
			return false;
		}
	}
	
	

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level20.EventListener#handleEvent(int, java.lang.Object)
	 */
	@Override
	public void handleEvent(int eventId, Object eventData) {
		
		switch (eventId) {
		case EventManager.ANIMATION_COMPLETE:
		{
			// Remove the Animator from the HashTable, effectively destroying it.
			// NOTE: Does this have to happen at a fixed point in time in the update loop?
			Animator a = (Animator)eventData;
			animators.remove(a.id);
			
			// HACK: But probably the fastest solution
			if (a.re instanceof ShoppingCart) {
				// This says, it is now a fixed shopping cart and ready to be filled.
				a.re.animated = false;
			}
		}	
		break;
		
		case EventManager.PRODUCT_COLLECTED:
		{
			ProductEntity pe = (ProductEntity)eventData;
			
			// Get a random position
			int cartIndex = (int)(Math.random() * nShoppingCarts);			
			float[] pos = shoppingCarts[cartIndex].getNextProductPosition();
			
			// Move it to the basket.
			Animator a = new Animator(pe, pos[0], pos[1], 30);
			animators.put(a.id, a);
			shoppingCarts[0].addProduct(pe);
			
			// Just for fun.
			if (9 == pe.type) {
				soundManager.laugh();
			}
			
			totalMoney -= ProductInfo.price(pe.type);
			
			// This prevents displaying a negative money count.
			if (totalMoney <= 0) {
				totalMoney = 0;
				
				gameOver();
				return;
			}				
		}
		break;
		
		case EventManager.SHOPPING_CART_COLLECTED:
		{
			ShoppingCart sc = (ShoppingCart)eventData;			
			shoppingCarts[nShoppingCarts++] = sc;
			
			Animator a = new Animator(sc, shoppingCarts[0].x * nShoppingCarts, shoppingCarts[0].y, 30);
			animators.put(a.id, a);
			
		}
		break;
		
		default:
			break;
		}
		
	}
	
}
