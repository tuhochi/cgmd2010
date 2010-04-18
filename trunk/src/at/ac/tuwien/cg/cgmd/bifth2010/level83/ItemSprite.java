package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;

/**
 * A Sprite with two textures. One black/white and one in color.
 */
public class ItemSprite {
	
	float x,y,width,height;
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
		textureNum[0] = MyTextureManager.singleton.addTexturefromAssets(file + 
				".png", gl);
		textureNum[1] = MyTextureManager.singleton.addTexturefromAssets(file + 
				"_bw.png", gl);
	}
	
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
			textureNum[0] = MyTextureManager.singleton.addTexturefromResources(ITEM_BOMB, gl);
			textureNum[1] = MyTextureManager.singleton.addTexturefromResources(ITEM_BOMB_BW, gl);
			break;
		case ItemQueue.LASER:
			textureNum[0] = MyTextureManager.singleton.addTexturefromResources(ITEM_LASER, gl);
			textureNum[1] = MyTextureManager.singleton.addTexturefromResources(ITEM_LASER_BW, gl);
			break;
		case ItemQueue.DYNAMITE:
			textureNum[0] = MyTextureManager.singleton.addTexturefromResources(ITEM_DYNAMITE, gl);
			textureNum[1] = MyTextureManager.singleton.addTexturefromResources(ITEM_DYNAMITE_BW, gl);
			break;
		case ItemQueue.WALL:
			textureNum[0] = MyTextureManager.singleton.addTexturefromResources(ITEM_WALL, gl);
			textureNum[1] = MyTextureManager.singleton.addTexturefromResources(ITEM_WALL_BW, gl);
			break;
		}
	}

	/**
	 * Creates a copy of the ItemSprite <code>sprite</code> with the new 
	 * position <code>x, y</code>.
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
		this.width = sprite.width;
		this.height = sprite.height;
		
		this.textureNum = sprite.textureNum;
	}

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
