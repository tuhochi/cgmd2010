package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.GLObject;


public class Gamefield extends GLObject{
	private Field[] mFields = null;
	private int mXSegCount = -1;
	private int mYSegCount = -1;
	private float mSegLength = 1;
	private float[] mSegCol1  = {1.0f, 1.0f, 1.0f };
	private float[] mSegCol2  = {1.0f, 1.0f, 1.0f };
	
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
	
	public void onResume(){
		for( int i = 0; i < mFields.length; i++) mFields[i].initVBOs();
		this.initVBOs();
	}
	
	
	public void draw( GL10 gl){
		for( int i = 0; i < mFields.length; i++){
			mFields[i].draw(gl);
		}
		int[] res = GameWorld.getSingleton().getRes();
		//gl.glTranslatef(res[0]*0.5f, res[1]*0.5f, 0.0f);
		//gl.glScalef(res[0], res[1], 1.0f);
		gl.glTranslatef(res[0]-res[0]*0.1f*0.5f, res[1]*0.5f, 0.0f);
		gl.glScalef(res[0]*0.1f, res[1], 1.0f);
		TextureManager.getSingletonObject().setTexture(R.drawable.l12_road);
		super.draw(gl);
		gl.glLoadIdentity();
		gl.glTranslatef(res[0]*0.135f*0.5f, res[1]*0.5f, 0.0f);
		gl.glScalef(res[0]*0.135f, res[1], 1.0f);
		TextureManager.getSingletonObject().setTexture(R.drawable.l12_house);
		super.draw(gl);
		gl.glLoadIdentity();
	}


	public int[] correctXYpos(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		return mFields[ f ].getMiddle();
	}


	public void setFieldOccupied(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		mFields[ f ].setOccupied(true);
	}
	
	public int getFieldCount( float xpos, float ypos ){
		int xf = (int)(Math.floor(xpos / mSegLength));
		int yf = (int)(Math.floor(ypos / mSegLength)) * mXSegCount;
		System.out.println("xpos: "+xpos+" ypos: "+ypos+" xf: "+xf+" yf: "+yf+" f: "+(xf+yf)+" fmax: "+mFields.length);
		if( (xf + yf)>mFields.length) return mFields.length-1;
		return (xf+yf);
	}


	public boolean getOccupied(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		if( f >= mFields.length) return true;
		return mFields[ f ].getOccupied();
	}
	
}
