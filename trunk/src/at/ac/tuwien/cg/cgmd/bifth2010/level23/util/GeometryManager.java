package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES11;

/**
 * The Class GeometryManager manages all geometry 
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class GeometryManager {

	/** The geometry manager. */
	private static GeometryManager geometryManager; 
	
	
	/**
	 * Instantiates a new geometry manager.
	 */
	public GeometryManager() {
		geometryManager = this; 
	}
	
	/**
	 * Creates the vertex buffer as a quad.
	 *
	 * @param width the width
	 * @param height the height
	 * @return the float buffer
	 */
	public FloatBuffer createVertexBufferQuad(float width, float height)
	{
		FloatBuffer buffer;
		
		
		float[] vertices = new float[12];
		
		//origin = 0 0
		//bottom left
		vertices[0] = 0f;
		vertices[1] = 0f;
		vertices[2] = 0f;
		//bottom right
		vertices[3] = width;
		vertices[4] = 0f;
		vertices[5] = 0f;
		//top left
		vertices[6] = 0f;
		vertices[7]= height;
		vertices[8]= 0f;
		//top right
		vertices[9] = width;
		vertices[10] = height;
		vertices[11] = 0f;
				
		ByteBuffer vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		buffer = vertexBBuffer.asFloatBuffer();
		buffer.put(vertices);
		buffer.position(0);	
		
		return buffer;
	}
	
	
	/**
	 * Creates the texture coordinate buffer mapped to a quad.
	 *
	 * @return the float buffer
	 */
	public FloatBuffer createTexCoordBufferQuad()
	{
		FloatBuffer texCoordBuffer; 
		
		float[] textureCoordinates = new float[8];
		//flipping the y-coordinate here
		//bottom left
		textureCoordinates[0] = 0.f;
		textureCoordinates[1] = 1.f;
		//bottom right
		textureCoordinates[2] = 1.f;
		textureCoordinates[3] = 1.f;
		//top left
		textureCoordinates[4] = 0.f;
		textureCoordinates[5] = 0.f;
		//top right
		textureCoordinates[6] = 1.f;
		textureCoordinates[7] = 0.f;
				
		ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		texCoordBuffer = tcbb.asFloatBuffer();
		texCoordBuffer.put(textureCoordinates);
		texCoordBuffer.position(0);
		
		return texCoordBuffer; 
		
	}
	
	/**
	 * Creates an array of texture coordinate buffers as quads.
	 *
	 * @param coordinates the coordinates for each quad
	 * @return the float buffer[]
	 */
	public FloatBuffer[] createTexCoordBufferQuad(float[] coordinates)
	{

		FloatBuffer[] texCoordBuffer = new FloatBuffer[coordinates.length / 8]; 
		float[] textureCoordinates = new float[8];
		
		for (int i=0; i< coordinates.length; i=i+8) {
			//bottom left
			textureCoordinates[0] = coordinates[i];
			textureCoordinates[1] = coordinates[i+1];
			//bottom right
			textureCoordinates[2] = coordinates[i+2];
			textureCoordinates[3] = coordinates[i+3];
			//top left
			textureCoordinates[4] = coordinates[i+4];
			textureCoordinates[5] = coordinates[i+5];
			//top right
			textureCoordinates[6] = coordinates[i+6];
			textureCoordinates[7] = coordinates[i+7];
					
			ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
			tcbb.order(ByteOrder.nativeOrder());
			texCoordBuffer[i/8] = tcbb.asFloatBuffer();
			texCoordBuffer[i/8].put(textureCoordinates);
			texCoordBuffer[i/8].position(0);
		}
		
		
		return texCoordBuffer; 
	}
	
		/**
		 * Creates the vertex buffer object for the given buffer.
		 *
		 * @param vertexBuffer the vertex buffer
		 * @param texCoordBuffer the texture coordinate buffer
		 * @return the unique vbo id 
		 */
		public int createVBO(FloatBuffer vertexBuffer, FloatBuffer texCoordBuffer) {
			
			int vboId; 
			int[] ids = new int[1];
			GLES11.glGenBuffers(1, ids, 0);
			vboId = ids[0];
			
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
			int size = (vertexBuffer.capacity()+texCoordBuffer.capacity())* 4; //4*3 Vertices, 4*2 Texcoords, 4 Byte je Float
			GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, size, null, GLES11.GL_STATIC_DRAW);
			
			GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, 0, vertexBuffer.capacity()*4, vertexBuffer);
			GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, texCoordBuffer.capacity()*4, texCoordBuffer);
			
			
			//GLES11.glBufferSubData(GLES11.GL_ELEMENT_ARRAY_BUFFER, (vertexBuffer.capacity() + texCoordBuffer.capacity())*4, indexBuffer.capacity()*2, indexBuffer);
		
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, 0);
			
			return vboId; 
		}
	
	/**
	 * Gets the singleton of GeometryManager.
	 *
	 * @return singleton of GeometryManager
	 */
	public static GeometryManager getInstance() {
		if (geometryManager == null) 
			geometryManager = new GeometryManager(); 
		
		return geometryManager; 
	}
}
