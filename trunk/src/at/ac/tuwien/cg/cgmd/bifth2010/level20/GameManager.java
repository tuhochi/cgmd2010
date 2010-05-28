package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;


/**
 * This class is responsible for managing the whole game. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameManager implements EventListener, OnTouchListener {
	
	// TODO: Not used yet
//	 /** TestHandle Class */
//	class TestThread implements Runnable{
//		  
//    	@Override
//        public void run() {
//    		moneyText.setText(Math.round(renderView.timer.getFPS()));
//        }    	
//    };
	
	
	
	protected LevelActivity activity;
	protected RenderView renderView;
	
	/** @see at.ac.tuwien.cg.cgmd.bifth2010.level20.TimeUtil */
	protected TimeUtil time;
	
	/** The animator collection */
	protected Hashtable<Integer, Animator> animators;

	/** The background Shelf of the game */		 
	protected Shelf shelf;
	
	/** The moving speed of the background and the products */ 
	protected float scrollSpeed;
	
	protected int totalMoney;
	protected ShoppingCart shoppingCart;
	protected SpriteAnimationEntity bunny;
	
//	public Handler handler;
//	public TestThread testThread;
	
	/** The TextView to show the money count. */
	private TextView moneyText;
	/** The run time of the game in seconds. */
	private float gameTime;
	/** The TextView to show the time left. */
	private TextView timeText;

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
//		createEntities(gl);
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

		// Preload textures
		for (int i = 0; i < ProductInfo.length; i++) {
			renderView.getTexture(ProductInfo.texture(i), gl);
		}
		
		// Create shopping cart.
		shoppingCart = new ShoppingCart(125, 60, 2, 200, 110);
		shoppingCart.texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
		
		// Create bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++)
		{
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
		bunny = new SpriteAnimationEntity(45, 40, 2, 64, 64);
		bunny.setFps(10);
		bunny.setAnimationSequence(bunnySequence);
				
		// Create text view for display of money count.
		moneyText = (TextView)activity.findViewById(R.id.l20_MoneyText);
		// Create text view for display of time left.		
		timeText = (TextView)activity.findViewById(R.id.l20_TimeText);
		
		firstRun = false;
	}
	
	/**
	 * 
	 */
	public void reCreateEntities(GL10 gl) {
		
		// Clear all textures. If this method is called, existing textures are invalid
		renderView.textures.clear();
		
		// Create background shelf.
//		shelf = new Shelf(renderView.getWidth(), renderView.getHeight());
		shelf.texture = renderView.getTexture(RenderView.TEXTURE_SHELF, gl);			

		// Preload textures
		for (int i = 0; i < ProductInfo.length; i++) {
			renderView.getTexture(ProductInfo.texture(i), gl);
		}	
		
		// Assign each product the same texture again
		Enumeration<Integer> keys = shelf.products.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = shelf.products.get(keys.nextElement());
			pe.texture = renderView.getTexture(ProductInfo.texture(pe.type), gl);
			
		}
		
		// Create shopping cart.
//		shoppingCart = new ShoppingCart(125, 60, 2, 200, 110);
		shoppingCart.texture = renderView.getTexture(RenderView.TEXTURE_CART, gl);
		
		// Create bunny.
		int[] bunnySequence = new int[RenderView.TEXTURE_BUNNY.length];
		for (int i = 0; i < RenderView.TEXTURE_BUNNY.length; i++)
		{
			bunnySequence[i] = renderView.getTexture(RenderView.TEXTURE_BUNNY[i], gl);
		}
				
//		bunny = new SpriteAnimationEntity(45, 40, 2, 64, 64);
//		bunny.setFps(10);
		bunny.setAnimationSequence(bunnySequence);
				
		// Create text view for display of money count.
//		moneyText = (TextView)activity.findViewById(R.id.l20_MoneyText);
		// Create text view for display of time left.		
//		timeText = (TextView)activity.findViewById(R.id.l20_TimeText);
		
	}


	/**
	 * Handles all updates for this frame
	 */
	public void update() {
		
		// Advance in time
		time.update();		
		float dt = time.getDt();
		
		
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


//	@Override
//	public void render(GL10 gl) {
//		
//		shelf.render(gl);
//		shoppingCart.render(gl);
//		bunny.render(gl);
//
//		int seconds = (int) (gameTime);		
//		
//
//	}
	
	
	
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
    		shelf.touchEvent(touchX, touchY);
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
		}	
		break;
		
		case EventManager.PRODUCT_COLLECTED:
		{
			ProductEntity pe = (ProductEntity)eventData;
			totalMoney -= ProductInfo.price(pe.type);
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


	


	
}
