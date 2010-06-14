package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
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
	protected int mDyingTextur1 = R.drawable.l12_enemie_dying1;
	protected int mDyingTextur2 = R.drawable.l12_enemie_dying2;
	protected int mDyingTextur3 = R.drawable.l12_enemie_dying3;
	protected int mDyingTextur4 = R.drawable.l12_enemie_dying4;
	protected int mSound = R.raw.l12_enemie1_dying;
	protected short mSlowed = 0;
	protected short mMoney = 10;
	protected short mSpeed = 5;
	protected int mIronToDrop = Definitions.FIRST_ROUND_ENEMIE_IRON;
	protected long mStartDyingTime = -1;
	boolean mReadyToRemove = false;
	
	public void activate(){
		super.setActiveState(true);
		mLastFrametime = System.currentTimeMillis();
		mReadyToRemove = false;
		mStartDyingTime = -1;
		mSlowed = 0;
		GameMechanics.getSingleton().addMoney( mMoney );
	}

	public void deactivate(){
		super.setActiveState(false);
		mMovePos = 0;
		mLastFrametime = -1;
		mReadyToRemove = true;
	}
	
	public void setXY(int xCentr, int yCentr ){
		mStartPos = xCentr;	
		mX = xCentr;
		mY = yCentr;
		initVBOs();
	}
	
	
	public void initVBOs(){
		SoundHandler.getSingleton().addResource(mSound);
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
		int distance = 0;
		if( mStartDyingTime != -1 )distance = 0;
		else if( mSlowed == 0) distance = (int)(mSpeed * dt);
		else distance = (int)(mSpeed * dt * 0.5);//( mSpeed * 0.01 * (100 - mSlowed) * dt);
		mMovePos -= distance;
		//calculate actual position
		mX = (int)(mStartPos + mMovePos);
		gl.glPushMatrix();
		gl.glTranslatef(mMovePos, 0.0f, 0.0f);
		
		if( mStartDyingTime == -1) TextureManager.getSingletonObject().setTexture( mTexture );
		else{
			long dyt = System.currentTimeMillis() - mStartDyingTime;
			//System.out.println("DYT: "+dyt+" cycle time: "+Definitions.DIE_ANIMTE_CYCLE_TIME);
			if( dyt > (0 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur1 );
			if( dyt > (1 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur2 );
			if( dyt > (2 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur3 );
			if( dyt > (3 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur4 );
			if( dyt > (4 * Definitions.ANIMTE_CYCLE_TIME) ) {
				TextureManager.getSingletonObject().setTexture( mDyingTextur4 );
				mReadyToRemove = true;
			}
		}
		super.draw(gl);
		gl.glPopMatrix();
	}
	
	
	public void hit( short dmg, short slow ){
		mHp -= dmg;
		if( mSlowed == 0 && slow != 0 ){
			mSlowed = slow;
			setFrozenColor();
			mHp = (short)(mHp*0.5f);
		}
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
	
	public void die(){
		if( mStartDyingTime == -1 ){
			SoundHandler.getSingleton().play(mSound);
			GameMechanics.getSingleton().addIron(mIronToDrop);
			mStartDyingTime = System.currentTimeMillis();
		}
	}

	public boolean toRemove() {
		return mReadyToRemove;
	}
	
	public void setFrozenColor(){
		mColor[0] = 0.0f;
		mColor[1] = 0.0f;
		mColor[2] = 1.0f;
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
	}

	public int getFrontX() {
		return mX - mRadius;
	}
	
	public int getBackX(){
		return mX + mRadius;
	}
}