package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */

public class ShoppingCart extends RenderEntity implements Clickable {

	/** Intermediate storage, because it's Threadsafe */
	protected Hashtable<Integer, ProductEntity> addProducts;
	
	/** Holds all products in the ShoppingCart */
	protected LinkedList<ProductEntity> products;
	
	/**
	 * Counts the quantities for all product types
	 */
	protected int[] quantities;
	
	
	/**
	 * Creates the shopping cart in the given place. It holds all clicked products and handles their prices
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 */
	public ShoppingCart(float x, float y, float z, float width, float height) {
		super(x, y, z, width, height);
		addProducts = new Hashtable<Integer, ProductEntity>();
		products = new LinkedList<ProductEntity>();
		quantities = new int[ProductInfo.length];
	}
	
	
	
	@Override
	public void init() {		
		super.init();
		
		
	}



	@Override
	public void render(GL10 gl) {	
		// Render shopping cart products
//		Enumeration<Integer> keys = entities.keys();				
//		while(keys.hasMoreElements()) {
//			entities.get(keys.nextElement()).render(gl);
//		}
		
		for (Iterator iterator = products.iterator(); iterator.hasNext();) {
			ProductEntity pe = (ProductEntity) iterator.next();
			pe.render(gl);
		}
		
		super.render(gl);
	}
	
	/**
	 * @param product
	 */
	public void addProduct(ProductEntity pe) {				
		addProducts.put(pe.id, pe);
		quantities[pe.type]++;
	}
	
	/**
	 * 
	 */
	public void clearProducts() {
//		entities.clear();
		products.clear();
	}
	
	/**
	 * 
	 */
	public int getNumberProducts() {
//		return entities.size();
		return products.size();
	}

	/**
	 * @return
	 */
	public float[] getNextProductPosition() {
		float[] pos = new float[2];
		int nrProducts = getNumberProducts();		
		int row = nrProducts / 8; // Integer division is fun :D
		float width = this.width * 0.5f - 100;
		pos[0] = (float) (x - width + Math.random()* width*2);
		pos[1] = y + row*15;
		return pos;
	}

	/**
	 * @param bunnyPos moves the cart according to the position of the bunny
	 */
	public void update(float bunnyPos) {
		float oldPos = x;
		x = bunnyPos + width*0.45f;
		float posDiff = x - oldPos;

		// Add new products into the list on a safe spot
		Enumeration<Integer> keys = addProducts.keys();				
		while(keys.hasMoreElements()) {
			products.addFirst(addProducts.get(keys.nextElement()));
		}
		addProducts.clear();
		
		for (Iterator iterator = products.iterator(); iterator.hasNext();) {
			ProductEntity pe = (ProductEntity) iterator.next();
			pe.x += posDiff;
		}
	}
	
	
	/**
	 * Checks the cart for discounts.
	 * 
	 * @param pe The last inserted product.
	 */
	public int checkDiscounts(ProductEntity pe) {
				
		if (quantities[pe.type] < ProductInfo.amount(pe.type)) {
			// If that's 0: We did not reach the amount of products to get a discount
			return ProductInfo.price(pe.type);
		}
		
		// All units > ProductInfo.discount are free. We didn't pay for this product yet (== -1), but refund previous units. 
		int refundUnits = ProductInfo.amount(pe.type) - ProductInfo.discount(pe.type) - 1;
		
		// If amount is reached, set to 0 again. From now on, we have to fill the quantities again, before getting a discount. 
		quantities[pe.type] = 0;
		
		// Dispatch an event, telling that we hit a discount
		EventManager.getInstance().dispatchEvent(EventManager.DISCOUNT_ACQUIRED, pe);
		
		// Return a negative price
		return -1 * refundUnits * ProductInfo.price(pe.type);
	}
	
}
