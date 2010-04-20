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
	
	
	// The currently active products in the shelf.	
//	protected List<ProductEntity> products;

	// The product collection
	protected Hashtable<Integer, ProductEntity> entities;
	
	protected float scrollX;
	protected float distMovedX;
	protected int productSpawnColumns;
	protected float[] productSpawnY;
	
	protected static int NUMBER_PRODUCTS; 
	protected boolean productsActive;
	
	/**
	 * @param width The screen width.
	 * @param height The screen height.
	 */
	public Shelf(float width, float height) {
		
		super(width * 0.5f, height * 0.5f, 0, width, height);
		//products = new List<ProductEntity>();
		// The overall distance moved so far
		distMovedX = 0;
		// The num of product collumns already spawned
		productSpawnColumns = 0;
		
		// The y position of spawned products
		float spawnY = 145;
		float spawnInc = 70;		
		productSpawnY = new float[]{spawnY, spawnY + spawnInc, spawnY + spawnInc*2};
		
		// The product collection
		entities = new Hashtable<Integer, ProductEntity>();
//		products = new LinkedList<ProductEntity>();
		productsActive = false;
		NUMBER_PRODUCTS = 3;
	}

	/**
	 * Updates the shelf.
	 * @param scroll The new scroll value for the shelf animation.
	 */
	public void update(float scroll) {
		distMovedX += scroll;
		
		// Calculation of background movement (in texture coordinate system).
		scrollX = distMovedX / width;
		
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
		// Everytime there's a new column, spawn a new set of products. Needs to be an integer division		
		
//		if (products.isEmpty()) {
			createProducts();
//		}
	}
	
	/**
	 * Creates a product for the shelf.
	 */
	public void createProducts() {
		// This varies due to changes in scroll speed
		float d = distMovedX % 150;
		
		for (int i = 0; i < NUMBER_PRODUCTS; i++) {
			// Spawn outside the screen and subtract the amount of d already passed.
			float offsetX = 75;
			float jitterX = (int)(Math.random() * 50);
			float x = width - d + offsetX + jitterX;			
			float y = productSpawnY[i];			
			// The product icons are optimized for a screen resolution of 800 x 480. Calculate the scale factor the items if the resolution is different. 
			float productSize = 70 * height / width;

			ProductEntity pe = new ProductEntity(x, y, 1, productSize);	
			int texIdx = (int)(Math.random() * 10) % GameManager.TEXTURE_PRODUCTS.length;
			texIdx = Math.min(texIdx, GameManager.TEXTURE_PRODUCTS.length-1);				
			
			pe.visible = true;
			pe.clickable = true;
			pe.texture = GameManager.getTexture(GameManager.TEXTURE_PRODUCTS[texIdx]);		
			entities.put(pe.id, pe);
		}

		productsActive = true;
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
		gl.glTranslatef(scrollX, 0, 0);		
		
		// Switch to ModelView and render as usual
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		super.render(gl);
			
		// Now pop Texture Matrix back 
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
		if (!productsActive) 
			return;
		
		Enumeration<Integer> keys = entities.keys();		
		while(keys.hasMoreElements()) {
			
			ProductEntity pe = entities.get(keys.nextElement());
			
			if (pe.visible && pe.clickable && pe.hitTest(x, y)) {
				pe.clickable = false;
				EventManager.getInstance().dispatchEvent(EventManager.PRODUCT_COLLECTED, pe);
				productsActive = false;
				break;
			}
		}
	}
	
}
	
