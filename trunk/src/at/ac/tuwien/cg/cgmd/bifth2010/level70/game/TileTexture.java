package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GlTexture;
/**
 * TileTexture. Manages the tile textures for the pipe game.
 */
public class TileTexture {
	
	// ----------------------------------------------------------------------------------
	// -- Static members ----
	
	private static int TILE_SIZE = 32;          //< Size of a tile sprite
	private static TileTexture instance = null; //< Instance for global access.

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private GlTexture tex; //< Tile texture
	private ArrayList<FloatBuffer> texBufs; //< Texture coordinates of all tiles
	
	
	// ----------------------------------------------------------------------------------
	// -- Static methods ----
	
	/**
	 * Return instance of TileTexture for global access.
	 * @return Instance of global access.
	 */
	public static TileTexture getInstance() {
		if (instance == null) {
			instance = new TileTexture();
		}
		return instance;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create tile texture.
	 */
	private TileTexture() {
		
		tex = new GlTexture(R.drawable.l70_pipetiles);
		
		texBufs = new ArrayList<FloatBuffer>();
		
		float tw = (float)TILE_SIZE / tex.getWidth();
		float th = (float)TILE_SIZE / tex.getHeight();
		
		// Create texcoord buffers
		for (TileEnum it : TileEnum.values()) {
			float[] texcoords = getTexcoords(it, tw, th);
			ByteBuffer tbb = ByteBuffer.allocateDirect(texcoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			FloatBuffer texBuf = tbb.asFloatBuffer();
			texBuf.put(texcoords);
			texBuf.position(0);
			texBufs.add(texBuf);
		}
		
		instance = this;
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
	 * Return texture coordinates for the specified tile.
	 * @param type The tile type.
	 * @param tw Normalized sprite width.
	 * @param th Normalized sprite height.
	 */
	private static float[] getTexcoords(TileEnum type, float tw, float th) {
		
		if (type == TileEnum.TILE_OBSTACLE) {
			float texcoords[] = { tw, 1,  tw * 2, 1,  tw * 2, 0,  tw, 0 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_VERTICAL) {
			float texcoords[] = { tw * 2, 1,  tw * 3, 1,  tw * 3, 0,  tw * 2, 0 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_HORIZONTAL) {
			float texcoords[] = { tw * 3, 1,  tw * 3, 0,  tw * 2, 0,  tw * 2, 1 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_RIGHT_UP) {
			float texcoords[] = { tw * 3, 1,  tw * 4, 1,  tw * 4, 0,  tw * 3, 0 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_RIGHT_DOWN) {
			float texcoords[] = { tw * 4, 1,  tw * 4, 0,  tw * 3, 0,  tw * 3, 1 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_LEFT_UP) {
			float texcoords[] = { tw * 3, 0,  tw * 3, 1,  tw * 4, 1,  tw * 4, 0 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_LEFT_DOWN) {
			float texcoords[] = { tw * 4, 0,  tw * 3, 0,  tw * 3, 1,  tw * 4, 1 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_SOURCE_SEL) {
			float texcoords[] = { tw * 4, 1,  tw * 5, 1,  tw * 5, 0,  tw * 4, 0 };
			return texcoords;
		}
		else if (type == TileEnum.TILE_TARGET_SEL) {
			float texcoords[] = { tw * 5, 1,  tw * 6, 1,  tw * 6, 0,  tw * 5, 0 };
			return texcoords;
		}
		else {
			float texcoords[] = { 0, 1,  tw, 1,  tw, 0,  0, 0 };
			return texcoords;
		}
	}
}
