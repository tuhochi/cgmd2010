package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;

public class Circle {
	// the vertices
	private float mVertices[] = null;
	// the indices
	private short[] mIndices = null;
	// the vertexbuffer
	private FloatBuffer mVertexBuffer;
	// the indexbuffer
	private ShortBuffer mIndexBuffer;
	
	private int mTextureId = -1;


	public Circle(float fRadius, int iSegments)
	{
		 mVertices = new float[(iSegments+1)*3];
		 mIndices = new short[iSegments+2];
		 
		 mVertices[0] = 0.f;
		 mVertices[1] = 0.f;
		 mVertices[2] = 0.f;
		 mIndices[0] = 0;
		 
		for(int i=0; i<iSegments; i++) {
			float fD = (2.f*(float)Math.PI*(float)i) /(float)iSegments;
			float fX = (float) (fRadius * Math.cos(fD)); 
			float fY = (float) (fRadius * Math.sin(fD));
			mVertices[3+i*3] = fX;
			mVertices[4+i*3] = fY;
			mVertices[5+i*3] = 0.f;
			mIndices[i+1] = (short) (i+1);
		}
		
		mIndices[iSegments+1] = (short) 1;		
		
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
		
		if(mTextureId>=0){
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
		} else {
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		//enable the buffer of vertices
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//set the vertex pointer format and offset 
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		
		//call the draw method
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, mIndices.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		//disable the buffer
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		//disable back face culling
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	public void setTexture(GL10 gl, Resources cResources, int iId) {
		mTextureId = CommonFunctions.loadTexture(gl, cResources, iId);
	}
	
	

}
