/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This class creates, recreates and knows all properties of the ProductEntity
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class ProductInfo {

	/**
	 * A list of product properties
	 */
	private static final ProductInfo[] p = new ProductInfo[]{
		
		new ProductInfo(0, 	R.drawable.l20_bananamilk, 	1.29f),
		new ProductInfo(1, 	R.drawable.l20_beer, 		0.69f),
		new ProductInfo(2, 	R.drawable.l20_broccoli, 	1.49f),
		new ProductInfo(3, 	R.drawable.l20_cereal, 		2.3f),
		new ProductInfo(4, 	R.drawable.l20_drink, 		0.85f),
		new ProductInfo(5, 	R.drawable.l20_icecream, 	3.29f),
		new ProductInfo(6, 	R.drawable.l20_lollipop, 	0.49f),
		new ProductInfo(7, 	R.drawable.l20_penne, 		0.99f),
		new ProductInfo(8, 	R.drawable.l20_pizza, 		2.45f),
		new ProductInfo(9, 	R.drawable.l20_playbunny, 	6.60f),
		new ProductInfo(10,	R.drawable.l20_probio, 		1.1f),
		new ProductInfo(11,	R.drawable.l20_soup, 		0.89f),
		new ProductInfo(12,	R.drawable.l20_yoghurt, 	1.35f),
					
		};
	

	/**
	 * The type of the product
	 */
	private int _type;
	
	/**
	 * The texture file of the product
	 */
	private int _texture;
	
	/**
	 * The price of the product
	 */
	private float _price;
	
	
	/**
	 * The number of products 
	 */
	public static final int length = p.length;
	
	
	/**
	 * @param type
	 * @param texture
	 * @param price
	 */
	private ProductInfo(int type, int texture, float price) {
		
		this._type = type;
		this._texture = texture;
		this._price = price;
	}

	/**
	 * @param type
	 * @param x
	 * @param y
	 * @param z
	 * @param size
	 * @return
	 */
	public static ProductEntity createEntity(int type, float x, float y, float z, float size) {
		
		ProductEntity pe = new ProductEntity(type, x, y, z, size);
		pe.texture = LevelActivity.renderView.getTexture(ProductInfo.texture(type));
		
		return pe;	
	}
	
	/**
	 * @param type The product type.
	 * @return Returns the texture file of the product.
	 */
	public static int texture(int type) {
		return ProductInfo.p[type]._texture;
	}
	
	/**
	 * @param type The product type.
	 * @return Returns the price of the product.
	 */
	public static float price(int type) {
		return ProductInfo.p[type]._price;
	}
	
}
