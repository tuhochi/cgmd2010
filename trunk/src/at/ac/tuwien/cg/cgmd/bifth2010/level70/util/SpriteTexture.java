package at.ac.tuwien.cg.cgmd.bifth2010.level70.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Sprite texture - Load sprite texture coordinates.
 * 
 * @author Christoph Winklhofer
 */

public class SpriteTexture {
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
    /** The sprite texture */
	private GlTexture tex;
	
	/** List with all possible sprite texture coordinates */
	private ArrayList<FloatBuffer> texBufs;
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create sprite texture. Load all possible sprite texture coordinates.
	 * @param resid resid The resource id of the texture.
	 * @param spriteSize The dimension of the size. Width and height of a sprite must be equal
	 *             in the texture.
	 * @param spriteCnt The number of sprites to load.            
	 */
	public SpriteTexture(int resid, int spriteSize, int spriteCnt) {
		
		tex = new GlTexture(resid);
		texBufs = new ArrayList<FloatBuffer>();
		
		float tw = (float)spriteSize / tex.getWidth();
		float th = (float)spriteSize / tex.getHeight();
		
		// Create texcoord buffers and store them in an array
		for (int i = 0; i < spriteCnt; i++) {
			float[] texcoords = getTexcoords(i, tw, th);
			ByteBuffer tbb = ByteBuffer.allocateDirect(texcoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			FloatBuffer texBuf = tbb.asFloatBuffer();
			texBuf.put(texcoords);
			texBuf.position(0);
			texBufs.add(texBuf);
		}
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Bind tile texture.
	 */
	public void bind() {
		tex.bind();
	}
	
	
	
	/**
	 * Unbind tile texture.
	 */
	public void unbind() {
		tex.unbind();
	}
	
	
	/**
	 * Return texture coordinate buffer for the specified sprite.
	 * @param index Sprite index.
	 * @return Texture coordinate buffer.
	 */
	public FloatBuffer getTexBuffer(int index) {
		return texBufs.get(index);
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----
	
	/**
	 * Return texture coordinates for the specified tile. The order in TileEnum
	 * must correspond to the order in the texture image file.
	 * @param type The tile type.
	 * @param tw Normalized sprite width.
	 * @param th Normalized sprite height.
	 */
	private static float[] getTexcoords(int index, float tw, float th) {
		float left  = tw * index;
		float right = tw * (index + 1);
		float texcoords[] = { left, 1,  right, 1,  right, 0,  left, 0 };
		return texcoords;
	}
}
