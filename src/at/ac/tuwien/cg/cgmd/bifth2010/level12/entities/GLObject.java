package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import javax.microedition.khronos.opengles.GL10;

public abstract class GLObject {
	protected FloatBuffer mVerticesBuffer = null;
	protected FloatBuffer mColorBuffer = null;
	protected ShortBuffer mIndicesBuffer = null;
	protected FloatBuffer mTextureBuffer = null;
	protected float[] mColor = {0.3f, 1.0f, 0.5f, 1.0f };
	protected int mIndicesCounter = -1;
	protected float mY = 0;
	protected float mX = 0;
	protected float mSpeed = 0;  //pixel/sec
	protected boolean mActive = false;
	protected int mTexture = 0;
	
    /** The initial texture coordinates (u, v) */	
    protected float mTexturePoints[] = {    		
    	 	0.0f, 1.0f,
			1.0f, 1.0f, 	
			0.0f, 0.0f,
			1.0f, 0.0f
	};
	
    
	public void draw( GL10 gl ){		
		gl.glFrontFace( GL10.GL_CCW );	
		if( mVerticesBuffer != null && mIndicesBuffer != null && mTextureBuffer != null) {
			gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
			gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, mVerticesBuffer);		
			gl.glColorPointer( 4, GL10.GL_FLOAT, 0, mColorBuffer );	
			TextureManager.getSingletonObject().setTexture(mTexture);
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
	
	
	public boolean getActiveState(){
		return mActive;
	}
	
	
	public void setActiveState( boolean state ){
		mActive = state;
	}
	
	
	public float getY(){
		return mY;
	}
	
	
	public float getX(){
		return mX;
	}
	
	
	public int getTexture(){
		return mTexture;
	}
	
	
}
