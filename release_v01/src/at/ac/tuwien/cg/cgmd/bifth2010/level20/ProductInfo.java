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
		
		// type, texture name, price, n products, "amount" for price of "discount" products
		new ProductInfo(0, 	R.drawable.l20_pineapple, 	1, 3, 2),
		new ProductInfo(1, 	R.drawable.l20_beer, 		1, 5, 3),
		new ProductInfo(2, 	R.drawable.l20_broccoli, 	2, 3, 2),
		new ProductInfo(3, 	R.drawable.l20_hotdog, 		2, 3, 2),
		new ProductInfo(4, 	R.drawable.l20_drink, 		1, 5, 3),
		new ProductInfo(5, 	R.drawable.l20_icecream, 	3, 5, 3),
		new ProductInfo(6, 	R.drawable.l20_lollipop, 	1, 5, 3),
		new ProductInfo(7, 	R.drawable.l20_burger, 		2, 3, 2),
		new ProductInfo(8, 	R.drawable.l20_pizza, 		3, 3, 2),
		new ProductInfo(9, 	R.drawable.l20_playbunny, 	5, 3, 2),
		new ProductInfo(10,	R.drawable.l20_cheese, 		2, 5, 3),
		new ProductInfo(11,	R.drawable.l20_cupcake, 	3, 3, 2),
		new ProductInfo(12,	R.drawable.l20_washingpowder, 	4, 5, 3),

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
	private int _price;
	
	/**
	 * You get "amount" units of this product for the price of "discount" units.
	 */
	private int _amount;
	
	/**
	 * You get "amount" units of this product for the price of "discount" units.
	 */
	private int _discount;
	
	
	/**
	 * The number of products 
	 */
	public static final int length = p.length;
	
	
	/**
	 * @param type The type of the product
	 * @param texture The texture name
	 * @param price The price
	 * @param amount You get "amount" units of this product for the price of "discount" units. 
	 * @param discount You get "amount" units of this product for the price of "discount" units. 
	 */
	private ProductInfo(int type, int texture, int price, int amount, int discount) {
		
		this._type = type;
		this._texture = texture;
		this._price = price;
		this._amount = amount;
		this._discount = discount;
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
	public static int price(int type) {
		return ProductInfo.p[type]._price;
	}
	
	/**
	 * @param type The product type.
	 * @return You get "amount" units of this product for the price of "discount" units. 
	 */
	public static int amount(int type) {
		return ProductInfo.p[type]._amount;
	}
	
	/**
	 * @param type The product type.
	 * @return You get "amount" units of this product for the price of "discount" units. 
	 */
	public static int discount(int type) {
		return ProductInfo.p[type]._discount;
	}
	
}
