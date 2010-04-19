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
	
	protected boolean clickable;

	/**
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param size The side length of the quadratic product entity.
	 */
	public ProductEntity(float x, float y, float z, float size) {
		super(x, y, z, size, size);
		clickable = true;
	}

	@Override
	public boolean hitTest(float hitX, float hitY) {
		// Hm, don't ask this here. 
//		if (!clickable)
//			return false;
		
		float hW = width * 0.5f;
		float hH = height * 0.5f;
		
		return (hitX >= x - hW && hitX < x + hW   &&   hitY >= y - hH && hitY < y + hH);
	}

}
