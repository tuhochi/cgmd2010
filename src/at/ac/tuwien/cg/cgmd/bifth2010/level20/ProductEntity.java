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
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param width The width of the entity.
	 * @param height The height of the entity.
	 */
	public ProductEntity(float x, float y, float z, float width, float height) {
		super(x, y, z, width, height);
		
	}

	@Override
	public boolean hitTest(float hitX, float hitY) {
		
		float hW = width * 0.5f;
		float hH = height * 0.5f;
		
		if (hitX >= x - hW && hitX < x + hW   &&   hitY >= y - hH && hitY < y + hH)
			return true;
		
		return false;
	}

}
