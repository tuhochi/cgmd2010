package at.ac.tuwien.cg.cgmd.bifth2010.level12;



import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.GLObject;

/**
 * one field of the gamefield
 * @see Gamefield
 * @see GLObject
 */
public class Field extends GLObject{
	private boolean mOccupied = false; /** is a tower on this field= */
	private float mXStart;  /** x coordinate of the first quad point */
	private float mYStart;  /** y coordinate of the first quad point */ 
	private float mXEnd;  /** x coordinate of the last quad point */
	private float mYEnd; /** y coordinate of the last quad point */
	
	/**
	 * Constructor of the field, setting the coordinates from which the VBOs are created, adding textures needed
	 * @param xstart x coordinate of the first quad point
	 * @param ystart y coordinate of the first quad point
	 * @param xend x coordinate of the last quad point
	 * @param yend y coordinate of the last quad point
	 */
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
	
	/**
	 * creates the opengl VBOs
	 */
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
	

	/**
	 * returns the center of the field
	 * @return int[] x and y coordinate
	 */
	public int[] getMiddle() { 
		int[] xyMid = { mX, mY };
		return xyMid;
	}
	
	/**
	 * sets if the field is occupied or not
	 * @param v occupied or not
	 */
	public void setOccupied( boolean v ){
		mOccupied = v;
	}
	
	/**
	 * delivers if the field is occupied by a tower
	 * @return boolean the occupation
	 */
	public boolean getOccupied(){
		return mOccupied;
	}
	
	/**
	 * sets the color of the field and the textures
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param texture sample id
	 */
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
	
	/**
	 * draws the field after setting its texture active
	 */
	public void draw( GL10 gl){
		TextureManager.getSingletonObject().setTexture( mTexture);
		super.draw(gl);
	}
	
}
