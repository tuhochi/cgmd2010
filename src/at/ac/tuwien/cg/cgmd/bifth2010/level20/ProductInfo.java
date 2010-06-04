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
		
		new ProductInfo(0, 	R.drawable.l20_bananamilk, 	1),
		new ProductInfo(1, 	R.drawable.l20_beer, 		2),
		new ProductInfo(2, 	R.drawable.l20_broccoli, 	3),
		new ProductInfo(3, 	R.drawable.l20_cereal, 		4),
		new ProductInfo(4, 	R.drawable.l20_drink, 		5),
		new ProductInfo(5, 	R.drawable.l20_icecream, 	6),
		new ProductInfo(6, 	R.drawable.l20_lollipop, 	7),
		new ProductInfo(7, 	R.drawable.l20_penne, 		8),
		new ProductInfo(8, 	R.drawable.l20_pizza, 		9),
		new ProductInfo(9, 	R.drawable.l20_playbunny, 	0),
		new ProductInfo(10,	R.drawable.l20_probio, 		7),
		new ProductInfo(11,	R.drawable.l20_soup, 		6),
		new ProductInfo(12,	R.drawable.l20_yoghurt, 	9),
					
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
