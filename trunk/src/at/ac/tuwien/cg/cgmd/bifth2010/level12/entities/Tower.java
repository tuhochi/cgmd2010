package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import java.lang.System;



public abstract class Tower extends GLObject {
	private float mRadius = -1.0f;
	protected float mShootingInterval = 1.0f; //secs
	protected double mTimeLastProjectileShot = System.currentTimeMillis();
	protected Projectile[] mProjectiles = null;
	protected int mScreenWidth = 800;
	protected short mDmg = 10;
	
	
	protected Tower(float xCentr, float yCentr, float radius ){
		this( radius );	
		setXY( xCentr, yCentr );
	}
	
	protected Tower( float radius ){
		mRadius = radius;
		mColor[0] = 0.5f;
		mColor[1] = 0.5f;
		mColor[2] = 0.0f;
		mColor[3] = 1.0f;
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(texture);
		mTextureBuffer.position(0);
	}
	
	
	public void setXY( float xCentr, float yCentr ){
		mX = xCentr;
		mY = yCentr;
		System.out.println("TOWER SETXY: mX: "+mX+" my: "+mY);
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
		System.out.println("Vertices.length: "+vertices.length+" Indices.length: "+indices.length);
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
		this.setActiveState(true);
	}
	
	public void setViewPortLength(int width) {
		mScreenWidth = width;
	}
	
	
	
	@Override
	public void draw( GL10 gl ){
		//new projectile
		if( this.getActiveState() == false ) return;
		double dt =(System.currentTimeMillis() - mTimeLastProjectileShot ) * 0.001;//secs
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
			if(mProjectiles[i].getActiveState()) {
				mProjectiles[i].draw(gl);
			}
		}
		TextureManager.getSingletonObject().add(R.drawable.l12_icon);
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
}


