package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;


import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This class manages product creation and storing of the items in the shopping carts. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class ProductManager{

	
	/**
	 * FERDI: Das ist static. Geht das anders?
	 * The number of products to spawn at each row
	 */
	protected static int NUMBER_PRODUCTS;
	
	/**
	 * The distance between 2 product rows in pixels
	 */
	protected float PRODUCT_DISTANCE;
	
	/**
	 * The distance after which a shopping cart is spawned
	 */
	protected float SHOPPING_CART_DISTANCE;
	
	/**
	 * This value increases every frame until PRODUCT_DISTANCE is reached and is set to 0 again.
	 */
	protected float productDistance;
	
	/**
	 * This value increases every frame until SHOPPING_CART_DISTANCE is reached and is set to 0 again.
	 */
	protected float shoppingCartDistance;
	
	/** 
	 * The product collection. 
	 */
	protected Hashtable<Integer, ProductEntity> products;
	
	/** 
	 *  ProductEntities are enqueued in this List and are removed each frame from the products Hashtable
	 */
	protected LinkedList<ProductEntity> removeFromProducts;
	
	/** 
	 * If a new shopping carts spawns, it's stored here
	 */
	protected ShoppingCart movingShoppingCart;
	
	/**
	 * The distance in pixels, the shelf has moved since the start of the game
	 */
	protected float pixelsMoved;
	
	/**
	 * The Y positions of the shelves. (At this height, the products are spawning)
	 */
	protected float[] productSpawnY;
	
	/**
	 * 
	 */
	public ProductManager() {
		
		NUMBER_PRODUCTS = 3;		
		SHOPPING_CART_DISTANCE = LevelActivity.instance.getResources().getInteger(R.integer.l20_shopping_cart_distance);
		
		pixelsMoved = 0;
		productDistance = 0;
		shoppingCartDistance = 0;
		
		products = new Hashtable<Integer, ProductEntity>();
		removeFromProducts = new LinkedList<ProductEntity>();		
				
	}

	/**
	 * Initializes the manager. 
	 */
	public void init() {
		// TODO: Calc better values
		float height = LevelActivity.renderView.getHeight();
		float width = LevelActivity.renderView.getWidth();
		float row = height / 8.f;
		productSpawnY = new float[]{height - row, height - row*2.6f, height - row*4.3f};
		
		PRODUCT_DISTANCE = LevelActivity.instance.getResources().getInteger(R.integer.l20_product_distance) * width * 0.005f;
	}
	
	/**
	 * Updates the shelf.
	 * @param scroll The new scroll value for the shelf animation.
	 */
	public void update(float scroll) {
		
		// Move screen pixels
		pixelsMoved += scroll;
		
		// Update all Products
		Enumeration<Integer> keys = products.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = products.get(keys.nextElement());
			
			// If animated, they are updated elsewhere
			if (pe.animated) {
				continue;
			}
			
			pe.x -= scroll;			
		
			// If they are out of the screen
			if (pe.x < -pe.width) {
				// If added to this list, they are removed from the rendered products Hashtable later
				removeFromProducts.add(pe);
			}
		}
		
		// Move a shopping cart if there is one
		if (movingShoppingCart != null) {
			movingShoppingCart.x -= scroll;
			
			// If it is out of the screen remove it
			if (movingShoppingCart.x < -movingShoppingCart.width) {				
				movingShoppingCart = null;
			}
		}			
			
		// Check every frame if the touch touches a product or a shopping cart
		if (LevelActivity.gameManager.touchDown) {
			touchEvent(LevelActivity.gameManager.touchX, LevelActivity.gameManager.touchY);
		}
				
		// Now remove all marked products and empty the list again
		for (ProductEntity pe : removeFromProducts) {			
			products.remove(pe.id);
		}
		removeFromProducts.clear();

		// At last, add some new products every few pixels
		productDistance += scroll;
		
		if (productDistance >= PRODUCT_DISTANCE) {			
			productDistance -= PRODUCT_DISTANCE;				
			createProducts();
		}
		
		// And a shopping cart if it's time
		shoppingCartDistance += scroll;
		
		if (shoppingCartDistance >= SHOPPING_CART_DISTANCE && LevelActivity.gameManager.nShoppingCarts < LevelActivity.gameManager.shoppingCarts.length) {			
			shoppingCartDistance -= SHOPPING_CART_DISTANCE;	
			
			//createShoppingCart();
		}
	}
	
	

	/**
	 * Checks if there is a clickable product on this position
	 * 
	 * @param x The x coord on screen
	 * @param y The y coord on screen
	 */
	public void touchEvent(float x, float y) {
		
		// Check if we hit the shopping cart and tell the Eventmanager
		if (movingShoppingCart != null && movingShoppingCart.clickable && movingShoppingCart.hitTest(x, y)) {
			
			movingShoppingCart.clickable = false;
			EventManager.getInstance().dispatchEvent(EventManager.SHOPPING_CART_COLLECTED, movingShoppingCart);
			movingShoppingCart = null;
		}
		
		
		Enumeration<Integer> keys = products.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = products.get(keys.nextElement());
			
			if (pe.visible && pe.clickable && pe.hitTest(x, y)) {
				
				pe.clickable = false;				
				EventManager.getInstance().dispatchEvent(EventManager.PRODUCT_COLLECTED, pe);
				
				//Mark neighbors as not clickable too
				for (int i = 0; i < pe.neighbors.length; i++) {
					
					ProductEntity ne = products.get(pe.neighbors[i]);
					ne.clickable = false;
					
					// TODO: Small hack, do something better:
					ne.visible = false;
				}				
				break;
			}
		}
	}
	
	

	/**
	 * Creates a product for the shelf.
	 */
	public void createProducts() {
		
		float width = LevelActivity.renderView.getWidth();
		float height = LevelActivity.renderView.getHeight();
		
		for (int i = 0; i < NUMBER_PRODUCTS; i++) {
			// Spawn outside the screen and subtract the amount of d already passed.
			float offsetX = 75;
			float x = width + offsetX;// + jitterX;			
			float y = productSpawnY[i];
			
			// The product icons are optimized for a screen resolution of 800 x 480. Calculate the scale factor the items if the resolution is different.
			float productSize = LevelActivity.instance.getResources().getInteger(R.integer.l20_product_default_size);
			productSize *= height / 480.0f;

			// TODO: Spawn rare products more often
			int productType = (int)(Math.random() * ProductInfo.length);
			ProductEntity pe = ProductInfo.createEntity(productType, x, y, 1, productSize);
			
			// Declaring neighbors
			// INFO: This might be unsafe, because we are declaring ids which haven't been created yet. But should work for now			
			int nIndex = 0;			
			for (int j = 0; j < NUMBER_PRODUCTS; j++) {
				
				// Don't mark yourself as neighbor
				if (i != j) {
				
					pe.neighbors[nIndex] = pe.id + j - i;
					nIndex++;
				}
			}
			
			
			products.put(pe.id, pe);
		}	
	}
	
	/**
	 * Creates a moving shopping cart.
	 */
	public void createShoppingCart() {
		
		float width = LevelActivity.renderView.getWidth();		
		
		// Spawn outside the screen and subtract the amount of d already passed.
		float offsetX = 75;
		float x = width + offsetX;
		float y = LevelActivity.gameManager.shoppingCarts[0].y;
		float shoppingCartSize = LevelActivity.gameManager.shoppingCarts[0].width;
		
		movingShoppingCart = new ShoppingCart(x, y, 2, shoppingCartSize, shoppingCartSize);
		movingShoppingCart.texture = LevelActivity.renderView.getTexture(RenderView.TEXTURE_CART);
	}
}
	
