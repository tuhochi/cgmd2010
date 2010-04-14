package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class is an object representation of 
 * a Triangle containing the vertex information,
 * color information and drawing functionality, 
 * which is called by the renderer.
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Triangle {
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the colors */
//	private FloatBuffer colorBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
//	private ByteBuffer indexBuffer;
	
	/** Our texture pointer */
	public int texture;
	
	
	
	
	/** The initial color definition */
//	private float colors[] = {
//		    					0.0f, 0.0f, 0.0f, 1.0f, 
//		    					1.0f, 0.0f, 0.0f, 1.0f,
//		    					0.0f, 0.0f, 1.0f, 0.0f,
//		    					1.0f, 0.0f, 1.0f, 0.0f
//		    					};
	
	private float textureCoords[] = {    		
    		//Mapping coordinates for the vertices
    		0.0f, 1.0f,
    		1.0f, 1.0f,
    		0.0f, 0.0f,
    		1.0f, 0.0f
	};
	
	/** The initial indices definition */	
//    private byte indices[] = {
//    					//Faces definition
//			    		0,1,3, 0,3,2
//    };
	
	/**
	 * The Triangle constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Triangle(final int width, final int height) {
		
		/** The initial vertex definition */
		float vertices[] = {	0,   	0, 0, 		
							width,   	0, 0, 		
								0, height, 0,
							width, height, 0};
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		//
//		byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		colorBuffer = byteBuf.asFloatBuffer();
//		colorBuffer.put(colors);
//		colorBuffer.position(0);
		
		//
		byteBuf = ByteBuffer.allocateDirect(textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoords);
		textureBuffer.position(0);
		
		
		

		//
//		indexBuffer = ByteBuffer.allocateDirect(indices.length);
//		indexBuffer.put(indices);
//		indexBuffer.position(0);
	}

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {
		
		//Bind our only previously generated texture in this case
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
				
		//Draw the vertices as triangles
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public void updateUV(float f) {
		
		textureCoords[0] += f;
		textureCoords[3] += f;
		textureCoords[5] += f;
		textureCoords[7] += f;
		
//		float tuv[] = textureBuffer.array();
//		System.out.println(tuv);
	}
}
