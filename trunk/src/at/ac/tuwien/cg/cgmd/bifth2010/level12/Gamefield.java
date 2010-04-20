package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import javax.microedition.khronos.opengles.GL10;

public class Gamefield {
	private Field[] mFields = null;
	private Field[] mLastFields = null;
	private int mXSegCount = -1;
	private int mYSegCount = -1;
	private float mSegLength = 1;
	private float mLastFieldLength = 5.0f;
	private float[] mSegCol1  = {0.8f, 1.0f, 0.2f };
	private float[] mSegCol2  = {0.0f, 0.8f, 0.0f };
	private float[] mBorderCol = { 0.5f, 0.5f, 0.0f };
	
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
					f = new Field( x*mSegLength, y*mSegLength, (x+1)*mSegLength, (y+1)*mSegLength );
					f.setColor(mSegCol1[0], mSegCol1[1], mSegCol1[2]);
					fieldone = false;
				}
				else{
					f = new Field( x*mSegLength, y*mSegLength, (x+1)*mSegLength, (y+1)*mSegLength );
					f.setColor(mSegCol2[0], mSegCol2[1], mSegCol2[2]);
					fieldone = true;
				}
				mFields[fieldcounter] = f;
				fieldcounter++;
			}
		}
		
		//Abgrenzungsfelder
		mLastFields = new Field[ mXSegCount ];
		for( int i = 0; i < mLastFields.length; i++){
			mLastFields[i] = new Field( mXSegCount * mSegLength, i * mSegLength, (mXSegCount * mSegLength + mLastFieldLength), (i+1)*mSegLength );
			mLastFields[i].setColor( 1.0f, 1.0f, 0.0f);
		}
	}
	
	
	public void draw( GL10 gl ){
		for( int i = 0; i < mFields.length; i++){
			mFields[i].draw(gl);
		}
		for( int i = 0; i < mLastFields.length; i++ ){
			mLastFields[i].draw(gl);
		}
	}


	public float[] correctXYpos(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		return mFields[ f ].getMiddle();
	}


	public void setFieldOccupied(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		mFields[ f ].setOccupied(true);
	}
	
	public int getFieldCount( float xpos, float ypos ){
		int xf = (int)(Math.abs(xpos / mSegLength));
		int yf = (int)(Math.abs(ypos / mSegLength)) * mXSegCount;
		return (xf+yf);
	}


	public boolean getOccupied(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		return mFields[ f ].getOccupied();
	}
	
}
