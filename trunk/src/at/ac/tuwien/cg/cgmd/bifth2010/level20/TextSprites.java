/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.khronos.opengles.GL10;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Extracts and builds single sprites out of a single texture containing various characters.
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 *
 */
public class TextSprites {
	
	/** ID to identify text sprite RenderEntities. */	
	static final int TEXT_SPRITE = 9999;
	
	/** Number of chars per row in the texture. */
	private float nRowChars;
	/** Number of chars per column in the texture. */
	private float nColumnChars;
	/** The width of one character in the texture (in texture space). */
	private float charWidth;
	/** The height of one character in the texture (in texture space). */
	private float charHeight;
	/** Stores a sprite for every available number. */
	private Hashtable<Integer, RenderEntity> spriteNumbers;
	/** Stores a sprite for every available character. */
	private Hashtable<String, RenderEntity> spriteChars;
	/** Counts the number of created sprites used for assigning IDs. */
	private int spriteCount;
	
	/** Constructor of TextSprites */
	public TextSprites() {
		spriteNumbers = new Hashtable<Integer, RenderEntity>();
		spriteChars = new Hashtable<String, RenderEntity>();
		nRowChars = 4.5f;
		nColumnChars = 6.0f;
		charWidth =  1.0f / nColumnChars;
		charHeight =  1.0f / nRowChars;
		spriteCount = 0;
	}
	
	/** Constructs single sprites out of the character texture. */
	public void buildSprites(GL10 gl) {	
		int charTexture = LevelActivity.renderView.getTexture(R.drawable.l20_chars, gl); 
		float spriteSize = 32 * GameManager.screenRatio;
		float texCoords[] = new float[4];
		texCoords[2] = charWidth;
		texCoords[3] = charHeight;
		
		for (int i = 0; i < 10; i++) {
			texCoords[0] = i % 6 * charWidth;
			texCoords[1] = i / (int)nColumnChars * charHeight;
			
			RenderEntity sprite = new RenderEntity(i*spriteSize, 200f, 2f, spriteSize, spriteSize, 
					texCoords[0], texCoords[1], texCoords[2], texCoords[3]); 
			sprite.visible = false;
			sprite.id = TEXT_SPRITE + spriteCount++;
			sprite.texture = charTexture;
			spriteNumbers.put(i, sprite);
		}
		
		// Create character sprites.
		// +
		texCoords[0] = 4 * charWidth;
		texCoords[1] = charHeight;
		RenderEntity sprite = new RenderEntity(0f, 0f, 4f, spriteSize, spriteSize, 
				texCoords[0], texCoords[1], texCoords[2], texCoords[3]);
		sprite.visible = false;
		sprite.id = TEXT_SPRITE + spriteCount++;
		sprite.texture = charTexture;
		spriteChars.put("+", sprite);
		
		// -
		texCoords[0] = 5 * charWidth;
		texCoords[1] = charHeight;
		sprite = new RenderEntity(0f, 0f, 4f, spriteSize, spriteSize, 
				texCoords[0], texCoords[1], texCoords[2], texCoords[3]); 
		sprite.visible = false;
		sprite.id = TEXT_SPRITE + spriteCount++;
		sprite.texture = charTexture;		
		spriteChars.put("-", sprite);
		
	}
	
	/** Returns the RenderEntity with the texture representing the passed number. */
	public RenderEntity getNumberSprite(int number) {
		if (number > 9 || number < 0) {
			return spriteNumbers.get(0);
		}
		else {
			return spriteNumbers.get(number);
		}
	}
	
	/** Returns the RenderEntity with the texture representing the passed character. */
	public RenderEntity getCharSprite(String character) {
		return spriteChars.get(character);
		
	}
	
	/** Renders the visible text sprites. 
	 * @param gl */
	void render(GL10 gl) {
		// Render number sprites.
		Enumeration<Integer> keys = spriteNumbers.keys();				
		while(keys.hasMoreElements()) {
			spriteNumbers.get(keys.nextElement()).render(gl);
		}
		
		// Render char sprites.
		Enumeration<String> charKeys = spriteChars.keys();				
		while(charKeys.hasMoreElements()) {
			spriteChars.get(charKeys.nextElement()).render(gl);
		}
	}
}
