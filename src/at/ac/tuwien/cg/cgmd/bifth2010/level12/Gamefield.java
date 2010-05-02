package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Gamefield {
	private Field[] mFields = null;
	private Field[] mLastFields = null;
	private int mXSegCount = -1;
	private int mYSegCount = -1;
	private float mSegLength = 1;
	private float mLastFieldLength = 5.0f;
	private float[] mSegCol1  = {1.0f, 0.0f, 0.0f };
	private float[] mSegCol2  = {1.0f, 0.0f, 0.0f };
	
	
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
					f.setColor(mSegCol1[0], mSegCol1[1], mSegCol1[2], 0);
					f.initVBOs();
					fieldone = false;
				}
				else{
					f = new Field( x*mSegLength, y*mSegLength, (x+1)*mSegLength, (y+1)*mSegLength );
					f.setColor(mSegCol2[0], mSegCol2[1], mSegCol2[2], 1);
					f.initVBOs();
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
			mLastFields[i].setColor( 1.0f, 1.0f, 0.0f, 3);
			mLastFields[i].setOccupied(true);
		}
		
		/*ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(colors.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
	    colorBuffer = byteBuf.asFloatBuffer();
	    colorBuffer.put(colors);
	    colorBuffer.position(0); */
	}
	
	public void onResume(){
		for( int i = 0; i < mFields.length; i++) mFields[i].initVBOs();
		for( int i = 0; i < mLastFields.length; i++) mLastFields[i].initVBOs();
	}
	
	
	public void draw( GL10 gl){
		for( int i = 0; i < mFields.length; i++){
			mFields[i].draw(gl);
		}
		for( int i = 0; i < mLastFields.length; i++ ){
			mLastFields[i].draw(gl);
		}
		
		/*gl.glTranslatef(mWidth*0.5f, mHeight*0.5f, 0.0f);
		gl.glScalef(mWidth, mHeight, 1.0f);
		//tex.setTexture(R.drawable.icon);
		//TextureManager.getSingletonObject().setTexture(R.drawable.l12_icon);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Enable the vertex and texture state
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(3, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glLoadIdentity();*/
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
