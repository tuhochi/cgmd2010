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
	
	/** Manages all time related stuff */
	protected TimeUtil time;

	/** The background Shelf of the game */		 
	protected RenderEntity shelf;
	
	/** Manages products and interactions */		 
	protected ProductManager productManager;
	/** Whether collecting products is enabled. 
	 *  Set to false if the bunny crashes into an obstacle. */
	protected boolean collectingEnabled;
	
	/** Manager audio resources and playback. */
	protected SoundManager soundManager;
	
	/** Handles obstacles in the game. */
	protected ObstacleManager obstacleManager;
	
	/** The animator collection */
	protected Hashtable<Integer, Animator> animators;
	
	/** The moving speed of the background and the products */ 
	protected float scrollSpeed;
	/** The speed of tranform animations. */
	protected float animationSpeed;
	
	/** The amount of all money spent on products */
	protected float totalMoney;
	
	/** The time left in the game */
	protected float remainingTime;	
	
	/** Our collection of shopping carts */
	protected ShoppingCart[] shoppingCarts;
	
	/** The amount of shopping carts */
	protected int nShoppingCarts;
	
	/** The animated bunny */
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
		collectingEnabled = true;
		
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
		float width = renderView.getWidth();
		float widthPercent = width * 0.01f;
		float height = renderView.getHeight();
		float heightPercent = height * 0.01f;
		
		shelf = new Shelf(width*0.5f, height*0.5f, 0f, width, height);
		shelf.texture = renderView.getTexture(RenderView.TEXTURE_SHELF, gl);		

		// Preload product textures
		for (int i = 0; i < ProductInfo.length; i++) {
			renderView.getTexture(ProductInfo.texture(i), gl);
		}
		
		obstacleManager.init(gl);			
		productManager.init();
		
		float height = renderView.getHeight();
		
		// This is the default for a screen height of 480px. 
		float shoppingCartSize = activity.getResources().getInteger(R.integer.l20_shopping_cart_default_size);
		shoppingCartSize *= height / 480.0f;
		
		// Calc bounding box size
		float bbX = activity.instance.getResources().getInteger(R.integer.l20_shopping_cart_bb_default_size_x);
		float bbY = activity.instance.getResources().getInteger(R.integer.l20_shopping_cart_bb_default_size_y);
		bbX *= height / 480.0f;
		bbY *= height / 480.0f;
			
		// Create shopping cart.		
		float cartPosY = height / 5.f;
		//cartPosY += (shoppingCartSize * .5f);
		shoppingCarts[0] = new ShoppingCart(200, cartPosY, 2, shoppingCartSize, shoppingCartSize);
		shoppingCarts[0].texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
		shoppingCarts[0].clickable = false;
		shoppingCarts[0].setBBDim(bbX, bbY);
		nShoppingCarts = 1;
		
		// Create bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++) {
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
		bunny = new SpriteAnimationEntity(45, 40, 2, 64, 64);
		bunny.setFps(10);
		bunny.setAnimationSequence(bunnySequence);
		
		productManager.initProductSpawn();
		// Pixel per second
		scrollSpeed = activity.getResources().getInteger(R.integer.l20_scroll_speed) * widthPercent * 0.0001f;
		animationSpeed = activity.getResources().getInteger(R.integer.l20_animation_speed) * widthPercent;
		
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
		
		productManager.initProductSpawn();
	}


	/**
	 * Handles all updates for this frame
	 */
	public void update() {
		
		// Advance in time
		time.update();
		
		float dt = time.getDt();
		
		// Difference in movement since last frame
		float scroll = scrollSpeed * dt;
		productManager.update(scroll);
		obstacleManager.update(dt, scroll);
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
		
		// INFO: Removing click and hold for now. seems too easy.
		
		boolean tempDown = (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN);
		
		// If there is a touch now, and it's a new touch, forward it. (Don't trigger more events until the finger is removed again)
		if (tempDown && !touchDown) {
			touchDown = true;
        
		// Forward the event if there is a touchDown, so we don't miss very short touches. (Useful on the emulator where fps are very low)
        // If bunny crashed no products can be collected.
        if(!bunny.crashed) {
        	productManager.touchEvent(touchX, touchY);
        }
    		obstacleManager.touchEvent(touchX, touchY);
    		
	        // Else, if we release the touch, reset touchDown to false too.
		} else if (!tempDown) {
			touchDown = false;
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
						
			// Get the nearest cart index
			int cartIndex = (int)(pe.x * nShoppingCarts / renderView.getWidth());			
			float[] pos = shoppingCarts[cartIndex].getNextProductPosition();
			
			// Move it to the basket.
			Animator a = new Animator(pe, pos[0], pos[1], animationSpeed);
			animators.put(a.id, a);
			
			// Remove from products
			productManager.products.remove(pe.id);
			// And add to cart. (PE is now also rendered from here)
			shoppingCarts[cartIndex].addProduct(pe);
			
			// Just for fun.
			if (9 == pe.type) {
				soundManager.laugh();
			}
			
			totalMoney -= ProductInfo.price(pe.type);
			
			// This prevents displaying a negative money count and exits the game.
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
		
		case EventManager.OBSTACLE_CRASH: {
			soundManager.pauseMusic();
			soundManager.playSound(SOUNDS.CRASH);
			bunny.crash();
			obstacleManager.removeObstacle();			
		}
		break;			
		
		default:
			break;
		}
		
	}
	
}
