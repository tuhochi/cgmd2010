package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class ShoppingCart extends RenderEntity {

	protected List<ProductEntity> products;
	
	public ShoppingCart(float x, float y, float z, float width, float height) {
		super(x, y, z, width, height);
 
		products = new LinkedList<ProductEntity>();		
	}
	
	@Override
	public void render(GL10 gl) {		
		// Render products.
		Iterator<ProductEntity> itr = products.iterator();
		while(itr.hasNext()) {
			itr.next().render(gl);
		}		
		super.render(gl);
	}
	
	public void addProduct(ProductEntity product) {				
		products.add(product);
	}
	
	public void clearProducts() {
		products.clear();
	}
	
	public int getNumberProducts() {
		return products.size();
	}

	public float[] getNextProductPosition() {
		float[] pos = new float[2];
		int nrProducts = products.size();		
		int row = (int) Math.floor(nrProducts / 4);
		float width = this.width*.5f - 50;
		pos[0] = (float) (x - width + Math.random()* width*2);
		pos[1] = y + row*15;
		return pos;
	}

}
