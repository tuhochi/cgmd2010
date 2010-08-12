package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;

/**
 * A Sprite with an item-type and two textures. One black/white and one in 
 * color.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class ItemSprite {
	
	float x,y,width,height;
	
	/** The type of the <code>ItemSprite</code> as defined in 
	 * {@link ItemQueue}. 
	 */
	int type;
	private int[] textureNum;

	/**
	 * Creates a new ItemSprite.
	 * 
	 * <p>
	 * <b>Attention:</b> The filename is expected to be passed without its file
	 * extension. The constructor will look for files called 
	 * <code>file.png</code> and <code>file_bw.png</code> in the assets 
	 * directory.
	 * </p>
	 * 
	 * @param file		The filename without file extension. Is supposed to be 
	 * 					a .png file.
	 * @param x			XPosition.
	 * @param y			YPosition.
	 * @param width		Width of the sprite.
	 * @param height	Height of the sprite.
	 * @param gl
	 */
	public ItemSprite(String file, float x, float y, float width, float height,
			GL10 gl) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		textureNum = new int[2];
		textureNum[0] = MyTextureManager.singleton.addTextureFromAssets(file + 
				".png", gl);
		textureNum[1] = MyTextureManager.singleton.addTextureFromAssets(file + 
				"_bw.png", gl);
	}
	
	/**
	 * Creates a new ItemSprite.
	 * 
	 * @param type		The type of the item that should be created, as defined
	 * 					in {@link ItemQueue}.
	 * @param x			XPosition.
	 * @param y			YPosition.
	 * @param width		Width of the sprite.
	 * @param height	Height of the sprite.
	 * @param gl
	 */
	public ItemSprite(int type, float x, float y, float width, 
			float height, GL10 gl) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		textureNum = new int[2];
		this.type = type;
		
		switch (type) {
		case ItemQueue.BOMB:
			textureNum[0] = MyTextureManager.singleton.addTextureFromResources(ITEM_BOMB, gl);
			textureNum[1] = MyTextureManager.singleton.addTextureFromResources(ITEM_BOMB_BW, gl);
			break;
//		case ItemQueue.LASER:
//			textureNum[0] = MyTextureManager.singleton.addTextureFromResources(ITEM_LASER, gl);
//			textureNum[1] = MyTextureManager.singleton.addTextureFromResources(ITEM_LASER_BW, gl);
//			break;
		case ItemQueue.DELETEWALL:
			textureNum[0] = MyTextureManager.singleton.addTextureFromResources(ITEM_DELETEWALL, gl);
			textureNum[1] = MyTextureManager.singleton.addTextureFromResources(ITEM_DELETEWALL_BW, gl);
			break;
		case ItemQueue.WALL:
			textureNum[0] = MyTextureManager.singleton.addTextureFromResources(ITEM_WALL, gl);
			textureNum[1] = MyTextureManager.singleton.addTextureFromResources(ITEM_WALL_BW, gl);
			break;
		}
	}

	/**
	 * Creates a copy of the ItemSprite <code>sprite</code> with the new 
	 * position <code>x, y</code>.
	 * 
	 * @param sprite	The sprite to be copied.
	 * @param x			XPosition.
	 * @param y			YPosition.
	 */
	public ItemSprite(ItemSprite sprite, float x, float y) {
		this(sprite);
		
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a copy of the ItemSprite <code>sprite</code>.
	 * @param sprite	The sprite to be copied.
	 */
	public ItemSprite(ItemSprite sprite) {
		this.x = sprite.x;
		this.y = sprite.y;
		this.type = sprite.type;
		this.width = sprite.width;
		this.height = sprite.height;
		
		this.textureNum = sprite.textureNum;
	}

	/**
	 * Can be used to free resources.
	 * 
	 * <p>
	 * Not implemented yet.
	 * </p>
	 * 
	 * @param gl
	 */
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Draws this ItemSprite.
	 * 
	 * @param gl		
	 * @param active	If true, the colored texture is drawn. Otherwise 
	 * 					the black/white texture is used.
	 */
	public void Draw(GL10 gl, boolean active) {
		if (active)
			MyTextureManager.singleton.textures[textureNum[0]].Bind(gl);
		else
			MyTextureManager.singleton.textures[textureNum[1]].Bind(gl);
		((GL11Ext) gl).glDrawTexfOES(x,y,0,width, height);
	}

}
