package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;


import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * skeleton from which every drawn object is inherited
 */
public abstract class GLObject {
	protected FloatBuffer mVerticesBuffer = null; /** the vertices buffer for opengl */
	protected FloatBuffer mColorBuffer = null;  /** the color buffer for opengl */
	protected ShortBuffer mIndicesBuffer = null;  /** the indices buffer for opengl */
	protected FloatBuffer mTextureBuffer = null;  /** the texture buffer for opengl */
	protected float[] mColor = {1.0f, 1.0f, 1.0f, 1.0f };  /** color array */
	protected int mIndicesCounter = -1;  /** counter of the indices */
	protected int mY = 0;  /** x-coordinate of the center */
	protected int mX = 0;  /** y-coordinate of the center */
	protected boolean mActive = false; /** will be drawn or not */
	protected int mTexture = 0; /** the texture sample to draw on this object */
	
    /** The initial texture coordinates (u, v) */	
    protected float mTexturePoints[] = {    		
    	 	0.0f, 1.0f,
			1.0f, 1.0f, 	
			0.0f, 0.0f,
			1.0f, 0.0f
	};
	
    /** draws the object with or without texture */
	public void draw( GL10 gl ){		
		gl.glFrontFace( GL10.GL_CCW );	
		if( mVerticesBuffer != null && mIndicesBuffer != null && mTextureBuffer != null) {
			gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
			gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, mVerticesBuffer);		
			gl.glColorPointer( 4, GL10.GL_FLOAT, 0, mColorBuffer );	
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			//TextureManager.getSingletonObject().setTexture(mTexture);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
			
			gl.glDrawElements(GL10.GL_TRIANGLES, mIndicesCounter, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
			gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		else if( mVerticesBuffer != null && mIndicesBuffer != null) {
			gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
			gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
			
			gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, mVerticesBuffer);		
			gl.glColorPointer( 4, GL10.GL_FLOAT, 0, mColorBuffer );
			
			gl.glDrawElements(GL10.GL_TRIANGLES, mIndicesCounter, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer);
			
			gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
			gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
		}
	}
	
	/** returns of the object is drawn or not */
	public boolean getActiveState(){
		return mActive;
	}
	
	/** sets if the object is to be drawn or not */
	public void setActiveState( boolean state ){
		mActive = state;
	}
	
	/** 
	 * returns the center of the object to be drawed
	 * @return int the y-coordinate of the center
	 */
	public int getY(){
		return mY;
	}
	
	/** 
	 * returns the center of the object to be drawed
	 * @return int the x-coordinate of the center
	 */
	public int getX(){
		return mX;
	}
	
	
	/** 
	 * returns the texture sample id
	 * @return int the sample id
	 */
	public int getTexture(){
		return mTexture;
	}
	
	
}
