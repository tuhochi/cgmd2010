package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class RenderableQuad {

	/** VBO for  vertices*/
	private FloatBuffer vertexBuffer;
	/** VBO for  texture coordinates*/
	private FloatBuffer texCoordBuffer;
	/** VBO for  indeces*/
	private ShortBuffer indexBuffer;
	
	/**
	 * Creates a new quad
	 * @param width The width of the quad
	 * @param height The height of the quad
	 */
    public RenderableQuad(float width, float height)
    {

    	float vertices[] = {
    			 -width / 2.0f,  -height / 2.0f, 0,
    			width / 2.0f,  -height / 2.0f, 0,
    			width / 2.0f,  height / 2.0f, 0,
    			-width / 2.0f,  height / 2.0f, 0
    	};
    	
    	float texCoords[] = {
    			0, 	0, 
    			1.0f, 	0, 
    			1.0f, 	1.0f, 
    			0, 	1.0f
    	};


    	short indices[] = {
    			3, 1, 0,    3, 2, 1
    	};
    	
    
    	
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(texCoords.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		texCoordBuffer = byteBuf.asFloatBuffer();
		texCoordBuffer.put(texCoords);
		texCoordBuffer.position(0);	

		byteBuf = ByteBuffer.allocateDirect(indices.length*2);
		byteBuf.order(ByteOrder.nativeOrder());
		indexBuffer = byteBuf.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);	
		
    }

    public void draw(GL10 gl)
    {
    
    	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
    	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
    
    	gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
