package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */

public class ShoppingCart extends RenderEntity {

	// Holds all products in the ShoppingCart
	protected Hashtable<Integer, ProductEntity> entities;
//	protected List<ProductEntity> products;
	
	public ShoppingCart(float x, float y, float z, float width, float height) {
		super(x, y, z, width, height);
 
//		products = new LinkedList<ProductEntity>();
		entities = new Hashtable<Integer, ProductEntity>();
	}
	
	@Override
	public void render(GL10 gl) {		
		
		// Render products.	
		Enumeration<Integer> keys = entities.keys();
		while(keys.hasMoreElements()) {
			entities.get(keys.nextElement()).render(gl);
		}
	}
	
	/**
	 * @param product
	 */
	public void addProduct(ProductEntity pe) {				
		entities.put(pe.id, pe);
	}
	
	/**
	 * 
	 */
	public void clearProducts() {
		entities.clear();
	}
	
	public int getNumberProducts() {
		return entities.size();
	}

	/**
	 * @return
	 */
	public float[] getNextProductPosition() {
		float[] pos = new float[2];
		int nrProducts = getNumberProducts();		
		int row = (int) Math.floor(nrProducts / 4);
		float width = this.width*.5f - 50;
		pos[0] = (float) (x - width + Math.random()* width*2);
		pos[1] = y + row*15;
		return pos;
	}

}
