package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Projectile extends GLObject{
	
	private long mLastFrametime = -1;
	private int mXTranslate = 0;
	protected short mRadius = 4;
	protected short mSpeed = 5;
	protected short mDmg = 10;
	protected short mSlowing = 0;
	protected int mTexture = -1;
	protected int mDyingTextur1 = R.drawable.l12_enemie_dying1;
	protected int mDyingTextur2 = R.drawable.l12_enemie_dying2;
	protected int mDyingTextur3 = R.drawable.l12_enemie_dying3;
	protected int mDyingTextur4 = R.drawable.l12_enemie_dying4;
	protected long mStartDyingTime = -1;
	protected boolean mReadyToRemove = false;
	protected boolean mIsExploding = false;
	
	public float getSpeed(){
		return mSpeed;
	}
	
	public void setXY( int x, int y){
		mX = x;
		mY = y;
		mReadyToRemove = false;
		mStartDyingTime = -1;
		mIsExploding = false;
		initVBOs();
	}
	
	
	public void initVBOs(){	
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
		
		float[] colors = { mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3]};
		
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(mTexturePoints);
		mTextureBuffer.position(0);
	}
	
	@Override
	public void draw( GL10 gl ){
		long ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		//pause
		if( GameMechanics.getSingleton().running() == false ) dt = 0;		
		mLastFrametime = ms;
		int distance = 0;
		if( mStartDyingTime != -1 )distance = 0;
		else distance = (int)(mSpeed * dt);
		mXTranslate += distance;		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( mXTranslate, 0.0f, 0.0f);
		if( mStartDyingTime == -1) TextureManager.getSingletonObject().setTexture( mTexture );
		else{
			mIsExploding = true;
			long dyt = System.currentTimeMillis() - mStartDyingTime;
			if( dyt > (0 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur1 );
			if( dyt > (1 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur2 );
			if( dyt > (2 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur3 );
			if( dyt > (3 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur4 );
			if( dyt > (4 * Definitions.ANIMTE_CYCLE_TIME) ) {
				mReadyToRemove = true;
				TextureManager.getSingletonObject().setTexture( mDyingTextur4 );
			}
		}
		super.draw(gl);
		gl.glPopMatrix();	
	}
	
	@Override
	public int getX(){
		return mXTranslate + mX + mRadius; //returns the bullet real position because it gets translated and not moved
	}


	public short getDamage() {
		return mDmg;
	}
	
	public short getSlow(){
		return mSlowing;
	}
	
	public void reset(){
		mXTranslate = 0;
		this.setActiveState(false);
	}
	
	public void die(){
		if( mStartDyingTime == -1 ){
			mStartDyingTime = System.currentTimeMillis();
		}
	}

	public boolean toRemove() {
		return mReadyToRemove;
	}

	public boolean isExploding() {
		return mIsExploding;
	}
	
}
