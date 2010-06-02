package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */

public class ShoppingCart extends RenderEntity implements Clickable {

	/**
	 * Holds all products in the ShoppingCart
	 */
	protected Hashtable<Integer, ProductEntity> entities;
	
	
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
 
		entities = new Hashtable<Integer, ProductEntity>();
	}
	
	@Override
	public void render(GL10 gl) {		
		
		super.render(gl);
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
	
	/**
	 * 
	 */
	public int getNumberProducts() {
		return entities.size();
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
	
}
