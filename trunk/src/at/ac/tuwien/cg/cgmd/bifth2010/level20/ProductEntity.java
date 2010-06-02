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
public class ProductEntity extends RenderEntity {
	
	/** The type of the product. */
	protected int type;		
	/** The neighbors of the product in the same row. */
	protected int[] neighbors;	

	/**
	 * @param x The X coordinate. 
	 * @param y The Y coordinate. 
	 * @param z The Z coordinate (the depth). 
	 * @param size The side length of the quadratic product entity. 
	 */
	public ProductEntity(int type, float x, float y, float z, float size) {
		super(x, y, z, size, size);
		this.type = type;
		neighbors = new int[ProductManager.NUMBER_PRODUCTS - 1];
	}

	

}
