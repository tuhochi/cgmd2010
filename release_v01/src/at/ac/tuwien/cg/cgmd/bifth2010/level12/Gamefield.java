package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.GLObject;

/**
 * Responsible for the Gamefield
 * @see GLObject
 */
public class Gamefield extends GLObject{
	private Field[] mFields = null; /** array holding the fields */
	private int mXSegCount = -1; /** amount of fields in the x direction */
	private int mYSegCount = -1; /** amount of fields in the y direction */
	private float mSegLength = 1; /** lenght of a fields */
	private float[] mSegCol1  = {1.0f, 1.0f, 1.0f }; /** color of one segment */
	private float[] mSegCol2  = {1.0f, 1.0f, 1.0f }; /** color of another segment */
	int[] res;
	
	public void initVBOs(){
		float[] vertices = {
				-0.5f, -0.5f, 0.5f,  //Vertex 0
				 0.5f, -0.5f, 0.5f,  //v1
				-0.5f,  0.5f, 0.5f,  //v2
				 0.5f,  0.5f, 0.5f,  //v3			
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
	
	
	public Gamefield( int xSegCount, int ySegCount, float segLength){		
		mXSegCount = xSegCount;
		mYSegCount = ySegCount;
		mFields = new Field[ xSegCount * ySegCount ];	
		mSegLength = segLength;
		int fieldcounter = 0;
		boolean fieldone = true;
		for( int y = 0; y < mYSegCount; y++){
			if( fieldone) fieldone = false;
			else fieldone = true;
			for( int x = 0; x < mXSegCount; x++ ){
				Field f;
				if( fieldone) {
					f = new Field( (int)Math.abs(x*mSegLength), (int)Math.abs(y*mSegLength), (int)Math.abs((x+1)*mSegLength), (int)Math.abs((y+1)*mSegLength) );
					f.setColor(mSegCol1[0], mSegCol1[1], mSegCol1[2], 0);
					f.initVBOs();
					fieldone = false;
					if( x*mSegLength > mXSegCount * mSegLength * 0.6 ) f.setOccupied(true);
				}
				else{
					f = new Field( (int)Math.abs(x*mSegLength), (int)Math.abs(y*mSegLength), (int)Math.abs((x+1)*mSegLength), (int)Math.abs((y+1)*mSegLength) );
					f.setColor(mSegCol2[0], mSegCol2[1], mSegCol2[2], 1);
					f.initVBOs();
					fieldone = true;
					if( x*mSegLength > mXSegCount * mSegLength * 0.6 ) f.setOccupied(true);
				}
				mFields[fieldcounter] = f;
				fieldcounter++;
			}
		}
	}
	
	/**
	 * (re)creates the VBOs
	 */
	public void onResume(){
		for( int i = 0; i < mFields.length; i++) mFields[i].initVBOs();
		this.initVBOs();
	}
	
	/**
	 * draws the gamefield 
	 */
	public void draw( GL10 gl){
		for( int i = 0; i < mFields.length; i++){
			mFields[i].draw(gl);
		}
		res = GameWorld.getSingleton().getRes();
		//gl.glTranslatef(res[0]*0.5f, res[1]*0.5f, 0.0f);
		//gl.glScalef(res[0], res[1], 1.0f);
		gl.glTranslatef(res[0]-res[0]*0.4f*0.5f, res[1]*0.5f, 0.0f);
		gl.glScalef(res[0]*0.4f, res[1], 1.0f);
		TextureManager.getSingletonObject().setTexture(R.drawable.l12_road);
		super.draw(gl);
		gl.glLoadIdentity();
		gl.glTranslatef(res[0]*0.4f*0.5f, res[1]*0.5f, 0.0f);
		gl.glScalef(res[0]*0.4f, res[1], 1.0f);
		TextureManager.getSingletonObject().setTexture(R.drawable.l12_house);
		super.draw(gl);
		gl.glLoadIdentity();
	}

	/** 
	 * corrects a given point to the center of the field in which the point lies
	 * @param xpos x-coordinate of the point
	 * @param ypos y-coordinate of the point
	 * @return int[] the x/y coordinate of the center
	 */
	public int[] correctXYpos(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		return mFields[ f ].getMiddle();
	}

	/** 
	 * sets the field occupied in which the given x/y coordinate lies
	 * @param xpos x-coordinate of the point of the field to set to occupied
	 * @param ypos y-coordinate of the point of the field to set to occupied
	 */
	public void setFieldOccupied(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		mFields[ f ].setOccupied(true);
	}
	
	/**
	 * delivers the number of the field in the array of a given coordinate values
	 * @param xpos 
	 * @param ypos
	 * @return int the fieldcount
	 */
	public int getFieldCount( float xpos, float ypos ){
		int xf = (int)(Math.floor(xpos / mSegLength));
		int yf = (int)(Math.floor(ypos / mSegLength)) * mXSegCount;
		//System.out.println("xpos: "+xpos+" ypos: "+ypos+" xf: "+xf+" yf: "+yf+" f: "+(xf+yf)+" fmax: "+mFields.length);
		if( (xf + yf)>mFields.length) return mFields.length-1;
		return (xf+yf);
	}

	/**
	 * returns if the field in which coordinates lies is occupied or not
	 * @param xpos
	 * @param ypos
	 * @return boolean occupied or not
	 */
	public boolean getOccupied(int xpos, int ypos) {
		int f = getFieldCount( xpos, ypos );
		if( f >= mFields.length) return true;
		return mFields[ f ].getOccupied();
	}


	/**
	 * sets the field in which the given coordinates lie to unoccupied
	 * @param xpos
	 * @param ypos
	 */
	public void setFieldUnOccupied(int xpos, int ypos) {
		int f = getFieldCount( xpos, ypos );
		if( f >= mFields.length) return;
		mFields[ f ].setOccupied( false );
	}
	
}
