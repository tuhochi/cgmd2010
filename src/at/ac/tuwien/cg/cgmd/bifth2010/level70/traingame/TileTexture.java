package at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame;

import java.nio.ByteBuffer;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GlTexture;

/**
 * TileTexture. Manages the tile textures for play field. It holds an OpenGL 
 * texture handle and the texture coordinate buffer for each tile.
 * 
 * @author herrjohann
 */

public class TileTexture {
	
	// ----------------------------------------------------------------------------------
	// -- Static members ----
	
	private static int TILE_SIZE = 64; //< Size of a tile sprite
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private GlTexture tex;                  //< OpenGl texture handle
	private ArrayList<FloatBuffer> texBufs; //< Texture coordinates
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create tile texture.
	 */
	public TileTexture() {
		
		tex = new GlTexture(R.drawable.l70_tiles);
		
		texBufs = new ArrayList<FloatBuffer>();
		
		float tw = (float)TILE_SIZE / tex.getWidth();
		float th = (float)TILE_SIZE / tex.getHeight();
		
		// Create texcoord buffers and store them in an array
		for (TileEnum it : TileEnum.values()) {
			float[] texcoords = getTexcoords(it, tw, th);
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
	 * Return texture coordinate buffer for the specified tile.
	 * @param type The tile type.
	 * @return Texture coordinate buffer.
	 */
	public FloatBuffer getTexBuffer(TileEnum type) {
		int ind = type.ordinal();
		return texBufs.get(ind);
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
	private static float[] getTexcoords(TileEnum type, float tw, float th) {
		float left  = tw * type.ordinal();
		float right = tw * (type.ordinal() + 1);
		float texcoords[] = { left, 1,  right, 1,  right, 0,  left, 0 };
		return texcoords;
	}
}
