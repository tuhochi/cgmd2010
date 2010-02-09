package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Color;
import android.test.IsolatedContext;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;

public class Cylinder {
	/**
	 *  the vertexbuffer
	 */

	private FloatBuffer mVertexBuffer;
	/**
	 *  the indexbuffer
	 */
	private ShortBuffer mIndexBuffer;

	/**
	 *  the texture of the surfaces
	 */
	private int mTextureId = -1;
	
	/**
	 * number of segments to approximate the cylinder 
	 */
	private short mSegments = 0;

	/**
	 *  texture coordinate buffer
	 */
	private FloatBuffer mTextureCoordinateBuffer;
	
	private Color4 mColor = null;
	
	private class Color4 {
		float mR = 0; 
		float mG = 0;
		float mB = 0;
		float mA = 0;
	};
	
	
	/**
	 * 
	 * @param radius The radius of the cylinder
	 * @param height The height of the cylinder
	 * @param segments The number of segments that are used to appriximate the top and bottom surface of the cylinder
	 */
	public Cylinder(float radius, float height, short segments)
	{
		
		// the vertices
		float vertices[] = null;
		// the indices for the top and bottom surface
		short[] indicesSurface = null;
		//the indices for the side wall
		short[] indicesSide = null;

		mSegments=segments;
		vertices = new float[(segments+1)*2*3];
		indicesSurface = new short[(segments+2)*2];
		
		indicesSide = new short[(segments+1)*2];

		//top middle vertex
		vertices[segments*2*3+0] = 0.f;
		vertices[segments*2*3+1] = 0.f;
		vertices[segments*2*3+2] = 0.5f*height;
		
		//bottom middle vertex
		vertices[(segments*2+1)*3+0] = 0.f;
		vertices[(segments*2+1)*3+1] = 0.f;
		vertices[(segments*2+1)*3+2] = -0.5f*height;
		
		for(short i=0; i<segments; i++) {
			float fD = (2.f*(float)Math.PI*(float)i) /(float)segments;
			float fX = (float) (radius * Math.cos(fD)); 
			float fY = (float) (radius * Math.sin(fD));
			vertices[i*3+0] = fX;
			vertices[i*3+1] = fY;
			vertices[i*3+2] = 0.5f*height;
			
			vertices[(segments+i)*3+0] = fX;
			vertices[(segments+i)*3+1] = fY;
			vertices[(segments+i)*3+2] = -0.5f*height;
		}

		
		
		for(short i=0; i<segments; i++) {
			indicesSurface[i+1] = i;
			indicesSurface[segments+i+3] = (short) (segments+i);
		}
		

		//indices to the middle vertices
		indicesSurface[0] = (short) (segments*2);
		indicesSurface[segments+2] = (short) (segments*2+1);

		//indices to close the surfaces
		indicesSurface[segments+1] = 0;
		indicesSurface[segments*2+3] = segments;
		
		for(short i=0; i<segments; i++){
			indicesSide[i*2]=i;
			indicesSide[i*2+1]= (short) (segments+i);
		}
		
		indicesSide[segments*2]=0;
		indicesSide[segments*2+1]=segments;
	
		// float is 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		// short is 2 bytes
		ByteBuffer ibb = ByteBuffer.allocateDirect((indicesSurface.length+indicesSide.length) * 2 );
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		mIndexBuffer.put(indicesSurface, 0, indicesSurface.length);
		mIndexBuffer.put(indicesSide, 0, indicesSide.length);
		mIndexBuffer.position(0);
	};

	public void setColor(float r, float g, float b, float a)
	{
		if(mColor==null){
			mColor = new Color4(); 
		}
		mColor.mR = r;				
		mColor.mG = g;
		mColor.mB = b;
		mColor.mA = a;
	}
	
	public void deleteColor(){
		mColor = null;
	}


	/**
	 * drawing the object
	 * @param gl
	 */

	public void draw(GL10 gl) {
		//back face culling 
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		
		//enable the buffer of vertices
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//set the vertex pointer format and offset 
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		
		if(mTextureId>=0){
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
			//enable the texture coordinates
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			//set the texture coordinate format and offset
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureCoordinateBuffer);
			gl.glColor4f(1, 1, 1, 1);
			
		} else {
			gl.glDisable(GL10.GL_TEXTURE_2D);
			if(mColor!=null){
				gl.glColor4f(mColor.mR, mColor.mG, mColor.mB, mColor.mA);
			}
		}
		
		//specify counter clockwise order
		gl.glFrontFace(GL10.GL_CW);
		
		//call the bottom surface
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, mSegments+2, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.position(mSegments+2));
		
		//specify counter clockwise order
		gl.glFrontFace(GL10.GL_CCW);

		//draw the top surface
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, mSegments+2, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.position(0));
		gl.glDisable(GL10.GL_TEXTURE_2D);
				
				
		if(mTextureId>=0){
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		
		if(mColor!=null){
			gl.glColor4f(mColor.mR, mColor.mG, mColor.mB, mColor.mA);
		}
		
		//draw the side walls 
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, (mSegments+1)*2, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.position((mSegments+2)*2));
		
		//disable the buffer
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		//disable back face culling
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	public void setTexture(GL10 gl, Resources cResources, int iId) {
		mTextureId = CommonFunctions.loadTexture(gl, cResources, iId);
		float[] mTextureCoordinates = new float[(mSegments+1)*2*2];
		
		//top middle vertex
		mTextureCoordinates[mSegments*2*2+0] = 0.5f;
		mTextureCoordinates[mSegments*2*2+1] = 0.5f;
		
		//bottom middle vertex
		mTextureCoordinates[(mSegments*2+1)*2+0] = 0.5f;
		mTextureCoordinates[(mSegments*2+1)*2+1] = 0.5f;
		
		for(short i=0; i<mSegments; i++) {
			float fD = (2.f*(float)Math.PI*(float)i) /(float)mSegments;
			float fX = (float) (Math.cos(fD)*0.5f)+0.5f; 
			float fY = (float) (Math.sin(fD)*0.5f)+0.5f;
			mTextureCoordinates[i*2+0] = fX;
			//flip the y coordinate here (since opengl's origin is at the bottom left)
			mTextureCoordinates[i*2+1] = 1.0f-fY;
			
			mTextureCoordinates[(mSegments+i)*2+0] = fX;
			//flip the y coordinate here (since opengl's origin is at the bottom left)
			mTextureCoordinates[(mSegments+i)*2+1] = 1.0f-fY;
		}

			
		ByteBuffer tcbb = ByteBuffer.allocateDirect(mTextureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		mTextureCoordinateBuffer = tcbb.asFloatBuffer();
		mTextureCoordinateBuffer.put(mTextureCoordinates);
		mTextureCoordinateBuffer.position(0);
	}



}
