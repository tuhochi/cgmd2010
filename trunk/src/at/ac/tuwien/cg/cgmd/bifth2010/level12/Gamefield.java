package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import javax.microedition.khronos.opengles.GL10;


public class Gamefield {
	private Field[] mFields = null;
	private int mXSegCount = -1;
	private int mYSegCount = -1;
	private float mSegLength = 1;
	private float[] mSegCol1  = {1.0f, 1.0f, 1.0f };
	private float[] mSegCol2  = {1.0f, 1.0f, 1.0f };
	
	
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
	}
	
	
	public void draw( GL10 gl){
		for( int i = 0; i < mFields.length; i++){
			mFields[i].draw(gl);
		}
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
		return (xf+yf-1);
	}


	public boolean getOccupied(float xpos, float ypos) {
		int f = getFieldCount( xpos, ypos );
		if( f >= mFields.length) return true;
		return mFields[ f ].getOccupied();
	}
	
}
