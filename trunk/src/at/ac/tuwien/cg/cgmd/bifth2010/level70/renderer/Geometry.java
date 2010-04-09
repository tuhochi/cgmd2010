package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Geometry {

	private float[] vertices;
	private short[] indices;
	public  float[] pos = { 0.0f, -1.0f, 0.0f };
	
	private FloatBuffer vertexBuf;
	private ShortBuffer indexBuf;

	public Geometry(float[] vertices, short[] indices) {
		
		// Set vertices and indices
		this.vertices = vertices;
		this.indices  = indices;
		
		// Create vertex buffer
		ByteBuffer vbuf = ByteBuffer.allocateDirect(vertices.length * 4);
		vbuf.order(ByteOrder.nativeOrder());
		vertexBuf = vbuf.asFloatBuffer();
		vertexBuf.put(vertices);
		vertexBuf.position(0);
		
		// Create index buffer
		ByteBuffer ibuf = ByteBuffer.allocateDirect(indices.length * 2);
		ibuf.order(ByteOrder.nativeOrder());
		indexBuf = ibuf.asShortBuffer();
		indexBuf.put(indices);
		indexBuf.position(0);
	}
	
	
	void draw(GL10 gl) {
		
		//gl.glTranslatef(0.0f, 0.5f, 0.0f);
		gl.glTranslatef(pos[0], pos[1], pos[2]);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuf);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuf);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
