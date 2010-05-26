package at.ac.tuwien.cg.cgmd.bifth2010.level88.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Class for the vertexbuffers
 * @author Asperger, Radax
 */
public class Vertexbuffers {
	/**
	 * Types for a vertex buffer
	 * @author Asperger, Radax
	 */
	public enum Type {
		INDEX,
		POSITION,
		TEX_COORD
	}
	
	/** Buffer for the positions*/
	private FloatBuffer posBuffer;
	/** Buffer for the texture coordinates*/
	private FloatBuffer texCoordBuffer;
	/** Buffer for the indizes*/
	public ShortBuffer indexBuffer;

	/**
	 * Set the data of the vertex buffer
	 * @param type type of the vertices
	 * @param vertices array of vertices
	 */
	public void setData(Type type, Vector2[] vertices) {
		float[] data = new float[vertices.length * 2];
		for(int i = 0; i < vertices.length; i++)
		{
			data[i * 2] = vertices[i].x;
			data[i * 2 + 1] = vertices[i].y;
		}
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*4);
        vbb.order(ByteOrder.nativeOrder());
        if( type == Type.POSITION ) {
        	posBuffer = vbb.asFloatBuffer();
            posBuffer.put(data);
            posBuffer.position(0);        	
        }
        else if( type == Type.TEX_COORD ) {
        	texCoordBuffer = vbb.asFloatBuffer();
            texCoordBuffer.put(data);
            texCoordBuffer.position(0);        	
        }
	}
	
	
	/**
	 * Set the data of the vertex buffer
	 * @param vertices Array of vertices for the buffer
	 */
	public void setData(Short[] vertices)
	{		
		short[] data = new short[vertices.length];
		for(int i = 0; i < vertices.length; i++)
			data[i] = vertices[i];
	
		ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*2);
		vbb.order(ByteOrder.nativeOrder());
        indexBuffer = vbb.asShortBuffer();
        indexBuffer.put(data);
        indexBuffer.position(0);
	}
	
	
	/**
	 * Set the pointer of the buffer
	 * @param gl OpenGL context of android
	 */
	public void set(GL10 gl)
	{
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, posBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
	}
}
