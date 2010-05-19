package at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Geometry {

	private FloatBuffer posBuf;
	private FloatBuffer texBuf;
	private ShortBuffer indexBuf;

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
	
	
	/**
	 * Set texture coordinate buffer.
	 * @param buf The texture coordinate buffer
	 */
	public void setTexBuffer(FloatBuffer buf) {
		texBuf = buf;
	}
	
	
	/**
	 * Draw the quad.
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
