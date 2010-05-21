package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public abstract class MoneyCarrier extends GLObject {
	protected short mRadius = -1;
	private int mMovePos = 0;
	private long mLastFrametime = -1;
	private int mStartPos;
	protected short mHp = 10;
	protected short mStrength = 1; //how much damage it can do
	protected int mType = 0; //zombie type
	protected int mTexture = R.drawable.l12_enemie_lvl0;
	protected short mMoney = 10;
	protected short mSpeed = 5;
	
	
	public void activate(){
		super.setActiveState(true);
		mLastFrametime = System.currentTimeMillis();
	}

	public void deactivate(){
		super.setActiveState(false);
		mMovePos = 0;
		mLastFrametime = -1;
	}
	
	public void setXY(int xCentr, int yCentr ){
		mStartPos = xCentr;	
		mX = xCentr;
		mY = yCentr;
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
		
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
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
	public void draw(GL10 gl){	
		long ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		if( GameMechanics.getSingleton().running() == false ) dt = 0;
		mLastFrametime = ms;
		double distance = mSpeed * dt;
		mMovePos -= distance;
		//calculate actual position
		mX = (int)(mStartPos + mMovePos);
		gl.glPushMatrix();
		gl.glTranslatef(mMovePos, 0.0f, 0.0f);
		TextureManager.getSingletonObject().setTexture( mTexture );
		super.draw(gl);
		gl.glPopMatrix();
	}
	
	
	public void hit( short dmg ){
		mHp -= dmg;
	}

	public int getMoney() {
		return mMoney;
	}

	public int getHP() {
		return mHp;
	}	
	
	@Override
	public int getX(){
		return (int)(mX - mRadius);
	}
}