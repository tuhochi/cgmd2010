package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;

public class Rectangle {
	// texture coordinates
	private FloatBuffer mVertexBuffer;
	// index buffer
	private ShortBuffer mIndexBuffer;
	// texture coordinate buffer
	private FloatBuffer mTextureCoordinateBuffer;
	
	private int mTextureId = -1;
	private float mWidth = 0;
	private float mHeight = 0;



	public Rectangle(float fWidth, float fHeight)
	{
		mWidth = fWidth;
		mHeight = fHeight;
		float[] vertices = new float[12];
		//bottom left
		vertices[0] = fWidth * -0.5f;
		vertices[1] = fHeight * -0.5f;
		vertices[2] = 0.f;
		//bottom right
		vertices[3] = fWidth * 0.5f;
		vertices[4] = fHeight * -0.5f;
		vertices[5] = 0.f;
		//top right
		vertices[6] = fWidth * 0.5f;
		vertices[7] = fHeight * 0.5f;
		vertices[8] = 0.f;
		//top left
		vertices[9] = fWidth * -0.5f;
		vertices[10]= fHeight * 0.5f;
		vertices[11]= 0.f;
		
		
		// float is 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		// indices
		short[] indices = { 0, 1, 2, 0, 2, 3 };

		// short is 2 bytes
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		mIndexBuffer.put(indices);
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
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		//disable the buffer
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		//disable back face culling
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	public void setTexture(GL10 gl, Resources cResources, int iId) {
		mTextureId = CommonFunctions.loadTexture(gl, cResources, iId);
		if(mTextureId>=0) {
			initTextureCoordinateBuffer();
		}
	}
	
	public void setTexture(GL10 gl, Bitmap bm) {
		mTextureId = CommonFunctions.loadTexture(gl, bm);
		if(mTextureId>=0) {
			initTextureCoordinateBuffer();
		}
	}

	
	private void initTextureCoordinateBuffer() {
		float[] textureCoordinates = new float[8];
		//flipping the y-coordinate here
		//bottom left
		textureCoordinates[0] = 0.f;
		textureCoordinates[1] = 1.f;
		//bottom right
		textureCoordinates[2] = 1.f;
		textureCoordinates[3] = 1.f;
		//top right
		textureCoordinates[4] = 1.f;
		textureCoordinates[5] = 0.f;
		//top left
		textureCoordinates[6] = 0.f;
		textureCoordinates[7] = 0.f;
		
		ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		mTextureCoordinateBuffer = tcbb.asFloatBuffer();
		mTextureCoordinateBuffer.put(textureCoordinates);
		mTextureCoordinateBuffer.position(0);
	}



	public float getHeight() {
		
		return mHeight;
	}



	
}
