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
	 * Flag whether the entity is currently animated or not. (If an animator is attached to it)
	 */
	protected boolean animated;
	
	/**
	 * The type of the product.
	 */
	protected int type;
		
	/**
	 * The neighbors of the product in the same row. We need to know this because they get deactivated once this product has been clicked
	 */
	protected int[] neighbors;
	

	/**
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param size The side length of the quadratic product entity.
	 * FERDI: Alle products sind im Moment quadratisch? Soll ma das so lassen oder auch rechtwinkelige zulassen?
	 */
	public ProductEntity(int type, float x, float y, float z, float size) {
		super(x, y, z, size, size);
		this.type = type;
		
		clickable = true;
		animated = false;
		neighbors = new int[ProductManager.NUMBER_PRODUCTS - 1];
	}

	@Override
	public boolean hitTest(float hitX, float hitY) {
		
		float hW = width * 0.5f;
		float hH = height * 0.5f;
		
		return (hitX >= x - hW && hitX < x + hW   &&   hitY >= y - hH && hitY < y + hH);
	}

}
