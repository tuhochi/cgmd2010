package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;


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
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.5f;
		mXTranslate = 0.0f;
		mActive = false;
	}
	
	public void setXY( float x, float y){
		mX = x;
		mY = y;
		mXTranslate = 0.0f;
		mLastFrametime = System.currentTimeMillis();
		float[] vertices = {
				(mX - mRadius),	(mY - mRadius), 1.0f,
				(mX + mRadius),	(mY - mRadius), 1.0f,
				(mX - mRadius),	(mY + mRadius), 1.0f,
				(mX + mRadius),	(mY + mRadius), 1.0f
		};
		ByteBuffer v = ByteBuffer.allocateDirect( vertices.length * 4 );
		v.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = v.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position(0);
		
		short[] indices = {
				0,1,3,
				0,3,2,
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
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(texture);
		mTextureBuffer.position(0);
	}
	
	@Override
	public void draw( GL10 gl ){
		long ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		//pause
		if( GameMechanics.getGameMecanics().running() == false ) dt = 0;
		
		mLastFrametime = ms;
		double distance = this.getSpeed() * dt;
		mXTranslate += (float)distance;
		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( mXTranslate, 0.0f, 0.0f);
		super.draw(gl);
		gl.glPopMatrix();	
	}
	
	@Override
	public float getX(){
		return mXTranslate + mX; //returns the bullet real position because it gets translated and not moved
	}


	public short getDamage() {
		return mDmg;
	}
	
	public void reset(){
		mXTranslate = 0.0f;
		this.setActiveState(false);
	}
	
}
