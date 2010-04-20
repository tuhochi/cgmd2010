/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/**
 * These Entities are displayed as Products in the Shelf and can be collected by clicking on them. 
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class ProductEntity extends RenderEntity implements Clickable {
	
	/**
	 * Flag whether the entity is clickable or not.
	 */
	protected boolean clickable;
	
	/**
	 * The price of the product.
	 */
	protected int price;

	/**
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param size The side length of the quadratic product entity.
	 */
	public ProductEntity(float x, float y, float z, float size) {
		super(x, y, z, size, size);
		clickable = true;
		price = (int)(Math.random() * 10.f);
	}

	@Override
	public boolean hitTest(float hitX, float hitY) {
		
		float hW = width * 0.5f;
		float hH = height * 0.5f;
		
		return (hitX >= x - hW && hitX < x + hW   &&   hitY >= y - hH && hitY < y + hH);
	}

}
