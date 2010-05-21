package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import java.lang.System;



public abstract class Tower extends GLObject {
	protected short mRadius = -1;
	protected int mShootingInterval = 3000;//ms
	protected long mTimeLastProjectileShot = System.currentTimeMillis();
	protected Projectile[] mProjectiles = null;
	protected int mScreenWidth = 800;
	protected short mDmg = 10;
	
	
	
	public void setXY( int xCentr, int yCentr ){
		mX = xCentr;
		mY = yCentr;
		this.setActiveState(true);
		initVBOs();
	}
	
	public void initVBOs(){
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
		
		float[] colors = { mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3]};
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		
		mTimeLastProjectileShot = System.currentTimeMillis();
		for( int c = 0; c < mProjectiles.length; c++){
			mProjectiles[c].initVBOs();
		}
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(mTexturePoints);
		mTextureBuffer.position(0);
	}
	
	
	public void setViewPortLength(int width) {
		mScreenWidth = width;
	}
	
	
	
	@Override
	public void draw( GL10 gl ){	
		//pause
		if( GameMechanics.getSingleton().running() == false) mTimeLastProjectileShot = System.currentTimeMillis();
		if( this.getActiveState() == false ) return;
		long dt =(System.currentTimeMillis() - mTimeLastProjectileShot );
		if( dt >= mShootingInterval ){
			if( mProjectiles != null ){
				boolean found = false;
				for ( int  i = 0; i < mProjectiles.length && !found; i++){
					if( mProjectiles[i].getActiveState() == false ){
						found = true;
						mProjectiles[i].setActiveState( true );
						mProjectiles[i].setXY( this.getX(), mY);
						mTimeLastProjectileShot = System.currentTimeMillis();		
						break;
					}
				}
			}
		}
		for( int i = 0; i < mProjectiles.length; i++){
			if( mProjectiles[i].getActiveState() && ( mProjectiles[i].getX() >= mScreenWidth )){
					mProjectiles[i].reset();
			}
		}
		
		//all active sollen gezeichnet werden
		for( int  i = 0; i < mProjectiles.length; i++){
			if(mProjectiles[i].getActiveState() == true) {
				mProjectiles[i].draw(gl);
			}
		}
		TextureManager.getSingletonObject().setTexture( mTexture );
		super.draw(gl);
	}
	
	public void collideX( MoneyCarrier carrier ){
		for( int i = 0; i < mProjectiles.length; i++){
			if( mProjectiles[i].getActiveState() && (mProjectiles[i].getX() >= carrier.getX()) ){
				carrier.hit( mDmg );
				mProjectiles[i].reset();
			}
		}
		if( carrier.getX() <= this.getX() ){
			this.reset();
		}
	}
	
	
	public void reset(){
		this.setActiveState(false);
	}
	
	@Override
	public int getX(){
		return mX + mRadius;
	}
}


