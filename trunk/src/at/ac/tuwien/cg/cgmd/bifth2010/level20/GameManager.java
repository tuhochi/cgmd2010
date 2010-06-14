package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

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
import at.ac.tuwien.cg.cgmd.bifth2010.level11.Vector2;
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
	
	/** Responsible for creating and managing particle systems. */
	// couldn't compile: protected ParticleEngine particleEngine;   
	
	/** Provides sprites containing characters for rendering. */
	protected TextSprites textSprites;
	
	/** Handles obstacles in the game. */
	protected ObstacleManager obstacleManager;
	
	/** The animator collection */
	protected Hashtable<Integer, Animator> animators;
	
	/** The moving speed of the background and the products */ 
	protected float curScrollSpeed;
	
	/** The default speed of the background and the products */ 
	protected float defaultScrollSpeed;
	
	/** Flag if to increase the scroll speed next frame */
	protected boolean scrollSpeedIncr;
	
	/** The value to increase the scroll speed next frame */
	protected float scrollSpeedIncrVal;
	
	/** The speed of transform animations. */
	protected float animationSpeed;
	
	/** The gravity acceleration of fall animations. */
	protected float fallAcceleration;
	
	/** The amount of all money spent on products */
	protected float totalMoney;
	
	/** The time left in the game */
	protected float remainingTime;	
	
	/** Our collection of shopping carts */
	protected ShoppingCart[] shoppingCarts;
	
	/** The pop-ups for discounts. */
	protected RenderEntity[] discountPopUps;
	
	/** The pop-ups index for discounts. */
	protected int discountPopUpsIndex;
	
	/** The time, the discount sprite is displayed */
	protected float discountTime;
	
	/** The amount of shopping carts */
	protected int nShoppingCarts;
	
	/** The animated bunny */
	protected SpriteAnimationEntity bunny;
	
	/** Ratio to multiply object sizes with for proper size dependent on the current screen resolution. */
	protected static float screenRatio;
		
	

	/** If there's a touch on the screen */
	protected boolean touchDown;
	protected float touchX;
	protected float touchY;
	
	/** If this is false, the game won't stop after the finish is reached */
	protected boolean finish;
	
	/** If the Game has been run before. If this is not the first time, textures need to be rebuilt instead */
	protected boolean firstRun;
	
	/** In this mode the products are only bought if they fall into the cart. */
	static protected boolean catchMode;
	
	/**
	 * @param gl
	 * @param context
	 */
	public GameManager() {
		
		firstRun = true;
		catchMode = true;
		collectingEnabled = true;
		
		activity = LevelActivity.instance;
		renderView = LevelActivity.renderView;
		
		time = TimeUtil.instance;
		
		productManager = new ProductManager();	
		textSprites = new TextSprites();
		
		soundManager = new SoundManager((Context)LevelActivity.instance);	
		// couldn't compile: particleEngine = new ParticleEngine();
		// couldn't compile: particleEngine.speed = activity.getResources().getInteger(R.integer.l20_particle_speed);
		// couldn't compile: particleEngine.nParticles = activity.getResources().getInteger(R.integer.l20_particle_count);
		// couldn't compile: particleEngine.lifetime = activity.getResources().getInteger(R.integer.l20_particle_life);
		
		obstacleManager = new ObstacleManager();
		
		animators = new Hashtable<Integer, Animator>();
		shoppingCarts = new ShoppingCart[3];
		nShoppingCarts = 0;
		
		discountPopUps = new RenderEntity[2];
		discountTime = -1;
		discountPopUpsIndex = -1;
		
		
		EventManager.getInstance().addListener(this);			
		
		// Time in milliseconds
		remainingTime = activity.getResources().getInteger(R.integer.l20_level_time) * 1000;
		totalMoney = 100;

		screenRatio = 1f;
		touchDown = false;
		touchX = 0;
		touchY = 0;
		
		finish = activity.getResources().getBoolean(R.bool.l20_endgame);
	}
	
	
	/**
	 * Invoked at the beginning of the game
	 */
	public void createEntities(GL10 gl) {
		
		// Clear all textures. If this method is called, existing textures are invalid
		renderView.textures.clear();			
		
		// Create background shelf.
		float width = renderView.getWidth();
		float widthPercent = width * 0.01f;
		float height = renderView.getHeight();
		float heightPercent = height * 0.01f;
		screenRatio = height / 480.f;
		
		shelf = new Shelf(width*0.5f, height*0.5f, -1, width, height);
		shelf.texture = renderView.getTexture(RenderView.TEXTURE_SHELF, gl);		

		// Preload product textures
		for (int i = 0; i < ProductInfo.length; i++) {
			renderView.getTexture(ProductInfo.texture(i), gl);
		}
		
		obstacleManager.init(gl);			
		productManager.init();
		textSprites.buildSprites(gl);		
		
		// Textures for particels.
		int[] texIds = new int[2];
		texIds[0] = renderView.getTexture(R.drawable.l88_stash_yellow, gl);
		texIds[1] = renderView.getTexture(R.drawable.l88_stash_orange, gl);		
		//texIds[2] = renderView.getTexture(R.drawable.l88_stash_red, gl);
		// couldn't compile: particleEngine.textureIds = texIds;		
		// couldn't compile: particleEngine.size = (int) (activity.getResources().getInteger(R.integer.l20_particle_size) * screenRatio);
		
		// Create bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++) {
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
		float bunnySize = activity.getResources().getInteger(R.integer.l20_bunny_size);
		bunnySize = (int) (bunnySize * screenRatio);
		float bunnyPosX = activity.getResources().getInteger(R.integer.l20_bunny_x);
		bunnyPosX = bunnyPosX * screenRatio + bunnySize*0.5f;
		float bunnyPosY = activity.getResources().getInteger(R.integer.l20_bunny_y);
		bunnyPosY = bunnyPosY * screenRatio + bunnySize*0.5f;
		bunny = new SpriteAnimationEntity(bunnyPosX, bunnyPosY, 2, bunnySize, bunnySize);
		bunny.setFps(10);
		bunny.setAnimationSequence(bunnySequence);
		// reset below! (after shopping cart width is known)
		bunny.maxPosX = activity.getResources().getInteger(R.integer.l20_bunny_max_x) * screenRatio;		
		bunny.speed = activity.getResources().getInteger(R.integer.l20_bunny_speed) * widthPercent;
		
		float bubbleSize = activity.getResources().getInteger(R.integer.l20_bubble_size) * screenRatio;
		float bubblePos = bunnyPosY+bunnySize*0.4f + bubbleSize*0.5f;
		bunny.curseBubble = new RenderEntity(bunny.x + bubbleSize*0.5f, bubblePos, 3, bubbleSize, bubbleSize);
		bunny.curseBubble.texture = renderView.getTexture(R.drawable.l20_bunny_curse, gl);
		bunny.curseBubble.visible = false;
		
		// Create shopping cart.
		float shoppingCartSize = activity.getResources().getInteger(R.integer.l20_shopping_cart_default_size);
		shoppingCartSize = (int) (shoppingCartSize*screenRatio);
		float shoppingCartSizeY = (int) (shoppingCartSize*0.6);
		float cartPosY = heightPercent*19;
		float cartPos = bunnyPosX + shoppingCartSize*0.45f;		
		
		shoppingCarts[0] = new ShoppingCart(cartPos, cartPosY, 2, shoppingCartSize, shoppingCartSizeY);
		shoppingCarts[0].texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
		shoppingCarts[0].clickable = false;
		
		// Move bunny around whole screen width
		bunny.maxPosX = renderView.getWidth() - bunny.minPosX - shoppingCartSize * 0.9f;
		
		// Calc bounding box size
//		float bbX = activity.getResources().getInteger(R.integer.l20_shopping_cart_bb_default_size_x);
//		float bbY = activity.getResources().getInteger(R.integer.l20_shopping_cart_bb_default_size_y);
		// The actual cart x-region is 146px of 256px.
		float bbX = shoppingCartSize * 0.57f;
		// The actual cart y-region is 70px of 150px.
		float bbY = shoppingCartSizeY * 0.46f;
		shoppingCarts[0].setBBDim(bbX, bbY);
		nShoppingCarts = 1;
		
		// Discount Pop-Ups.
		discountPopUps[0] = new RenderEntity(cartPos, cartPosY, 2, shoppingCartSize, shoppingCartSizeY);
		discountPopUps[0].texture = renderView.getTexture(R.drawable.l20_discount, gl);
		discountPopUps[0].visible = false;
		discountPopUps[1] = new RenderEntity(cartPos, cartPosY, 2, shoppingCartSize, shoppingCartSizeY);
		discountPopUps[1].texture = renderView.getTexture(R.drawable.l20_discount1, gl);
		discountPopUps[1].visible = false;
		
		// Set crash pos for obstacle to the right of the shopping cart.
		obstacleManager.setCrashPosition(shoppingCarts[0]);	
		
		productManager.initProductSpawn();
		// Pixel per second
		animationSpeed = activity.getResources().getInteger(R.integer.l20_animation_speed) * widthPercent;
		fallAcceleration = activity.getResources().getInteger(R.integer.l20_fall_acceleration) * widthPercent;
		
		defaultScrollSpeed = activity.getResources().getInteger(R.integer.l20_scroll_speed) * widthPercent * 0.001f;
		curScrollSpeed = defaultScrollSpeed;
		scrollSpeedIncrVal = activity.getResources().getInteger(R.integer.l20_scroll_speed_incr) * widthPercent * 0.001f;
		scrollSpeedIncr = false;
		
		firstRun = false;
	}
	
	/**
	 * Invoked, everytime the RenderView looses focus. (Textures need to be recreated)
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
		for (int i = 0; i < shoppingCarts.length; i++) {
			if (shoppingCarts[i] != null) {
				shoppingCarts[i].texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
			}
		}
		
		
		// ReCreate bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++) {
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
		bunny.setAnimationSequence(bunnySequence);
		
		productManager.initProductSpawn();
		
		obstacleManager.init(gl);			
		productManager.init();
		bunny.curseBubble.texture = renderView.getTexture(R.drawable.l20_bunny_curse, gl);
		textSprites.buildSprites(gl);
		
		// Textures for particels.
		int[] texIds = new int[2];
		texIds[0] = renderView.getTexture(R.drawable.l88_stash_yellow, gl);
		texIds[1] = renderView.getTexture(R.drawable.l88_stash_orange, gl);		
		//texIds[2] = renderView.getTexture(R.drawable.l88_stash_red, gl);
		// couldn't compile: particleEngine.textureIds = texIds;
		
		// Discount Pop-Ups.
		discountPopUps[0].texture = renderView.getTexture(R.drawable.l20_discount, gl);
		discountPopUps[1].texture = renderView.getTexture(R.drawable.l20_discount1, gl);
	}


	/**
	 * Handles all updates for this frame
	 */
	public void update() {
		if (!renderView.running) return;
		
		// Advance in time
		time.update();		
		float dt = time.getDt();
		
		if (scrollSpeedIncr) {
			scrollSpeedIncr = false;
			curScrollSpeed += scrollSpeedIncrVal;
		}
		
		// Difference in movement since last frame
		float scroll = curScrollSpeed * dt;
		
		// Move only if not crashed
		if (!bunny.crashed) {
			productManager.update(scroll);		}
		
		obstacleManager.update(dt, scroll);
		
		// couldn't compile: particleEngine.update(dt);
		
		bunny.update(dt);		
 
		if (catchMode) {
			shoppingCarts[0].update(bunny.x);
			obstacleManager.setCrashPosition(shoppingCarts[0]);
			productManager.collisionTest(shoppingCarts[0]);			
		}
						
		// Update all Animators
		Enumeration<Integer> keys = animators.keys();		
		while(keys.hasMoreElements()) {
			animators.get(keys.nextElement()).update(dt);
		}
		
		
		// Update display of discount symbols
		if (discountPopUpsIndex > -1) {
			
			discountPopUps[discountPopUpsIndex].x = shoppingCarts[0].x;
			
			if (time.getTotalTimeInSeconds() >= discountTime) {

				discountTime = -1;
				discountPopUpsIndex = -1;
				
				for (int i = 0; i < discountPopUps.length; i++) {
					discountPopUps[i].visible = false;					
				}
			}
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
		stop();
		
		SessionState s = new SessionState();
		s.setProgress(100 - (int)totalMoney); 
		activity.setResult(Activity.RESULT_OK, s.asIntent());
		
		// Hopefully this will kill the game for sure
		// No, theres a crash
//		LevelActivity.instance = null;
//		LevelActivity.gameManager = null;
//		LevelActivity.renderView = null;
		
		
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
//		case KeyEvent.KEYCODE_SEARCH:
//			
//			curScrollSpeed += 0.02f;
//			return true;
//			
//		case KeyEvent.KEYCODE_MENU:
//			
//			curScrollSpeed -= 0.02f;
//			return true;
		
		case KeyEvent.KEYCODE_BACK:
			
			// Return false, or it won't stop!
			gameOver();
			return false;
			
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
			// The animated price number sprite has to be set invisible when animation is finished.
			else if (a.re.id >= TextSprites.TEXT_SPRITE) {
				a.re.visible = false;
			}
			
			// Remove all those falling products from the screen
			else if (a instanceof FallAnimator)
			{
				productManager.removeFromProducts.add((ProductEntity)a.re);
			}
		}
		break;
		
		case EventManager.PRODUCT_COLLECTED:
		{
			ProductEntity pe = (ProductEntity)eventData;
						
			// Get the nearest cart index
			int cartIndex = (int)(pe.x * nShoppingCarts / renderView.getWidth());			
			float[] pos = shoppingCarts[cartIndex].getNextProductPosition();
			
			if(catchMode) {
				// Falling to the ground.
				Animator a = new FallAnimator(pe, fallAcceleration);
				animators.put(a.id, a);
			} else {
				
				//INFO: Don't disable catch mode anymore!!!
				
				// Move it to the basket. 
				Animator a = new LineAnimator(pe, pos[0], pos[1], animationSpeed);
				animators.put(a.id, a);
				
				// Remove from products
				productManager.products.remove(pe.id);
				// And add to cart. (PE is now also rendered from here)
				shoppingCarts[cartIndex].addProduct(pe);
				
				// Check if there is a discount on the product.
				int price = shoppingCarts[0].checkDiscounts(pe);
				// And remove what it's worth
				totalMoney -= price;
				
				// This prevents displaying a negative money count and exits the game.
				if (totalMoney <= 0) {
					totalMoney = 0;
					
					gameOver();
					return;
				}
				
				createTextAnimation(pe.x, pe.y, "-", price);
			}
			
			// Create Particle System.
			// couldn't compile: particleEngine.createParticleSystem(pe.x, pe.y);

		}
		break;
		
		case EventManager.PRODUCT_HIT_CART:
		{
			ProductEntity pe = (ProductEntity)eventData;
			
			if(catchMode) {
				// Stop animation of product.
				removeAnimator(pe);
				
				// Get the nearest cart index
				int cartIndex = (int)(pe.x * nShoppingCarts / renderView.getWidth());			
				float[] pos = shoppingCarts[cartIndex].getNextProductPosition();
				// Move it to the basket. 
				Animator a = new LineAnimator(pe, pos[0], pos[1], animationSpeed);
				animators.put(a.id, a);

				// Remove from products
				productManager.products.remove(pe.id);
				// And add to cart. (PE is now also rendered from here)
				shoppingCarts[0].addProduct(pe);
				
				// Check if there is a discount on the product.
				int price = shoppingCarts[0].checkDiscounts(pe);
				// And remove what it's worth
				totalMoney -= price;
				
				
				// This prevents displaying a negative money count and exits the game.
				if (totalMoney <= 0) {
					totalMoney = 0;
					
					gameOver();
					return;
				}
				// Increase scrollSpeed next frame
				scrollSpeedIncr = true;
				// Now create a text label displaying the price or discount
				createTextAnimation(pe.x, pe.y, (price >= 0 ? "-" : "+"), price);
			}
		}
		break;
		
		case EventManager.DISCOUNT_ACQUIRED:
		{
			ProductEntity pe = (ProductEntity)eventData;
		
			int amount = ProductInfo.amount(pe.type);
			int discount = ProductInfo.discount(pe.type);
			
			discountPopUpsIndex = 0;
			
			if (amount == 3 && discount == 2) {				
				discountPopUpsIndex = 0;
				
			} else if (amount == 5 && discount == 3) {
				discountPopUpsIndex = 1;				
			}
			
			for (int j = 0; j < discountPopUps.length; j++) {
				
				if (j == discountPopUpsIndex) {
					discountPopUps[j].visible = true;
					discountPopUps[j].x = shoppingCarts[0].x;
					
				} else {
					discountPopUps[j].visible = false;
				}
			}
			
			discountTime = time.getTotalTimeInSeconds() + activity.getResources().getInteger(R.integer.l20_discount_duration);
		}
		break;
		
		
		case EventManager.BUNNY_MOST_LEFT:
		{
			// Here, the bunny is at it's left most position. Spawn an obstacle only here			
			// Spawn only if not crashed and the first time after collecting 6 products
			if (!obstacleManager.crashed && shoppingCarts[0].products.size() >= 6) {
				obstacleManager.spawnObstacle();
			}
		}
		break;
		
		
		case EventManager.OBSTACLE_AVOIDED:
		{
			// Just for fun.
			soundManager.laugh();
		}
		break;

		
		case EventManager.SHOPPING_CART_COLLECTED:
		{
			ShoppingCart sc = (ShoppingCart)eventData;			
			shoppingCarts[nShoppingCarts++] = sc;
			
			Animator a = new LineAnimator(sc, shoppingCarts[0].x * nShoppingCarts, shoppingCarts[0].y, 30);
			animators.put(a.id, a);
			
		}
		break;
		
		case EventManager.OBSTACLE_CRASH: {
			curScrollSpeed = defaultScrollSpeed;
			soundManager.stopRunSound();
			soundManager.playSound(SOUNDS.CRASH);
			bunny.crash();					
		}
		break;			
		
		case EventManager.BUNNY_RUN: {
			soundManager.startRunSound();				
		}
		break;		
		
		default:
			break;
		}
		
	}
	
	public void pause() {
		soundManager.pauseMusic();
		soundManager.stopRunSound();
	}
	
	public void stop() {
		soundManager.stopSounds();
		soundManager.destroy();
	}
	
	public void resume() {
		time.reset();
		soundManager.startRunSound();
		soundManager.startMusic();
	}
	
	/**
	 * Removes the animator object for this product.
	 * @param product
	 */
	public void removeAnimator(ProductEntity product) {
		Enumeration<Integer> keys = animators.keys();		
		while(keys.hasMoreElements()) {
			int key = keys.nextElement();
			Animator a = animators.get(key);
			if (a.re == product) {
				animators.remove(key);
				a.re = null;
			}
		}
	}


	/**
	 * Renders all objects the GameManager is responsible for.
	 * @param gl The OpengGL Context to render to. 
	 * */
	public void render(GL10 gl) {
		// Render all objects individually. NOTE: No more stacked renders
		shelf.render(gl);		
		obstacleManager.render(gl);	
		
		// Render products.	
		Enumeration<Integer> keys = productManager.products.keys();
		while(keys.hasMoreElements()) {
			productManager.products.get(keys.nextElement()).render(gl);
		}	
		
		
		// Render shopping carts
		for (int i = 0; i < shoppingCarts.length; i++) {			
			if (shoppingCarts[i] != null) {
				shoppingCarts[i].render(gl);
			}
		}
		
		if (productManager.movingShoppingCart != null) {
			productManager.movingShoppingCart.render(gl);
		}
					
		
		bunny.render(gl);
		for(int i = 0; i < discountPopUps.length; i++) {
			if (null != discountPopUps[i]) {
				discountPopUps[i].render(gl);
			}
		}
		
		// couldn't compile: particleEngine.render(gl);
		textSprites.render(gl);
		
	}
	
	/** 
	 * Creates an animated text sprite.
	 * @param x The x origin.
	 * @param y The y origin.
	 * @param sign The first character (either '+' or '-').
	 * @param number The number to animate.
	 */
	public void createTextAnimation(float x, float y, String sign, int number) {
		// Get and animate number sprite.
		RenderEntity numberSprite = textSprites.getNumberSprite(number);
		RenderEntity plusSprite = textSprites.getCharSprite(sign);
		numberSprite.x = x + (((float)Math.random()-0.5f)*10);
		numberSprite.y = y + (((float)Math.random()-0.5f)*10);
		plusSprite.x = numberSprite.x - numberSprite.width;
		plusSprite.y = numberSprite.y;
		
		// set visibility correctly
		for (int i = 0; i <= 9; i++) {
			numberSprite.visible = (i == number);
		}
//		numberSprite.visible = true;
		
		if (sign.equals("-")) {
			textSprites.getCharSprite("+").visible = false;
		} else if (sign.equals("+")) {
			textSprites.getCharSprite("-").visible = false;
		}
		plusSprite.visible = true;
		
		LineAnimator spriteAnim = new LineAnimator(numberSprite, numberSprite.x, numberSprite.y, animationSpeed*0.5f);
		spriteAnim.random(50);
		Animator plusAnim = new LineAnimator(plusSprite, spriteAnim.destX - numberSprite.width, spriteAnim.destY, spriteAnim.speed);
		animators.put(numberSprite.id, spriteAnim);
		animators.put(plusSprite.id, plusAnim);
	}

}
