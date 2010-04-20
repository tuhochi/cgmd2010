package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Projectile extends GLObject{
	
	private long mLastFrametime = -1;
	private short mDmg = 10;
	private float mXTranslate = 0.0f;
	private float mRadius = 4;
	
	public Projectile( float speed, short dmg ){
		mSpeed = speed;
		mDmg = dmg;
		mColor[0] = 0.0f;
		mColor[1] = 0.0f;
		mColor[2] = 0.0f;
		mColor[3] = 0.5f;
		mXTranslate = 0.0f;
	}
	
	public void setXY( float x, float y){
		//System.out.println("SETXY Projectile: X:"+x+" Y:"+y);
		mX = x;
		mY = y;
		mXTranslate = 0.0f;
		mLastFrametime = System.currentTimeMillis();
		float[] vertices = {
				(mX - mRadius),	(mY - mRadius), 1.0f,
				(mX + mRadius),	(mY - mRadius), 1.0f,
				(mX + mRadius),	(mY + mRadius), 1.0f,
				(mX - mRadius),	(mY + mRadius), 1.0f
		};
		ByteBuffer v = ByteBuffer.allocateDirect( vertices.length * 4 );
		v.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = v.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position(0);
		
		short[] indices = {
				0,	1,	2,
				0,	2,	3
		};

		ByteBuffer i = ByteBuffer.allocateDirect( indices.length * 2 );
		i.order( ByteOrder.nativeOrder() );
		mIndicesBuffer = i.asShortBuffer();
		mIndicesBuffer.put( indices );
		mIndicesBuffer.position(0);	
		mIndicesCounter = indices.length;
		
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		System.out.println("SETXY Projectile: mLastFrameTime: "+mLastFrametime+" SystemTime: "+System.currentTimeMillis());
	}
	
	@Override
	public void draw( GL10 gl ){
		getX();
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( mXTranslate, 0.0f, 0.0f);
		super.draw(gl);
		gl.glPopMatrix();
		if( mCollisionPointX <= mXTranslate && mCollisionPointX > -1 ){
			System.out.println("mCollPointX: "+mCollisionPointX+ " mXTranslate: "+mXTranslate);
			this.reset();
		}
	}
	
	@Override
	public float getX(){
		long ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		mLastFrametime = ms;
		double distance = this.getSpeed() * dt;
		//System.out.print("getX: mLastFrameTime: "+mLastFrametime+" SystemTime: "+System.currentTimeMillis()+" mXTranslateOld: "+mXTranslateOld+" mXTranslateNew: "+mXTranslateNew+" distance: "+distance);
		mXTranslate += (float)distance;
		//System.out.println(" mXTranslateNew: "+mXTranslateNew);
		return mXTranslate; //returns the bullet real position because it gets translated and not moved
	}


	public short getDamage() {
		return mDmg;
	}
	
	public void reset(){
		mXTranslate = 0.0f;
		this.setActiveState(false);
		mCollisionPointX = -1;
	}
	
}
