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
	private float[] mSegCol1  = {0.8f, 1.0f, 0.2f };
	private float[] mSegCol2  = {0.0f, 0.8f, 0.0f };
	private float[] mBorderCol = { 0.5f, 0.5f, 0.0f };
	
	/** Buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** Buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** Buffer holding the indices */
	private ByteBuffer indexBuffer;
	/** Buffer holding the colors */
	private FloatBuffer colorBuffer;
	
	 private float vertices[] = {
				// vertices according to faces
				-0.5f, -0.5f, 0.5f,  //Vertex 0
				 0.5f, -0.5f, 0.5f,  //v1
				-0.5f,  0.5f, 0.5f,  //v2
				 0.5f,  0.5f, 0.5f,  //v3
			};
		    
		    /** The initial texture coordinates (u, v) */	
		    private float texture[] = {    		
				// mapping coordinates for the vertices
		    	 	0.0f, 1.0f,
					1.0f, 1.0f, 	
					0.0f, 0.0f,
					1.0f, 0.0f
			};
		        
		    /** The initial indices definition */	
		    private byte indices[] = {
				// faces definition
				0,1,3, 0,3,2,
			};

		    /** The initial color definition */	
		    private float colors[] = {
				// faces definition
				1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
			};
	
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
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
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
	    colorBuffer.position(0);
	    
	}
	
	
	public void draw( GL10 gl, float mWidth, float mHeight){
		for( int i = 0; i < mFields.length; i++){
			mFields[i].draw(gl);
		}
		for( int i = 0; i < mLastFields.length; i++ ){
			mLastFields[i].draw(gl);
		}
		
		gl.glTranslatef(mWidth*0.5f, mHeight*0.5f, 0.0f);
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
		gl.glLoadIdentity();
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
