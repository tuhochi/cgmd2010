package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class represents the Shelf in the background of the game area. It is a fullscreen quad with changing texture coordinates. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class Shelf extends RenderEntity {

	
	/**
	 * FERDI: Das ist static. Geht das anders?
	 * The number of products to spawn at each row
	 */
	protected static int NUMBER_PRODUCTS;
	
	/**
	 * The distance between 2 product rows in pixels
	 */
	protected float PRODUCT_DISTANCE_X;
	
	/** 
	 * The product collection.
	 * TODO: Rename to products
	 */
	protected Hashtable<Integer, ProductEntity> entities;
	
	/**
	 * The distance in pixels, the shelf has moved since the start of the game
	 */
	protected float pixelsX;
	
	/**
	 * This value increases every frame until PRODUCT_DISTANCE_X is reached and is set to 0 again.
	 */
	protected float distToLastProduct;
	
	/**
	 * The Y positions of the shelves. (At this height, the products are spawning)
	 */
	protected float[] productSpawnY;
	
	/**
	 * Products can only be clicked while this is true	 *
	 * FERDI: Was ist wenn mehr Produktreihen gleichzeitig am Bildschirm sind?
	 */ 
//	protected boolean productsActive;
	
	/**
	 * @param width The screen width.
	 * @param height The screen height.
	 */
	public Shelf(float width, float height) {
		
		super(width * 0.5f, height * 0.5f, 0, width, height);
				
		NUMBER_PRODUCTS = 3;
		PRODUCT_DISTANCE_X = 150;
		
		
		entities = new Hashtable<Integer, ProductEntity>();
		
		pixelsX = 0;
		distToLastProduct = 0;
		
//		float spawnY = 145;
//		float spawnInc = 70;
		// TODO: Calc better values
		productSpawnY = new float[]{height * 285 / 320f, height * 220 / 320f, height * 155 / 320f};
				
//		productsActive = false;
	}

	/**
	 * Updates the shelf.
	 * @param scroll The new scroll value for the shelf animation.
	 */
	public void update(float scroll) {
		
		// Move screen pixels
		pixelsX += scroll;
		
		// Update all Animators
		Enumeration<Integer> keys = entities.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = entities.get(keys.nextElement());
			
			pe.x -= scroll;
			
			// If they are out of the screen remove them
			if (pe.x < -pe.width) {				
				entities.remove(pe.id);	
				pe = null;
			}
		}
		
		// At last, add some new products every few pixels
		// FERDI: Ich würd das Spawnen von den Products nicht nur auf 1 Bildschirm beschränken. 
		distToLastProduct += scroll;
		
		if (distToLastProduct >= PRODUCT_DISTANCE_X) {			
			distToLastProduct -= PRODUCT_DISTANCE_X;	
			
			createProducts();
		}
	}
	
	/**
	 * Creates a product for the shelf.
	 */
	public void createProducts() {
		
		// This is the amount of x, the shelf has already passed since the product-creation point
		// FERDI: Eigentlich muss es ja gar nicht Punkt-genau spawnen. Wenn wir den Jitter haben... 
//		float dx = pixelsX % 150;
		
		// FERDI: jitterX vielleicht für alle 3 products gleich?
		float jitterX = (int)(Math.random() * 25);
		
		for (int i = 0; i < NUMBER_PRODUCTS; i++) {
			// Spawn outside the screen and subtract the amount of d already passed.
			float offsetX = 75;
			float x = width + offsetX + jitterX;			
			float y = productSpawnY[i];
			
			// The product icons are optimized for a screen resolution of 800 x 480. Calculate the scale factor the items if the resolution is different. 
			float productSize = 64 * height / 480;

			int texId = GameManager.TEXTURE_PRODUCTS[(int)(Math.random() * GameManager.TEXTURE_PRODUCTS.length)];

			ProductEntity pe = new ProductEntity(x, y, 1, productSize);	
			pe.texture = GameManager.getTexture(texId);
			
			
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
			
			
			entities.put(pe.id, pe);
		}

//		productsActive = true;
	}
	
	
	
	
	
	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void render(GL10 gl) {
		
		
		// Apply Texture Matrix Transformation
		gl.glMatrixMode(GL10.GL_TEXTURE);		
		gl.glPushMatrix();
		// Translate the texture in texture coordinate system (, so divide it by the screen width).
		gl.glTranslatef(pixelsX / width, 0, 0);
		
		// Switch to ModelView and render as usual
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		super.render(gl);
			
		// Now pop Texture Matrix back, so that further objects are drawn with texture at origin. 
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glPopMatrix();
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		// Render products.	
		Enumeration<Integer> keys = entities.keys();
		while(keys.hasMoreElements()) {
			entities.get(keys.nextElement()).render(gl);
		}	
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void hitTest(float x, float y) {
//		if (!productsActive) 
//			return;
		
		Enumeration<Integer> keys = entities.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = entities.get(keys.nextElement());
			
			if (pe.visible && pe.clickable && pe.hitTest(x, y)) {
				pe.clickable = false;
//				productsActive = false;
				EventManager.getInstance().dispatchEvent(EventManager.PRODUCT_COLLECTED, pe);
				entities.remove(pe.id);
				
				//Mark neighbors as not clickable too
				for (int i = 0; i < pe.neighbors.length; i++) {
					entities.get(pe.neighbors[i]).clickable = false;
					// TODO: Small hack, do something better:
					entities.get(pe.neighbors[i]).angle = 90;
				}
				
				break;
			}
		}
	}
	
}
	
