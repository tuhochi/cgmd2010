package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;

public class Rectangle {
	// vertices
	private float[] mVertices = new float[12];
	// indices
	private short[] mIndices = { 0, 1, 2, 0, 2, 3 };
	// texture coordinates
	private float[] mTextureCoordinates;	
	// vertex buffer
	private FloatBuffer mVertexBuffer;
	// index buffer
	private ShortBuffer mIndexBuffer;
	// texture coordinate buffer
	private FloatBuffer mTextureCoordinateBuffer;
	
	private int mTextureId = -1;



	public Rectangle(float fWidth, float fHeight)
	{
		//bottom left
		mVertices[0] = fWidth * -0.5f;
		mVertices[1] = fHeight * -0.5f;
		mVertices[2] = 0.f;
		//bottom right
		mVertices[3] = fWidth * 0.5f;
		mVertices[4] = fHeight * -0.5f;
		mVertices[5] = 0.f;
		//top right
		mVertices[6] = fWidth * 0.5f;
		mVertices[7] = fHeight * 0.5f;
		mVertices[8] = 0.f;
		//top left
		mVertices[9] = fWidth * -0.5f;
		mVertices[10]= fHeight * 0.5f;
		mVertices[11]= 0.f;
		
		
		// float is 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(mVertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(mVertices);
		mVertexBuffer.position(0);

		// short is 2 bytes
		ByteBuffer ibb = ByteBuffer.allocateDirect(mIndices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		mIndexBuffer.put(mIndices);
		mIndexBuffer.position(0);
	};


	
	/**
	 * drawing the sqaure
	 * @param gl
	 */
	
	public void draw(GL10 gl) {
		//specify counter clockwise order
		gl.glFrontFace(GL10.GL_CCW);
		//back face culling 
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		//texturing
		if(mTextureId>=0){
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
		} else {
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		//enable the buffer of vertices
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//set the vertex pointer format and offset 
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		
		//enable the texture coordinates
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//set the texture coordinate format and offset
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureCoordinateBuffer);
		
		//call the draw method
		gl.glDrawElements(GL10.GL_TRIANGLES, mIndices.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		//disable the buffer
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		//disable back face culling
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	public void setTexture(GL10 gl, Resources cResources, int iId) {
		mTextureId = CommonFunctions.loadTexture(gl, cResources, iId);
		
		mTextureCoordinates = new float[8];
		//bottom left
		mTextureCoordinates[0] = 0.f;
		mTextureCoordinates[1] = 0.f;
		//bottom right
		mTextureCoordinates[2] = 1.f;
		mTextureCoordinates[3] = 0.f;
		//top right
		mTextureCoordinates[4] = 1.f;
		mTextureCoordinates[5] = 1.f;
		//top left
		mTextureCoordinates[6] = 0.f;
		mTextureCoordinates[7] = 1.f;
		
		ByteBuffer tcbb = ByteBuffer.allocateDirect(mTextureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		mTextureCoordinateBuffer = tcbb.asFloatBuffer();
		mTextureCoordinateBuffer.put(mTextureCoordinates);
		mTextureCoordinateBuffer.position(0);
		
	}

}
