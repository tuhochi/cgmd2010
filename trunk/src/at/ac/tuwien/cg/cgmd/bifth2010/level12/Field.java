package at.ac.tuwien.cg.cgmd.bifth2010.level12;



import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.GLObject;


public class Field extends GLObject{
	private boolean mOccupied = false;
	
	private float mXStart, mYStart, mXEnd, mYEnd;
	
	
	public Field( int xstart, int ystart, int xend, int yend ){
		mXStart = xstart;
		mYStart = ystart;
		mXEnd = xend;
		mYEnd = yend;
		mX = ( xend - xstart ) / 2 + xstart;
		mY = ( yend - ystart ) / 2 + ystart;
	   	TextureManager.getSingletonObject().add(R.drawable.l12_grass);
    	TextureManager.getSingletonObject().add(R.drawable.l12_road);		
    	TextureManager.getSingletonObject().add(R.drawable.l12_house);
	}
	
	public void initVBOs(){
		float[] vertices = {
				mXStart,	mYStart,	0.0f,
				mXEnd,		mYStart,	0.0f,
				mXStart,	mYEnd,		0.0f,
				mXEnd,		mYEnd,		0.0f,				
		};
		
		ByteBuffer vertbuf = ByteBuffer.allocateDirect(vertices.length * 4);
		vertbuf.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = vertbuf.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position( 0 );
		
		short[] points = {
				0,1,3,
				0,3,2,
		};

		
		ByteBuffer indbuf = ByteBuffer.allocateDirect(points.length * 2);
		mIndicesCounter = points.length;
		indbuf.order( ByteOrder.nativeOrder());
		mIndicesBuffer = indbuf.asShortBuffer();
		mIndicesBuffer.put(  points );
		mIndicesBuffer.position(0);
		
		float[] colors = { mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3]};

		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put( mTexturePoints );
		mTextureBuffer.position(0);
	}
	

	
	public int[] getMiddle() { 
		int[] xyMid = { mX, mY };
		return xyMid;
	}
	
	
	public void setOccupied( boolean v ){
		mOccupied = v;
	}
	
	
	public boolean getOccupied(){
		return mOccupied;
	}
	
	public void setColor( float r, float g, float b, int texture){
		mColor[0]=r;
		mColor[1]=g;
		mColor[2]=b;
		
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		mColor = colors;
		if( texture == 0 ) mTexture = R.drawable.l12_grass;
		if( texture == 1 ) mTexture = R.drawable.l12_grass;
	}
	
	public void draw( GL10 gl){
		TextureManager.getSingletonObject().setTexture( mTexture);
		super.draw(gl);
	}
	
}
