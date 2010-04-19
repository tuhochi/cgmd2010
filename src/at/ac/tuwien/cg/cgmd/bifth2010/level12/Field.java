package at.ac.tuwien.cg.cgmd.bifth2010.level12;



import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.GLObject;


public class Field extends GLObject{
	private boolean mOccupied = false;
	
	public Field( float xstart, float ystart, float xend, float yend ){
		mX = ( xend - xstart ) / 2 + xstart;
		mY = ( yend - ystart ) / 2 + ystart;
		float[] vertices = {
				xstart,	ystart,	0.0f,
				xend,	ystart,	0.0f,
				xend,	yend,	0.0f,
				xstart,	yend,	0.0f,
		};
		
		ByteBuffer vertbuf = ByteBuffer.allocateDirect(vertices.length * 4);
		vertbuf.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = vertbuf.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position( 0 );
		
		short[] points = { 
				0, 1, 2, 
				0, 2, 3,
				};
		
		ByteBuffer indbuf = ByteBuffer.allocateDirect(points.length * 2);
		mIndicesCounter = points.length;
		indbuf.order( ByteOrder.nativeOrder());
		mIndicesBuffer = indbuf.asShortBuffer();
		mIndicesBuffer.put(  points );
		mIndicesBuffer.position(0);
		
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		//mColor = colors;

		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
	}
	

	
	public float[] getMiddle() { 
		float[] xyMid = { mX, mY };
		return xyMid;
	}
	
	
	public void setOccupied( boolean v ){
		mOccupied = v;
	}
	
	
	public boolean getOccupied(){
		return mOccupied;
	}
	
	public void setColor( float r, float g, float b){
		mColor[0]=r;
		mColor[1]=g;
		mColor[2]=b;
		
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		//mColor = colors;

		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		
	}
	
}
