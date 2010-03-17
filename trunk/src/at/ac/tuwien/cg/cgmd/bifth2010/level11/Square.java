package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * This class is an object representation of 
 * a Square containing the vertex information,
 * texture coordinates, the vertex indices
 * and drawing functionality.
 * 
 * @author Albors
 */
public class Square {

	/** Buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** Buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** Buffer holding the indices */
	private ByteBuffer indexBuffer;

	/**
	 * The initial vertex definition
	 * 
	 * Vertices have to be defined 
	 * for proper texturing.
	 */	
    private float vertices[] = {
		// vertices according to faces
		-0.5f, -0.5f, 0.0f,  //Vertex 0
		 0.5f, -0.5f, 0.0f,  //v1
		-0.5f,  0.5f, 0.0f,  //v2
		 0.5f,  0.5f, 0.0f,  //v3
	};
    
    /** The initial texture coordinates (u, v) */	
    private float texture[] = {    		
		// mapping coordinates for the vertices
		0.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 0.0f,
		1.0f, 1.0f, 
	};
        
    /** The initial indices definition */	
    private byte indices[] = {
		// faces definition
		0,1,3, 0,3,2,
	};

	/**
	 * The Square constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Square() {
	
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - the GL context
	 */
	public void draw(GL10 gl) {
		
		//Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Enable the vertex and texture state
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
