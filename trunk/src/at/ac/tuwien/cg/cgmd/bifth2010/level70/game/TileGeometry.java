package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Geometry data for a tile.
 */
public class TileGeometry {

	// ----------------------------------------------------------------------------------
	// -- Static members ----
	
	public static float TILE_SIZE = 1.0f;
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private float llx;     //< Lower left x coordinate
	private float lly;     //< Lower left y cooridnate
	private TileEnum type; //< Tile type
	
	private FloatBuffer posBuf; //< Position buffer
	private FloatBuffer texBuf; //< Texture coordinate buffer
	private ShortBuffer indBuf; //< Index buffer
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create tile geometry.
	 * @param llx lower left x coordinate
	 * @param lly lower left y coordinate
	 */
	public TileGeometry(float llx, float lly) {
		this.llx = llx;
		this.lly = lly;
		
		// Create positions
		float poss[] = { llx, lly, 0,
	                     llx + TILE_SIZE, lly, 0,
	                     llx + TILE_SIZE, lly + TILE_SIZE, 0,
	                     llx, lly + TILE_SIZE, 0 };
		ByteBuffer pbb = ByteBuffer.allocateDirect(poss.length * 4);
		pbb.order(ByteOrder.nativeOrder());
		posBuf = pbb.asFloatBuffer();
		posBuf.put(poss);
		posBuf.position(0);
		
		// Create indices
		short inds[] = { 0, 1, 2, 0, 2, 3 };
		ByteBuffer ibb = ByteBuffer.allocateDirect(inds.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indBuf = ibb.asShortBuffer();
		indBuf.put(inds);
		indBuf.position(0);
		
		setType(TileEnum.TILE_EMPTY);
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Set tile type.
	 * @type type The tile type.
	 */
	public void setType(TileEnum type) {
		this.type = type;
		texBuf = TileTexture.getInstance().getTexBuffer(type);
	}
	
	
	/**
	 * Draw the tile geometry
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, posBuf);
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuf);

		gl.glDrawElements(GL10.GL_TRIANGLES, indBuf.capacity(), GL10.GL_UNSIGNED_SHORT, indBuf);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	
	/**
	 * Return lower left x coordinate.
	 * @return The lower left x coordinate.
	 */
	public float getLlx() {
		return llx;
	}
	
	
	/**
	 * Return lower left y coordinate
	 * @return The lower left y coordinate.
	 */
	public float getLly() {
		return lly;
	}
}
