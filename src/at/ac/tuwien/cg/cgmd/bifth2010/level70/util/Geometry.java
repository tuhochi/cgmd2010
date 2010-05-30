package at.ac.tuwien.cg.cgmd.bifth2010.level70.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Geometry - Stores the position, the texcoords and the index buffer
 * of the geometry.
 * 
 * @author Christoph Winklhofer
 */
public class Geometry {

    // ----------------------------------------------------------------------------------
    // -- Members ----
    
    /** Position vertex buffer */
	private FloatBuffer posBuf;
	
	/** Texture coordinate buffer */
	private FloatBuffer texBuf;
	
	/** Index buffer */
	private ShortBuffer indexBuf;

	
	// ----------------------------------------------------------------------------------
    // -- Ctor ----
	
	/**
	 * Create a geometry - The geometry is drawn with triangles.
	 * @param positions Array with the vertex positions.
	 * @param texcoords Array with the texture coordinates.
	 * @param indices Array with indices.
	 */
	public Geometry(float[] positions, float[] texcoords, short[] indices) {
		
		// Create positions buffer 
		ByteBuffer pbuf = ByteBuffer.allocateDirect(positions.length * 4);
		pbuf.order(ByteOrder.nativeOrder());
		posBuf = pbuf.asFloatBuffer();
		posBuf.put(positions);
		posBuf.position(0);
		
		// Create texcoord buffer 
		ByteBuffer tbuf = ByteBuffer.allocateDirect(texcoords.length * 4);
		tbuf.order(ByteOrder.nativeOrder());
		texBuf = tbuf.asFloatBuffer();
		texBuf.put(texcoords);
		texBuf.position(0);
				
		// Create index buffer
		ByteBuffer ibuf = ByteBuffer.allocateDirect(indices.length * 2);
		ibuf.order(ByteOrder.nativeOrder());
		indexBuf = ibuf.asShortBuffer();
		indexBuf.put(indices);
		indexBuf.position(0);
	}
	
	
	// ----------------------------------------------------------------------------------
    // -- Public methods ----
	
	/**
	 * Set texture coordinate buffer.
	 * @param buf The texture coordinate buffer
	 */
	public void setTexBuffer(FloatBuffer buf) {
		texBuf = buf;
	}
	
	
	/**
	 * Draw the geometry.
	 * @param gl OpenGl context.
	 */
	public void draw(GL10 gl) {
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, posBuf);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuf);

		gl.glDrawElements(GL10.GL_TRIANGLES, indexBuf.capacity(), GL10.GL_UNSIGNED_SHORT, indexBuf);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
