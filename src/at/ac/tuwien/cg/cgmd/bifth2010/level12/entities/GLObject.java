package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public abstract class GLObject {
	protected FloatBuffer mVerticesBuffer = null;
	protected ShortBuffer mIndicesBuffer = null;
	protected float[] mColor = {0.3f, 1.0f, 0.5f, 1.0f };
	protected int mIndicesCounter = -1;
	protected float mY = 0;
	protected float mX = 0;
	protected float mSpeed = 0;  //pixel/sec
	private boolean mActive = false;
	private float mCollisionPointX = -1;
	
	
	public void draw( GL10 gl ){
		gl.glFrontFace( GL10.GL_CCW );
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		if( mVerticesBuffer != null && mIndicesBuffer != null) {
			gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, mVerticesBuffer);
			gl.glColor4f( mColor[0], mColor[1], mColor[2], mColor[3]);
			gl.glDrawElements(GL10.GL_TRIANGLES, mIndicesCounter, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer);
		}
		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
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
	
	public float distanceX( float x ){
		return x-mX; //bei -? achtung!
	}
	
	public float collideX( GLObject obj ){
		float d = this.distanceX( obj.getX() );
		if((int)d == 0) Log.d("COL", "COLLISION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if (mSpeed == 0 ) return mX; //nicht bewegendes Object, Kollisionspunkt immer da wo es steht
		float sec = d / mSpeed;
		float sec2 = sec * 0.5f;
		return mX + (sec2*mSpeed); //kollisionspunkt
	}
	
	public void setCollisionPointX( float x ){
		mCollisionPointX = x;
	}
	
	public float getCollisionPointX(){
		return mCollisionPointX;
	}
}
